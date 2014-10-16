public class Parser {
	// Commands
	private static final String COM_ADD = "add";
	private static final String COM_SEARCH = "search";
	private static final String COM_EDIT = "edit";
	private static final String COM_DELETE = "delete";

	// Position of various inputs
	private static final int MAX_TYPES = 8;
	private static final int COMMAND_POSITION = 0;

	public static String[] parseInput(String inputFromUser) {
		String[] parsedInput = new String[MAX_TYPES];
		inputFromUser = inputFromUser.replaceAll("\\s+", " ");
		String[] input = inputFromUser.trim().split(" ");
		Index index = new Index();

		CommandProcessor cmdPro = new CommandProcessor();
		cmdPro.process(parsedInput, input, index);
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
		} 
		return parsedInput;

	}

	private static void parseCommand(String[] parsedInput, String[] input,
			Index index, ParserCommand command) {
		index.increment();
		command.parse(parsedInput, input, index);
	}
}

class Index {
	private int value;

	public Index() {
		value = 0;
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