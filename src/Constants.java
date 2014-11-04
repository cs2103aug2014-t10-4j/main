import java.awt.Font;
import java.text.SimpleDateFormat;

public class Constants {
	public static final String MSG_WELCOME = "Welcome to DoubleUp! ";
	public static final String MSG_PROGRESS_BAR = "You have %d tasks due today,\n %d overdue tasks, %d tasks due\n eventually and %d floating tasks.";
	public static final String MSG_QOTD = "QOTD: \n";
	public static final String MSG_GOAL = "Your goal is: ";
	public static final String MSG_HELP = "Press F2 to view all the commands. Happy doubling up!";
	public static final String MSG_ENTER_COMMAND = "Enter a command:";
	public static final String MSG_RESULT = "Result: ";
	public static final String FILE_TASK = "DoubleUp.txt";
	public static final String FILE_ARCHIVE = "Archive.txt";
	public static final String FILE_LOCK = "Lock.txt";
	public static final String SPAN_TAG = "<span class=\"%s\">%s</span>";
	public static final String COLOR_CHAMPAGNE_GOLD = "#F7E7CE";
	public static final String COLOR_BLOOD_RED = "#A61B34";
	public static final String COLOR_PINKISH_RED = "#D93B65";
	public static final String COLOR_BRIGHT_RED = "#F23631";
	public static final String COLOR_LIGHT_BLUE = "#ADC5DD";
	public static final String COLOR_BRIGHT_BLUE = "#147DD9";
	public static final String COLOR_MIDNIGHT_BLUE = "#132B40";
	public static final String COLOR_DARK_BLUE = "#326173";
	public static final String COLOR_LIME_GREEN = "#BEF99F";
	public static final String COLOR_MID_GREEN = "#2ABF6B";
	public static final String COLOR_DARK_GREEN = "#32A664";
	public static final String COLOR_SNOW_WHITE = "#FDFAF3";
	public static final String COLOR_BLUE = "#030ABC"; //for time
	public static final String COLOR_HOT_PINK="#FC26AB"; // for date
	public static final String COLOR_ORANGER = "#FF5500";//for details
	public static final String COLOR_RED = "#CF1700"; //for important level
	public static final String MSG_PREVIOUS_INSTANCE = "DoubleUp is already running.\nPress Ctrl + Space to open it.";
	public static final String TITLE_MAIN_WINDOW = "DoubleUp To-do-List";
	
	//Controller Constants
	public static final String MSG_DELETE_PAST_FAIL = "There are no past tasks to be deleted.";
	public static final String MSG_DELETE_PAST_SUCCESS = "All past tasks have been deleted.";
	public static final String MSG_NO_TASK_FOR_DATE = "There is no task found for %s.";
	public static final String ERROR_NULL_COMMAND = "command type string cannot be null!";
	public static final String ACTION_VIEW_ARCHIVE = "view archive";
	public static final String ACTION_UNDO = "undo";
	public static final String ACTION_REDO = "redo";
	public static final String ACTION_RESTORE = "restore";
	public static final String ACTION_SHOW_WEEK = "show week";
	public static final String ACTION_SHOW_THIS_WEEK = "show this week";
	public static final String ACTION_SHOW_DETAILS = "show details";
	public static final String ACTION_SHOW_TODAY = "show today";
	public static final String ACTION_SHOW_FLOATING = "show floating";
	public static final String ACTION_SHOW_ALL = "show all";
	public static final String ACTION_SORT_IMPORTANCE = "sort importance";
	public static final String ACTION_SORT_ALPHA = "sort alpha";
	public static final String ACTION_SORT_TIME = "sort time";
	public static final String ACTION_SEARCH = "search";
	public static final String ACTION_HIDE_DETAILS = "hide details";
	public static final String ACTION_HELP = "help";
	public static final String ACTION_EXIT = "exit";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_DELETE_DATE = "delete date";
	public static final String ACTION_DELETE_PAST = "delete past";
	public static final String ACTION_DELETE_TODAY = "delete today";
	public static final String ACTION_DELETE_ALL = "delete all";
	public static final String ACTION_DELETE = "delete";
	public static final String ACTION_CLEAR_ARCHIVE = "clear archive";
	public static final String ACTION_CLEAR = "clear";
	public static final String ACTION_ADD = "add";

