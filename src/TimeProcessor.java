//@author A0110937J

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

//The following class checks and assigns valid time and time ranges

class TimeProcessor extends Processor {
	private static Logger logger = Logger.getLogger(Constants.TIME_PROCESSOR);

	public int getItemPosition() {
		return Constants.START_TIME_POSITION;
	}

	public int getSecItemPosition() {
		return Constants.END_TIME_POSITION;
	}

	// Method adds zeroes in the date where needed
	private String addZeroes(String possibleTime) {
		Date date = new Date();
		try {
			date = Constants.timeFormatOne.parse(possibleTime);
		} catch (ParseException e) {
			logger.log(Level.WARNING, Constants.ERROR_ADDING_ZEROES);
		}
		return Constants.timeFormatOne.format(date);
	}

	@Override
	public void process(String[] parsedInput, String[] input, Index index) {

		if (isIndexValid(index.getValue(), input)) {
			if (isPartOfList(input, index, Constants.LIST_NO_TIME)) {
				parsedInput[getItemPosition()] = getFirstMember(Constants.LIST_NO_TIME);
				index.increment();
			}
			if (!isFloating(parsedInput)
					&& parsedInput[getItemPosition()] == null) {
				processTimeRange(index, input, parsedInput);
				String possibleTime = getPossibleTime(index, input);
				if (possibleTime != null) {

					if (isValidTime(possibleTime)) {
						assignTime(parsedInput, possibleTime, getItemPosition());
						index.increment();
					} else {

						assignErrorMsg(parsedInput,
								Constants.ERROR_INVALID_TIME);
					}
				}
			}
		}
	}

	private void assignTime(String[] parsedInput, String possibleTime,
			int position) {
		possibleTime = addZeroes(possibleTime);
		parsedInput[position] = possibleTime;
	}

	private boolean isFloating(String[] parsedInput) {
		return !isNull(parsedInput[Constants.DATE_POSITION])
				&& parsedInput[Constants.DATE_POSITION]
						.equalsIgnoreCase(getFirstMember(Constants.LIST_FLOATING_TASK));
	}

	private void processTimeRange(Index index, String[] input,
			String[] parsedInput) {
		int current = index.getValue();
		int prev = current - 1;
		int next = current + 1;
		String[] startInput = new String[Constants.MAX_LENGTH_TIME];
		String[] endInput = new String[Constants.MAX_LENGTH_TIME];
		Index index1 = new Index(Constants.MAX_LENGTH_TIME - 1);
		Index index2 = new Index();
		if (isJoinedByDash(index, input, current)) {
			processJoined(index, input, parsedInput, current, prev, next,
					startInput, endInput, index1, index2);
		} else if (isSplitRange(input, prev)) {
			processSplit(index, input, parsedInput, current, prev, next,
					startInput, endInput, index1, index2);
		}

	}

	private void processSplit(Index index, String[] input,
			String[] parsedInput, int current, int prev, int next,
			String[] startInput, String[] endInput, Index index1, Index index2) {
		int prev2 = index.getValue() - 2;
		int prev3 = index.getValue() - 3;

		setupSplitRange(input, current, next, startInput, endInput, prev2,
				prev3);
		String possibleStart = processStartTime(startInput, index1);
		String possibleEnd = processEndTime(endInput, index2);
		checkAndAssignSplit(index, input, parsedInput, current, prev, next,
				index1, index2, prev2, prev3, possibleStart, possibleEnd);
	}

	private void processJoined(Index index, String[] input,
			String[] parsedInput, int current, int prev, int next,
			String[] startInput, String[] endInput, Index index1, Index index2) {
		String[] possibleTimes = getSplitPossibleTime(index, input);
		if (possibleTimes.length == Constants.MAX_TIME_TYPES) {
			setupJoinedRange(input, prev, next, startInput, endInput,
					possibleTimes);

			String possibleStart = processStartTime(startInput, index1);
			String possibleEnd = processEndTime(endInput, index2);
			checkAndAssignJoined(index, input, parsedInput, current, prev,
					next, index1, index2, possibleStart, possibleEnd);
		}
	}

	private String[] getSplitPossibleTime(Index index, String[] input) {
		String[] possibleTimes = input[index.getValue()].split(Constants.DASH);
		return possibleTimes;
	}

	private boolean isJoinedByDash(Index index, String[] input, int current) {
		return isIndexValid(current, input) && input[current] != null
				&& input[index.getValue()].contains(Constants.DASH);
	}

	private void checkAndAssignJoined(Index index, String[] input,
			String[] parsedInput, int current, int prev, int next,
			Index index1, Index index2, String possibleStart, String possibleEnd) {
		if (isBothTimeValid(possibleStart, possibleEnd)) {
			if (!isValidRange(possibleStart, possibleEnd)) {
				assignErrorMsg(parsedInput, Constants.ERROR_TIME_RANGE);
			} else {
				assignJoined(index, input, parsedInput, current, prev, next,
						index1, index2, possibleStart, possibleEnd);
			}
		} else if (possibleStart != null && possibleEnd != null) {
			assignErrorMsg(parsedInput, Constants.ERROR_FOUND_TIME);
		}
	}

