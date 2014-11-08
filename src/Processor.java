import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@author A0110937J
//The following class is the abstract class for attribute processor

public abstract class Processor {

	protected final String FLOATING_TASK = "ft";

	public abstract void process(String[] parsedInput, String[] input,
			Index index);

	protected boolean isRegexMatch(String input, String regexPattern) {
		Pattern pattern = Pattern.compile(regexPattern);
		try {
			Matcher matchPattern = pattern.matcher(input);
			matchPattern.find();
			if (matchPattern.group().length() != 0) {
				return true;
			}
		} catch (IllegalStateException | NullPointerException e) {
			return false;
		}
		return false;
	}

	protected void assignNull(String[] parsedInput) {
		if (parsedInput[getItemPosition()].equals(Constants.EMPTY_STRING)) {
			parsedInput[getItemPosition()] = null;
		}
	}

	// check if a String is part of the list
	protected boolean isPartOfList(String[] input, Index index, String[] list) {
		for (int i = 0; i < list.length; i++) {
			if (input == null) {
				return false;
			}
			if (isMatchString(input, index, list[i])) {
				return true;
			}
		}
		return false;
	}

	// returns first member of list
	protected String getFirstMember(String[] list) {
		return list[0];
	}

	protected boolean isMatchString(String[] input, Index index, String command) {
		String[] possibleCommand = command.split(Constants.SPACE);
		int startIndex = index.getValue();
		for (int i = 0; i < possibleCommand.length; i++) {
			if (!isIndexValid(index.getValue(), input)) {
				index.setValue(startIndex);
				return false;
			}
			if (!possibleCommand[i].equalsIgnoreCase(input[index.getValue()])) {
				index.setValue(startIndex);
				return false;
			}
			if (i == possibleCommand.length - 1) {
				break;
			}
			index.increment();
		}
		return true;
	}

	protected boolean isIndexValid(int index, String[] input) {
		if (index >= input.length || index < 0) {
			return false;
		} else {
			return true;
		}
	}

	protected void assignErrorMsg(String[] parsedInput, String message) {
		parsedInput[Constants.ERROR_MSG_POSITION] = message;
		parsedInput[Constants.COMMAND_POSITION] = Constants.ERROR;
	}

	// check if a String is part of the list
	protected boolean isPartOfList(String input, String[] list) {
		for (int i = 0; i < list.length; i++) {
			if (input == null) {
				return false;
			}
			if (input.equalsIgnoreCase(list[i])) {
				return true;
			}
		}
		return false;
	}

	protected boolean isEmpty(String taskName) {
		return taskName.length() == 0;
	}

	protected boolean isNull(String str) {
		if (str == null) {
			return true;
		} else {
			return false;
		}
	}

	public abstract int getItemPosition();

	public abstract String getItemName();

	// sets up the string before the processors process
	public void processAfter(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		putAllFullStop(input, fullStopArr);
	}

	protected static void putAllFullStop(String[] input,
			ArrayList<Integer> fullStopArr) {
		int origin;
		for (int i = fullStopArr.size() - 1; i >= 0; i--) {
			int position = fullStopArr.get(i);
			origin = position;
			if (input[position] == null) {
				position = getPossibleLastWord(input, position);
				if (position < 0) {
					break;
				}
				i = removeCollision(fullStopArr, i, position);
				putFullStop(input, position, origin);
			} else {
				putFullStop(input, position, origin);
			}
		}
	}

	private static int getPossibleLastWord(String[] input, int position) {
		while (position >= 0 && input[position] == null) {
			position--;
		}
		return position;
	}

	private static int removeCollision(ArrayList<Integer> fullStopArr, int i,
			int position) {
		for (int j = i - 1; j >= 0; j--) {
			if (fullStopArr.get(j) >= position) {
				fullStopArr.remove(j);
				i--;
			}
		}
		return i;
	}

	private static void putFullStop(String[] input, int position, int origin) {
		if (origin == position) {
			input[position] = input[position] + Constants.FULL_STOP;
		} else if (!isLastWord(input, position)) {
			input[position] = input[position] + Constants.FULL_STOP;
		}

	}

	protected static void removeAllFullStop(String[] input,
			ArrayList<Integer> fullStopArr) {
		for (int i = 0; i < input.length; i++) {
			if (isLastWord(input, i)) {
				removeFullStop(input, i);
				fullStopArr.add(i);
			}
		}
	}