	public static final String TITLE_ARCHIVED_TASKS = "Archived Tasks (view-only)";
	public static final String MSG_ARCHIVED_TASKS = "These are all your completed and archived tasks.";
	public static final String MSG_CLASH_FOUND = "Something is happening at the same time! Continue %sing?";
	public static final String MSG_DELETE_NO_INDEX = "You must add a number after delete";
	public static final String MSG_DELETED_TODAY = "All today tasks have been cleared.";
	public static final String MSG_FOUND_N_ITEMS = "Found %d items.";
	public static final String MSG_HIDE_DETAILS_SUCCESS = "Details are collapsed.";
	public static final String MSG_ITEM_TO_DELETE_NOT_FOUND = "item #%d is not found, ";
	public static final String MSG_USER_CONFIRMED_NO = "Task is not %sed.";
	public static final String MSG_SHOW_FLOATING_SUCCESS = "These are your floating tasks.";
	public static final String MSG_SHOW_TODAY_SUCCESS = "These are your tasks for the day.";
	public static final String MSG_SHOW_DETAILS_SUCCESS = "Details are expanded.";
	public static final String MSG_SHOW_ALL_SUCCESS = "These are all your tasks.";
	public static final String MSG_SHOW_THIS_WEEK_SUCCESS = "These are your tasks for this week.";

	public static final String TITLE_ALL_TASKS = "All Tasks:";
	public static final String TITLE_ALPHABETICAL_ORDER = "All tasks by alphabetical order";
	public static final String TITLE_FLOATING_TASKS = "Floating Tasks:";
	public static final String TITLE_IMPORTANCE_ORDER = "All tasks by importance order";
	public static final String TITLE_JDIALOG_CLASH_FOUND = "Clash found";
	public static final String TITLE_SEARCH_RESULTS = "Search Results for \"%s\"";
	public static final String TITLE_SHOW_WEEK = "Tasks for this week(%s):";
	public static final String TITLE_TODAY_TASKS = "Today Tasks:";
	public static final int MAX_LEN_FEEDBACK = 180;
	
	//ResultOfCommand
	public static final String MSG_EMPTY_TYPES = "No tasks for these types!";
	public static final String DATE_WITH_LINE = 
			"<font style='color:%s;'>" +
					"<b> ========================== %s%s ==========================</b></font><br>";
	public static final String DATE_WITH_LINE_TODAY = 
			"<font style='color:%s;'>" +
					"<b> ====================== %s%s ======================</b></font><br>";
	
	//Parser
	public static final int RESET = 0;
	public static final String INVALID_PARAMETER = "Invalid parameter";
	public static final String EMPTY_DETAILS_MSG = "No details were found. Please input your details";
	public static final String INVALID_IMPORTANCE = "invalid importance level";
	public static final String FLOATING_TASK = "ft";
	//Patterns for matching
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
	public static final int MAX_TYPES = 9;
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
	public static final int START_TIME_POSITION = 3;
	public static final int END_TIME_POSITION = 4;
	public static final int DETAILS_POSITION = 5;
	public static final int IMPT_POSITION = 6;
	public static final int ERROR_MSG_POSITION = 7;
	public static final int PARAMETER_POSITION = 8;

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
	public static final SimpleDateFormat fullDateFormatTwo = new SimpleDateFormat(
			"dd/MM/yyyyHH:mm");
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String DATE_FT = "ft";
	public static final String NO_TIME = "no time";
	
	//Feedback 
	public static final String ADD_MESSAGE = "Added to %s: \"%s\". Type .u to undo.";
	public static final String MSG_FAIL_ADD = "Unable to add line";
	public static final String MSG_FAIL_DELETE = "Unable to delete line";
	public static final String MSG_FAIL_EDIT = "Unable to edit line.";
	public static final String MSG_DELETE_FAIL = "Unable to delete line";
	public static final String MSG_DELETE_SUCCESS = "deleted \'%d. %s\'";
	public static final String MSG_EDIT_SUCCESS = "Successfully edited.";
	public static final String MSG_NO_PREVIOUS_ACTION = "No previous action to be undone";
	public static final String MSG_NO_FUTURE_ACTION = "No previous action to be redone";
	public static final String MSG_NTH_DELETE = "Nothing to delete";	
	public static final String MSG_UNDO_SUCCESS = "Previous action is undone";
	public static final String MSG_REDO_SUCCESS = "Previous action is done again";
	public static final String MSG_SORT_FAIL = "Sorting failed";
	public static final String MSG_SORT_SUCCESS = "Successfully sorted by %s.";
	public static final String MSG_NO_TASKS_TO_SORT = "Not enough tasks to sort";	
	public static final String MSG_TIME_PASSED = "The time have passed";
	public static final String WRONG_FORMAT = "\"%s\" is wrong format";

	public static final String MSG_SCREEN_CLEARED = "Screen is cleared. Type show all, show today or show floating again.";
	public static final String MSG_EMPTY_FILE = "%s is empty.";
	public static final String MSG_EMPTY_TODAY = "No tasks for today!";
	public static final String MSG_EMPTY_ALL_DAYS = "No tasks for anyday!";
	public static final String MSG_EMPTY_FLOATING = "No floating tasks!";
	public static final String MSG_COMMAND_LINE = "Enter a command: ";
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
