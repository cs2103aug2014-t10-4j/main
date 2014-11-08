//@author A0110937J

//The following class creates a loop for attribute Processor
//If the attribute is not found, it will assign an error message

class StrictNaturalProcessor extends NaturalProcessor {
	@Override
	protected void assignErrorMsg(String[] parsedInput, Processor processor) {
		if (parsedInput[processor.getItemPosition()] == null
				&& parsedInput[Constants.ERROR_MSG_POSITION] == null) {
			assignErrorMsg(parsedInput, fomatEmpty(processor));
		}
	}

	private String fomatEmpty(Processor processor) {
		return String.format(Constants.ERROR_EMPTY_ITEM,
				processor.getItemName());
	}

	protected void assignErrorMsg(String[] parsedInput, String message) {
		parsedInput[Constants.ERROR_MSG_POSITION] = message;
		parsedInput[Constants.COMMAND_POSITION] = Constants.ERROR;
	}
}

