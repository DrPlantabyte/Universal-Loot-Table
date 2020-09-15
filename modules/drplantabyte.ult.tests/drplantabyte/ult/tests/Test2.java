package drplantabyte.ult.tests;

import drplantabyte.ult.core.*;
import drplantabyte.ult.dice.DiceRoller;

public class Test2 {
	public static void main(String[] args){
		var loot = new LootTableCollection();
		var t1 = new LootTable("Quality");
		t1.addEntry(new LootTableEntry(3, "rough"));
		t1.addEntry(new LootTableEntry(2, "cut"));
		t1.addEntry(new LootTableEntry(1, "fine"));
		var t2 = new LootTable("Gem");
		t2.addEntry(new LootTableEntry(1, "diamond"));
		t2.addEntry(new LootTableEntry(1, "emerald"));
		t2.addEntry(new LootTableEntry(1, "ruby"));
		var t3 = new LootTable("Treasure");
		t3.addEntry(new LootTableEntry(1, "1 [QUALITY] [GEM]"));
		t3.addEntry(new LootTableEntry(1, "1x [QUALITY] [GEM] and 2x [QUALITY] [GEM]"));
		t3.addEntry(new LootTableEntry(1, "{1d4@[GEM]}"));
		
		loot.addLootTable(t1);
		loot.addLootTable(t2);
		loot.addLootTable(t3);
		
		print(loot.rollLootFromTable("treasure"));
		
	}
	
	private static void print(Object... msg){
		for(Object o : msg){
			System.out.print(String.valueOf(o));
			System.out.print(' ');
		}
		System.out.println();
	}
}
