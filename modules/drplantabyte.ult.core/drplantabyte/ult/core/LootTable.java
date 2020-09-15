package drplantabyte.ult.core;

import drplantabyte.ult.dice.DiceRoller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LootTable {
	private final String id;
	private final List<LootTableEntry> loot;
	private final Random prng;
	private double totalWeight = 0;
	
	public LootTable(String id, Random prng){
		this.prng = prng;
		this.id = id;
		loot = new LinkedList<>();
	}
	public LootTable(String id){
		this(id, new Random());
	}
	
	public synchronized void addEntry(LootTableEntry e){
		this.loot.add(e);
		totalWeight += e.weight;
	}
	
	public synchronized LootTableEntry rollLoot(){
		double target = prng.nextDouble() * totalWeight;
		for(LootTableEntry e : loot){
			if(target < e.weight) return e;
			target -= e.weight;
		}
		throw new IllegalStateException(String.format("Error in %s.rollLoot(): totalWeight not equal to sum of loot table entry weights", this.getClass().getName()));
	}
	
	public static LootTable parseFile(Path filepath, Optional<Random> prng) throws IOException, InvalidFormatException{
		try(BufferedReader fin = Files.newBufferedReader(filepath, StandardCharsets.UTF_8)){
			String name = fileToID(filepath);
			Random r = prng.orElse(new Random());
			LootTable t = new LootTable(name, new Random(r.nextLong()));
			while(true){
				String line = fin.readLine();
				if(line == null) break;
				t.addEntry(LootTableEntry.parseLine(line));
			}
			return t;
		}
	}
	
	private static String fileToID(Path filepath) {
		String n = filepath.getFileName().toString();
		int i = n.lastIndexOf('.');
		if(i > 0){
			n = n.substring(0,i);
		}
		return n.toUpperCase(Locale.ENGLISH);
	}
	
	public String getID() {
		return id;
	}
}