	private static boolean isLastWord(String[] input, int index) {
		if (input[index] == null) {
			return false;
		} else if (input[index].length() == 0) {
			return false;
		} else if (input[index].charAt(input[index].length() - 1) == Constants.CHAR_FULL_STOP) {
			return true;
		} else {
			return false;
		}
	}

	private static void removeFullStop(String[] input, int index) {
		input[index] = input[index].substring(0, input[index].length() - 1);
		if (input[index].length() == 0) {
			input[index] = null;
		}
	}

	public void processBefore(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		removeAllFullStop(input, fullStopArr);
	}

}

class TaskProcessor extends DetailsProcessor {
	public int getItemPosition() {
		return Constants.TASK_NAME_POSITION;
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

	@Override
	public void processBefore(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		parsedInput[getItemPosition()] = Constants.EMPTY_STRING;
	}

	@Override
	protected void removeSlashes(String[] input, Index index) {
		if (input[index.getValue()].length() > 0
				&& input[index.getValue()].charAt(0) == Constants.CHAR_SLASH) {
			input[index.getValue()] = input[index.getValue()].substring(1,
					input[index.getValue()].length());
		}
	}

	protected void checkAndAssignTask(String[] parsedInput, String taskName) {
		if (!isEmpty(taskName)) {
			parsedInput[Constants.TASK_NAME_POSITION] = taskName;
		}
	}

	@Override
	public String getItemName() {
		return Constants.TASK_NAME;
	}
}

class SingleParaProcessor extends Processor {
	public void process(String[] parsedInput, String[] input, Index index) {
		setupParameter(input, index.getValue());
		parseRange(input, index, parsedInput);
		if (isIndexValid(index.getValue(), input)
				&& isInteger(input[index.getValue()])) {
			assignPara(parsedInput, index, input);
		}
	}

	// For overriding by other classes
	protected void parseRange(String[] input, Index index, String[] parsedInput) {
	}

	// For overriding by other classes
	protected void setupParameter(String[] input, int index) {
	}

	protected void assignPara(String[] parsedInput, Index index, String[] input) {
		parsedInput[getItemPosition()] = Integer.toString(Integer
				.parseInt(input[index.getValue()]));
		index.increment();
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
			if (isIndexValid(index.getValue() + 1, input)
					&& input[index.getValue() + 1] != null
					&& input[index.getValue() + 1].equals(Constants.DASH)) {
				if (isIndexValid(index.getValue() + 2, input)
						&& isInteger(input[index.getValue()])
						&& isInteger(input[index.getValue() + 2])) {
					String[] temp = new String[Constants.LIMIT_RANGE_PARA];
					temp[0] = input[index.getValue()];
					temp[1] = input[index.getValue() + 2];
					index.incrementByTwo();
					processRange(index, parsedInput, startIndex, temp, input);
				} else {
					assignErrorMsg(parsedInput,
							Constants.ERROR_INVALID_PARAMETER);
					index.setValue(startIndex);
				}
			} else if (isIndexValid(index.getValue(), input)
					&& input[index.getValue()] != null
					&& input[index.getValue()].charAt(input[index.getValue()]
							.length() - 1) == '-') {
				input[index.getValue()] = input[index.getValue()].substring(0,
						input[index.getValue()].length() - 1);
				if (isIndexValid(index.getValue() + 1, input)
						&& isInteger(input[index.getValue()])
						&& isInteger(input[index.getValue() + 1])) {
					String[] temp = new String[Constants.LIMIT_RANGE_PARA];
					temp[0] = input[index.getValue()];
					temp[1] = input[index.getValue() + 1];
					index.increment();
					processRange(index, parsedInput, startIndex, temp, input);
				} else {
					assignErrorMsg(parsedInput,
							Constants.ERROR_INVALID_PARAMETER);
					index.setValue(startIndex);
				}
			} else if (isIndexValid(index.getValue() + 1, input)
					&& input[index.getValue() + 1] != null
					&& input[index.getValue() + 1].charAt(0) == '-') {
				input[index.getValue() + 1] = input[index.getValue() + 1]
						.substring(1, input[index.getValue() + 1].length());
				if (isIndexValid(index.getValue() + 1, input)
						&& isInteger(input[index.getValue()])
						&& isInteger(input[index.getValue() + 1])) {
					String[] temp = new String[Constants.LIMIT_RANGE_PARA];
					temp[0] = input[index.getValue()];
					temp[1] = input[index.getValue() + 1];
					index.increment();
					processRange(index, parsedInput, startIndex, temp, input);
				} else {
					assignErrorMsg(parsedInput,
							Constants.ERROR_INVALID_PARAMETER);
					index.setValue(startIndex);
				}
			} else if (isIndexValid(index.getValue(), input)
					&& input[index.getValue()] != null
					&& input[index.getValue()].contains(Constants.DASH)) {
				String[] temp = input[index.getValue()].split(Constants.DASH);
				if (temp.length == Constants.LIMIT_RANGE_PARA
						&& (isInteger(temp[0]) && isInteger(temp[1]))) {

					processRange(index, parsedInput, startIndex, temp, input);
				} else {
					assignErrorMsg(parsedInput, Constants.ERROR_PARA_RANGE);
					index.setValue(startIndex);
				}
			}
		}
	}

