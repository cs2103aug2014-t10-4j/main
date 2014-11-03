import java.util.Scanner;


public class TestDriverParser {

	/*
	private static Scanner sc;
	public static void main(String[] args){
		
		String input = new String("");
		sc = new Scanner(System.in);
		while(true){
			input = sc.nextLine();
			System.out.println(getParsedInput(input));
		}
		
	}
	*/
	public static String getParsedInput(String input){
		String[] result = Parser.parseInput(input);
		String finalResult = new String("");
		for(int i = 0;i< result.length;i++){
			finalResult = finalResult + result[i]+" ";
		}
		return finalResult;
	}
}