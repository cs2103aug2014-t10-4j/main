//@author A0110937J

// The following class checks and assigns valid parameters

class SingleParaProcessor extends Processor {
	public void process(String[] parsedInput, String[] input, Index index) {
		setupParameter(input, index.getValue());
		parseRange(input, index, parsedInput);
		if (isValidInteger(input, index)) {
			assignPara(parsedInput, index, input);
		}
	}

	private boolean isValidInteger(String[] input, Index index) {
		return isIndexValid(index.getValue(), input)
				&& isInteger(input[index.getValue()]);
	}

	// For overriding by other classes
	protected void parseRange(String[] input, Index index, String[] parsedInput) {
	}

	// For overriding by other classes
	protected void setupParameter(String[] input, int index) {
	}

	protected void assignPara(String[] parsedInput, Index index, String[] input) {
		parsedInput[getItemPosition()] = getInteger(index, input);
		index.increment();
	}

	private String getInteger(Index index, String[] input) {
		return Integer.toString(Integer
				.parseInt(input[index.getValue()]));
	}

	protected boolean isInteger(String possiblePara) {
		try {
			Integer.parseInt(possiblePara);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	@Override
	public int getItemPosition() {
		return Constants.PARAMETER_POSITION;
	}

	@Override
	public String getItemName() {
		return Constants.PARAMETERS_NAME;
	}

}