	private void assignNullInput(String[] input, Index index, int startIndex) {
		for (int i = startIndex; i <= index.getValue(); i++) {
			input[i] = null;
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
			if (input[index].contains(Constants.TO)) {
				input[index] = input[index].replaceAll(Constants.TO,
						Constants.DASH);
			}
			if (input[index].length() != 0
					&& input[index].charAt(input[index].length() - 1) == Constants.CHAR_COMMA) {
				input[index] = input[index].substring(0,
						input[index].length() - 1);
			} else if (input[index].equals(Constants.COMMA)) {
				input[index] = null;
			}
			if (input[index].length() == 0) {
				input[index] = null;
			}
		}
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
		parsedInput[getItemPosition()] = parsedInput[getItemPosition()]
				+ Constants.SPACE
				+ Integer.toString(Integer.parseInt(input[index.getValue()]));
		input[index.getValue()] = null;
	}
}

class DetailsProcessor extends Processor {
	private static final String NAME_DETAILS = null;

	@Override
	public int getItemPosition() {
		return Constants.DETAILS_POSITION;
	}

	@Override
	public void processBefore(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		while (isIndexValid(index.getValue(), input)) {
			if (isPartOfList(input[index.getValue()], Constants.LIST_DETAILS)) {
				input[index.getValue()] = null;
				parsedInput[getItemPosition()] = Constants.EMPTY_STRING;
				index.increment();
				break;
			}
			index.increment();
		}
	}

	@Override
	public void processAfter(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		if (parsedInput[getItemPosition()] != null) {
			parsedInput[getItemPosition()] = parsedInput[getItemPosition()]
					.trim();
		}
	}

	@Override
	public void process(String[] parsedInput, String[] input, Index index) {
		if (input[index.getValue()] != null) {
			removeSlashes(input, index);
			parsedInput[getItemPosition()] = parsedInput[getItemPosition()]
					+ input[index.getValue()] + Constants.SPACE;
			input[index.getValue()] = null;
		}
	}

	protected void removeSlashes(String[] input, Index index) {
	}

	@Override
	public String getItemName() {
		return NAME_DETAILS;
	}
}

class ImportanceProcessor extends Processor {

	public int getItemPosition() {
		return Constants.IMPT_POSITION;
	}

