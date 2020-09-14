package drplantabyte.ult.core;
import drplantabyte.ult.dice.*;
public class LootTableCollection{
	public static String testMessage(){
		DiceRoller dr = new DiceRoller();
		return "Rolled a "+dr.roll(1,6);
	}
}
