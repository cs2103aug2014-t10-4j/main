import java.util.ArrayList;

class MultiParaProcessor extends SingleParaProcessor {

	@Override
	public void processBefore(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		removeAllFullStop(input, fullStopArr);
		parsedInput[getItemPosition()] = Constants.EMPTY_STRING;
	}

	@Override
	protected void parseRange(String[] input, Index index, String[] parsedInput) {

		if (isIndexValid(index.getValue(), input)) {
			int startIndex = index.getValue();
			if (isDash(input, index)) {
				processRangeOne(input, index, parsedInput, startIndex);
			} else if (isDashLast(input, index)) {
				removeLastDash(input, index);
				processRangeTwo(input, index, parsedInput, startIndex);
			} else if (isFirstCharDash(input, index)) {
				removeFirstDash(input, index);
				processRangeTwo(input, index, parsedInput, startIndex);
			} else if (isJoinedByDash(input, index)) {
				processRangeThree(input, index, parsedInput, startIndex);
			}
		}
	}

	// process range in the form 12-13
	private void processRangeThree(String[] input, Index index,
			String[] parsedInput, int startIndex) {
		String[] temp = input[index.getValue()].split(Constants.DASH);
		if (isValidJoin(temp)) {
			processRange(index, parsedInput, startIndex, temp, input);
		} else {
			assignErrorMsg(parsedInput, Constants.ERROR_PARA_RANGE);
			index.setValue(startIndex);
		}
	}

	private boolean isValidJoin(String[] temp) {
		return temp.length == Constants.LIMIT_RANGE_PARA
				&& (isInteger(temp[0]) && isInteger(temp[1]));
	}

	// process range in the form 12- 13 or 12 -13
	private void processRangeTwo(String[] input, Index index,
			String[] parsedInput, int startIndex) {
		if (areBothPossibleInteger(input, index)) {
			String[] temp = setupTem(input, index);
			index.increment();
			processRange(index, parsedInput, startIndex, temp, input);
		} else {
			assignErrorMsg(parsedInput, Constants.ERROR_INVALID_PARAMETER);
			index.setValue(startIndex);
		}
	}

	private void removeLastDash(String[] input, Index index) {
		input[index.getValue()] = input[index.getValue()].substring(0,
				input[index.getValue()].length() - 1);
	}

	private boolean isDashLast(String[] input, Index index) {
		return isIndexValid(index.getValue(), input)
				&& input[index.getValue()] != null
				&& input[index.getValue()].charAt(input[index.getValue()]
						.length() - 1) == Constants.CHAR_DASH;
	}

	// process range in the form 12 - 13
	private void processRangeOne(String[] input, Index index,
			String[] parsedInput, int startIndex) {
		if (areBothInteger(input, index)) {
			String[] temp = setupTempOne(input, index);
			index.incrementByTwo();
			processRange(index, parsedInput, startIndex, temp, input);
		} else {
			assignErrorMsg(parsedInput, Constants.ERROR_INVALID_PARAMETER);
			index.setValue(startIndex);
		}
	}

	private String[] setupTempOne(String[] input, Index index) {
		String[] temp = new String[Constants.LIMIT_RANGE_PARA];
		temp[0] = input[index.getValue()];
		temp[1] = input[index.getValue() + 2];
		return temp;
	}

	private String[] setupTem(String[] input, Index index) {
		String[] temp = new String[Constants.LIMIT_RANGE_PARA];
		temp[0] = input[index.getValue()];
		temp[1] = input[index.getValue() + 1];
		return temp;
	}

	private boolean areBothPossibleInteger(String[] input, Index index) {
		return isIndexValid(index.getValue() + 1, input)
				&& isInteger(input[index.getValue()])
				&& isInteger(input[index.getValue() + 1]);
	}

	private void removeFirstDash(String[] input, Index index) {
		input[index.getValue() + 1] = input[index.getValue() + 1].substring(1,
				input[index.getValue() + 1].length());
	}