	private void checkAndAssignSplit(Index index, String[] input,
			String[] parsedInput, int current, int prev, int next,
			Index index1, Index index2, int prev2, int prev3,
			String possibleStart, String possibleEnd) {
		if (isBothTimeValid(possibleStart, possibleEnd)) {
			if (!isValidRange(possibleStart, possibleEnd)) {
				assignErrorMsg(parsedInput, Constants.ERROR_TIME_RANGE);
			} else {
				assignSplit(index, input, parsedInput, current, prev, next,
						index1, index2, prev2, prev3, possibleStart,
						possibleEnd);
			}

		} else if (possibleStart != null && possibleEnd != null) {
			assignErrorMsg(parsedInput, Constants.ERROR_FOUND_TIME);
		}
	}

	private void assignSplit(Index index, String[] input, String[] parsedInput,
			int current, int prev, int next, Index index1, Index index2,
			int prev2, int prev3, String possibleStart, String possibleEnd) {
		assignSplitNull(index, input, current, prev, next, index1, index2,
				prev2, prev3);
		assignTime(parsedInput, possibleStart, getItemPosition());
		assignTime(parsedInput, possibleEnd, getSecItemPosition());
	}

	private void assignJoined(Index index, String[] input,
			String[] parsedInput, int current, int prev, int next,
			Index index1, Index index2, String possibleStart, String possibleEnd) {
		assignNullJoined(index, input, current, prev, next, index1, index2);
		assignTime(parsedInput, possibleStart, getItemPosition());
		assignTime(parsedInput, possibleEnd, getSecItemPosition());
	}

	private void assignNullJoined(Index index, String[] input, int current,
			int prev, int next, Index index1, Index index2) {
		input[current] = null;
		if (index1.getValue() == 0) {
			input[prev] = null;
			index.setValue(prev);
		}
		if (index2.getValue() != 0) {
			input[next] = null;
		}
	}

	private void setupJoinedRange(String[] input, int prev, int next,
			String[] startInput, String[] endInput, String[] possibleTimes) {
		startInput[1] = possibleTimes[0];
		endInput[0] = possibleTimes[1];
		if (isIndexValid(prev, input)) {
			startInput[0] = input[prev];
		}
		if (isIndexValid(next, input)) {
			endInput[1] = input[next];
		}
	}

	private void assignSplitNull(Index index, String[] input, int current,
			int prev, int next, Index index1, Index index2, int prev2, int prev3) {
		input[current] = null;
		input[prev] = null;
		assignNullJoined(index, input, prev2, prev3, next, index1, index2);
	}

	private void setupSplitRange(String[] input, int current, int next,
			String[] startInput, String[] endInput, int prev2, int prev3) {
		if (isIndexValid(prev3, input)) {
			startInput[0] = input[prev3];
		}
		if (isIndexValid(prev2, input)) {
			startInput[1] = input[prev2];
		}
		if (isIndexValid(next, input)) {
			endInput[1] = input[next];
		}
		if (isIndexValid(current, input)) {
			endInput[0] = input[current];
		}
	}

	private boolean isSplitRange(String[] input, int prev) {
		return isIndexValid(prev, input)
				&& input[prev] != null
				&& (input[prev].equals(Constants.DASH) || input[prev]
						.equalsIgnoreCase(Constants.TO));
	}

	private boolean isBothTimeValid(String possibleStart, String possibleEnd) {
		return isValidTime(possibleStart) && isValidTime(possibleEnd);
	}

	private boolean isValidRange(String possibleStart, String possibleEnd) {
		Date startTime = new Date();
		Date endTime = new Date();
		try {
			startTime = Constants.timeFormatOne.parse(possibleStart);
			endTime = Constants.timeFormatOne.parse(possibleEnd);
		} catch (ParseException e) {
			logger.log(Level.WARNING, Constants.ERROR_PARSING_POSSIBLETIMES);
		}
		if (startTime.compareTo(endTime) < 0) {
			return true;
		}
		return false;
	}

	private String processStartTime(String[] input, Index index) {
		String possibleTime = null;
		int current = index.getValue();
		int prev = current - 1;
		if (isRegexMatch(input[current], Constants.PATTERN_TIME_ONE)) {
			possibleTime = input[current];
		} else if (isRegexMatch(input[current], Constants.PATTERN_TIME_TWO)) {
			possibleTime = formatPatternTwo(input, current);
		} else if (isRegexMatch(input[current], Constants.PATTERN_TIME_THREE)) {
			possibleTime = formatPatternThree(input, current);
		} else if (isSplitPatternTwo(input, prev, current)) {
			possibleTime = formatSplitPatternTwo(input, prev, current);
			index.decrement();
		} else if (isSplitPatternThree(input, prev, current)) {
			possibleTime = formatSplitPatternThree(input, prev, current);
			index.decrement();
		}
		return possibleTime;
	}

