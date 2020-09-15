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
	
	public String eval(String diceExpression) {//throws UnsupportedOperationException {
		
		int startIndex = 0;
		int nextIndex;
		while(startIndex < diceExpression.length()){
			nextIndex = nextOperator(diceExpression, startIndex);
			char operator = diceExpression.charAt(nextIndex);
			/*switch (operator){
				case '(': {
					// find closing parenthesis
					int indent = 1;
					for(int i = nextIndex; i < diceExpression.length(); i++){
						if(diceExpression.charAt(i) == '(') indent += 1;
						if(diceExpression.charAt(i) == ')') indent -= 1;
						if(indent == 0){
							String subString = diceExpression.substring(nextIndex+1, i);
							String subTotal = eval(subString);
							diceExpression =
									diceExpression.substring(0,nextIndex)
									+ subTotal
									+ diceExpression.substring(i+1);
							break;
						}
					}
				}
				default:{
					throw new UnsupportedOperationException(String.format("Dice expression operstor '%s' is not supported",operator));
				}
			}*/
			//
			startIndex++;
		}
		return diceExpression;
	}
	
	private int nextOperator(String diceExpression, int startIndex) {
		int nextIndex = startIndex+1;
		while(nextIndex < diceExpression.length() && Character.isDigit(diceExpression.charAt(nextIndex))){
			nextIndex++;
		}
		while(nextIndex < diceExpression.length() && Character.isWhitespace(diceExpression.charAt(nextIndex))){
			nextIndex++;
		}
		return nextIndex;
	}
}