	private boolean isFirstCharDash(String[] input, Index index) {
		return isIndexValid(index.getValue() + 1, input)
				&& input[index.getValue() + 1] != null
				&& input[index.getValue() + 1].charAt(0) == '-';
	}

	// checks for the form 12-13
	private boolean isJoinedByDash(String[] input, Index index) {
		return isIndexValid(index.getValue(), input)
				&& input[index.getValue()] != null
				&& input[index.getValue()].contains(Constants.DASH);
	}

	private boolean areBothInteger(String[] input, Index index) {
		return isIndexValid(index.getValue() + 2, input)
				&& isInteger(input[index.getValue()])
				&& isInteger(input[index.getValue() + 2]);
	}

	private boolean isDash(String[] input, Index index) {
		return isIndexValid(index.getValue() + 1, input)
				&& input[index.getValue() + 1] != null
				&& input[index.getValue() + 1].equals(Constants.DASH);
	}

	private void assignNullInput(String[] input, Index index, int startIndex) {
		for (int i = startIndex; i <= index.getValue(); i++) {
			assingNull(input, i);
		}
	}

	private void processRange(Index index, String[] parsedInput,
			int startIndex, String[] temp, String[] input) {
		if (isRangeValid(temp)) {
			splitAndAssign(parsedInput, temp);
			assignNullInput(input, index, startIndex);
		} else {
			assignErrorMsg(parsedInput, Constants.ERROR_PARA_RANGE);
			index.setValue(startIndex);
		}
	}

	private void splitAndAssign(String[] parsedInput, String[] temp) {
		String range = getRange(Integer.parseInt(temp[0]),
				Integer.parseInt(temp[1]));
		assignMultiPara(parsedInput, range);
	}

	private boolean isRangeValid(String[] temp) {
		return Integer.parseInt(temp[0]) < Integer.parseInt(temp[1]);
	}

	private void assignMultiPara(String[] parsedInput, String range) {
		parsedInput[getItemPosition()] = parsedInput[getItemPosition()] + range;

	}

	private String getRange(int lower, int upper) {
		String range = new String(Constants.EMPTY_STRING);
		for (int i = lower; i <= upper; i++) {
			range = range + Constants.SPACE + i;
		}
		return range;
	}

	@Override
	protected void setupParameter(String[] input, int index) {
		if (input[index] != null) {
			processForDash(input, index);
			processForComma(input, index);
			processForEmpty(input, index);
		}
	}

	private void processForDash(String[] input, int index) {
		if (input[index].contains(Constants.TO)) {
			replaceDash(input, index);
		}
	}

	private void processForEmpty(String[] input, int index) {
		if (input[index].length() == 0) {
			assingNull(input, index);
		}
	}

	private void processForComma(String[] input, int index) {
		if (hasComma(input, index)) {
			removeComma(input, index);
		} else if (input[index].equals(Constants.COMMA)) {
			assingNull(input, index);
		}
	}

	private void assingNull(String[] input, int index) {
		input[index] = null;
	}

	private boolean hasComma(String[] input, int index) {
		return input[index].length() != 0
				&& input[index].charAt(input[index].length() - 1) == Constants.CHAR_COMMA;
	}

	private void removeComma(String[] input, int index) {
		input[index] = input[index].substring(0, input[index].length() - 1);
	}

	private void replaceDash(String[] input, int index) {
		input[index] = input[index].replaceAll(Constants.TO, Constants.DASH);
	}

	@Override
	public void processAfter(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		assignNull(parsedInput);
		if (parsedInput[getItemPosition()] != null) {
			parsedInput[getItemPosition()] = parsedInput[getItemPosition()]
					.trim();
			assignNull(parsedInput);
		}
	}

	protected void assignPara(String[] parsedInput, Index index, String[] input) {
		parsedInput[getItemPosition()] = formatString(parsedInput, index, input);
		input[index.getValue()] = null;
	}

	private String formatString(String[] parsedInput, Index index,
			String[] input) {
		return parsedInput[getItemPosition()] + Constants.SPACE
				+ Integer.toString(Integer.parseInt(input[index.getValue()]));
	}
}