//@author A0110937J

//The following class is a test driver for testing the parser class
public class TestDriverParser {

	public static String getParsedInput(String input){
		String[] result = Parser.parseInput(input);
		String finalResult = new String("");
		for(int i = 0;i< result.length;i++){
			finalResult = finalResult + result[i]+" ";
		}
		return finalResult;
	}
}