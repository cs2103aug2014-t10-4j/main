import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	private static final int RESET = 0;
	private static final String INVALID_PARAMETER = "Invalid parameter";
	private static final String EMPTY_DETAILS_MSG = "No details were found. Please input your details";
	private static final String INVALID_IMPORTANCE = "invalid importance level";

	private static final String FLOATING_TASK = "ft";
	// patterns for matching
	private static final String PATTERN_YEAR = "^\\d{4}$|^\\d{2}$";
	private static final String PATTERN_DATE_TWO = "^([2-3]?[1][s][t])$|^([2]?[2][n][d])$|^([2]?[3][r][d])$|^([1][0-9][t][h])$|^([2]?[4-9][t][h])$|^([0-2]?[0-9])$|^([3][0-1])$|^(([2]|[3])[0][t][h])$";
	private static final String PATTERN_DATE_POSSIBLE = "^\\d{1,2}(([t][h])|([s][t])|([n][d])|([r][d]))?$";
	private static final String PATTERN_DATE_ONE = "^(\\d{1,2})([/.-])(\\d{1,2})([/.-])((\\d{4})|(\\d{2}))$";
	private static final String PATTERN_TIME_ONE = "^\\d{1,2}[:]\\d{2}$";
	private static final String EMPTY_TASKNAME_MSG = "No task name found. Please enter a task name.";
	private static final String PATTERN_TIME_THREE = "^\\d{1,2}([a][m]|[p][m])$";
	private static final String PATTERN_TIME_TWO = "^\\d{1,2}([:.](\\d{2}))(([a][m])|([p][m]))$";
	// Commands
	private static final String COM_ADD = "add";
	private static final String COM_SEARCH = "search";
	private static final String COM_EDIT = "edit";
	private static final String COM_DELETE = "delete";
	private static final String COM_DISPLAY = "display";
	private static final String COM_REDO = "redo";
	private static final String COM_UNDO = "undo";

	protected static final String INVALID_TIME = "invalid time";
	private static final String ERROR = "error";
	private static final String INVALID_DATE = "invalid Date";
	private static final int MAX_TYPES = 8;
	// List of commands
	private static final String[] LIST_ADD = { "/a", "/add" };
	private static final String[] LIST_DELETE = { "/delete", "/d" };
	private static final String[] LIST_EDIT = { "/edit", "/e" };
	private static final String[] LIST_SEARCH = { "/search", "/s" };
	private static final String[] LIST_IMPORTANCE = { "/i", "/importance" };
	private static final String[] LIST_DETAILS = { "/details", "/dtl" };
	private static final String[] LIST_UNDO = { "/undo", "/u" };
	private static final String[] LIST_REDO = { "/redo", "/r" };
	private static final String[] LIST_DISPLAY = { "/dly", "/display" };
	private static final String[] LIST_MONTHS = { "january", "jan", "february",
			"feb", "march", "mar", "april", "apr", "may", "june", "jun",
			"july", "jul", "august", "aug", "september", "sept", "november",
			"oct", "october", "nov", "december", "dec" };

	private static final String[] LIST_DAYS = { "sunday", "sun", "saturday",
			"sat", "mon", "monday", "tuesday", "tues", "wed", "wednesday",
			"thurs", "thursday", "fri", "friday", "saturday", "sat" };
	// Position of various inputs
	private static final int COMMAND_POSITION = 0;
	private static final int TASK_NAME_POSITION = 1;
	private static final int DATE_POSITION = 2;
	private static final int TIME_POSITION = 3;
	private static final int DETAILS_POSITION = 4;
	private static final int IMPT_POSITION = 5;
	private static final int ERROR_MSG_POSITION = 6;
	private static final int PARAMETER_POSITION = 7;

	// date formats
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy");
	private static final SimpleDateFormat finalDateFormat = new SimpleDateFormat(
			"ddMMyyyy");
	private static final SimpleDateFormat timeFormatOne = new SimpleDateFormat(
			"HH:mm");
	private static final SimpleDateFormat timeFormatTwo = new SimpleDateFormat(
			"hh:mma");
	private static final SimpleDateFormat timeFormatThree = new SimpleDateFormat(
			"hha");
	private static final SimpleDateFormat fullDateFormat = new SimpleDateFormat(
			"ddMMyyyyHH:mm");

	public static String[] parseInput(String inputFromUser) {
		String[] parsedInput = new String[MAX_TYPES];
		String[] input = inputFromUser.trim().split(" ");
		Index index = new Index();

		parsedInput[COMMAND_POSITION] = getCommand(input, index);

		/*
		 * if (parsedInput[COMMAND_POSITION] == null) {
		 * processNaturalCommand(parsedInput, input, index); } else
		 */if (parsedInput[COMMAND_POSITION].equals(COM_ADD)) {

			processFutureTask(parsedInput, input, index);
		} else if (parsedInput[COMMAND_POSITION].equals(COM_SEARCH)) {
			processFutureTask(parsedInput, input, index);
		} else if (parsedInput[COMMAND_POSITION].equals(COM_DELETE)) {
			processMultiParameter(parsedInput, input, index);
		} else if (parsedInput[COMMAND_POSITION].equals(COM_EDIT)) {
			processOneParameter(parsedInput, input, index);
			processFutureTask(parsedInput, input, index);
		}

		return parsedInput;

	}

	private static void processMultiParameter(String[] parsedInput,
			String[] input, Index index) {
		index.increment();
		input = sortAndCheck(parsedInput, input, index);
		parsedInput[PARAMETER_POSITION] = formatParameters(input, index);

	}

	private static String formatParameters(String[] input, Index index) {
		String finalParameters = new String("");
		while (isIndexValid(index.getValue(), input)) {
			finalParameters = finalParameters + " "+ input[index.getValue()];
			index.increment();
		}
		return finalParameters.trim();
	}

	private static String[] sortAndCheck(String[] parsedInput, String[] input,
			Index index) {
		if (index.getValue() == (input.length - 1)) {
			try {
				Integer.parseInt(input[index.getValue()]);
			} catch (Exception e) {
				assignErrorMsg(parsedInput, INVALID_PARAMETER);
			}
		}
		sortItems(parsedInput, input, index);
		return input;
	}

	private static void sortItems(String[] parsedInput, String[] input,
			Index index) {
		for (int j = index.getValue(); j < input.length; j++) {
			for (int i = index.getValue(); i < input.length - 1; i++) {
				checkAndSwapItems(parsedInput, input, i);
			}
		}
	}

	private static void checkAndSwapItems(String[] parsedInput, String[] input,
			int i) {
		try {
			if (Integer.parseInt(input[i]) < Integer
					.parseInt(input[i + 1])) {
				swapItem(input, i);
			}
		} catch (Exception e) {
			assignErrorMsg(parsedInput, INVALID_PARAMETER);
		}
	}

	private static void swapItem(String[] input, int i) {
		String temp = input[i];
		input[i] = input[i + 1];
		input[i + 1] = temp;
	}

	/*
	 * private static void processNaturalCommand(String[] parsedInput, String[]
	 * input, Index index) { // process command naturally while
	 * (isIndexValid(index.getValue(), input)) { String command =
	 * getCommand(input, index); if (command == null) { command =
	 * getActualCommand(input, index); } if (command != null) {
	 * parsedInput[COMMAND_POSITION] = input[index.getValue()];
	 * input[index.getValue()] = null; break; } index.increment(); } // process
	 * add commands naturally if (parsedInput[COMMAND_POSITION] == null) {
	 * assignErrorMsg(parsedInput, "No commands found. Please enter a command");
	 * } else if (parsedInput[COMMAND_POSITION].equals(COM_ADD)) { // process
	 * natural date index.setValue(RESET); while (isIndexValid(index.getValue(),
	 * input)) { int startingIndex = index.getValue();
	 * processFutureDate(parsedInput, input, index); if
	 * (parsedInput[DATE_POSITION] != null) { for (int i = startingIndex; i <=
	 * index.getValue(); i++) { input[i] = null; } break; } else if
	 * (parsedInput[ERROR_MSG_POSITION] != null) { break; } index.increment(); }
	 * // process natural time index.setValue(RESET); while
	 * (isIndexValid(index.getValue(), input)) { int startingIndex =
	 * index.getValue(); processFutureTime(parsedInput, input, index); if
	 * (parsedInput[TIME_POSITION] != null) { for (int i = startingIndex; i <=
	 * index.getValue(); i++) { input[i] = null; } break; } else if
	 * (parsedInput[ERROR_MSG_POSITION] != null) { break; } index.increment(); }
	 * // process importance index.setValue(RESET); while
	 * (isIndexValid(index.getValue(), input)) { int startingIndex =
	 * index.getValue(); processImportance(parsedInput, input, index); if
	 * (parsedInput[IMPT_POSITION] != null) { for (int i = startingIndex; i <=
	 * index.getValue(); i++) { input[i] = null; } break; } else if
	 * (parsedInput[ERROR_MSG_POSITION] != null) { break; } index.increment(); }
	 * // process taskname index.setValue(RESET); while
	 * (isIndexValid(index.getValue(), input)) { int startingIndex =
	 * index.getValue(); processDetails(parsedInput, input, index); if
	 * (parsedInput[TASK_NAME_POSITION] != null) { for (int i = startingIndex; i
	 * <= index.getValue(); i++) { input[i] = null; } break; } else if
	 * (parsedInput[ERROR_MSG_POSITION] != null) { break; } index.increment(); }
	 * // process taskname index.setValue(RESET); while
	 * (isIndexValid(index.getValue(), input)) { int startingIndex =
	 * index.getValue(); processDetails(parsedInput, input, index); if
	 * (parsedInput[DETAILS_POSITION] != null) { for (int i = startingIndex; i
	 * <= index.getValue(); i++) { input[i] = null; } break; } else if
	 * (parsedInput[ERROR_MSG_POSITION] != null) { break; } index.increment(); }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * private static String getActualCommand(String[] input, Index index) { if
	 * (input[index.getValue()].equals(COM_ADD)) { return COM_ADD; } else if
	 * (input[index.getValue()].equals(COM_DELETE)) {
	 * 
	 * return COM_DELETE; } else if (input[index.getValue()].equals(COM_EDIT)) {
	 * 
	 * return COM_EDIT; } else if (input[index.getValue()].equals(COM_SEARCH)) {
	 * 
	 * return COM_SEARCH; } else if (input[index.getValue()].equals(COM_UNDO)) {
	 * 
	 * return COM_UNDO; } else if (input[index.getValue()].equals(COM_REDO)) {
	 * 
	 * return COM_REDO; } else if (input[index.getValue()].equals(COM_DISPLAY))
	 * {
	 * 
	 * return COM_DISPLAY; } else { return null; } }
	 */
	private static void processOneParameter(String[] parsedInput,
			String[] input, Index index) {
		index.increment();
		if (isIndexValid(index.getValue(), input)) {

			try {
				int parameter = Integer.parseInt(input[index.getValue()]);
				parsedInput[PARAMETER_POSITION] = Integer.toString(parameter);
				index.increment();
			} catch (Exception e) {
				assignErrorMsg(parsedInput, INVALID_PARAMETER);
			}
		}
	}

	private static void processFutureTask(String[] parsedInput, String[] input,
			Index index) {
		index.increment();
		processFutureDate(parsedInput, input, index);
		processFutureTime(parsedInput, input, index);
		processImportance(parsedInput, input, index);
		processTaskName(parsedInput, input, index);
		processDetails(parsedInput, input, index);
	}

	private static void processDetails(String[] parsedInput, String[] input,
			Index index) {
		// process details
		if (isIndexValid(index.getValue(), input)
				&& (isPartOfList(input[index.getValue()], LIST_DETAILS))) {
			index.increment();
			String info = new String("");
			while (isIndexValid(index.getValue(), input)) {
				info = info + input[index.getValue()] + " ";
				index.increment();
			}
			info = info.trim();
			if (isEmpty(info)) {
				assignErrorMsg(parsedInput, EMPTY_DETAILS_MSG);
			}
			parsedInput[DETAILS_POSITION] = info;
		}
	}

	private static void processTaskName(String[] parsedInput, String[] input,
			Index index) {
		// process taskname
		String taskName = new String("");
		while (isIndexValid(index.getValue(), input)
				&& !isPartOfList(input[index.getValue()], LIST_DETAILS)) {
			taskName = taskName + input[index.getValue()] + " ";
			index.increment();
		}
		taskName = taskName.trim();
		if (isEmpty(taskName)) {
			assignErrorMsg(parsedInput, EMPTY_TASKNAME_MSG);
		}
		parsedInput[TASK_NAME_POSITION] = taskName;
	}

	private static void processImportance(String[] parsedInput, String[] input,
			Index index) {
		// process importance
		if (isIndexValid(index.getValue(), input)
				&& (isPartOfList(input[index.getValue()], LIST_IMPORTANCE))) {
			input[index.getValue()] = null;
			index.increment();
			try {
				if (isIndexValid(index.getValue(), input)) {
					int importance = Integer.parseInt(input[index.getValue()]);
					if (importance < 0 || importance > 3) {
						assignErrorMsg(parsedInput, INVALID_IMPORTANCE);
					} else {
						parsedInput[IMPT_POSITION] = Integer
								.toString(importance);
						index.increment();
					}

				} else {
					assignErrorMsg(parsedInput, INVALID_IMPORTANCE);
				}
			} catch (Exception e) {
				assignErrorMsg(parsedInput, INVALID_IMPORTANCE);
			}
		}
	}

	private static void processFutureTime(String[] parsedInput, String[] input,
			Index index) {
		// process time

		if (isIndexValid(index.getValue(), input)
				&& !parsedInput[COMMAND_POSITION].equals(ERROR)) {
			String possibleTime = getPossibleTime(index, input);
			if (possibleTime == null) {

			} else if (parsedInput[DATE_POSITION]
					.equalsIgnoreCase(FLOATING_TASK)) {

			} else if (validateFutureTime(possibleTime,
					parsedInput[DATE_POSITION])) {
				parsedInput[TIME_POSITION] = removeColon(possibleTime);
				index.increment();
			} else {
				assignErrorMsg(parsedInput, INVALID_TIME);
			}
		}
	}

	private static void processFutureDate(String[] parsedInput, String[] input,
			Index index) {
		// process date
		int initialIndex = index.getValue();
		if (isIndexValid(index.getValue(), input)) {
			String possibleDate = getPossibleDate(index, input);
			if (possibleDate == null) {
				index.setValue(initialIndex);
			} else if (possibleDate.equalsIgnoreCase(FLOATING_TASK)) {
				parsedInput[DATE_POSITION] = FLOATING_TASK;
				index.increment();
			} else if (validateFutureDate(possibleDate)) {
				parsedInput[DATE_POSITION] = removeSlashes(possibleDate);
				index.increment();
			} else {
				assignErrorMsg(parsedInput, INVALID_DATE);
				index.setValue(initialIndex);
			}
		}
	}

	private static boolean isEmpty(String taskName) {
		return taskName.length() == 0;
	}

	private static String removeColon(String possibleTime) {
		if (possibleTime.contains(":")) {
			possibleTime = possibleTime.replaceAll(":", "");
		}
		return possibleTime;
	}

	private static boolean validateFutureTime(String possibleTime,
			String dateInput) {
		String fullDate = dateInput + possibleTime;
		Date today = new Date();
		Date possibleDate = new Date();

		fullDateFormat.setLenient(false);
		try {
			possibleDate = fullDateFormat.parse(fullDate);
			if (possibleDate.compareTo(today) < 0) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private static void assignErrorMsg(String[] parsedInput, String message) {
		parsedInput[ERROR_MSG_POSITION] = message;
		parsedInput[COMMAND_POSITION] = ERROR;
	}

	private static boolean validateFutureDate(String possibleDate) {

		dateFormat.setLenient(false);
		try {
			Date date = dateFormat.parse(possibleDate);
			Date today = new Date();

			if (dateFormat.format(date).equals(dateFormat.format(today))) {
				return true;
			} else if (date.compareTo(today) < 0) {

				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	// Removes any slashes in the string
	private static String removeSlashes(String possibleDate) {
		Date date = new Date();
		try {
			date = dateFormat.parse(possibleDate);
		} catch (Exception e) {
			return null;
		}
		return finalDateFormat.format(date);
	}

	private static String getPossibleTime(Index index, String[] input) {
		String possibleTime = null;
		if (isTimeFormatOne(index.getValue(), input)) {
			possibleTime = input[index.getValue()];
		} else if (isTimeFormatTwo(index.getValue(), input)) {
			possibleTime = input[index.getValue()];
			possibleTime = replaceFullStop(possibleTime);
			possibleTime = reformatTimeTwo(possibleTime);
		} else if (isTimeFormatThree(index.getValue(), input)) {
			possibleTime = input[index.getValue()];
			possibleTime = reformatTimeThree(possibleTime);
		}
		return possibleTime;
	}

	private static String reformatTimeThree(String possibleTime) {
		/*
		 * if(possibleTime.length()==3){ possibleTime="0"+possibleTime; }
		 */
		timeFormatThree.setLenient(false);
		Date date = new Date();
		try {
			date = timeFormatThree.parse(possibleTime);
		} catch (Exception e) {
			return INVALID_TIME;
		}
		return timeFormatOne.format(date);
	}

	private static boolean isTimeFormatThree(Integer index, String[] input) {
		if (isRegexMatch(index, input, PATTERN_TIME_THREE)) {
			return true;
		}
		return false;
	}

	private static String reformatTimeTwo(String possibleTime) {
		timeFormatTwo.setLenient(false);
		Date date = new Date();
		try {
			date = timeFormatTwo.parse(possibleTime);
		} catch (Exception e) {
			return INVALID_TIME;
		}
		return timeFormatOne.format(date);
	}

	private static String replaceFullStop(String possibleTime) {
		if (possibleTime.contains(".")) {
			possibleTime = possibleTime.replace(".", ":");
		}
		return possibleTime;
	}

	private static boolean isTimeFormatTwo(Integer index, String[] input) {
		if (isRegexMatch(index, input, PATTERN_TIME_TWO)) {
			return true;
		}
		return false;
	}

	private static boolean isTimeFormatOne(Integer index, String[] input) {
		if (isRegexMatch(index, input, PATTERN_TIME_ONE)) {
			return true;
		}
		return false;
	}

	private static String getPossibleDate(Index index, String[] input) {

		String possibleDate = null;

		if (isDateFormatOne(index.getValue(), input)) {
			possibleDate = formatDate(index.getValue(), input,
					new FirstDateFormatter(), possibleDate);
		} else if (isSpelledDateOne(index.getValue(), input)) {
			if (isRegexMatch(index.getValue(), input, PATTERN_DATE_TWO)) {
				possibleDate = formatDate(index.getValue(), input,
						new SpelledDateOneFormatter(), possibleDate);
				index.incrementByTwo();
			} else {
				possibleDate = INVALID_DATE;
			}
		} else if (isSpelledDateTwo(index.getValue(), input)) {
			if (isRegexMatch(index.getValue(), input, PATTERN_DATE_TWO)) {
				possibleDate = formatDate(index.getValue(), input,
						new SpelledDateTwoFormatter(), possibleDate);
				index.increment();
			} else {
				possibleDate = INVALID_DATE;
			}
		} else if (isSpelledDay(index.getValue(), input)) {
			possibleDate = formatDate(index.getValue(), input,
					new SpelledDayFormatter(), possibleDate);
		} else if (isFloatingTask(index.getValue(), input)) {
			possibleDate = FLOATING_TASK;
		} else {
			possibleDate = formatDate(index.getValue(), input,
					new DateFormatter(), possibleDate);
			index.decrement();
		}
		return possibleDate;

	}

	private static boolean isFloatingTask(int value, String[] input) {
		if (input[value].equalsIgnoreCase(FLOATING_TASK)) {
			return true;
		}
		return false;
	}

	private static boolean isSpelledDay(Integer index, String[] input) {
		if (isPartOfList(input[index], LIST_DAYS)) {
			return true;
		}
		return false;
	}

	private static boolean isSpelledDateTwo(Integer index, String[] input) {
		if (isRegexMatch(index, input, PATTERN_DATE_POSSIBLE)
				&& (isIndexValid(index + 1, input) && isPartOfList(
						input[index + 1], LIST_MONTHS))) {
			return true;
		}
		return false;
	}

	private static String formatDate(Integer index, String[] input,
			DateFormatter formatter, String possibleDate) {
		possibleDate = formatter.formatDate(index, input);
		return possibleDate;
	}

	// Checks if the month with the following format dd MMM yyyy (e.g. 12th mar
	// 2015)
	private static boolean isSpelledDateOne(Integer index, String[] input) {
		if (isRegexMatch(index, input, PATTERN_DATE_POSSIBLE)
				&& (isIndexValid(index + 1, input) && isPartOfList(
						input[index + 1], LIST_MONTHS))
				&& isIndexValid(index + 2, input)
				&& isRegexMatch(index + 2, input, PATTERN_YEAR)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isDateFormatOne(Integer index, String[] input) {
		return isRegexMatch(index, input, PATTERN_DATE_ONE);
	}

	private static boolean isRegexMatch(Integer index, String[] input,
			String regexPattern) {
		Pattern pattern = Pattern.compile(regexPattern);
		try {
			Matcher matchPattern = pattern.matcher(input[index]);
			matchPattern.find();
			if (matchPattern.group().length() != 0) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	private static boolean isIndexValid(int index, String[] input) {
		if (index >= input.length) {
			return false;
		} else {
			return true;
		}
	}

	private static String getCommand(String[] input, Index index) {
		if (isPartOfList(input[index.getValue()], LIST_ADD)) {

			return COM_ADD;
		} else if (isPartOfList(input[index.getValue()], LIST_DELETE)) {

			return COM_DELETE;
		} else if (isPartOfList(input[index.getValue()], LIST_EDIT)) {

			return COM_EDIT;
		} else if (isPartOfList(input[index.getValue()], LIST_SEARCH)) {

			return COM_SEARCH;
		} else if (isPartOfList(input[index.getValue()], LIST_UNDO)) {

			return COM_UNDO;
		} else if (isPartOfList(input[index.getValue()], LIST_REDO)) {

			return COM_REDO;
		} else if (isPartOfList(input[index.getValue()], LIST_DISPLAY)) {

			return COM_DISPLAY;
		} else {
			return null;
		}
	}

	// check if a String is part of the list
	private static boolean isPartOfList(String input, String[] list) {
		for (int i = 0; i < list.length; i++) {
			if (input.equalsIgnoreCase(list[i])) {
				return true;
			}
		}
		return false;

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

}