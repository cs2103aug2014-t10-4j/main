import java.awt.Font;
import java.text.SimpleDateFormat;

public class Constants {
	public static final int RESET = 0;
	public static final String INVALID_PARAMETER = "Invalid parameter";
	public static final String EMPTY_DETAILS_MSG = "No details were found. Please input your details";
	public static final String INVALID_IMPORTANCE = "invalid importance level";

	public static final String FLOATING_TASK = "ft";
	// patterns for matching
	public static final String PATTERN_YEAR = "^\\d{4}$|^\\d{2}$";
	public static final String PATTERN_DATE_TWO = "^([2-3]?[1][s][t])$|^([2]?[2][n][d])$|^([2]?[3][r][d])$|^([1][0-9][t][h])$|^([2]?[4-9][t][h])$|^([0-2]?[0-9])$|^([3][0-1])$|^(([2]|[3])[0][t][h])$";
	public static final String PATTERN_DATE_POSSIBLE = "^\\d{1,2}(([t][h])|([s][t])|([n][d])|([r][d]))?$";
	public static final String PATTERN_DATE_ONE = "^(\\d{1,2})([/.-])(\\d{1,2})([/.-])((\\d{4})|(\\d{2}))$";
	public static final String PATTERN_TIME_ONE = "^\\d{1,2}[:]\\d{2}$";
	public static final String EMPTY_TASKNAME_MSG = "No task name found. Please enter a task name.";
	public static final String PATTERN_TIME_THREE = "^\\d{1,2}([a][m]|[p][m])$";
	public static final String PATTERN_TIME_TWO = "^\\d{1,2}([:.](\\d{2}))(([a][m])|([p][m]))$";
	// Commands
	public static final String COMMAND_ADD = "add";
	public static final String COMMAND_DELETE = "delete";
	public static final String COMMAND_EDIT = "edit";
	public static final String COMMAND_UNDO = "undo";
	public static final String COMMAND_REDO = "redo";
	public static final String COMMAND_SEARCH = "search";
	public static final String COMMAND_DELETE_ALL = "delete all";
	public static final String COMMAND_MIDWAY_DELETE = "still deleting";
	public static final String COM_DISPLAY = "display";
	protected static final String INVALID_TIME = "invalid time";
	public static final String ERROR = "error";
	public static final String INVALID_DATE = "invalid Date";
	public static final int MAX_TYPES = 8;
	// List of commands
	public static final String[] LIST_ADD = { "/a", "/add" };
	public static final String[] LIST_DELETE = { "/delete", "/d" };
	public static final String[] LIST_EDIT = { "/edit", "/e" };
	public static final String[] LIST_SEARCH = { "/search", "/s" };
	public static final String[] LIST_IMPORTANCE = { "/i", "/importance" };
	public static final String[] LIST_DETAILS = { "/details", "/dtl" };
	public static final String[] LIST_UNDO = { "/undo", "/u" };
	public static final String[] LIST_REDO = { "/redo", "/r" };
	public static final String[] LIST_DISPLAY = { "/dly", "/display" };
	public static final String[] LIST_MONTHS = { "january", "jan", "february",
			"feb", "march", "mar", "april", "apr", "may", "june", "jun",
			"july", "jul", "august", "aug", "september", "sept", "november",
			"oct", "october", "nov", "december", "dec" };

	public static final String[] LIST_DAYS = { "sunday", "sun", "saturday",
			"sat", "mon", "monday", "tuesday", "tues", "wed", "wednesday",
			"thurs", "thursday", "fri", "friday", "saturday", "sat" };
	// Position of various inputs
	public static final int COMMAND_POSITION = 0;
	public static final int TASK_NAME_POSITION = 1;
	public static final int DATE_POSITION = 2;
	public static final int TIME_POSITION = 3;
	public static final int DETAILS_POSITION = 4;
	public static final int IMPT_POSITION = 5;
	public static final int ERROR_MSG_POSITION = 6;
	public static final int PARAMETER_POSITION = 7;

