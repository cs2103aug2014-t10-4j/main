import java.text.SimpleDateFormat;


public class Constants {
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

	private static final String MSG_WELCOME = "Welcome to DoubleUp!\n";
	private static final String MSG_PROGRESS_BAR = "You have %d tasks due today, %d tasks due tomorrow and %d free tasks.\n";
	private static final String MSG_QOTD = "QOTD: \n";
	private static final String MSG_GOAL = "Your goal is: ";
	private static final String MSG_HELP = "Type /help to view all the commands for various actions. Happy doubling up!\n";
	private static final String MSG_EMPTY_FILE = "%s is empty.";
	private static final String MSG_EMPTY_TODAY = "No tasks for today!";
	private static final String MSG_EMPTY_ALL_DAYS = "No tasks for anyday!";
	private static final String MSG_EMPTY_FLOATING = "No floating tasks!";
	private static final String MSG_COMMAND_LINE = "Enter a command: ";
	private static final String MSG_RESULT = "Result: ";
	private static final String MSG_FAIL_READ_FILE = "Unable to read file.";
	private static final String MSG_MISSING_FILE = "File not found.";
	private static final String MSG_INVALID_COMMAND = "Invalid command";
	private static final String MSG_FAIL_ADD = "Unable to add line.";
	private static final String DELETE_MESSAGE = "deleted from %s: \"%s\"";
	private static final String WRONG_FORMAT = "\"%s\" is wrong format";
	private static final String BAD_INDEX_MESSAGE = "%d is not a valid number.Valid range is %d to %d.";
	private static final int INITIAL_VALUE = 0;
	private static final String NO_MESSAGE_DELETE = "nothing to delete!";
	private static final int INVAILD_NUMBER = -1;
	public static final int MAXIMUM_UNDO_TIMES = 30;
	public static final int MAXIMUM_REDO_TIMES = 30;
	private static final String DIVIDER_DATE = "//!@#DOUBLEUP_DIVIDER_DATE#@!//";
	private static final String DIVIDER_TIME = "//!@#DOUBLEUP_DIVIDER_TIME#@!//";
	private static final String DIVIDER_DETAILS = "//!@#DOUBLEUP_DIVIDER_DETAILS#@!//";
	private static final String DIVIDER_IMPORTANCE = "//!@#DOUBLEUP_DIVIDER_IMPORTANCE#@!//";


}
