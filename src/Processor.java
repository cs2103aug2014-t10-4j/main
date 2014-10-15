import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Processor {
	protected final int RESET = 0;
	protected final String INVALID_PARAMETER = "Invalid parameter";
	protected final String EMPTY_DETAILS_MSG = "No details were found. Please input your details";
	protected final String INVALID_IMPORTANCE = "invalid importance level";

	protected final String FLOATING_TASK = "ft";
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
	protected final String[] LIST_DETAILS = { "details", ".dtl" };

	protected final String INVALID_TIME = "invalid time";
	protected final String ERROR = "error";
	protected final String INVALID_DATE = "invalid Date";
	// Position of various inputs
	protected final int COMMAND_POSITION = 0;
	protected final int MAX_TYPES = 8;
	protected final int TASK_NAME_POSITION = 1;
	protected final int DATE_POSITION = 2;
	protected final int TIME_POSITION = 3;
	protected final int DETAILS_POSITION = 4;
	protected final int IMPT_POSITION = 5;
	protected final int ERROR_MSG_POSITION = 6;
	protected final int PARAMETER_POSITION = 7;
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
			"ddMMyyyyHH:mm");

	public abstract void process(String[] parsedInput, String[] input,
			Index index);

	protected boolean isRegexMatch(Integer index, String[] input,
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

	public abstract void processSpecial(String[] parsedInput, String[] input,
			Index index);

}

class TaskProcessor extends Processor {
	public int getItemPosition() {
		return TASK_NAME_POSITION;
	}

	public void process(String[] parsedInput, String[] input, Index index) {
		// process taskname
		String taskName = new String("");
		while (isIndexValid(index.getValue(), input)
				&& !isPartOfList(input[index.getValue()], LIST_DETAILS)) {
			if (input[index.getValue()] != null) {
				if (input[index.getValue()].length() > 0
						&& input[index.getValue()].charAt(0) == '/') {
					input[index.getValue()] = input[index.getValue()]
							.substring(1, input[index.getValue()].length());
				}
				taskName = taskName + input[index.getValue()] + " ";
			}
			index.increment();
		}
		taskName = taskName.trim();
		checkAndAssignTask(parsedInput, taskName);
	}

	protected void checkAndAssignTask(String[] parsedInput, String taskName) {
		if (!isEmpty(taskName)) {

			parsedInput[TASK_NAME_POSITION] = taskName;
		}
	}

	@Override
	public String getItemName() {
		return TASK_NAME;
	}

	@Override
	public void processSpecial(String[] parsedInput, String[] input, Index index) {
	};

}

class SingleParaProcessor extends Processor {
	public void process(String[] parsedInput, String[] input, Index index) {
		if (isIndexValid(index.getValue(), input)) {
			try {
				int parameter = Integer.parseInt(input[index.getValue()]);
				parsedInput[PARAMETER_POSITION] = Integer.toString(parameter);
				index.increment();
			} catch (Exception e) {
				// not needed for now
				// assignErrorMsg(parsedInput, INVALID_PARAMETER);
			}
		}
	}

	@Override
	public int getItemPosition() {
		return PARAMETER_POSITION;
	}

	@Override
	public String getItemName() {
		return PARAMETERS_NAME;
	}

	@Override
	public void processSpecial(String[] parsedInput, String[] input, Index index) {
	};

}

class MultiParaProcessor extends SingleParaProcessor {
	public void process(String[] parsedInput, String[] input, Index index) {
		index.reset();

		String possibleParameter = checkAndAdd(input, index);
		if (possibleParameter.equalsIgnoreCase(INVALID_PARAMETER)) {
			assignErrorMsg(parsedInput, INVALID_PARAMETER);
		} else {
			parsedInput[PARAMETER_POSITION] = possibleParameter.trim();
		}
	}

	protected String checkAndAdd(String[] input, Index index) {
		String possibleParmeter = new String("");
		while (isIndexValid(index.getValue(), input)) {
			if (input[index.getValue()] != null) {
				try {
					Integer.parseInt(input[index.getValue()]);
					possibleParmeter = possibleParmeter + " "
							+ input[index.getValue()];
				} catch (Exception e) {
					return INVALID_PARAMETER;
				}
			}
			index.increment();
		}
		// sortItems(parsedInput, input, index);
		return possibleParmeter;
	}

