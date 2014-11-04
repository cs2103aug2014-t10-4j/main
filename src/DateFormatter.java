import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateFormatter {

	protected static SimpleDateFormat spelledDayFormat = new SimpleDateFormat(
			"EEE");
	protected static SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
	protected static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	protected static SimpleDateFormat spelledMonthFormat = new SimpleDateFormat(
			"MMM");
	protected static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy");
	protected static SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
	protected static int SHORT_LENGTH_YEAR = 2;

	protected String getCentury() {
		Date today = new Date();
		return yearFormat.format(today).substring(0, 2);

	}

	public String formatDate(Integer index, String[] input) {
		Date today = new Date();
		return dateFormat.format(today);
	}

	protected String addSlashes(String[] temp) {
		String result = new String("");
		int lastPosition = temp.length - 1;
		for (int i = 0; i < lastPosition; i++) {
			result = result + temp[i] + "/";
		}
		result = result + temp[lastPosition];
		return result;
	}

	protected String getYear() {
		Date today = new Date();
		return yearFormat.format(today);
	}

	protected String getMonth() {
		Date today = new Date();
		return monthFormat.format(today);
	}

	protected String getDay() {
		Date today = new Date();
		return dayFormat.format(today);
	}

}

class TmrDateFormatter extends DateFormatter {
	@Override
	public String formatDate(Integer index, String[] input) {
		GregorianCalendar tmr = new GregorianCalendar();
		tmr.add(Calendar.DATE, 1);
		return dateFormat.format(tmr.getTime());
	}
}

class SpelledDayFormatter extends DateFormatter {

	@Override
	public String formatDate(Integer index, String[] input) {
		String possibleDay = input[index];
		Date date = new Date();
		spelledDayFormat.setLenient(false);

		try {
			date = spelledDayFormat.parse(possibleDay);
		} catch (ParseException e) {

		}

		GregorianCalendar possibleDate = new GregorianCalendar();
		possibleDate.setTime(date);
		GregorianCalendar today = new GregorianCalendar();

		int dayOfWeekToday = today.get(Calendar.DAY_OF_WEEK);
		int finalDay = possibleDate.get(Calendar.DAY_OF_WEEK);

		if (dayOfWeekToday < finalDay) {
			today.add(Calendar.DATE, (finalDay - dayOfWeekToday));
			return dateFormat.format(today.getTime());
		} else {
			today.add(Calendar.DATE, (finalDay + 7 - dayOfWeekToday));
			return dateFormat.format(today.getTime());

		}

	}

}

class SpelledDateTwoFormatter extends SpelledDateOneFormatter {
	public String formatDate(Integer index, String[] input) {
		String[] possibleDate = new String[MAX_LENGTH];
		possibleDate[DAY_POSITION] = removeSuffix(input[index]);
		possibleDate[MONTH_POSITION] = formatMonth(input[index + 1]);
		possibleDate[YEAR_POSITION] = formatYear(possibleDate[MONTH_POSITION],
				possibleDate[DAY_POSITION]);
		return addSlashes(possibleDate);
	}

	protected String formatYear(String possibleMonth, String possibleDay) {

		if (Integer.parseInt(possibleMonth) < Integer.parseInt(getMonth())) {

			return Integer.toString(Integer.parseInt(getYear()) + 1);
		} else if (Integer.parseInt(possibleMonth) == Integer
				.parseInt(getMonth())
				&& Integer.parseInt(possibleDay) < Integer.parseInt(getDay())) {
			return Integer.toString(Integer.parseInt(getYear()) + 1);
		}

		return getYear();
	}
}

// Formats date with initial format of 1st mar 2015 or 1st mar 15 to 01/03/2015
class SpelledDateOneFormatter extends DateFormatter {

	private static final String EMPTY_STRING = "";
	private static final String SUFFIX_THREE = "rd";
	private static final String SUFFIX_TWO = "nd";
	private static final String SUFFIX_ONE = "st";
	private static final String SUFFIX_OTHERS = "th";
	protected static final int DAY_POSITION = 0;
	protected static final int MONTH_POSITION = 1;
	protected static final int YEAR_POSITION = 2;
	protected static final int MAX_LENGTH = 3;

	@Override
	public String formatDate(Integer index, String[] input) {
		String[] possibleDate = new String[MAX_LENGTH];
		possibleDate[DAY_POSITION] = removeSuffix(input[index]);
		possibleDate[MONTH_POSITION] = formatMonth(input[index + 1]);
		possibleDate[YEAR_POSITION] = formatYear(input[index + 2]);
		return addSlashes(possibleDate);
	}

	protected String formatYear(String possibleYear) {
		if (possibleYear.length() == SHORT_LENGTH_YEAR) {
			possibleYear = getCentury() + possibleYear;
		}
		return possibleYear;
	}

	protected String formatMonth(String possibleMonth) {
		Date date = new Date();
		try {
			date = spelledMonthFormat.parse(possibleMonth);
		} catch (ParseException e) {

		}
		return monthFormat.format(date);
	}

	protected String removeSuffix(String possibleDay) {

		if (possibleDay.contains(SUFFIX_OTHERS)) {
			possibleDay = possibleDay.replaceAll(SUFFIX_OTHERS, EMPTY_STRING);
		} else if (possibleDay.contains(SUFFIX_ONE)) {
			possibleDay = possibleDay.replaceAll(SUFFIX_ONE, EMPTY_STRING);
		} else if (possibleDay.contains(SUFFIX_TWO)) {
			possibleDay = possibleDay.replaceAll(SUFFIX_TWO, EMPTY_STRING);
		} else if (possibleDay.contains(SUFFIX_THREE)) {
			possibleDay = possibleDay.replaceAll(SUFFIX_THREE, EMPTY_STRING);
		}
		return possibleDay;
	}

}

class FirstDateFormatter extends DateFormatter {
	@Override
	public String formatDate(Integer index, String[] input) {
		String[] temp = removePunctuations(index, input).split("/");
		int yearPosition = temp.length - 1;
		addCentury(temp, yearPosition);

		return addSlashes(temp);
	}

	private void addCentury(String[] temp, int yearPosition) {
		if (temp[yearPosition].length() == SHORT_LENGTH_YEAR) {
			temp[yearPosition] = getCentury() + temp[yearPosition];
		}
	}

	private String removePunctuations(Integer index, String[] input) {
		String possibleDate = input[index];
		if (possibleDate.contains(".")) {
			possibleDate = possibleDate.replace(".", "/");
		} else if (possibleDate.contains("-")) {
			possibleDate = possibleDate.replaceAll("-", "/");
		}
		return possibleDate;
	}

}