	// date formats
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy");
	public static final SimpleDateFormat finalDateFormat = new SimpleDateFormat(
			"ddMMyyyy");
	public static final SimpleDateFormat timeFormatOne = new SimpleDateFormat(
			"HH:mm");
	public static final SimpleDateFormat timeFormatTwo = new SimpleDateFormat(
			"hh:mma");
	public static final SimpleDateFormat timeFormatThree = new SimpleDateFormat(
			"hha");
	public static final SimpleDateFormat fullDateFormat = new SimpleDateFormat(
			"ddMMyyyyHH:mm");

	public static final String DATE_FORMAT = "dd/MM/yyyy";

	public static final String TIME_FORMAT = "HH:mm";

	
	//Feedback 
	public static final String ADD_MESSAGE = "Added to %s: \"%s\". Type .u to undo.";
	public static final String MSG_FAIL_ADD = "Unable to add line";
	public static final String MSG_FAIL_DELETE = "Unable to delete line";
	public static final String MSG_FAIL_EDIT = "Unable to edit line.";
	public static final String MSG_DELETE_FAIL = "Unable to delete line";
	public static final String MSG_DELETE_SUCCESS = "deleted from your list: \"%s\"";
	public static final String MSG_EDIT_SUCCESS = "Successfully edited.";
	public static final String MSG_NO_PREVIOUS_ACTION = "Nothing to undo";
	public static final String MSG_NO_FUTURE_ACTION = "Nothing to redo";
	public static final String MSG_NTH_DELETE = "nothing to delete";	
	public static final String MSG_UNDO_SUCCESS = "Undo successful";
	public static final String MSG_REDO_SUCCESS = "Redo successful";
	public static final String MSG_SORT_FAIL = "Sorting failed";
	public static final String MSG_SORT_SUCCESS = "Successfully sorted by %s.";
	public static final String MSG_NO_TASKS_TO_SORT = "Not enough tasks to sort";	
	public static final String MSG_TIME_PASSED = "The time have passed";
	public static final String WRONG_FORMAT = "\"%s\" is wrong format";

	public static final String MSG_SCREEN_CLEARED = "Screen is cleared. Type show all, show today or show floating again.";
	public static final String MSG_WELCOME = "Welcome to DoubleUp!\n";
	public static final String MSG_PROGRESS_BAR = "You have %d tasks due today, %d tasks due tomorrow and %d free tasks.\n";
	public static final String MSG_QOTD = "QOTD: \n";
	public static final String MSG_GOAL = "Your goal is: ";
	public static final String MSG_HELP = "Type /help to view all the commands for various actions. Happy doubling up!\n";
	public static final String MSG_EMPTY_FILE = "%s is empty.";
	public static final String MSG_EMPTY_TODAY = "No tasks for today!";
	public static final String MSG_EMPTY_ALL_DAYS = "No tasks for anyday!";
	public static final String MSG_EMPTY_FLOATING = "No floating tasks!";
	public static final String MSG_COMMAND_LINE = "Enter a command: ";
	public static final String MSG_RESULT = "Result: ";
	public static final String MSG_FAIL_READ_FILE = "Unable to read file.";
	public static final String MSG_MISSING_FILE = "File not found.";
	public static final String MSG_INVALID_COMMAND = "Invalid command";
	public static final String DELETE_MESSAGE = "deleted from %s: \"%s\"";
	public static final String MSG_CLEARED_FILE = "List is cleared";
	public static final String NO_MESSAGE_CLEAR = "Nothing to clear!";
	public static final String BAD_INDEX_MESSAGE = "%d is not a valid number.Valid range is %d to %d.";
	public static final int INITIAL_VALUE = 0;
	public static final String NO_MESSAGE_DELETE = "nothing to delete!";
	public static final int INVAILD_NUMBER = -1;
	public static final String DIVIDER_DATE = "//!@#DOUBLEUP_DIVIDER_DATE#@!//";
	public static final String DIVIDER_TIME = "//!@#DOUBLEUP_DIVIDER_TIME#@!//";
	public static final String DIVIDER_DETAILS = "//!@#DOUBLEUP_DIVIDER_DETAILS#@!//";
	public static final String DIVIDER_IMPORTANCE = "//!@#DOUBLEUP_DIVIDER_IMPORTANCE#@!//";


}
