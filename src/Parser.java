//@author A0110937
//The following class acts as the controller of the entire Parser component
public class Parser {

	public static String[] parseInput(String inputFromUser) {
		String[] parsedInput = initString();
		inputFromUser = inputFromUser.replaceAll(Constants.PATTERN_MULTI_SPACE, Constants.SPACE);
		String[] input = splitString(inputFromUser);
		Index index = new Index();
		getCommand(parsedInput, input, index, new CommandProcessor(),
				new NaturalProcessor());
		return getParsedInput(parsedInput, input, index);
	}

	private static String[] initString() {
		String[] parsedInput = new String[Constants.MAX_TYPES];
		return parsedInput;
	}
	
	private static String[] getParsedInput(String[] parsedInput, String[] input,
			Index index) {
		if (parsedInput[Constants.COMMAND_POSITION] == null) {
			parseCommand(parsedInput, input, index, new ParserAdd());
		} else if (parsedInput[Constants.COMMAND_POSITION].equals(Constants.ACTION_ADD)) {
			parseCommand(parsedInput, input, index, new ParserAdd());
		} else if (parsedInput[Constants.COMMAND_POSITION].equals(Constants.ACTION_SEARCH)) {
			parseCommand(parsedInput, input, index, new ParserSearch());
		} else if (parsedInput[Constants.COMMAND_POSITION].equals(Constants.ACTION_DELETE)) {
			parseCommand(parsedInput, input, index, new ParserDelete());
		} else if (parsedInput[Constants.COMMAND_POSITION].equals(Constants.ACTION_EDIT)) {
			parseCommand(parsedInput, input, index, new ParserEdit());
		}else if(parsedInput[Constants.COMMAND_POSITION].equals(Constants.ACTION_DELETE_DATE)){
			parseCommand(parsedInput, input, index, new ParserDeleteDate());
		}else if(parsedInput[Constants.COMMAND_POSITION].equals(Constants.ACTION_SHOW_TODAY)){
			parsedInput = parseSearchToday(parsedInput);
		}
		return parsedInput;
	}

	private static String[] parseSearchToday(String[] parsedInput) {
		parsedInput=Parser.parseInput(Constants.SEARCH_TODAY);
		return parsedInput;
	}

	private static String[] splitString(String inputFromUser) {
		String[] input = inputFromUser.trim().split(Constants.SPACE);
		return input;
	} 

	private static void parseCommand(String[] parsedInput, String[] input,
			Index index, ParserCommand command) {
		command.parse(parsedInput, input, index);
	}
	private static void getCommand(String[] parsedInput, String[] input,
			Index index, Processor processor, NaturalProcessor natProcessor) {
		natProcessor.process(parsedInput, input, index, processor);
	}

}

