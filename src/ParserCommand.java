//@author A0110937J
//The following class is the abstract class for command objects

public abstract class ParserCommand {

	public abstract void parse(String[] parsedInput, String[] input, Index index);

	protected void processType(String[] parsedInput, String[] input,
			Index index, Processor processor, NaturalProcessor natProcessor) {
		natProcessor.process(parsedInput, input, index, processor);
	}

	// checks if parsedInput is empty
	protected void processEmpty(String[] parsedInput) {
		if (isAllNull(parsedInput)) {
			assignErrorMsg(parsedInput, Constants.MSG_NO_ATTRIBUTES);
		}
	}

	protected void assignErrorMsg(String[] parsedInput, String message) {
		parsedInput[ Constants.ERROR_MSG_POSITION] = message;
		parsedInput[ Constants.COMMAND_POSITION] =  Constants.ERROR;
	}

	// checks if all items of an array is null
	private boolean isAllNull(String[] parsedInput) {
		for (int i = 1; i < parsedInput.length; i++) {
			if (parsedInput[i] != null) {
				return false;
			}
		}
		return true;
	}
}



