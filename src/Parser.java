public class Parser {
	
	private static final String SEARCH_TODAY = "search today";
	private static final String COM_SHOW_TODAY = "show today";
	// Commands
	private static final String COM_ADD = "add";
	private static final String COM_SEARCH = "search";
	private static final String COM_EDIT = "edit";
	private static final String COM_DELETE = "delete";
	private static final String COM_DELETE_DATE = "delete date";


	// Position of various inputs
	private static final int MAX_TYPES = 9;
	private static final int COMMAND_POSITION = 0;

	public static String[] parseInput(String inputFromUser) {
		String[] parsedInput = new String[MAX_TYPES];
		inputFromUser = inputFromUser.replaceAll("\\s+", " ");
		String[] input = inputFromUser.trim().split(" ");
		Index index = new Index();
		processType(parsedInput, input, index, new CommandProcessor(),
				new NaturalProcessor());
		/*
		CommandProcessor cmdPro = new CommandProcessor();
		cmdPro.process(parsedInput, input, index);
		*/
		if (parsedInput[COMMAND_POSITION] == null) {
			parseCommand(parsedInput, input, index, new ParserAdd());
		} else if (parsedInput[COMMAND_POSITION].equals(COM_ADD)) {
			parseCommand(parsedInput, input, index, new ParserAdd());
		} else if (parsedInput[COMMAND_POSITION].equals(COM_SEARCH)) {
			parseCommand(parsedInput, input, index, new ParserSearch());
		} else if (parsedInput[COMMAND_POSITION].equals(COM_DELETE)) {
			parseCommand(parsedInput, input, index, new ParserDelete());
		} else if (parsedInput[COMMAND_POSITION].equals(COM_EDIT)) {
			parseCommand(parsedInput, input, index, new ParserEdit());
		}else if(parsedInput[COMMAND_POSITION].equals(COM_DELETE_DATE)){
			parseCommand(parsedInput, input, index, new ParserDeleteDate());
		}else if(parsedInput[COMMAND_POSITION].equals(COM_SHOW_TODAY)){
			parsedInput=Parser.parseInput(SEARCH_TODAY);
		}
		return parsedInput;
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