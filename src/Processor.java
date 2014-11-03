import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Processor {
	protected final int RESET = 0;
	protected final String INVALID_PARAMETER = "Invalid parameter";
	protected final String EMPTY_DETAILS_MSG = "No details were found. Please input your details";
	protected final String INVALID_IMPORTANCE = "invalid importance level";

	protected final String FLOATING_TASK = "ft";
	protected final String[] LIST_TODAY = { "today", "tdy" };
	protected final String[] LIST_TMR = { "tomorrow", "tmr" };
	// patterns for matching
	protected final String PATTERN_YEAR = "^\\d{4}$|^\\d{2}$";
	protected final String PATTERN_DATE_TWO = "^([2-3]?[1][s][t])$|^([2]?[2][n][d])$|^([2]?[3][r][d])$|^([1][0-9][t][h])$|^([2]?[4-9][t][h])$|^([0-2]?[0-9])$|^([3][0-1])$|^(([2]|[3])[0][t][h])$";
	protected final String PATTERN_DATE_POSSIBLE = "^\\d{1,2}(([t][h])|([s][t])|([n][d])|([r][d]))?$";
	protected final String PATTERN_DATE_ONE = "^(\\d{1,2})([/.-])(\\d{1,2})([/.-])((\\d{4})|(\\d{2}))$";
	protected final String PATTERN_TIME_ONE = "^\\d{1,2}[:]\\d{2}$";
	protected final String EMPTY_TASKNAME_MSG = "No task name found. Please enter a task name.";
	protected final String PATTERN_TIME_THREE = "^\\d{1,2}([a][m]|[p][m])$";
	protected final String PATTERN_TIME_TWO = "^\\d{1,2}([:.](\\d{2}))(([a][m])|([p][m]))$";
	// Commands
	protected final String[] LIST_IMPORTANCE = { ".i", "/importance", "impt",
			"importance", "important" };

	protected final String INVALID_TIME = "invalid time";
	protected final String ERROR = "error";
	protected final String INVALID_DATE = "invalid Date";
	// Position of various inputs
	protected final int COMMAND_POSITION = 0;
	protected final int MAX_TYPES = 9;
	protected final int TASK_NAME_POSITION = 1;
	protected final int DATE_POSITION = 2;
	protected final int START_TIME_POSITION = 3;
	protected final int DETAILS_POSITION = 4;
	protected final int IMPT_POSITION = 5;
	protected final int ERROR_MSG_POSITION = 6;
	protected final int PARAMETER_POSITION = 7;
	protected final int END_TIME_POSITION = 8;
	protected final String DATE_NAME = "Date";
	protected final String TIME_NAME = "Time";
	protected final String TASK_NAME = "Task name";
	protected final String PARAMETERS_NAME = "Parameters";
	protected final String IMPT_NAME = "Importance level";

	protected final String[] LIST_MONTHS = { "january", "jan", "february",
			"feb", "march", "mar", "april", "apr", "may", "june", "jun",
			"july", "jul", "august", "aug", "september", "sept", "november",
			"oct", "october", "nov", "december", "dec" };

	protected final String[] LIST_DAYS = { "sunday", "sun", "saturday", "sat",
			"mon", "monday", "tuesday", "tues", "wed", "wednesday", "thurs",
			"thursday", "fri", "friday", "saturday", "sat" };

	// date formats
	protected final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy");
	protected final SimpleDateFormat finalDateFormat = new SimpleDateFormat(
			"ddMMyyyy");
	protected final SimpleDateFormat timeFormatOne = new SimpleDateFormat(
			"HH:mm");
	protected final SimpleDateFormat timeFormatTwo = new SimpleDateFormat(
			"hh:mma");
	protected final SimpleDateFormat timeFormatThree = new SimpleDateFormat(
			"hha");
	protected final SimpleDateFormat fullDateFormat = new SimpleDateFormat(
			"dd/MM/yyyyHH:mm");
	protected final String[] LIST_DETAILS = { ".details", ".dtl" };

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
		if (parsedInput[getItemPosition()].equals("")) {
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
	protected boolean isMatchString(String[] input, Index index, String command) {
		String[] possibleCommand = command.split(" ");
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
		// index.setValue(startIndex);
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
		parsedInput[ERROR_MSG_POSITION] = message;
		parsedInput[COMMAND_POSITION] = ERROR;
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
			input[position] = input[position] + ".";
		} else if (!isLastWord(input, position)) {
			input[position] = input[position] + ".";
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
		/*
		 * if((input[index].length()>=2)&&!input[index].substring(input[index ].
		 * length() - 1, input[index].length()).equals("\\.")){
		 */
		if (input[index] == null) {
			return false;
		} else if (input[index].length() == 0) {
			return false;
		} else if (input[index].charAt(input[index].length() - 1) == '.') {
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
		return TASK_NAME_POSITION;
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
		parsedInput[getItemPosition()] = "";
		// index.reset();
	}

	@Override
	protected void removeSlashes(String[] input, Index index) {
		if (input[index.getValue()].length() > 0
				&& input[index.getValue()].charAt(0) == '/') {
			input[index.getValue()] = input[index.getValue()].substring(1,
					input[index.getValue()].length());
		}
	}

	// checkAndAssignTask(parsedInput, taskName);

	/*
	 * public void process(String[] parsedInput, String[] input, Index index) {
	 * // process taskname String taskName = new String(""); while
	 * (isIndexValid(index.getValue(), input)) { if (input[index.getValue()] !=
	 * null) { if (input[index.getValue()].length() > 0 &&
	 * input[index.getValue()].charAt(0) == '/') { input[index.getValue()] =
	 * input[index.getValue()] .substring(1, input[index.getValue()].length());
	 * } taskName = taskName + input[index.getValue()] + " "; }
	 * index.increment(); } taskName = taskName.trim();
	 * checkAndAssignTask(parsedInput, taskName); }
	 */

	protected void checkAndAssignTask(String[] parsedInput, String taskName) {
		if (!isEmpty(taskName)) {

			parsedInput[TASK_NAME_POSITION] = taskName;
		}
	}

	@Override
	public String getItemName() {
		return TASK_NAME;
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
		return PARAMETER_POSITION;
	}

	@Override
	public String getItemName() {
		return PARAMETERS_NAME;
	}

}

class MultiParaProcessor extends SingleParaProcessor {
	private static final String INVALID_PARA_RANGE = "Parameters range is invalid";
	private static final int LIMIT_RANGE_PARA = 2;

	@Override
	public void processBefore(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		removeAllFullStop(input, fullStopArr);
		parsedInput[getItemPosition()] = "";
		// index.reset();

	}

	@Override
	protected void parseRange(String[] input, Index index, String[] parsedInput) {

		if (isIndexValid(index.getValue(), input)) {
			int startIndex = index.getValue();
			if (isIndexValid(index.getValue(), input)
					&& input[index.getValue()] != null
					&& input[index.getValue()].charAt(input[index.getValue()]
							.length() - 1) == '-') {
				input[index.getValue()] = input[index.getValue()].substring(0,
						input[index.getValue()].length() - 1);
				if (isIndexValid(index.getValue() + 1, input)
						&& isInteger(input[index.getValue()])
						&& isInteger(input[index.getValue() + 1])) {
					String[] temp = new String[LIMIT_RANGE_PARA];
					temp[0] = input[index.getValue()];
					temp[1] = input[index.getValue() + 1];
					index.increment();
					processRange(index, parsedInput, startIndex, temp, input);
				} else {
					assignErrorMsg(parsedInput, INVALID_PARAMETER);
					index.setValue(startIndex);
				}
			} else if (isIndexValid(index.getValue()+1, input)
					&& input[index.getValue()+1] != null
					&& input[index.getValue()+1].charAt(0) == '-') {
				input[index.getValue()+1] = input[index.getValue()+1].substring(1,
						input[index.getValue()+1].length());
				if (isIndexValid(index.getValue() + 1, input)
						&& isInteger(input[index.getValue()])
						&& isInteger(input[index.getValue() + 1])) {
					String[] temp = new String[LIMIT_RANGE_PARA];
					temp[0] = input[index.getValue()];
					temp[1] = input[index.getValue() + 1];
					index.increment();
					processRange(index, parsedInput, startIndex, temp, input);
				} else {
					assignErrorMsg(parsedInput, INVALID_PARAMETER);
					index.setValue(startIndex);
				}
			} else if (isIndexValid(index.getValue(), input)
					&& input[index.getValue()] != null
					&& input[index.getValue()].contains("-")) {
				String[] temp = input[index.getValue()].split("-");
				if (temp.length == LIMIT_RANGE_PARA
						&& (isInteger(temp[0]) && isInteger(temp[1]))) {

					processRange(index, parsedInput, startIndex, temp, input);
				} else {
					assignErrorMsg(parsedInput, INVALID_PARA_RANGE);
					index.setValue(startIndex);
				}

			} else if (isIndexValid(index.getValue() + 1, input)
					&& input[index.getValue() + 1] != null
					&& input[index.getValue() + 1].equals("-")) {
				if (isIndexValid(index.getValue() + 2, input)
						&& isInteger(input[index.getValue()])
						&& isInteger(input[index.getValue() + 2])) {
					String[] temp = new String[LIMIT_RANGE_PARA];
					temp[0] = input[index.getValue()];
					temp[1] = input[index.getValue() + 2];
					index.incrementByTwo();
					processRange(index, parsedInput, startIndex, temp, input);
				} else {
					assignErrorMsg(parsedInput, INVALID_PARAMETER);
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
			assignErrorMsg(parsedInput, INVALID_PARA_RANGE);
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
		String range = new String("");
		for (int i = lower; i <= upper; i++) {
			range = range + " " + i;
		}
		return range;
	}

	@Override
	protected void setupParameter(String[] input, int index) {
		if (input[index] != null) {
			if (input[index].contains("to")) {
				input[index] = input[index].replaceAll("to", "-");
			}
			if (input[index].length() != 0
					&& input[index].charAt(input[index].length() - 1) == ',') {
				input[index] = input[index].substring(0,
						input[index].length() - 1);
			} else if (input[index].equals(",")) {
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
		parsedInput[getItemPosition()] = parsedInput[getItemPosition()] + " "
				+ Integer.toString(Integer.parseInt(input[index.getValue()]));
		input[index.getValue()] = null;
	}

	/*
	 * public void process(String[] parsedInput, String[] input, Index index) {
	 * index.reset(); ArrayList<Integer> fullStopArr = new ArrayList<Integer>();
	 * processBefore(input, fullStopArr, parsedInput, index); String
	 * possibleParameter = checkAndAdd(input, index); if
	 * (possibleParameter.equalsIgnoreCase(INVALID_PARAMETER)) {
	 * assignErrorMsg(parsedInput, INVALID_PARAMETER); } else {
	 * parsedInput[PARAMETER_POSITION] = possibleParameter.trim(); }
	 * processAfter(input, fullStopArr, parsedInput, index); }
	 * 
	 * protected String checkAndAdd(String[] input, Index index) { String
	 * possibleParmeter = new String(""); while (isIndexValid(index.getValue(),
	 * input)) { if (input[index.getValue()] != null) { try {
	 * Integer.parseInt(input[index.getValue()]); possibleParmeter =
	 * possibleParmeter + " " + input[index.getValue()]; } catch (Exception e) {
	 * return INVALID_PARAMETER; } } index.increment(); } //
	 * sortItems(parsedInput, input, index); return possibleParmeter; }
	 * 
	 * /* protected void sortItems(String[] parsedInput, String[] input, Index
	 * index) { for (int j = index.getValue(); j < input.length; j++) { for (int
	 * i = index.getValue(); i < input.length - 1; i++) {
	 * checkAndSwapItems(parsedInput, input, i); } } }
	 * 
	 * protected void checkAndSwapItems(String[] parsedInput, String[] input,
	 * int i) { if try { if (Integer.parseInt(input[i]) <
	 * Integer.parseInt(input[i + 1])) { swapItem(input, i); } } catch
	 * (Exception e) { assignErrorMsg(parsedInput, INVALID_PARAMETER); } }
	 * 
	 * protected void swapItem(String[] input, int i) { String temp = input[i];
	 * input[i] = input[i + 1]; input[i + 1] = temp; }
	 */
	/*
	 * protected String formatParameters(String[] input, Index index) { String
	 * finalParameters = new String(""); while (isIndexValid(index.getValue(),
	 * input)) { finalParameters = finalParameters + " " +
	 * input[index.getValue()]; index.increment(); } return
	 * finalParameters.trim(); }
	 */
}

class DetailsProcessor extends Processor {
	private static final String NAME_DETAILS = null;

	@Override
	public int getItemPosition() {
		return DETAILS_POSITION;
	}

	@Override
	public void processBefore(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		// index.reset();
		while (isIndexValid(index.getValue(), input)) {
			if (isPartOfList(input[index.getValue()], LIST_DETAILS)) {
				input[index.getValue()] = null;
				parsedInput[getItemPosition()] = "";
				index.increment();
				break;
			}
			index.increment();
		}

		/*
		 * if (isIndexValid(index.getValue(), input) &&
		 * isPartOfList(input[index.getValue()], LIST_DETAILS)) {
		 * input[index.getValue()] = null; index.increment(); }
		 */
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
		// process taskname
		if (input[index.getValue()] != null) {
			removeSlashes(input, index);
			parsedInput[getItemPosition()] = parsedInput[getItemPosition()]
					+ input[index.getValue()] + " ";
			input[index.getValue()] = null;
		}
	}

	protected void removeSlashes(String[] input, Index index) {
	}

	/*
	 * public void processNatural(String[] parsedInput, String[] input, Index
	 * index) { String info = new String(""); if (isIndexValid(index.getValue(),
	 * input) && isPartOfList(input[index.getValue()], LIST_DETAILS)) {
	 * input[index.getValue()] = null; index.increment(); } while
	 * (isIndexValid(index.getValue(), input)) { info = info + " " +
	 * input[index.getValue()]; input[index.getValue()] = null;
	 * index.increment(); } info = info.trim(); if (info.length() == 0) { info =
	 * null; } parsedInput[DETAILS_POSITION] = info; }
	 */
	/*
	 * // process details public void process(String[] parsedInput, String[]
	 * input, Index index) {
	 * 
	 * if (isIndexValid(index.getValue(), input)) { index.increment(); String
	 * info = new String(""); while (isIndexValid(index.getValue(), input)) {
	 * info = info + input[index.getValue()] + " "; index.increment(); } info =
	 * info.trim(); if (isEmpty(info)) { assignErrorMsg(parsedInput,
	 * EMPTY_DETAILS_MSG); } parsedInput[DETAILS_POSITION] = info; } }
	 */
	@Override
	public String getItemName() {
		return NAME_DETAILS;
	}
}

/*
 * class StrictTaskProcessor extends TaskProcessor {
 * 
 * @Override protected void checkAndAssignTask(String[] parsedInput, String
 * taskName) { if (!isEmpty(taskName)) {
 * 
 * parsedInput[TASK_NAME_POSITION] = taskName; } else {
 * assignErrorMsg(parsedInput, EMPTY_TASKNAME_MSG); } }
 * 
 * }
 */

class ImportanceProcessor extends Processor {
	protected final int NUM_INVALID = -1;
	protected final int HIGH_IMPT_LVL = 3;
	protected final int LOW_IMPT_LVL = 0;

	public int getItemPosition() {
		return IMPT_POSITION;
	}

	public void process(String[] parsedInput, String[] input, Index index) {
		// process importance
		int importance = NUM_INVALID;
		if (isIndexValid(index.getValue(), input)
				&& (isPartOfList(input[index.getValue()], LIST_IMPORTANCE))) {

			if (isIndexValid(index.getValue() + 1, input)) {
				if (isNextInteger(input, index.getValue())) {
					try {
						importance = Integer
								.parseInt(input[index.getValue() + 1]);
					} catch (Exception e) {

					}
					if (importance < LOW_IMPT_LVL || importance > HIGH_IMPT_LVL) {
						assignErrorMsg(parsedInput, INVALID_IMPORTANCE);
					} else {
						parsedInput[IMPT_POSITION] = Integer
								.toString(importance);
						index.incrementByTwo();
					}

				}
			}// else {
				// assignErrorMsg(parsedInput, INVALID_IMPORTANCE);
			// }

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

		return IMPT_NAME;
	}

}

class TimeProcessor extends Processor {
	private static Logger logger = Logger.getLogger("Time processor");
	public int getItemPosition() {
		return START_TIME_POSITION;
	}
	public int getSecItemPosition(){
		return END_TIME_POSITION;
	}
	// Method adds zeroes in the date where needed
		private String addZeroes(String possibleTime) {
			Date date = new Date();
			try {
				date = timeFormatOne.parse(possibleTime);
			} catch (ParseException e) {
				logger.log(Level.WARNING, "Error when adding zeroes");
			}
			return timeFormatOne.format(date);
		}

	@Override
	public void process(String[] parsedInput, String[] input, Index index) {
		// process time

		if (isIndexValid(index.getValue(), input)) {
			String possibleTime = getPossibleTime(index, input);
			if (possibleTime == null) {

			} else if (!isNull(parsedInput[DATE_POSITION])
					&& parsedInput[DATE_POSITION]
							.equalsIgnoreCase(FLOATING_TASK)) {

			}/* else if (validateTime(possibleTime, parsedInput[DATE_POSITION])) */
			else if (validateTime(possibleTime)) {
				// parsedInput[TIME_POSITION] = removeColon(possibleTime);
				possibleTime = addZeroes(possibleTime);
				parsedInput[START_TIME_POSITION] = possibleTime;
				index.increment();
			} else {

				assignErrorMsg(parsedInput, INVALID_TIME);
			}
		}
	}

	/*
	 * protected boolean validateTime(String possibleTime, String inputDate) {
	 * return validateJustTime(possibleTime); }
	 */
	/*
	 * protected boolean validateJustTime(String possibleTime) {
	 * timeFormatOne.setLenient(false); try { Date date = new Date(); date =
	 * timeFormatOne.parse(possibleTime); } catch (ParseException e) { return
	 * false; } return true; }
	 */
	protected boolean validateTime(String possibleTime) {
		timeFormatOne.setLenient(false);
		try {
			Date date = new Date();
			date = timeFormatOne.parse(possibleTime);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/*
	 * protected boolean isTimeValid(Date possibleDate, Date today) { return
	 * true; }
	 */

	protected String getPossibleTime(Index index, String[] input) {
		String possibleTime = null;
		if (isRegexMatch(input[index.getValue()], PATTERN_TIME_ONE)) {
			possibleTime = input[index.getValue()];
		} else if (isRegexMatch(input[index.getValue()], PATTERN_TIME_TWO)) {
			possibleTime = input[index.getValue()];
			possibleTime = replaceFullStop(possibleTime);
			possibleTime = reformatTimeTwo(possibleTime);
		} else if (isRegexMatch(input[index.getValue()], PATTERN_TIME_THREE)) {
			possibleTime = input[index.getValue()];
			possibleTime = reformatTimeThree(possibleTime);
		} else if (isIndexValid(index.getValue() + 1, input)
				&& isRegexMatch(
						input[index.getValue()] + input[index.getValue() + 1],
						PATTERN_TIME_TWO)) {
			possibleTime = input[index.getValue()]
					+ input[index.getValue() + 1];
			possibleTime = replaceFullStop(possibleTime);
			possibleTime = reformatTimeTwo(possibleTime);
			index.increment();
		} else if (isIndexValid(index.getValue() + 1, input)
				&& isRegexMatch(
						input[index.getValue()] + input[index.getValue() + 1],
						PATTERN_TIME_THREE)) {
			possibleTime = input[index.getValue()]
					+ input[index.getValue() + 1];
			possibleTime = reformatTimeThree(possibleTime);
			index.increment();
		}
		return possibleTime;
	}

	protected String removeColon(String possibleTime) {
		if (possibleTime.contains(":")) {
			possibleTime = possibleTime.replaceAll(":", "");
		}
		return possibleTime;
	}

	protected String reformatTimeTwo(String possibleTime) {
		timeFormatTwo.setLenient(false);
		Date date = new Date();
		try {
			date = timeFormatTwo.parse(possibleTime);
		} catch (ParseException e) {
			return INVALID_TIME;
		}
		return timeFormatOne.format(date);
	}

	protected String reformatTimeThree(String possibleTime) {
		/*
		 * if(possibleTime.length()==3){ possibleTime="0"+possibleTime; }
		 */
		timeFormatThree.setLenient(false);
		Date date = new Date();
		try {
			date = timeFormatThree.parse(possibleTime);
		} catch (ParseException e) {
			return INVALID_TIME;
		}
		return timeFormatOne.format(date);
	}

	protected String replaceFullStop(String possibleTime) {
		if (possibleTime.contains(".")) {
			possibleTime = possibleTime.replace(".", ":");
		}
		return possibleTime;
	}

	@Override
	public String getItemName() {
		return TIME_NAME;
	}

}

/*
 * class FutureTimeProcessor extends TimeProcessor {
 * 
 * @Override protected boolean validateTime(String possibleTime, String
 * dateInput) { if (dateInput != null) { String fullDate = dateInput +
 * possibleTime; Date today = new Date(); Date possibleDate = new Date();
 * 
 * fullDateFormat.setLenient(false); try { possibleDate =
 * fullDateFormat.parse(fullDate); if (possibleDate.compareTo(today) < 0) {
 * return false; } } catch (ParseException e) { return false; } return true;
 * 
 * } else { return validateJustTime(possibleTime); }
 * 
 * } }
 */
class DateProcessor extends Processor {
	public int getItemPosition() {
		return DATE_POSITION;
	}

	private static Logger logger = Logger.getLogger("Date processor");

	public void process(String[] parsedInput, String[] input, Index index) {

		int initialIndex = index.getValue();
		if (isIndexValid(index.getValue(), input)) {
			String possibleDate = getPossibleDate(index, input);
			if (possibleDate == null) {
				index.setValue(initialIndex);
			} else if (possibleDate.equalsIgnoreCase(FLOATING_TASK)) {
				parsedInput[DATE_POSITION] = FLOATING_TASK;
				index.increment();
			} else if (validateDate(possibleDate)) {
				possibleDate = addZeroes(possibleDate);
				assignDate(parsedInput, index, possibleDate);
			} else {
				assignErrorMsg(parsedInput, INVALID_DATE);
				index.setValue(initialIndex);
			}
		}
	}

	// Method adds zeroes in the date where needed
	private String addZeroes(String possibleDate) {
		Date date = new Date();
		try {
			date = dateFormat.parse(possibleDate);
		} catch (ParseException e) {
			logger.log(Level.WARNING, "Error when adding zeroes");
		}
		return dateFormat.format(date);
	}

	protected boolean isDateValid(Date date, Date today) {
		return true;
	}

	protected void assignDate(String[] parsedInput, Index index,
			String possibleDate) {
		parsedInput[DATE_POSITION] = possibleDate;
		index.increment();
	}

	/*
	 * protected String removeSlashes(String possibleDate) { Date date = new
	 * Date(); try { date = dateFormat.parse(possibleDate); } catch (Exception
	 * e) { return null; } return finalDateFormat.format(date); }
	 */
	protected String getPossibleDate(Index index, String[] input) {

		String possibleDate = null;

		if (isDateFormatOne(index.getValue(), input)) {
			possibleDate = formatDate(index.getValue(), input,
					new FirstDateFormatter());
		} else if (isSpelledDateOne(index.getValue(), input)) {
			if (isRegexMatch(input[index.getValue()], PATTERN_DATE_TWO)) {
				possibleDate = formatDate(index.getValue(), input,
						new SpelledDateOneFormatter());
				index.incrementByTwo();
			} else {
				possibleDate = INVALID_DATE;
			}
		} else if (isSpelledDateTwo(index.getValue(), input)) {
			if (isRegexMatch(input[index.getValue()], PATTERN_DATE_TWO)) {
				possibleDate = formatDate(index.getValue(), input,
						new SpelledDateTwoFormatter());
				index.increment();
			} else {
				possibleDate = INVALID_DATE;
			}
		} else if (isSpelledDay(index.getValue(), input)) {
			possibleDate = formatDate(index.getValue(), input,
					new SpelledDayFormatter());
		} else if (isFloatingTask(index.getValue(), input)) {
			possibleDate = FLOATING_TASK;
		} else if (isPartOfList(input[index.getValue()], LIST_TODAY)) {
			possibleDate = formatDate(index.getValue(), input,
					new DateFormatter());
		} else if (isPartOfList(input[index.getValue()], LIST_TMR)) {
			possibleDate = formatDate(index.getValue(), input,
					new TmrDateFormatter());
		} else {
			possibleDate = assignDate(input, index);
		}
		return possibleDate;

	}

	/*
	 * private String getTodayDate() { Date today = new Date(); return
	 * dateFormat.format(today); }
	 */
	protected String assignDate(String[] input, Index index) {
		return null;
	}

	protected boolean isDateFormatOne(Integer index, String[] input) {
		return isRegexMatch(input[index], PATTERN_DATE_ONE);
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

	// Checks if the month with the following format dd MMM yyyy (e.g. 12th
	// mar
	// 2015)
	protected boolean isSpelledDateOne(Integer index, String[] input) {
		if (isRegexMatch(input[index], PATTERN_DATE_POSSIBLE)
				&& (isIndexValid(index + 1, input) && isPartOfList(
						input[index + 1], LIST_MONTHS))
				&& isIndexValid(index + 2, input)
				&& isRegexMatch(input[index + 2], PATTERN_YEAR)) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean validateDate(String possibleDate) {

		dateFormat.setLenient(false);
		try {
			Date date = dateFormat.parse(possibleDate);
			// Date today = new Date();

			/*
			 * if (dateFormat.format(date).equals(dateFormat.format(today))) {
			 * return true; } else if (!isDateValid(date, today)) { return
			 * false; }
			 */
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	protected boolean isSpelledDay(Integer index, String[] input) {
		if (isPartOfList(input[index], LIST_DAYS)) {
			return true;
		}
		return false;
	}

	protected boolean isSpelledDateTwo(Integer index, String[] input) {
		if (isRegexMatch(input[index], PATTERN_DATE_POSSIBLE)
				&& (isIndexValid(index + 1, input) && isPartOfList(
						input[index + 1], LIST_MONTHS))) {
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
		return DATE_NAME;
	}

}

/*
 * class FutureDateProcessor extends DateProcessor {
 * 
 * protected boolean isDateValid(Date date, Date today) { if
 * (date.compareTo(today) < 0) { return false; } else { return true; }
 * 
 * } }
 */

class AutoDateProcessor extends DateProcessor {
	@Override
	public void processAfter(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		putAllFullStop(input, fullStopArr);
		if (parsedInput[ERROR_MSG_POSITION] == null) {
			String possibleDate = null;
			if (parsedInput[DATE_POSITION] == null) {
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
	protected final int COMMAND_POSITION = 0;
	protected final String ERROR = "error";
	protected final int ERROR_MSG_POSITION = 6;
	// list of prepositions and determiners
	protected final String[] LIST_REMOVABLES = { "and", "about", "after",
			"around", "as", "at", "before", "be", "behind", "below", "beneath",
			"beside", "besides", "between", "beyond", "due", "but", "by",
			"concerning", "considering", "despite", "down", "during", "except",
			"excepting", "excluding", "following", "for", "from", "in",
			"inside", "into", "like", "minus", "near", "of", "off", "on",
			"onto", "opposite", "outside", "over", "past", "per", "plus",
			"regarding", "round", "save", "since", "that", "than", "through",
			"to", "toward", "towards", "under", "	underneath", "unlike",
			"until", "up", "upon", "versus", "via", "which", "with", "within",
			"without", "is", "are", "the" };
	protected final String EMPTY_ITEM_MSG = "No %s is found.";

	/*
	 * public void process(String[] parsedInput, String[] input, Index index,
	 * Processor processor) { index.reset(); while
	 * (isIndexValid(index.getValue(), input)) { int startPosition =
	 * index.getValue(); processor.process(parsedInput, input, index); if
	 * (parsedInput[processor.getItemPosition()] != null) { for (int i =
	 * startPosition; i < index.getValue(); i++) {
	 * 
	 * input[i] = null; } if (processor instanceof DateProcessor || processor
	 * instanceof TimeProcessor) { backWordCleaner(input, index); } }
	 * cleanErrorMsg(parsedInput); index.increment(); }
	 * assignErrorMsg(parsedInput, processor); }
	 */
	public void process(String[] parsedInput, String[] input, Index index,
			Processor processor) {
		index.reset();
		ArrayList<Integer> fullStopArr = new ArrayList<Integer>();
		/*
		 * if (!(processor instanceof DetailsProcessor||processor instanceof
		 * TaskProcessor)){ processor.processBefore(input, fullStopArr,
		 * parsedInput, index);
		 * 
		 * }
		 */
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
				if (parsedInput[ERROR_MSG_POSITION] != null) {
					break;
				} else if (parsedInput[processor.getItemPosition()] != null) {
					for (int i = startPosition; i < index.getValue(); i++) {
						input[i] = null;
					}

					cleanWordBackward(input, startPosition - 1, processor);
					cleanWordForward(input, index.getValue(), processor);
					break;
					/*
					 * if (processor instanceof DateProcessor || processor
					 * instanceof TimeProcessor) { backWordCleaner(input,
					 * index.getValue()-1); break; }
					 */
				}
				index.increment();
			}
		}
		processor.processAfter(input, fullStopArr, parsedInput, index);
		/*
		 * if (!(processor instanceof DetailsProcessor||processor instanceof
		 * TaskProcessor)) {
		 * 
		 * processor.processAfter(input, fullStopArr, parsedInput, index); }
		 */
		assignErrorMsg(parsedInput, processor);
	}

	private void cleanWordForward(String[] input, int index, Processor processor) {
		if (processor instanceof CommandProcessor) {
			while (isIndexValid(index, input)
					&& isPartOfList(input[index], LIST_REMOVABLES)
					&& input[index] != null) {
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
			if (parsedInput[ERROR_MSG_POSITION] != null) {
				break;
			} else if (parsedInput[processor.getItemPosition()] != null) {
				for (int i = startPosition; i < index.getValue(); i++) {
					input[i] = null;
				}
				cleanWordBackward(input, startPosition - 1, processor);
				break;
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
					&& isPartOfList(input[index], LIST_REMOVABLES)
					&& input[index] != null) {
				input[index] = null;
				index--;
			}
		}

	}

	/*
	 * private void cleanErrorMsg(String[] parsedInput) { if
	 * (parsedInput[ERROR_MSG_POSITION] != null) {
	 * parsedInput[ERROR_MSG_POSITION] = null; } }
	 */
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
				&& parsedInput[ERROR_MSG_POSITION] == null) {
			assignErrorMsg(parsedInput,
					String.format(EMPTY_ITEM_MSG, processor.getItemName()));
		}
	}

	protected void assignErrorMsg(String[] parsedInput, String message) {
		parsedInput[ERROR_MSG_POSITION] = message;
		parsedInput[COMMAND_POSITION] = ERROR;
	}
}

class AntiNaturalProcessor extends NaturalProcessor {
	private final String EXTRA_ITEM_MSG = "There are extra attributes. Pls remove them.";

	@Override
	protected void assignErrorMsg(String[] parsedInput, Processor processor) {
		if (parsedInput[processor.getItemPosition()] != null
				&& parsedInput[ERROR_MSG_POSITION] == null) {
			assignErrorMsg(parsedInput, EXTRA_ITEM_MSG);
		}
	}

	protected void assignErrorMsg(String[] parsedInput, String message) {
		parsedInput[ERROR_MSG_POSITION] = message;
		parsedInput[COMMAND_POSITION] = ERROR;
	}
}

class CommandProcessor extends Processor {
	private static final String ERROR_MSG_SPECIAL_COM = "This is a special command. You cannot input extra attributes";
	private static final String COMMAND_NAME = "Command";
	private final int COMMAND_POSITION = 0;
	// List of commands
	private final String[] LIST_ADD = { "add", ".a", "added", "adding" };
	private final String[] LIST_DELETE = { "delete", ".d", "deleted",
			"deleting" };
	private final String[] LIST_EDIT = { "edit", ".e", "edited", "editing" };
	private final String[] LIST_SEARCH = { "search", ".s", "searched",
			"searching" };
	private final String[] LIST_UNDO = { "undo", ".u", ".ud" };
	private final String[] LIST_REDO = { "redo", ".r", ".rd" };
	private final String[] LIST_SHOW_FLOATING = { "show floating", "show ft",
			".sft" };
	private final String[] LIST_EXIT = { "exit", ".e" };
	private final String[] LIST_CLEAR = { "clear", ".c" };
	// private final String[] LIST_RESTORE = { "restore", ".rs" };
	private final String[] LIST_SHOW_ALL = { "show all", ".sha" };
	private final String[] LIST_SHOW_TODAY = { "show today", ".sht" };
	private final String[] LIST_SORT_IMPT = { "sort importance", "sort impt",
			"sort import", "sort important", ".si", ".sip" };
	private final String[] LIST_SORT_ALPHA = { "sort alpha", ".sap", ".sa" };
	private final String[] LIST_DELETE_ALL = { "delete all", ".da" };
	private final String[] LIST_DELETE_PAST = { "delete past", ".dp" };
	private final String[] LIST_DELETE_TODAY = { "delete today", ".dtd" };
	private final String[] LIST_SORT_TIME = { "sort time", "sort", ".st" };
	private final String[] LIST_SHOW_DETAILS = { "show details", ".sd" };
	private final String[] LIST_HIDE_DETAILS = { "hide details", ".hd" };
	private final String[] LIST_VIEW_ARCHIVE = { "view archive", ".va",
			"show archive" };
	private final String[] LIST_CLEAR_ARCHIVE = { "clear archive",
			"delete archive", ".ca" };
	private final String[] LIST_DELETE_DATE = { "delete date", ".dd", };
	private final String[] LIST_SHOW_THIS_WEEK = { "show this week", ".stw" };
	private final String[] LIST_SHOW_WEEK = { "show week", ".sw", };
	private final String[] LIST_SHOW_NEXT_WEEK = { "show next week", ".snw" };

	public void process(String[] parsedInput, String[] input, Index index) {
		/*
		 * ArrayList<Integer> fullStopArr = new ArrayList<Integer>();
		 * processBefore(input, fullStopArr, parsedInput, index);
		 */
		// while (isIndexValid(index.getValue(), input)) {
		// if (isPartOfList(input[index.getValue()], LIST_DETAILS)) {
		// break;
		// }
		if (isPartOfList(input[index.getValue()], LIST_DETAILS)) {
			index.setValue(input.length);
		} else {
			assignIfPossible(parsedInput, input, index);
		}

		// index.increment();
		// }
		/*
		 * if (isIndexValid(index.getValue(), input) && isLastWord(input,
		 * index.getValue())) { removeFullStop(input, index.getValue()); }
		 */
		// assignAddIfPossible(parsedInput, input, index);
		// processAfter(input, fullStopArr, parsedInput, index);

	}

	private void assignAddIfPossible(String[] parsedInput, String[] input,
			Index index) {
		if (parsedInput[COMMAND_POSITION] == null) {
			assignCommand(parsedInput, input, index, getFirstMember(LIST_ADD));
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
			if (parsedInput[COMMAND_POSITION] == null) {
				// int startIndex = index.getValue();
				String command = getCommand(input, index, parsedInput);
				if (command != null) {
					assignCommand(parsedInput, input, index, command);
					// removeCommand(input, index.getValue(), startIndex);
					// frontWordCleaner(input, index.getValue() + 1);
					// backWordCleaner(input, startIndex - 1);
				}
			}
		}
	}

	/*
	 * private void removeCommand(String[] input, int index, int startIndex) {
	 * // if (startIndex > index.getValue()) { while (index >= startIndex) { if
	 * (isIndexValid(index, input)) { input[index] = null; }
	 * 
	 * index--; } // } else if (startIndex == index.getValue()) { //
	 * input[startIndex] = null; // } }
	 */
	private void assignCommand(String[] parsedInput, String[] input,
			Index index, String command) {
		parsedInput[COMMAND_POSITION] = command;
		index.increment();
	}

	private String getCommand(String[] input, Index index, String[] parsedInput) {

		if (isPartOfList(input[index.getValue()], LIST_ADD)) {
			return getFirstMember(LIST_ADD);
		} else if (isPartOfList(input[index.getValue()], LIST_EDIT)) {
			return getFirstMember(LIST_EDIT);
		} else if (isPartOfList(input[index.getValue()], LIST_SEARCH)) {
			return getFirstMember(LIST_SEARCH);
		} else if (isPartOfList(input[index.getValue()], LIST_UNDO)) {
			return getFirstMember(LIST_UNDO);
		} else if (isPartOfList(input[index.getValue()], LIST_REDO)) {
			return getFirstMember(LIST_REDO);
		} else if (isPartOfList(input[index.getValue()], LIST_EXIT)) {
			return getFirstMember(LIST_EXIT);
		} else if (isSpecialCommand(input, index, LIST_CLEAR_ARCHIVE,
				parsedInput)) {
			return getFirstMember(LIST_CLEAR_ARCHIVE);
		} else if (isSpecialCommand(input, index, LIST_CLEAR, parsedInput)) {
			return getFirstMember(LIST_CLEAR);
		} /*
		 * else if (isPartOfList(input[index.getValue()], LIST_RESTORE)) {
		 * return getFirstMember(LIST_RESTORE); }
		 */else if (isSpecialCommand(input, index, LIST_SHOW_FLOATING,
				parsedInput)) {
			return getFirstMember(LIST_SHOW_FLOATING);
		} else if (isSpecialCommand(input, index, LIST_SHOW_ALL, parsedInput)) {
			return getFirstMember(LIST_SHOW_ALL);
		} else if (isSpecialCommand(input, index, LIST_SHOW_DETAILS,
				parsedInput)) {
			return getFirstMember(LIST_SHOW_DETAILS);
		} else if (isSpecialCommand(input, index, LIST_HIDE_DETAILS,
				parsedInput)) {
			return getFirstMember(LIST_HIDE_DETAILS);
		} else if (isSpecialCommand(input, index, LIST_SORT_IMPT, parsedInput)) {
			return getFirstMember(LIST_SORT_IMPT);
		} else if (isSpecialCommand(input, index, LIST_SORT_ALPHA, parsedInput)) {
			return getFirstMember(LIST_SORT_ALPHA);
		} else if (isSpecialCommand(input, index, LIST_SORT_TIME, parsedInput)) {
			return getFirstMember(LIST_SORT_TIME);
		} else if (isSpecialCommand(input, index, LIST_DELETE_ALL, parsedInput)) {
			return getFirstMember(LIST_DELETE_ALL);
		} else if (isSpecialCommand(input, index, LIST_DELETE_PAST, parsedInput)) {
			return getFirstMember(LIST_DELETE_PAST);
		} else if (isSpecialCommand(input, index, LIST_DELETE_TODAY,
				parsedInput)) {
			return getFirstMember(LIST_DELETE_TODAY);
		} else if (isPartOfList(input, index, LIST_DELETE_DATE)) {
			return getFirstMember(LIST_DELETE_DATE);
		} else if (isSpecialCommand(input, index, LIST_SHOW_TODAY, parsedInput)) {
			return getFirstMember(LIST_SHOW_TODAY);
		} else if (isSpecialCommand(input, index, LIST_SHOW_WEEK, parsedInput)) {
			return getFirstMember(LIST_SHOW_WEEK);
		} else if (isSpecialCommand(input, index, LIST_SHOW_THIS_WEEK,
				parsedInput)) {
			return getFirstMember(LIST_SHOW_THIS_WEEK);
		} else if (isSpecialCommand(input, index, LIST_SHOW_NEXT_WEEK,
				parsedInput)) {
			return getFirstMember(LIST_SHOW_NEXT_WEEK);
		} else if (isSpecialCommand(input, index, LIST_VIEW_ARCHIVE,
				parsedInput)) {
			return getFirstMember(LIST_VIEW_ARCHIVE);
		} else if (isPartOfList(input[index.getValue()], LIST_DELETE)) {
			return getFirstMember(LIST_DELETE);
		}
		return null;
	}

	// check if a String is part of the list
	private boolean isSpecialCommand(String[] input, Index index,
			String[] list, String[] parsedInput) {
		// int startIndex = index.getValue();
		for (int i = 0; i < list.length; i++) {
			if (input == null) {
				return false;
			}
			if (isFirstInstance(index.getValue())
					&& isMatchString(input, index, list[i])) {
				if (isRestEmpty(input, index.getValue())) {
					return true;
				} else {
					assignErrorMsg(parsedInput, ERROR_MSG_SPECIAL_COM);
				}
			}
		}
		return false;
	}

	/*
	 * // check if a String is part of the list private boolean
	 * isOneWordCom(String[] input, Index index, String[] list, String[]
	 * parsedInput) { int startIndex = index.getValue(); for (int i = 0; i <
	 * list.length; i++) { if (input == null) { return false; } if
	 * (isFirstInstance(index.getValue(), list[i]) && isMatchString(input,
	 * index, list[i])) { if (isRestEmpty(input, startIndex)) { return true; }
	 * else { assignErrorMsg(parsedInput, ERROR_MSG_ONE_COMMAND); } } } return
	 * false; } /* private boolean isPartOfList(String[] input, Index index,
	 * String[] list) { for (int i = 0; i < list.length; i++) { if (input ==
	 * null) { return false; } if (isMatchString(input, index, list[i])) {
	 * return true; } } return false; }
	 */
	/*
	 * private boolean isFirstInstance(int index, String command) { return index
	 * == (command.split(" ")).length - 1; }
	 */
	private boolean isFirstInstance(int index) {
		return index == 0;
	}

	private boolean isRestEmpty(String[] input, int index) {
		if ((input.length - 1) == index) {
			return true;
		}
		return false;
	}

	/*
	 * private boolean isLastWord(String[] input, int index) { /*
	 * if((input[index].length()>=2)&&!input[index].substring(input[index ].
	 * length() - 1, input[index].length()).equals("\\.")){
	 * 
	 * if (input[index] == null) { return false; } if
	 * (input[index].charAt(input[index].length() - 1) == '.') { return true; }
	 * else { return false; } }
	 * 
	 * private void removeFullStop(String[] input, int index) { input[index] =
	 * input[index].substring(0, input[index].length() - 1); }
	 */
	/*
	 * private boolean isMatchString(String[] input, Index index, String
	 * command) { String[] possibleCommand = command.split(" "); int startIndex
	 * = index.getValue(); for (int i = possibleCommand.length - 1; i >= 0; i--)
	 * { if (!isIndexValid(index.getValue(), input)) {
	 * index.setValue(startIndex); return false; } if
	 * (!possibleCommand[i].equalsIgnoreCase(input[index.getValue()])) {
	 * index.setValue(startIndex); return false; } if (i == 0) { break; }
	 * index.decrement(); } // index.setValue(startIndex); return true; }
	 */

	private String getFirstMember(String[] list) {
		return list[0];
	}

	/*
	 * private boolean isIndexValid(int index, String[] input) { if (index >=
	 * input.length || index < 0) { return false; } else { return true; } }
	 */
	/*
	 * private void backWordCleaner(String[] input, int index) { while
	 * (isIndexValid(index, input) && isPartOfList(input[index],
	 * LIST_REMOVABLES) && input[index] != null) { input[index] = null; index--;
	 * }
	 * 
	 * }
	 * 
	 * private void frontWordCleaner(String[] input, int index) { while
	 * (isIndexValid(index, input) && isPartOfList(input[index],
	 * LIST_REMOVABLES) && input[index] != null) { input[index] = null; index++;
	 * }
	 * 
	 * }
	 */
	@Override
	public int getItemPosition() {
		return COMMAND_POSITION;
	}

	@Override
	public String getItemName() {
		return COMMAND_NAME;
	}

}