	public void process(String[] parsedInput, String[] input, Index index) {
		int importance = Constants.NUM_INVALID;
		if (isIndexValid(index.getValue(), input)
				&& (isPartOfList(input[index.getValue()],
						Constants.LIST_IMPORTANCE))) {

			if (isIndexValid(index.getValue() + 1, input)) {
				if (isNextInteger(input, index.getValue())) {
					try {
						importance = Integer
								.parseInt(input[index.getValue() + 1]);
					} catch (Exception e) {

					}
					if (importance < Constants.LOW_IMPT_LVL
							|| importance > Constants.HIGH_IMPT_LVL) {
						assignErrorMsg(parsedInput,
								Constants.ERROR_INVALID_IMPORTANCE);
					} else {
						parsedInput[Constants.IMPT_POSITION] = Integer
								.toString(importance);
						index.incrementByTwo();
					}

				}
			}
		}
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

						assignErrorMsg(parsedInput, Constants.ERROR_INVALID_TIME);
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
						.equalsIgnoreCase(FLOATING_TASK);
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
		if (isIndexValid(current, input) && input[current] != null
				&& input[index.getValue()].contains(Constants.DASH)) {
			String[] possibleTimes = input[index.getValue()]
					.split(Constants.DASH);
			if (possibleTimes.length == Constants.MAX_TIME_TYPES) {

				startInput[1] = possibleTimes[0];
				endInput[0] = possibleTimes[1];
				if (isIndexValid(prev, input)) {
					startInput[0] = input[prev];
				}
				if (isIndexValid(next, input)) {
					endInput[1] = input[next];
				}

				String possibleStart = processStartTime(startInput, index1);
				String possibleEnd = processEndTime(endInput, index2);
				if (isBothTimeValid(possibleStart, possibleEnd)) {
					if (!isValidRange(possibleStart, possibleEnd)) {
						assignErrorMsg(parsedInput, Constants.ERROR_TIME_RANGE);
					} else {
						input[current] = null;
						if (index1.getValue() == 0) {
							input[prev] = null;
							index.setValue(prev);
						}
						if (index2.getValue() != 0) {
							input[next] = null;
						}
						assignTime(parsedInput, possibleStart,
								getItemPosition());
						assignTime(parsedInput, possibleEnd,
								getSecItemPosition());
					}
				} else if (possibleStart != null && possibleEnd != null) {
					assignErrorMsg(parsedInput,Constants.ERROR_FOUND_TIME);
				}
			}
		} else if (isIndexValid(prev, input)
				&& input[prev] != null
				&& (input[prev].equals(Constants.DASH) || input[prev]
						.equalsIgnoreCase(Constants.TO))) {
			int prev2 = index.getValue() - 2;
			int prev3 = index.getValue() - 3;

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
			String possibleStart = processStartTime(startInput, index1);
			String possibleEnd = processEndTime(endInput, index2);
			if (isBothTimeValid(possibleStart, possibleEnd)) {
				if (!isValidRange(possibleStart, possibleEnd)) {
					assignErrorMsg(parsedInput, Constants.ERROR_TIME_RANGE);
				} else {
					input[current] = null;
					input[prev] = null;
					input[prev2] = null;
					if (index1.getValue() == 0) {
						input[prev3] = null;
						index.setValue(prev3);
					}
					if (index2.getValue() != 0) {
						input[next] = null;
					}
					assignTime(parsedInput, possibleStart, getItemPosition());
					assignTime(parsedInput, possibleEnd, getSecItemPosition());
				}

			} else if (possibleStart != null && possibleEnd != null) {
				assignErrorMsg(parsedInput, Constants.ERROR_FOUND_TIME);
			}
		}

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

class DateProcessor extends Processor {


	public int getItemPosition() {
		return Constants.DATE_POSITION;
	}

	private static Logger logger = Logger.getLogger(Constants.DATE_PROCESSOR);

	public void process(String[] parsedInput, String[] input, Index index) {

		int initialIndex = index.getValue();
		if (isIndexValid(index.getValue(), input)) {
			String possibleDate = getPossibleDate(index, input);
			if (possibleDate == null) {
				index.setValue(initialIndex);
			} else if (possibleDate.equalsIgnoreCase(FLOATING_TASK)) {
				parsedInput[Constants.DATE_POSITION] = FLOATING_TASK;
				index.increment();
			} else if (validateDate(possibleDate)) {
				possibleDate = addZeroes(possibleDate);
				assignDate(parsedInput, index, possibleDate);
			} else {
				assignErrorMsg(parsedInput, Constants.INVALID_DATE);
				index.setValue(initialIndex);
			}
		}
	}

	// Method adds zeroes in the date where needed
	private String addZeroes(String possibleDate) {
		Date date = new Date();
		try {
			date = Constants.dateFormat.parse(possibleDate);
		} catch (ParseException e) {
			logger.log(Level.WARNING, Constants.ERROR_ADDING_ZEROES);
		}
		return Constants.dateFormat.format(date);
	}

	protected boolean isDateValid(Date date, Date today) {
		return true;
	}

	protected void assignDate(String[] parsedInput, Index index,
			String possibleDate) {
		parsedInput[Constants.DATE_POSITION] = possibleDate;
		index.increment();
	}

	protected String getPossibleDate(Index index, String[] input) {

		String possibleDate = null;

		if (isDateFormatOne(index.getValue(), input)) {
			possibleDate = formatDate(index.getValue(), input,
					new FirstDateFormatter());
		} else if (isSpelledDateOne(index.getValue(), input)) {
			if (isRegexMatch(input[index.getValue()],
					Constants.PATTERN_DATE_TWO)) {
				possibleDate = formatDate(index.getValue(), input,
						new SpelledDateOneFormatter());
				index.incrementByTwo();
			} else {
				possibleDate = Constants.INVALID_DATE;
			}
		} else if (isSpelledDateTwo(index.getValue(), input)) {
			if (isRegexMatch(input[index.getValue()],
					Constants.PATTERN_DATE_TWO)) {
				possibleDate = formatDate(index.getValue(), input,
						new SpelledDateTwoFormatter());
				index.increment();
			} else {
				possibleDate = Constants.INVALID_DATE;
			}
		} else if (isSpelledDay(index.getValue(), input)) {
			possibleDate = formatDate(index.getValue(), input,
					new SpelledDayFormatter());
		} else if (isFloatingTask(index.getValue(), input)) {
			possibleDate = FLOATING_TASK;
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_TODAY)) {
			possibleDate = formatDate(index.getValue(), input,
					new DateFormatter());
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_TMR)) {
			possibleDate = formatDate(index.getValue(), input,
					new TmrDateFormatter());
		} else {
			possibleDate = assignDate(input, index);
		}
		return possibleDate;

	}

