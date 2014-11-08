public class Parser {
	
	private static final String PATTERN_MULTI_SPACE = "\\s+";
	public static String[] parseInput(String inputFromUser) {
		String[] parsedInput = new String[Constants.MAX_TYPES];
		inputFromUser = inputFromUser.replaceAll(PATTERN_MULTI_SPACE, " ");
		String[] input = splitString(inputFromUser);
		Index index = new Index();
		processType(parsedInput, input, index, new CommandProcessor(),
				new NaturalProcessor());
		/*
		CommandProcessor cmdPro = new CommandProcessor();
		cmdPro.process(parsedInput, input, index);
		*/
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
			parsedInput=Parser.parseInput(Constants.SEARCH_TODAY);
		}
		return parsedInput;
	}

	private static String[] splitString(String inputFromUser) {
		String[] input = inputFromUser.trim().split(" ");
		return input;
	} 

	private static void parseCommand(String[] parsedInput, String[] input,
			Index index, ParserCommand command) {
		command.parse(parsedInput, input, index);
	}
	private static void processType(String[] parsedInput, String[] input,
			Index index, Processor processor, NaturalProcessor natProcessor) {
		natProcessor.process(parsedInput, input, index, processor);
	}

}


class Index {
	private int value;

	public Index() {
		value = 0;
	}
	public Index(int number){
		this.value = number;
	}

	public void decrement() {
		this.value = value - 1;
	}

	public void incrementByTwo() {
		this.value = value + 2;

	}

	public int getValue() {
		return value;
	}

	public void setValue(int index) {
		this.value = index;
	}

	public void increment() {
		value++;
	}

	public void reset() {
		this.value = 0;
	}

	public void resetToOne() {
		this.value = 1;
	}

}