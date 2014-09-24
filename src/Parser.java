import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	private static final String INVALID_COMMAND = "INVALID_COMMAND";
	private static final int LIMIT_HOURS_PM = 23;
	private static final int LIMIT_MINUTES = 59;

	protected static final String INVALID_TIME = "invalid time";
	private static final String ERROR = "error";
	private static final String INVALID_DATE = "invalid Date";
	private static final int MAX_TYPES = 4;
	private static final String[] LIST_ADD = { "/a", "/add" };
	private static final String COM_ADD = "add";
	
	private static final int COMMAND_POSITION = 0;
	private static final int DATE_POSITION = 2;
	private static final int TIME_POSITION = 3;
	private static final int TASK_NAME_POSITION = 1;

	private static final String[] LIST_DAYS = { "sunday", "sun", "saturday",
			"sat", "mon", "monday", "tuesday", "tues", "wed", "wednesday",
			"thurs", "thursday", "fri", "friday", "saturday", "sat" };

	public static String[] parseInput(String inputFromUser) {
		String[] parsedInput = new String[MAX_TYPES];
		String[] input = inputFromUser.trim().split(" ");
		Integer index = 0;
		String taskName = new String("");
		parsedInput[COMMAND_POSITION] = getCommand(input);

		if (parsedInput[COMMAND_POSITION].equals(COM_ADD)) {
			
			index++;

			if (getDateFirstFormat(input[index]) != null) {
				parsedInput[DATE_POSITION] = getDateFirstFormat(input[index]);
				
				if (parsedInput[DATE_POSITION].equals(INVALID_DATE)) {
					parsedInput[COMMAND_POSITION] = ERROR;
					parsedInput[TASK_NAME_POSITION] = INVALID_DATE;
					return parsedInput;
				}
				index++;

			} else if (getDateDayFormat(input[index]) != null) {
				parsedInput[DATE_POSITION] = getDateDayFormat(input[index]);
				if (parsedInput[DATE_POSITION].equals(INVALID_DATE)) {
					parsedInput[COMMAND_POSITION] = ERROR;
					parsedInput[TASK_NAME_POSITION] = INVALID_DATE;
					return parsedInput;
				}
				index++;
			}else if (input[index].equalsIgnoreCase("ft")){
				parsedInput[DATE_POSITION] = "ft";
				index++;
			}else{
				parsedInput[DATE_POSITION]= getTodayDateFormat();
				
			}
					// add month format

			if (getTimeFirstFormat(input[index], parsedInput[DATE_POSITION]) != null) {
				String timeResult = getTimeFirstFormat(input[index],
						parsedInput[DATE_POSITION]);
				if (timeResult.equals(INVALID_TIME)) {
					parsedInput[TASK_NAME_POSITION] = timeResult;
					parsedInput[COMMAND_POSITION] = ERROR;
					return parsedInput;
				} else {
					parsedInput[TIME_POSITION] = timeResult;
				}
				index++;
			}
			// taking input for taskname
			for (; index < input.length; index++) {
				taskName = taskName + input[index] + " ";
			}
			taskName = taskName.trim();
			parsedInput[TASK_NAME_POSITION] = taskName;

		} else {
			parsedInput[0] = ERROR;
			parsedInput[1] = INVALID_COMMAND;
		}
		return parsedInput;

	}

	private static String getTodayDateFormat() {
		GregorianCalendar today = new GregorianCalendar();
		SimpleDateFormat dateToday = new SimpleDateFormat("ddMMyyyy");
		return dateToday.format(today.getTime());
	}

	private static String getDateDayFormat(String possibleDay) {
		if(checkForDay(possibleDay)){
			return findNextPossibleDay(possibleDay);
		}
		return null;
	}
	//sunday is 1 for DAY_OF_WEEK, monday is 2 and so on
	private static String findNextPossibleDay(String possibleDay) {
		
	
		
			SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
			Date date = new Date();
			dayFormat.setLenient(false);
			try{
				 date = dayFormat.parse(possibleDay);
				
			}catch(Exception e){
				
			}
			GregorianCalendar possibleDate = new GregorianCalendar();
			possibleDate.setTime(date);
			GregorianCalendar today = new GregorianCalendar();
			int dayOfWeekToday = today.get(Calendar.DAY_OF_WEEK);
			int finalDay = possibleDate.get(Calendar.DAY_OF_WEEK);
			
			if(dayOfWeekToday < finalDay){
				today.add(Calendar.DATE,(finalDay-dayOfWeekToday));
				return dateFormat.format(today.getTime());
			}else{
				today.add(Calendar.DATE,(finalDay + 7 - dayOfWeekToday));
				return dateFormat.format(today.getTime());
			}
			
		
		
	}

	private static boolean checkForDay(String possibleDay) {
		for(int i = 0; i < LIST_DAYS.length;i++ ){
			if(LIST_DAYS[i].equalsIgnoreCase(possibleDay)){
				return true;
			}
		}
		return false;
	}

	private static String getTimeFirstFormat(String input, String date) {
		
		Pattern patternOne = Pattern.compile("^\\d{2}[:]\\d{2}$");
		String possibleTime = null;
		try {
			Matcher matchPattern = patternOne.matcher(input);
			matchPattern.find();
			if (matchPattern.group().length() != 0) {

				possibleTime = matchPattern.group();

				possibleTime = reformatTime(possibleTime);

				if (possibleTime.equals(INVALID_TIME)) {
					return possibleTime;
				}
				if (date != null) {
					return isTimeValid(possibleTime, date);
				} else {
					return possibleTime;
				}

			}
		} catch (Exception e) {

		}
		return possibleTime;
	}

	private static String isTimeValid(String possibleTime, String dateInput) {
		
		String possibleDate = dateInput + possibleTime;
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHss");
		dateFormat.setLenient(false);

		try {
			Date date = dateFormat.parse(possibleDate);
			Date today = new Date();
			if (date.compareTo(today) < 0) {

				return INVALID_TIME;
			}
		} catch (Exception e) {

			return INVALID_TIME;
		}

		return possibleTime;
	}

	// reformats the time
	private static String reformatTime(String possibleTime) {

		String[] temp;
		Integer hours;
		Integer mins;
	
		temp = possibleTime.split(":");
		hours = Integer.parseInt(temp[0]);
		if (hours > LIMIT_HOURS_PM) {
			return INVALID_TIME;
		}
		mins = Integer.parseInt(temp[1]);
		if (mins > LIMIT_MINUTES) {
			return INVALID_TIME;
		}
		return possibleTime.replace(":", "");
		
	}

	
	public static String getDateFirstFormat(String input) {
		Pattern pattern = Pattern
				.compile("^(\\d{2})([/.-])(\\d{2})([/.-])(\\d{4})$");
		String possibleDate = null;
		try {
			Matcher matchPattern = pattern.matcher(input);
			matchPattern.find();
			if (matchPattern.group().length() != 0) {
				possibleDate = matchPattern.group();
				possibleDate = removeDatePunctuations(possibleDate);

				return isDateValid(possibleDate);
			}
		} catch (Exception e) {

		}
		return null;

	}

	// removes /-. in dates
	private static String removeDatePunctuations(String possibleDate) {
		if (possibleDate.contains(".")) {
			possibleDate = possibleDate.replace(".", "");
		} else if (possibleDate.contains("-")) {
			possibleDate = possibleDate.replaceAll("-", "");
		} else if (possibleDate.contains("/")) {

			possibleDate = possibleDate.replaceAll("/", "");
		}
		return possibleDate;
	}

	// Checks if the date is valid
	private static String isDateValid(String possibleDate) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");

		dateFormat.setLenient(false);

		try {
			Date date = dateFormat.parse(possibleDate);
			Date today = new Date();
			if (possibleDate.equals(dateFormat.format(today))) {
				return possibleDate;
			} else if (date.compareTo(today) < 0) {
				
				return INVALID_DATE;
			}
		} catch (Exception e) {
			return INVALID_DATE;
		}

		return possibleDate;
	}

	private static String getCommand(String[] input) {
		if (isPartOfList(input[0], LIST_ADD)) {

			return COM_ADD;
		}
		return null;
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