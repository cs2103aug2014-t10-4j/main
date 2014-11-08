//@author A0110937J

//The following class creates loop for attribute Processor.
//If any attribute is found, an error message will be assigned
class AntiNaturalProcessor extends NaturalProcessor {

	@Override
	protected void assignErrorMsg(String[] parsedInput, Processor processor) {
		if (parsedInput[processor.getItemPosition()] != null
				&& parsedInput[Constants.ERROR_MSG_POSITION] == null) {
			assignErrorMsg(parsedInput, Constants.ERROR_EXTRA_ITEM);
		}
	}

	protected void assignErrorMsg(String[] parsedInput, String message) {
		parsedInput[Constants.ERROR_MSG_POSITION] = message;
		parsedInput[Constants.COMMAND_POSITION] = Constants.ERROR;
	}
}