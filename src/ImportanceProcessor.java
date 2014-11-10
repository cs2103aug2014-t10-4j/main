
//@author A0110937J

//The following class checks and assigns importance levels
class ImportanceProcessor extends Processor {

	public int getItemPosition() {
		return Constants.IMPT_POSITION;
	}

	public void process(String[] parsedInput, String[] input, Index index) {
		int importance = Constants.INVALID_NUMBER;
		if (isImpt(input, index)) {
			checkNextForInteger(parsedInput, input, index, importance);
		}
	}

	private void checkNextForInteger(String[] parsedInput, String[] input,
			Index index, int importance) {
		if (isIndexValid(index.getValue() + 1, input)) {
			if (isNextInteger(input, index.getValue())) {
				checkAndAssignImpt(parsedInput, input, index, importance);
			}
		}
	}

	private void checkAndAssignImpt(String[] parsedInput, String[] input,
			Index index, int importance) {
		if (isInteger(input[index.getValue() + 1])) {
			importance = Integer.parseInt(input[index.getValue() + 1]);
		}
		if (isValidImpt(importance)) {
			assignErrorMsg(parsedInput, Constants.ERROR_INVALID_IMPORTANCE);
		} else {
			assignImpt(parsedInput, importance);
			index.incrementByTwo();
		}
	}

	private boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private boolean isImpt(String[] input, Index index) {
		return isIndexValid(index.getValue(), input)
				&& (isPartOfList(input[index.getValue()],
						Constants.LIST_IMPORTANCE));
	}

	private void assignImpt(String[] parsedInput, int importance) {
		assert importance >= Constants.LOW_IMPT_LVL;
		assert importance <= Constants.HIGH_IMPT_LVL;
		parsedInput[Constants.IMPT_POSITION] = Integer.toString(importance);
	}

	private boolean isValidImpt(int importance) {
		return importance < Constants.LOW_IMPT_LVL
				|| importance > Constants.HIGH_IMPT_LVL;
	}

	private boolean isNextInteger(String[] input, int index) {
		try {
			Integer.parseInt(input[index + 1]);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public String getItemName() {
		return Constants.IMPT_NAME;
	}
}