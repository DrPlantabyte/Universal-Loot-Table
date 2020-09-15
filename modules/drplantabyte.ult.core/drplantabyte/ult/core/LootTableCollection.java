package drplantabyte.ult.core;
import drplantabyte.ult.dice.*;

import java.util.*;

public class LootTableCollection{
	private final HashMap<String, LootTable> lootTables;
	private final Random prng;
	private final DiceRoller dice;
	
	public LootTableCollection(){
		prng = new Random();
		lootTables = new HashMap<>();
		dice = new DiceRoller(new Random(prng.nextLong()));
	}
	
	public void addLootTable(LootTable table){
		lootTables.put(formatID(table.getID()), table);
	}
	public void addLootTables(LootTable... tables){
		for(var table : tables){
			this.addLootTable(table);
		}
	}
	
	public String rollLootFromTable(String tableName) throws IllegalArgumentException{
		return rollLootFromTable(tableName, "\n");
	}
	public String rollLootFromTable(String tableName, String delimiter) throws IllegalArgumentException{
		String id = formatID(tableName);
		if(lootTables.containsKey(id)){
			var table = lootTables.get(id);
			var entry = table.rollLoot();
			return processLootEntry(entry, delimiter);
		} else {
			throw new IllegalArgumentException(
					String.format("Table %s not found in Loot Table Collection (loot tables: %s)",
							id, Arrays.toString(lootTables.keySet().toArray()))
			);
		}
		
	}
	
	public boolean hasLootTable(String name){
		return lootTables.containsKey(formatID(name));
	}
	
	private String processLootEntry(LootTableEntry entry, String delimiter) {
		String item = entry.item;
		// scan for {dice expressions} and [other table]
		for(int i = 0; i < item.length(); i++){
			char c = item.charAt(i);
			if(c == '{'){
				int start = i+1;
				while(item.charAt(i) != '}' && item.charAt(i) != '@'){ i += 1;}
				int end = i;
				int result = dice.eval(item.substring(start, end));
				if(item.charAt(end) == '@'){
					// roll on another table this many times
					int start2 = next(item,'[',end);
					int end2 = next(item,']',start2);
					String id = item.substring(start2+1,end2);
					var list = new LinkedList<String>();
					for(int j = 0; j < result; j++){
						list.add(this.rollLootFromTable(id, delimiter));
					}
					int end3 = next(item, '}', end2);
					String insert = join(delimiter, list);
					item = item.substring(0,start-1)
							+ insert
							+ item.substring(end3+1, item.length());
					i = start-1 + insert.length();
				}
			} else if(c == '['){
				int start = i+1;
				int end = next(item,']',start);
				String id = item.substring(start,end);
				if(hasLootTable(id)){
					String insert = rollLootFromTable(id, delimiter);
					item = item.substring(0,i)
							+ insert
							+ item.substring(end+1, item.length());
					i = i + insert.length();
				}
			}
		}
		return item;
	}
	private static int next(String s, char target, int start){
		int i = s.indexOf(target, start);
		if(i < 0) return s.length();
		return i;
	}
	private static String join(String delimiter, Collection<Object> items){
		return join(delimiter, items.toArray());
	}
	private static String join(String delimiter, Object... items){
		StringBuilder sb = new StringBuilder();
		boolean notFirst = false;
		for(Object o : items){
			sb.append(String.valueOf(o));
			if(notFirst){
				sb.append(delimiter);
			}
			notFirst = true;
		}
		return sb.toString();
	}
	
	private static String formatID(String id){
		return id.toUpperCase(Locale.ENGLISH);
	}
}
