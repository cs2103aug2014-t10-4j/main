//@author A0110937J
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

//The following class formats various date inputs to dd/MM/yyyy

public class DateFormatter {
	private static Logger logger = Logger.getLogger(Constants.DATE_FORMATTER);

	private String getCentury() {
		Date today = new Date();
		return Constants.yearFormat.format(today).substring(0, 2);
	}

	public String formatDate(Integer index, String[] input) {
		Date today = new Date();
		return Constants.dateFormat.format(today);
	}

	private String addSlashes(String[] temp) {
		String result = new String(Constants.EMPTY_STRING);
		int lastPosition = getLastPosition(temp);
		for (int i = 0; i < lastPosition; i++) {
			result = result + temp[i] + Constants.SLASH;
		}
		result = concatString(temp, result, lastPosition);
		return result;
	}

	private int getLastPosition(String[] temp) {
		int lastPosition = temp.length - 1;
		return lastPosition;
	}

	private String concatString(String[] temp, String result, int lastPosition) {
		result = result + temp[lastPosition];
		return result;
	}

	private String getYear() {
		Date today = new Date();
		return Constants.yearFormat.format(today);
	}

	private String getMonth() {
		Date today = new Date();
		return Constants.monthFormat.format(today);
	}

	private String getDay() {
		Date today = new Date();
		return Constants.dayFormat.format(today);
	}
	//formats tomorrows date into dd/MM/yyyy
	public String formatTmrDate(Integer index, String[] input) {
		GregorianCalendar tmr = new GregorianCalendar();
		tmr.add(Calendar.DATE, 1);
		return Constants.dateFormat.format(tmr.getTime());
	}
	//formats yesterday date into dd/MM/yyyy
	public String formatYestDate(Integer index, String[] input) {
		GregorianCalendar ytd = new GregorianCalendar();
		ytd.add(Calendar.DATE, -1);
		return Constants.dateFormat.format(ytd.getTime());
	}
	//formats spelled day like monday into dd/MM/yyyy
	public String formatSpelledDay(Integer index, String[] input) {
		String possibleDay = input[index];
		Date date = new Date();
		Constants.spelledDayFormat.setLenient(false);

		try {
			date = Constants.spelledDayFormat.parse(possibleDay);
		} catch (ParseException e) {
			logger.log(Level.WARNING, Constants.ERROR_SPELLED_DAY_FORMAT);
		}
		GregorianCalendar possibleDate = new GregorianCalendar();
		possibleDate.setTime(date);
		GregorianCalendar today = new GregorianCalendar();

		int dayOfWeekToday = today.get(Calendar.DAY_OF_WEEK);
		int finalDay = possibleDate.get(Calendar.DAY_OF_WEEK);

		if (dayOfWeekToday < finalDay) {
			today.add(Calendar.DATE, (finalDay - dayOfWeekToday));
			return Constants.dateFormat.format(today.getTime());
		} else {
			today.add(Calendar.DATE, (finalDay + 7 - dayOfWeekToday));
			return Constants.dateFormat.format(today.getTime());
		}
	}

	// Formats date with initial format of 1st mar 2015 or 1st mar 15 to01/03/2015
	public String formatSpelledDate(Integer index, String[] input) {
		String[] possibleDate = new String[Constants.MAX_LENGTH];
		possibleDate[Constants.DAY_POSITION] = removeSuffix(input[index]);
		possibleDate[Constants.MONTH_POSITION] = formatMonth(input[index + 1]);
		possibleDate[Constants.YEAR_POSITION] = formatYear(input[index + 2]);
		return addSlashes(possibleDate);
	}

	private String formatYear(String possibleYear) {
		if (possibleYear.length() == Constants.SHORT_LENGTH_YEAR) {
			possibleYear = getCentury() + possibleYear;
		}
		return possibleYear;
	}

	private String formatMonth(String possibleMonth) {
		Date date = new Date();
		try {
			date = Constants.spelledMonthFormat.parse(possibleMonth);
		} catch (ParseException e) {
			logger.log(Level.WARNING, Constants.ERROR_SPELLED_DATE_ONE_FORMAT);
		}
		return Constants.monthFormat.format(date);
	}

	private String removeSuffix(String possibleDay) {

		if (possibleDay.contains(Constants.SUFFIX_OTHERS)) {
			possibleDay = possibleDay.replaceAll(Constants.SUFFIX_OTHERS,
					Constants.EMPTY_STRING);
		} else if (possibleDay.contains(Constants.SUFFIX_ONE)) {
			possibleDay = possibleDay.replaceAll(Constants.SUFFIX_ONE,
					Constants.EMPTY_STRING);
		} else if (possibleDay.contains(Constants.SUFFIX_TWO)) {
			possibleDay = possibleDay.replaceAll(Constants.SUFFIX_TWO,
					Constants.EMPTY_STRING);
		} else if (possibleDay.contains(Constants.SUFFIX_THREE)) {
			possibleDay = possibleDay.replaceAll(Constants.SUFFIX_THREE,
					Constants.EMPTY_STRING);
		}
		return possibleDay;
	}
	
	// Formats date with initial format of 1st mar to 01/03/2015 (dates without year)
	public String formatSpelledDateTwo(Integer index, String[] input) {
		String[] possibleDate = new String[Constants.MAX_LENGTH];
		possibleDate[Constants.DAY_POSITION] = removeSuffix(input[index]);
		possibleDate[Constants.MONTH_POSITION] = formatMonth(input[index + 1]);
		possibleDate[Constants.YEAR_POSITION] = formatYear(
				possibleDate[Constants.MONTH_POSITION],
				possibleDate[Constants.DAY_POSITION]);
		return addSlashes(possibleDate);
	}

	private String formatYear(String possibleMonth, String possibleDay) {

		if (Integer.parseInt(possibleMonth) < Integer.parseInt(getMonth())) {

			return Integer.toString(Integer.parseInt(getYear()) + 1);
		} else if (Integer.parseInt(possibleMonth) == Integer
				.parseInt(getMonth())
				&& Integer.parseInt(possibleDay) < Integer.parseInt(getDay())) {
			return Integer.toString(Integer.parseInt(getYear()) + 1);
		}
		return getYear();
	}

	// formats date like dd/MM/yy to dd/MM/yyyy
	public String formatStandardDate(Integer index, String[] input) {
		String[] temp = removePunctuations(index, input).split(Constants.SLASH);
		int yearPosition = temp.length - 1;
		addCentury(temp, yearPosition);
		return addSlashes(temp);
	}

	private void addCentury(String[] temp, int yearPosition) {
		if (temp[yearPosition].length() == Constants.SHORT_LENGTH_YEAR) {
			temp[yearPosition] = getCentury() + temp[yearPosition];
		}
	}

	private String removePunctuations(Integer index, String[] input) {
		String possibleDate = input[index];
		if (possibleDate.contains(Constants.FULL_STOP)) {
			possibleDate = possibleDate.replace(Constants.FULL_STOP,
					Constants.SLASH);
		} else if (possibleDate.contains(Constants.DASH)) {
			possibleDate = possibleDate.replaceAll(Constants.DASH,
					Constants.SLASH);
		}
		return possibleDate;
	}

}