	private String processEndTime(String[] input, Index index) {
		String possibleTime = null;
		int current = index.getValue();
		int next = current + 1;
		if (isRegexMatch(input[current], Constants.PATTERN_TIME_ONE)) {
			possibleTime = input[current];
		} else if (isRegexMatch(input[current], Constants.PATTERN_TIME_TWO)) {
			possibleTime = formatPatternTwo(input, current);
		} else if (isRegexMatch(input[current], Constants.PATTERN_TIME_THREE)) {
			possibleTime = formatPatternThree(input, current);
		} else if (isSplitPatternTwo(input, current, next)) {
			possibleTime = formatSplitPatternTwo(input, current, next);
			index.increment();
		} else if (isSplitPatternThree(input, current, next)) {
			possibleTime = formatSplitPatternThree(input, current, next);
			index.increment();
		}
		return possibleTime;
	}

	protected boolean isValidTime(String possibleTime) {
		Constants.timeFormatOne.setLenient(false);
		try {
			Date date = new Date();
			date = Constants.timeFormatOne.parse(possibleTime);
		} catch (ParseException | NullPointerException e) {
			return false;
		}
		return true;
	}

	protected String getPossibleTime(Index index, String[] input) {
		String possibleTime = null;
		int current = index.getValue();
		int next = current + 1;
		if (isRegexMatch(input[current], Constants.PATTERN_TIME_ONE)) {
			possibleTime = input[current];
		} else if (isRegexMatch(input[current], Constants.PATTERN_TIME_TWO)) {
			possibleTime = formatPatternTwo(input, current);
		} else if (isRegexMatch(input[current], Constants.PATTERN_TIME_THREE)) {
			possibleTime = formatPatternThree(input, current);
		} else if (isSplitPatternTwo(input, current, next)) {
			possibleTime = formatSplitPatternTwo(input, current, next);
			index.increment();
		} else if (isSplitPatternThree(input, current, next)) {
			possibleTime = formatSplitPatternThree(input, current, next);
			index.increment();
		}
		return possibleTime;
	}

	private String formatPatternTwo(String[] input, int current) {
		String possibleTime;
		possibleTime = input[current];
		possibleTime = replaceFullStop(possibleTime);
		possibleTime = reformatTimeTwo(possibleTime);
		return possibleTime;
	}

	private String formatPatternThree(String[] input, int current) {
		String possibleTime;
		possibleTime = input[current];
		possibleTime = reformatTimeThree(possibleTime);
		return possibleTime;
	}

	private boolean isSplitPatternTwo(String[] input, int current, int next) {
		return isIndexValid(next, input)
				&& isRegexMatch(input[current] + input[next],
						Constants.PATTERN_TIME_TWO);
	}

	private boolean isSplitPatternThree(String[] input, int current, int next) {
		return isIndexValid(next, input)
				&& isRegexMatch(input[current] + input[next],
						Constants.PATTERN_TIME_THREE);
	}

	private String formatSplitPatternThree(String[] input, int current, int next) {
		String possibleTime;
		possibleTime = input[current] + input[next];
		possibleTime = reformatTimeThree(possibleTime);
		return possibleTime;
	}

	private String formatSplitPatternTwo(String[] input, int current, int next) {
		String possibleTime;
		possibleTime = input[current] + input[next];
		possibleTime = replaceFullStop(possibleTime);
		possibleTime = reformatTimeTwo(possibleTime);
		return possibleTime;
	}

	protected String removeColon(String possibleTime) {
		if (possibleTime.contains(Constants.COLON)) {
			possibleTime = possibleTime.replaceAll(Constants.COLON,
					Constants.EMPTY_STRING);
		}
		return possibleTime;
	}

	protected String reformatTimeTwo(String possibleTime) {
		Constants.timeFormatTwo.setLenient(false);
		Date date = new Date();
		try {
			date = Constants.timeFormatTwo.parse(possibleTime);
		} catch (ParseException e) {
			return Constants.ERROR_INVALID_TIME;
		}
		return Constants.timeFormatOne.format(date);
	}

	protected String reformatTimeThree(String possibleTime) {
		Constants.timeFormatThree.setLenient(false);
		Date date = new Date();
		try {
			date = Constants.timeFormatThree.parse(possibleTime);
		} catch (ParseException e) {
			return Constants.ERROR_INVALID_TIME;
		}
		return Constants.timeFormatOne.format(date);
	}

	protected String replaceFullStop(String possibleTime) {
		if (possibleTime.contains(Constants.FULL_STOP)) {
			possibleTime = possibleTime.replace(Constants.FULL_STOP,
					Constants.COLON);
		}
		return possibleTime;
	}

	@Override
	public String getItemName() {
		return Constants.TIME_NAME;
	}

}