	/*
	 * protected void sortItems(String[] parsedInput, String[] input, Index
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

class DetailsProcessor extends TaskProcessor {
	public int getItemPosition() {
		return DETAILS_POSITION;
	}

	public void processNatural(String[] parsedInput, String[] input, Index index) {
		String info = new String("");
		while (isIndexValid(index.getValue(), input)) {
			info = info + " " + input[index.getValue()];
			input[index.getValue()] = null;
			index.increment();
		}
		info = info.trim();
		if (info.length() == 0) {
			info = null;
		}
		parsedInput[DETAILS_POSITION] = info;
	}

	// process details
	public void process(String[] parsedInput, String[] input, Index index) {
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
}

class StrictTaskProcessor extends TaskProcessor {
	@Override
	protected void checkAndAssignTask(String[] parsedInput, String taskName) {
		if (!isEmpty(taskName)) {

			parsedInput[TASK_NAME_POSITION] = taskName;
		} else {
			assignErrorMsg(parsedInput, EMPTY_TASKNAME_MSG);
		}
	}

}

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
				try {
					importance = Integer.parseInt(input[index.getValue() + 1]);
				} catch (Exception e) {
					assignErrorMsg(parsedInput, INVALID_IMPORTANCE);
				}
				if (importance < LOW_IMPT_LVL || importance > HIGH_IMPT_LVL) {
					assignErrorMsg(parsedInput, INVALID_IMPORTANCE);
				} else {
					parsedInput[IMPT_POSITION] = Integer.toString(importance);
					index.incrementByTwo();
				}

			} else {
				assignErrorMsg(parsedInput, INVALID_IMPORTANCE);
			}

		}
	}

	@Override
	public String getItemName() {

		return IMPT_NAME;
	}

	@Override
	public void processSpecial(String[] parsedInput, String[] input, Index index) {
	};

}

class TimeProcessor extends Processor {
	public int getItemPosition() {
		return TIME_POSITION;
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

			} else if (validateTime(possibleTime, parsedInput[DATE_POSITION])) {
				parsedInput[TIME_POSITION] = removeColon(possibleTime);
				index.increment();
			} else {

				assignErrorMsg(parsedInput, INVALID_TIME);
			}
		}
	}

	protected boolean validateTime(String possibleTime, String inputDate) {
		return validateJustTime(possibleTime);
	}

	protected boolean validateJustTime(String possibleTime) {
		timeFormatOne.setLenient(false);
		try {
			Date date = new Date();
			date = timeFormatOne.parse(possibleTime);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	protected boolean isTimeValid(Date possibleDate, Date today) {
		return true;
	}

	protected String getPossibleTime(Index index, String[] input) {
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

	protected boolean isTimeFormatOne(Integer index, String[] input) {
		if (isRegexMatch(index, input, PATTERN_TIME_ONE)) {
			return true;
		}
		return false;
	}

	protected boolean isTimeFormatThree(Integer index, String[] input) {
		if (isRegexMatch(index, input, PATTERN_TIME_THREE)) {
			return true;
		}
		return false;
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
		} catch (Exception e) {
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
		} catch (Exception e) {
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

	protected boolean isTimeFormatTwo(Integer index, String[] input) {
		if (isRegexMatch(index, input, PATTERN_TIME_TWO)) {
			return true;
		}
		return false;
	}

	@Override
	public String getItemName() {
		return TIME_NAME;
	}

	@Override
	public void processSpecial(String[] parsedInput, String[] input, Index index) {
	};

}

class FutureTimeProcessor extends TimeProcessor {
	@Override
	protected boolean validateTime(String possibleTime, String dateInput) {
		if (dateInput != null) {
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

		} else {
			return validateJustTime(possibleTime);
		}

	}
}

class DateProcessor extends Processor {
	public int getItemPosition() {
		return DATE_POSITION;
	}

	public void process(String[] parsedInput, String[] input, Index index) {

		// process date
		int initialIndex = index.getValue();
		if (isIndexValid(index.getValue(), input)) {
			String possibleDate = getPossibleDate(index, input);
			if (possibleDate == null) {
				index.setValue(initialIndex);
			} else if (possibleDate.equalsIgnoreCase(FLOATING_TASK)) {
				parsedInput[DATE_POSITION] = FLOATING_TASK;
				index.increment();
			} else if (validateDate(possibleDate)) {
				assignDate(parsedInput, index, possibleDate);
			} else {
				assignErrorMsg(parsedInput, INVALID_DATE);
				index.setValue(initialIndex);
			}
		}
	}

	protected boolean isDateValid(Date date, Date today) {
		return true;
	}

	protected void assignDate(String[] parsedInput, Index index,
			String possibleDate) {
		parsedInput[DATE_POSITION] = removeSlashes(possibleDate);
		index.increment();
	}

	protected String removeSlashes(String possibleDate) {
		Date date = new Date();
		try {
			date = dateFormat.parse(possibleDate);
		} catch (Exception e) {
			return null;
		}
		return finalDateFormat.format(date);
	}

	protected String getPossibleDate(Index index, String[] input) {

		String possibleDate = null;

		if (isDateFormatOne(index.getValue(), input)) {
			possibleDate = formatDate(index.getValue(), input,
					new FirstDateFormatter());
		} else if (isSpelledDateOne(index.getValue(), input)) {
			if (isRegexMatch(index.getValue(), input, PATTERN_DATE_TWO)) {
				possibleDate = formatDate(index.getValue(), input,
						new SpelledDateOneFormatter());
				index.incrementByTwo();
			} else {
				possibleDate = INVALID_DATE;
			}
		} else if (isSpelledDateTwo(index.getValue(), input)) {
			if (isRegexMatch(index.getValue(), input, PATTERN_DATE_TWO)) {
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
		} else {
			possibleDate = assignDate(input, index);
		}
		return possibleDate;

	}

	protected String assignDate(String[] input, Index index) {
		return null;
	}

	protected boolean isDateFormatOne(Integer index, String[] input) {
		return isRegexMatch(index, input, PATTERN_DATE_ONE);
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

	protected boolean validateDate(String possibleDate) {

		dateFormat.setLenient(false);
		try {
			Date date = dateFormat.parse(possibleDate);
			Date today = new Date();

			if (dateFormat.format(date).equals(dateFormat.format(today))) {
				return true;
			} else if (!isDateValid(date, today)) {
				return false;
			}
		} catch (Exception e) {
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
		if (isRegexMatch(index, input, PATTERN_DATE_POSSIBLE)
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

	@Override
	public void processSpecial(String[] parsedInput, String[] input, Index index) {
	};

}

class FutureDateProcessor extends DateProcessor {

	protected boolean isDateValid(Date date, Date today) {
		if (date.compareTo(today) < 0) {
			return false;
		} else {
			return true;
		}

	}
}

class AutoDateProcessor extends FutureDateProcessor {
	@Override
	public void processSpecial(String[] parsedInput, String[] input, Index index) {
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
			"beside", "besides", "between", "beyond", "but", "by",
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
		if (processor instanceof DateProcessor
				|| processor instanceof TimeProcessor) {
			backwardProcess(parsedInput, input, index, processor);
		} else {
			index.reset();
			while (isIndexValid(index.getValue(), input)) {
				int startPosition = index.getValue();
				processor.process(parsedInput, input, index);
				if (parsedInput[ERROR_MSG_POSITION] != null) {
					break;
				} else if (parsedInput[processor.getItemPosition()] != null) {
					for (int i = startPosition; i < index.getValue(); i++) {
						input[i] = null;
					}
					if (processor instanceof ImportanceProcessor) {
						backWordCleaner(input, startPosition - 1);
					}
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

		processor.processSpecial(parsedInput, input, index);
		// to be overridden
		assignErrorMsg(parsedInput, processor);
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
				backWordCleaner(input, startPosition - 1);
				break;
			}
			index.decrement();
		}
	}

	protected void assignErrorMsg(String[] parsedInput, Processor processor) {
	};

	private void backWordCleaner(String[] input, int index) {
		while (isIndexValid(index, input)
				&& isPartOfList(input[index], LIST_REMOVABLES)
				&& input[index] != null) {
			input[index] = null;
			index--;
		}

	}

	private void cleanErrorMsg(String[] parsedInput) {
		if (parsedInput[ERROR_MSG_POSITION] != null) {
			parsedInput[ERROR_MSG_POSITION] = null;
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

class CommandProcessor {
	// list of prepositions and determiners
	protected final String[] LIST_REMOVABLES = { "and", "about", "after",
			"around", "as", "at", "before", "be", "behind", "below", "beneath",
			"beside", "besides", "between", "beyond", "but", "by",
			"concerning", "considering", "	despite", "	down", "	during",
			"	except", "excepting", "	excluding", "	following", "for", "	from",
			"in", "inside", "	into", "like", "minus", "	near", "of", "off",
			"on", "onto", "	opposite", "outside", "	over", "past", "per",
			"plus", "	regarding", "	round", "save", "since", "that", "than",
			"through", "to", "toward", "towards", "under", "	underneath",
			"unlike", "until", "up", "upon", "versus", "via", "which", "with",
			"within", "without", "is", "are", "the" };

	private final int COMMAND_POSITION = 0;
	// List of commands
	private final String[] LIST_ADD = { "add", ".a", "added", "adding" };
	private final String[] LIST_DELETE = { "delete", ".d", "deleted",
			"deleting" };
	private final String[] LIST_EDIT = { "edit", ".e", "edited", "editing" };
	private final String[] LIST_SEARCH = { "search", ".s", "searched",
			"searching" };
	private final String[] LIST_UNDO = { "undo", ".u" };
	private final String[] LIST_REDO = { "redo", ".r" };
	private final String[] LIST_DISPLAY = { "display", ".dly" };
	private final String CLEAR_ALL_ARCHIVE = "clear all archive";

	public void process(String[] parsedInput, String[] input, Index index) {

		while (isIndexValid(index.getValue(), input)
				&& !isLastWord(input, index.getValue())) {
			assignIfPossible(parsedInput, input, index);
			index.increment();
		}
		if (isIndexValid(index.getValue(), input)
				&& isLastWord(input, index.getValue())) {
			removeFullStop(input, index.getValue());
		}
		assignIfPossible(parsedInput, input, index);
		assignAddIfPossible(parsedInput, input, index);

	}

	private void assignAddIfPossible(String[] parsedInput, String[] input,
			Index index) {
		if (parsedInput[COMMAND_POSITION] == null) {
			assignCommand(parsedInput, input, index, getFirstMember(LIST_ADD));
		}
	}

	private void assignIfPossible(String[] parsedInput, String[] input,
			Index index) {
		if (isIndexValid(index.getValue(), input)) {
			if (parsedInput[COMMAND_POSITION] == null) {
				int startIndex = index.getValue();
				String command = getCommand(input, index);
				if (command != null) {
					assignCommand(parsedInput, input, index, command);
					removeCommand(input, index.getValue(), startIndex);
					frontWordCleaner(input, startIndex + 1);
					backWordCleaner(input, index.getValue() - 1);
				}
			}
		}
	}

	private void removeCommand(String[] input, int index, int startIndex) {
		// if (startIndex > index.getValue()) {
		while (index <= startIndex) {
			if (isIndexValid(index, input)) {
				input[index] = null;
			}

			index++;
		}
		// } else if (startIndex == index.getValue()) {
		// input[startIndex] = null;
		// }
	}

	private void assignCommand(String[] parsedInput, String[] input,
			Index index, String possibleCommand) {
		parsedInput[COMMAND_POSITION] = possibleCommand;
	}

	private String getCommand(String[] input, Index index) {
		if (isPartOfList(input[index.getValue()], LIST_ADD)) {
			return getFirstMember(LIST_ADD);
		} else if (isPartOfList(input[index.getValue()], LIST_DELETE)) {
			return getFirstMember(LIST_DELETE);
		} else if (isPartOfList(input[index.getValue()], LIST_EDIT)) {

			return getFirstMember(LIST_EDIT);
		} else if (isPartOfList(input[index.getValue()], LIST_SEARCH)) {

			return getFirstMember(LIST_SEARCH);
		} else if (isPartOfList(input[index.getValue()], LIST_UNDO)) {

			return getFirstMember(LIST_UNDO);
		} else if (isPartOfList(input[index.getValue()], LIST_REDO)) {

			return getFirstMember(LIST_REDO);
		} else if (isPartOfList(input[index.getValue()], LIST_DISPLAY)) {

			return getFirstMember(LIST_DISPLAY);
		} else if (isMatchString(input, index, CLEAR_ALL_ARCHIVE)) {
			return CLEAR_ALL_ARCHIVE;
		} else {
			return null;
		}
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

	private boolean isLastWord(String[] input, int index) {
		/*
		 * if((input[index].length()>=2)&&!input[index].substring(input[index ].
		 * length() - 1, input[index].length()).equals("\\.")){
		 */
		if (input[index] == null) {
			return false;
		}
		if (input[index].charAt(input[index].length() - 1) == '.') {
			return true;
		} else {
			return false;
		}
	}

	private void removeFullStop(String[] input, int index) {
		input[index] = input[index].substring(0, input[index].length() - 1);
	}

	private boolean isMatchString(String[] input, Index index, String command) {
		String[] possibleCommand = command.split(" ");
		int startIndex = index.getValue();
		for (int i = possibleCommand.length - 1; i >= 0; i--) {
			if (!isIndexValid(index.getValue(), input)) {
				index.setValue(startIndex);
				return false;
			}
			if (!possibleCommand[i].equalsIgnoreCase(input[index.getValue()])) {
				index.setValue(startIndex);
				return false;
			}
			if (i == 0) {
				break;
			}
			index.decrement();
		}
		// index.setValue(startIndex);
		return true;
	}

	private String getFirstMember(String[] list) {
		return list[0];
	}

	private boolean isIndexValid(int index, String[] input) {
		if (index >= input.length || index < 0) {
			return false;
		} else {
			return true;
		}
	}

	private void backWordCleaner(String[] input, int index) {
		while (isIndexValid(index, input)
				&& isPartOfList(input[index], LIST_REMOVABLES)
				&& input[index] != null) {
			input[index] = null;
			index--;
		}

	}

	private void frontWordCleaner(String[] input, int index) {
		while (isIndexValid(index, input)
				&& isPartOfList(input[index], LIST_REMOVABLES)
				&& input[index] != null) {
			input[index] = null;
			index++;
		}

	}

}
