package drplantabyte.ult.dice;
import java.util.Random;
public class DiceRoller{
	private final Random prng;
	public DiceRoller(){
		prng = new Random();
	}

	public int roll(int n, int d){
		int sum = 0;
		for(int i = 0; i < n; i++){
			sum += prng.nextInt(d) + 1;
		}
		return sum;
	}
}
