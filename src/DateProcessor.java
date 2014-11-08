//@author A0110937J
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

//The following class checks and assigns valid dates

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
			} else if (isFloatingTask(possibleDate)) {
				parsedInput[Constants.DATE_POSITION] = getFirstMember(Constants.LIST_FLOATING_TASK);
				index.increment();
			} else if (validateDate(possibleDate)) {
				possibleDate = addZeroes(possibleDate);
				assignDate(parsedInput, index, possibleDate);
			} else {
				assignErrorMsg(parsedInput, Constants.ERROR_INVALID_DATE);
				index.setValue(initialIndex);
			}
		}
	}

	private boolean isFloatingTask(String possibleDate) {
		return possibleDate
				.equalsIgnoreCase(getFirstMember(Constants.LIST_FLOATING_TASK));
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
		DateFormatter dateFormatter = new DateFormatter();
		if (isDateFormatOne(index.getValue(), input)) {
			possibleDate = dateFormatter.formatStandardDate(index.getValue(),
					input);
		} else if (isSpelledDateOne(index.getValue(), input)) {
			possibleDate = processSpelledDate(index, input, dateFormatter);
		} else if (isSpelledDateTwo(index.getValue(), input)) {
			possibleDate = processSpelledDateTwo(index, input, dateFormatter);
		} else if (isSpelledDay(index.getValue(), input)) {
			possibleDate = dateFormatter.formatSpelledDay(index.getValue(),
					input);
		} else if (isPartOfList(input, index, Constants.LIST_FLOATING_TASK)) {
			possibleDate = getFirstMember(Constants.LIST_FLOATING_TASK);
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_TODAY)) {
			possibleDate = formatDate(index.getValue(), input,
					new DateFormatter());
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_TMR)) {
			possibleDate = dateFormatter.formatTmrDate(index.getValue(), input);
		} else if (isPartOfList(input[index.getValue()],
				Constants.LIST_YESTERDAY)) {
			possibleDate = dateFormatter
					.formatYestDate(index.getValue(), input);
		} else {
			possibleDate = assignDate(input, index);
		}
		return possibleDate;

	}

	private String processSpelledDateTwo(Index index, String[] input,
			DateFormatter dateFormatter) {
		String possibleDate;
		if (isRegexMatch(input[index.getValue()],
				Constants.PATTERN_DATE_TWO)) {
			possibleDate = dateFormatter.formatSpelledDateTwo(
					index.getValue(), input);
			index.increment();
		} else {
			possibleDate = Constants.ERROR_INVALID_DATE;
		}
		return possibleDate;
	}

	private String processSpelledDate(Index index, String[] input,
			DateFormatter dateFormatter) {
		String possibleDate;
		if (isRegexMatch(input[index.getValue()],
				Constants.PATTERN_DATE_TWO)) {
			possibleDate = dateFormatter.formatSpelledDate(
					index.getValue(), input);
			index.incrementByTwo();
		} else {
			possibleDate = Constants.ERROR_INVALID_DATE;
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
		if (input[value]
				.equalsIgnoreCase(getFirstMember(Constants.LIST_FLOATING_TASK))) {
			return true;
		}
		return false;
	}

	protected boolean isSpelledDateOne(Integer index, String[] input) {
		return isRegexMatch(input[index], Constants.PATTERN_DATE_POSSIBLE)
				&& (isIndexValid(index + 1, input) && isPartOfList(
						input[index + 1], Constants.LIST_MONTHS))
				&& isIndexValid(index + 2, input)
				&& isRegexMatch(input[index + 2], Constants.PATTERN_YEAR);
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