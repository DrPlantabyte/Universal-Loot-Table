package drplantabyte.ult.tests;
import drplantabyte.ult.dice.*;
public class Test1{
	public static void main(String[] args){
		DiceRoller dr = new DiceRoller();
		print(dr.roll(1,6), dr.eval("1"));
	}
	
	private static void print(Object... msg){
		for(Object o : msg){
			System.out.print(String.valueOf(o));
			System.out.print(' ');
		}
		System.out.println();
	}
}
