package drplantabyte.ult.dice;
import java.util.Random;


public class DiceRoller {
	
	final Random prng;
	
	/**
	 * Default constructor (uses standard java.util.Random for the random number generator)
	 */
	public DiceRoller(){
		this(new Random());
	}
	/**
	 * Standard constructor for the DiceRoller class. You need to supply a psuedo-
	 * random number generator to act as the digital dice.
	 * @param rand
	 */
	public DiceRoller(Random rand){
		prng = rand;
	}
	/**
	 * Computes the given dice expression and returns the result. If
	 * <code>averageValue</code> is <code>true</code>, then no dice are rolled
	 * and the returned value is the average result. Otherwise, a random number
	 * generator is used to simulate dice rolling. Note that 'X' and 'x' may be
	 * used for multiplication in the place of '*' and the '%' symbol will be
	 * expanded to 100 (e.g. "1d%" is same as "1d100").
	 * @param diceExpression A RPG style dice expression (e.g. "(2d6+3)*2-5")
	 * @param averageValue If <code>true</code>, return the average result; if
	 * <code>false</code>, roll dice and return the random result.
	 * @return The value of the expression.
	 */
	public int eval(String diceExpression, boolean averageValue){
		
		StringBuffer x = new StringBuffer(diceExpression);
		// need to remove all whitespace, also using this opportunity to throw common exceptions
		for(int i = x.length() - 1; i >= 0; i-- ){
			if(Character.isWhitespace(x.charAt(i))){
				x.deleteCharAt(i);
			} else if(x.charAt(i) == '.'){
				throw new NumberFormatException("Found '.', but decimal numbers are not supported (integer math only)");
			} else if(x.charAt(i) == '%'){// d% means d100
				x.replace(i, i+1, "100");
			} else if(x.charAt(i) == 'x' || x.charAt(i) == 'X'){// multiplication old-school notation
				x.replace(i, i+1, "*");
			}
		}
		
		
		// Parentheses
		while(x.indexOf("(") >= 0){
			int open = x.indexOf("(");
			int close = x.lastIndexOf(")");
			if(open > 0 && Character.isDigit(x.charAt(open-1))){// handle implied multiplication (e.g. "2(1d6+1)" == "2*(1d6)+1" )
				x.insert(open, '*');
				continue;
			}
			if(close < 0){throw new NumberFormatException("Found '(' without matching ')'");}
			x.replace(open, close+1, (Integer.valueOf(eval(x.substring(open+1,close),averageValue))).toString());
		}
		
		// Dice
		while(x.indexOf("d") > 0 ){
			int opIndex = x.indexOf("d");
			
			int left = opIndex - 1;
			while(left >= 0 && Character.isDigit(x.charAt(left))){
				left--;
			}
			int right = opIndex+1;
			while(right < x.length() && Character.isDigit(x.charAt(right))){
				right++;
			}
			String leftOperand = x.substring(left+1, opIndex);
			String rightOperand = x.substring(opIndex+1, right);
			int L = Integer.parseInt(leftOperand);
			int R = Integer.parseInt(rightOperand);
			Integer value;
			if(averageValue){
				value = averagedRoll(L,R);
			} else {
				value = roll(L,R);
			}
			x.replace(left+1, right, value.toString());
		}
		
		// multiply and divide
		while(x.indexOf("*") > 0 || x.indexOf("/") > 0){ // why doesn't StringBuffer have a "contains" method?
			int a = x.indexOf("*");
			int b = x.indexOf("/");
			int opIndex;
			if(a < 0){
				opIndex = b;
			} else if(b < 0) {
				opIndex = a;
			} else if(a < b){
				opIndex = a;
			} else {
				opIndex = b;
			}
			int left = opIndex - 1;
			while(left >= 0 &&Character.isDigit(x.charAt(left))){
				left--;
			}
			int right = opIndex+1;
			while(right < x.length() && Character.isDigit(x.charAt(right))){
				right++;
			}
			char operator = x.charAt(opIndex);
			String leftOperand = x.substring(left+1, opIndex);
			String rightOperand = x.substring(opIndex+1, right);
			int L = Integer.parseInt(leftOperand);
			int R = Integer.parseInt(rightOperand);
			Integer value;
			if(operator == '*') {
				value = L * R;
			} else {
				value = L / R;
			}
			x.replace(left+1, right, value.toString());
		}
		
		// add and subtract
		// replace all - with +- (but not the leading - if the first number is negative)
		for(int i = 1; i < x.length(); i++){
			if(x.charAt(i) == '-'){
				x.insert(i, '+');
				i++;
			}
		}
		while(x.indexOf("+") > 0 ){
			int opIndex = x.indexOf("+");
			
			int left = opIndex - 1;
			while(left >= 0 && (Character.isDigit(x.charAt(left)) || x.charAt(left) == '-')){
				left--;
			}
			int right = opIndex+1;
			while(right < x.length() && (Character.isDigit(x.charAt(right)) || x.charAt(right) == '-')){
				right++;
			}
			String leftOperand = x.substring(left+1, opIndex);
			String rightOperand = x.substring(opIndex+1, right);
			int L = Integer.parseInt(leftOperand);
			int R = Integer.parseInt(rightOperand);
			Integer value = L + R;
			x.replace(left+1, right, value.toString());
		}
		
		return Integer.parseInt(x.toString());
	}
	/**
	 * Computes the given dice expression and returns the rolled result. A
	 * random number generator is used to simulate dice rolling. Note that 'X'
	 * and 'x' may be used for multiplication in the place of '*' and the '%'
	 * symbol will be expanded to 100 (e.g. "1d%" is same as "1d100").
	 * @param diceExpression A RPG style dice expression (e.g. "(2d6+3)*2-5")
	 * @param diceExpression A d20 style dices expression (e.g. "(2d6+3)*2-5")
	 * @return The value of the expression, using random numbers to roll the
	 * dice values.
	 */
	public int eval(String diceExpression){
		return eval(diceExpression,false);
	}
	/**
	 * Roles <i>n</i>d<i>s</i>
	 * @param n number of dice
	 * @param s sides per die
	 * @return The rolled value
	 */
	public synchronized int roll(int n,int s){
		int sum = 0;
		for(int i = 0; i < n; i++){
			sum += prng.nextInt(s) + 1;
		}
		return sum;
	}
	/**
	 * Similar to the <code>roll(n,s)</code> methods, but instead of rolling
	 * digital dice, it calculates the statistical average (mean) result.
	 * @param n number of dice
	 * @param s sides per die
	 * @return The average roll value
	 */
	public int averagedRoll(int n,int s){
		double sum = 0;
		for(int i = 0; i < n; i++){
			sum += (1.0 + s)/2;
		}
		return (int)sum;
	}
	
}