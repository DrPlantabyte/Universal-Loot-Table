package drplantabyte.ult.core;

public class LootTableEntry {
	public final double weight;
	public final String item;
	
	public LootTableEntry(double w, String item){
		this.weight = w;
		this.item = item;
	}
	
	public static LootTableEntry parseLine(String line) {
		// glass bell
		// 0.5|glass bell
		// 0.5|[MATERIAL] bell
		// 0.5|{1d4} [MATERIAL] bell(s)
		// 0.5|{1d4@[GEM]}
		String s = line.trim();
		int startIndex = 0;
		double w = 1;
		if(s.contains("|")){
			// weight specified
			startIndex = s.indexOf('|');
			w = Double.parseDouble(s.substring(0,startIndex));
			startIndex += 1;
		}
		return new LootTableEntry(w, s.substring(startIndex).trim());
	}
}