	protected String assignDate(String[] input, Index index) {
		return null;
	}

	protected boolean isDateFormatOne(Integer index, String[] input) {
		return isRegexMatch(input[index], Constants.PATTERN_DATE_ONE);
	}

	protected boolean isFloatingTask(int value, String[] input) {
		if (input[value] == null) {
			return false;
		}
		if (input[value].equalsIgnoreCase(FLOATING_TASK)) {
			return true;
		}
		return false;
	}

	protected boolean isSpelledDateOne(Integer index, String[] input) {
		if (isRegexMatch(input[index], Constants.PATTERN_DATE_POSSIBLE)
				&& (isIndexValid(index + 1, input) && isPartOfList(
						input[index + 1], Constants.LIST_MONTHS))
				&& isIndexValid(index + 2, input)
				&& isRegexMatch(input[index + 2], Constants.PATTERN_YEAR)) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean validateDate(String possibleDate) {

		Constants.dateFormat.setLenient(false);
		try {
			Date date = Constants.dateFormat.parse(possibleDate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	protected boolean isSpelledDay(Integer index, String[] input) {
		if (isPartOfList(input[index], Constants.LIST_DAYS)) {
			return true;
		}
		return false;
	}

	protected boolean isSpelledDateTwo(Integer index, String[] input) {
		if (isRegexMatch(input[index], Constants.PATTERN_DATE_POSSIBLE)
				&& (isIndexValid(index + 1, input) && isPartOfList(
						input[index + 1], Constants.LIST_MONTHS))) {
			return true;
		}
		return false;
	}

	protected String formatDate(Integer index, String[] input,
			DateFormatter formatter) {
		return formatter.formatDate(index, input);
	}

	@Override
	public String getItemName() {
		return Constants.DATE_NAME;
	}
}

class AutoDateProcessor extends DateProcessor {
	@Override
	public void processAfter(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		putAllFullStop(input, fullStopArr);
		if (parsedInput[Constants.ERROR_MSG_POSITION] == null) {
			String possibleDate = null;
			if (parsedInput[Constants.DATE_POSITION] == null) {
				possibleDate = formatDate(index.getValue(), input,
						new DateFormatter());
				if (validateDate(possibleDate)) {
					assignDate(parsedInput, index, possibleDate);
				}
				index.decrement();
			}
		}
	}
}

class NaturalProcessor {

	public void process(String[] parsedInput, String[] input, Index index,
			Processor processor) {
		index.reset();
		ArrayList<Integer> fullStopArr = new ArrayList<Integer>();

		processor.processBefore(input, fullStopArr, parsedInput, index);
		if (processor instanceof TaskProcessor
				|| processor instanceof DetailsProcessor
				|| processor instanceof MultiParaProcessor) {
			collectProcess(parsedInput, input, index, processor);
		} else if (processor instanceof DateProcessor
				|| processor instanceof TimeProcessor) {
			backwardProcess(parsedInput, input, index, processor);
		} else {

			while (isIndexValid(index.getValue(), input)) {
				int startPosition = index.getValue();
				processor.process(parsedInput, input, index);
				if (parsedInput[Constants.ERROR_MSG_POSITION] != null) {
					break;
				} else if (parsedInput[processor.getItemPosition()] != null) {
					for (int i = startPosition; i < index.getValue(); i++) {
						input[i] = null;
					}
					cleanWordBackward(input, startPosition - 1, processor);
					cleanWordForward(input, index.getValue(), processor);
					break;
				}
				index.increment();
			}
		}
		processor.processAfter(input, fullStopArr, parsedInput, index);
		assignErrorMsg(parsedInput, processor);
	}

	private void cleanWordForward(String[] input, int index, Processor processor) {
		if (processor instanceof CommandProcessor) {
			while (isIndexValid(index, input) && input[index] != null
					&& isPartOfList(input[index], Constants.LIST_REMOVABLES)) {
				input[index] = null;
				index++;
			}
		}

	}

	private void collectProcess(String[] parsedInput, String[] input,
			Index index, Processor processor) {
		while (isIndexValid(index.getValue(), input)) {
			processor.process(parsedInput, input, index);
			index.increment();
		}
	}

	private void backwardProcess(String[] parsedInput, String[] input,
			Index index, Processor processor) {
		index.setValue(input.length - 1);
		while (isIndexValid(index.getValue(), input)) {
			int startPosition = index.getValue();
			processor.process(parsedInput, input, index);
			if (parsedInput[Constants.ERROR_MSG_POSITION] != null) {
				break;
			} else if (parsedInput[processor.getItemPosition()] != null) {
				if (index.getValue() <= startPosition) {
					cleanWordBackward(input, index.getValue(), processor);
				} else {
					for (int i = startPosition; i < index.getValue(); i++) {
						input[i] = null;
					}
					cleanWordBackward(input, startPosition - 1, processor);
					break;
				}
			}
			index.decrement();
		}
	}

	protected void assignErrorMsg(String[] parsedInput, Processor processor) {
	}

	private void cleanWordBackward(String[] input, int index,
			Processor processor) {
		if (processor instanceof DateProcessor
				|| processor instanceof TimeProcessor
				|| processor instanceof ImportanceProcessor
				|| processor instanceof CommandProcessor) {
			while (isIndexValid(index, input)
					&& isPartOfList(input[index], Constants.LIST_REMOVABLES)
					&& input[index] != null) {
				input[index] = null;
				index--;
			}
		}

	}

	protected boolean isIndexValid(int index, String[] input) {
		if (index >= input.length || index < 0) {
			return false;
		} else {
			return true;
		}
	}

	protected boolean isPartOfList(String input, String[] list) {
		for (int i = 0; i < list.length; i++) {
			if (input == null) {
				return false;
			}
			if (input.equalsIgnoreCase(list[i])) {
				return true;
			}
		}
		return false;

	}
}

class StrictNaturalProcessor extends NaturalProcessor {
	@Override
	protected void assignErrorMsg(String[] parsedInput, Processor processor) {
		if (parsedInput[processor.getItemPosition()] == null
				&& parsedInput[Constants.ERROR_MSG_POSITION] == null) {
			assignErrorMsg(
					parsedInput,
					String.format(Constants.ERROR_EMPTY_ITEM,
							processor.getItemName()));
		}
	}

	protected void assignErrorMsg(String[] parsedInput, String message) {
		parsedInput[Constants.ERROR_MSG_POSITION] = message;
		parsedInput[Constants.COMMAND_POSITION] = Constants.ERROR;
	}
}

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

class CommandProcessor extends Processor {

	public void process(String[] parsedInput, String[] input, Index index) {
		if (isPartOfList(input[index.getValue()], Constants.LIST_DETAILS)) {
			index.setValue(input.length);
		} else {
			assignIfPossible(parsedInput, input, index);
		}
	}

	private void assignAddIfPossible(String[] parsedInput, String[] input,
			Index index) {
		if (parsedInput[Constants.COMMAND_POSITION] == null) {
			assignCommand(parsedInput, input, index,
					getFirstMember(Constants.LIST_ADD));
		}
	}

	public void processAfter(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		putAllFullStop(input, fullStopArr);
		assignAddIfPossible(parsedInput, input, index);
	}

	private void assignIfPossible(String[] parsedInput, String[] input,
			Index index) {
		if (isIndexValid(index.getValue(), input)) {
			if (parsedInput[Constants.COMMAND_POSITION] == null) {
				String command = getCommand(input, index, parsedInput);
				if (command != null) {
					assignCommand(parsedInput, input, index, command);
				}
			}
		}
	}

	private void assignCommand(String[] parsedInput, String[] input,
			Index index, String command) {
		parsedInput[Constants.COMMAND_POSITION] = command;
		index.increment();
	}

	private String getCommand(String[] input, Index index, String[] parsedInput) {

		if (isPartOfList(input[index.getValue()], Constants.LIST_ADD)) {
			return getFirstMember(Constants.LIST_ADD);
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_EDIT)) {
			return getFirstMember(Constants.LIST_EDIT);
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_SEARCH)) {
			return getFirstMember(Constants.LIST_SEARCH);
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_UNDO)) {
			return getFirstMember(Constants.LIST_UNDO);
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_REDO)) {
			return getFirstMember(Constants.LIST_REDO);
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_EXIT)) {
			return getFirstMember(Constants.LIST_EXIT);
		} else if (isSpecialCommand(input, index, Constants.LIST_CLEAR_ARCHIVE,
				parsedInput)) {
			return getFirstMember(Constants.LIST_CLEAR_ARCHIVE);
		} else if (isSpecialCommand(input, index, Constants.LIST_CLEAR,
				parsedInput)) {
			return getFirstMember(Constants.LIST_CLEAR);
		} else if (isSpecialCommand(input, index, Constants.LIST_SHOW_FLOATING,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_FLOATING);
		} else if (isSpecialCommand(input, index, Constants.LIST_SHOW_ALL,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_ALL);
		} else if (isSpecialCommand(input, index, Constants.LIST_SHOW_DETAILS,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_DETAILS);
		} else if (isSpecialCommand(input, index, Constants.LIST_HIDE_DETAILS,
				parsedInput)) {
			return getFirstMember(Constants.LIST_HIDE_DETAILS);
		} else if (isSpecialCommand(input, index, Constants.LIST_SORT_IMPT,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SORT_IMPT);
		} else if (isSpecialCommand(input, index, Constants.LIST_SORT_ALPHA,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SORT_ALPHA);
		} else if (isSpecialCommand(input, index, Constants.LIST_SORT_TIME,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SORT_TIME);
		} else if (isSpecialCommand(input, index, Constants.LIST_DELETE_ALL,
				parsedInput)) {
			return getFirstMember(Constants.LIST_DELETE_ALL);
		} else if (isSpecialCommand(input, index, Constants.LIST_DELETE_PAST,
				parsedInput)) {
			return getFirstMember(Constants.LIST_DELETE_PAST);
		} else if (isSpecialCommand(input, index, Constants.LIST_DELETE_TODAY,
				parsedInput)) {
			return getFirstMember(Constants.LIST_DELETE_TODAY);
		} else if (isPartOfList(input, index, Constants.LIST_DELETE_DATE)) {
			return getFirstMember(Constants.LIST_DELETE_DATE);
		} else if (isSpecialCommand(input, index, Constants.LIST_SHOW_TODAY,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_TODAY);
		} else if (isSpecialCommand(input, index, Constants.LIST_SHOW_WEEK,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_WEEK);
		} else if (isSpecialCommand(input, index,
				Constants.LIST_SHOW_THIS_WEEK, parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_THIS_WEEK);
		} else if (isSpecialCommand(input, index,
				Constants.LIST_SHOW_NEXT_WEEK, parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_NEXT_WEEK);
		} else if (isSpecialCommand(input, index, Constants.LIST_VIEW_ARCHIVE,
				parsedInput)) {
			return getFirstMember(Constants.LIST_VIEW_ARCHIVE);
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_DELETE)) {
			return getFirstMember(Constants.LIST_DELETE);
		}
		return null;
	}

	// check if a String is part of the list
	private boolean isSpecialCommand(String[] input, Index index,
			String[] list, String[] parsedInput) {
		for (int i = 0; i < list.length; i++) {
			if (input == null) {
				return false;
			}
			if (isFirstInstance(index.getValue())
					&& isMatchString(input, index, list[i])) {
				if (isRestEmpty(input, index.getValue())) {
					return true;
				} else {
					assignErrorMsg(parsedInput, Constants.ERROR_MSG_SPECIAL_COM);
				}
			}
		}
		return false;
	}

	private boolean isFirstInstance(int index) {
		return index == 0;
	}

	private boolean isRestEmpty(String[] input, int index) {
		if ((input.length - 1) == index) {
			return true;
		}
		return false;
	}

	@Override
	public int getItemPosition() {
		return Constants.COMMAND_POSITION;
	}

	@Override
	public String getItemName() {
		return Constants.COMMAND_NAME;
	}

}
