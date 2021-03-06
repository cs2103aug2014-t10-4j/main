import java.text.SimpleDateFormat;

public class Constants {
	public static final String DOUBLE_UP = "DoubleUp";

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
	public static final String COLOR_BLUE = "#030ABC"; // for time
	public static final String COLOR_HOT_PINK = "#FC26AB"; // for date
	public static final String COLOR_ORANGER = "#FF5500";// for details
	public static final String COLOR_RED = "#CF1700"; // for important level
	public static final String COLOR_YELLOW = "#FFF9D6";

	public static final int SIZE_WIDTH_TEXT_AREA_RESULTS = 57;
	public static final int SIZE_TEXT_FIELD_CMD_IN = 40;
	public static final int SIZE_OF_DISPLAY_PANEL = 70;
	public static final int SIZE_FEEDBACK_MAX = 180;
	public static final String RES_HELP_HTML = "/res/help.html";
	public static final String RES_UP_ARROW_ICON = "/res/up-arrow-icon.png";
	public static final String RES_MONACO_TTF = "/res/monaco.ttf";
	public static final String RES_SYSTEM_TRAY_ICON = "res/up-arrow-small3.png";

	// Controller Constants
	public static final String LOGGER = "myLogger";
	public static final String ERROR_NULL_COMMAND = "Command type string cannot be null!";
	public static final String ERROR_ICON_NOT_FOUND = "Couldn't find file: %s";
	public static final String ERROR_CREATE_LOCK_FAIL = "Unable to create and/or lock file: ";
	public static final String ERROR_REMOVE_LOCK_FAIL = "Unable to remove lock file: ";
	public static final String ERROR_NATIVE_HOOK_FAIL = "There was a problem registering the native hook.";
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
	public static final String ACTION_HELP_SHORT = ".h";
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

	public static final String MSG_STILL_RUNNING_BG = "It is still running in the background.";
	public static final String MSG_DOUBLEUP_MINIMIZED = "DoubleUp is minimized";
	public static final String MSG_WELCOME = "Welcome to DoubleUp! ";
	public static final String MSG_PROGRESS_BAR = "You have %d tasks due today,\n %d overdue tasks, %d tasks due\n eventually and %d floating tasks.";
	public static final String MSG_HELP = "Press F2 to view all the commands. Happy doubling up!";
	public static final String MSG_ARCHIVED_TASKS = "These are all your completed and archived tasks.";
	public static final String MSG_CLASH_FOUND = "Something is happening at the same time! Continue %sing?";
	public static final String MSG_CREATE_GUI_SUCCESS = "Successfully create GUI";
	public static final String MSG_DELETE_PAST_FAIL = "There are no past tasks to be deleted.";
	public static final String MSG_DELETE_PAST_SUCCESS = "All past tasks have been deleted.";
	public static final String MSG_DELETE_NON_EXISTENT = " You tried to delete non-existent tasks.";
	public static final String MSG_DELETE_NO_INDEX = "You must add a number after delete";
	public static final String MSG_DELETED_TODAY = "All today tasks have been cleared.";
	public static final String MSG_EMPTY_TYPES = "No tasks for these types!";
	public static final String MSG_FOUND_N_ITEMS = "Found %d items.";
	public static final String MSG_HELP_SUCCESS = "Press ESC to return to All Tasks";
	public static final String MSG_HIDE_DETAILS_SUCCESS = "Details are collapsed.";
	public static final String MSG_ITEM_TO_DELETE_NOT_FOUND = "item #%d is not found, ";
	public static final String MSG_NO_TASK_FOR_DATE = "There is no task found for %s.";
	public static final String MSG_PREVIOUS_INSTANCE = "DoubleUp is already running.\nPress Ctrl + Space to open it.";
	public static final String MSG_SHOW_FLOATING_SUCCESS = "These are your floating tasks.";
	public static final String MSG_SHOW_TODAY_SUCCESS = "These are your tasks for the day.";
	public static final String MSG_SHOW_DETAILS_SUCCESS = "Details are expanded.";
	public static final String MSG_SHOW_ALL_SUCCESS = "These are all your tasks.";
	public static final String MSG_SHOW_THIS_WEEK_SUCCESS = "These are your tasks for this week.";
	public static final String MSG_SHOW_SEVEN_DAYS_SUCCESS = "These are your tasks for the next 7 days.";
	public static final String MSG_RANGE_OF_WEEK = "%s %s to %s %s";
	public static final String MSG_USER_CONFIRMED_NO = "Task is not %sed.";

	public static final String MENU_EXIT = "Exit";
	public static final String MENU_HELP = "Help";
	public static final String MENU_ABOUT_DOUBLE_UP = "About DoubleUp";

	public static final String TITLE_ENTER_COMMAND = "Enter a command:";
	public static final String TITLE_RESULT = "Result: ";
	public static final String TITLE_MAIN_WINDOW = "DoubleUp To-Do-List";
	public static final String TITLE_HELP_SCREEN = "Help Screen:";
	public static final String TITLE_ARCHIVED_TASKS = "Archived Tasks (view-only)";
	public static final String TITLE_ALL_TASKS = "All Tasks:";
	public static final String TITLE_ALPHABETICAL_ORDER = "All tasks by alphabetical order";
	public static final String TITLE_FLOATING_TASKS = "Floating Tasks:";
	public static final String TITLE_IMPORTANCE_ORDER = "All tasks by importance order";
	public static final String TITLE_JDIALOG_CLASH_FOUND = "Clash found";
	public static final String TITLE_SEARCH_RESULTS = "Search Results for \"%s\"";
	public static final String TITLE_SHOW_WEEK = "Tasks for this calendar week (%s):";
	public static final String TITLE_SHOW_SEVEN_DAYS = "Tasks for next seven days (%s):";
	public static final String TITLE_TODAY_TASKS = "Today Tasks:";

	// ResultOfCommand
	public static final String DATE_WITH_LINE = "<font style='color:%s;'>"
			+ "<b> ========================== %s%s ==========================</b></font><br>";
	public static final String DATE_WITH_LINE_TODAY = "<font style='color:%s;'>"
			+ "<b> ====================== %s%s ======================</b></font><br>";
	public static final int INVALID_IMPORTANCE_LEVEL = -1;

	// Parser
	public static final int RESET = 0;
	public static final String ERROR_INVALID_PARAMETER = "Invalid parameter";
	public static final String ERROR_EMPTY_DETAILS = "No details were found. Please input your details";
	public static final String ERROR_INVALID_IMPORTANCE = "invalid importance level";
	public static final String ERROR_MSG_SPECIAL_COM = "This is a special command. You cannot input extra attributes";
	public static final String ERROR_EXTRA_ITEM = "There are extra attributes. Pls remove them.";
	public static final String ERROR_EMPTY_ITEM = "No %s is found.";
	public static final String ERROR_ADDING_ZEROES = "Error when adding zeroes";
	public static final String ERROR_PARSING_POSSIBLETIMES = "Error while parsing possibleTimes";
	// Patterns for matching
	public static final String PATTERN_YEAR = "^\\d{4}$|^\\d{2}$";
	public static final String PATTERN_DATE_TWO = "^([2-3]?[1][s][t])$|^([2]?[2][n][d])$|^([2]?[3][r][d])$|^([1][0-9][t][h])$|^([2]?[4-9][t][h])$|^([0-2]?[0-9])$|^([3][0-1])$|^(([2]|[3])[0][t][h])$";
	public static final String PATTERN_DATE_POSSIBLE = "^\\d{1,2}(([t][h])|([s][t])|([n][d])|([r][d]))?$";
	public static final String PATTERN_DATE_ONE = "^(\\d{1,2})([/.-])(\\d{1,2})([/.-])((\\d{4})|(\\d{2}))$";
	public static final String PATTERN_TIME_ONE = "^\\d{1,2}[:]\\d{2}$";
	public static final String MSG_EMPTY_TASKNAME = "No task name found. Please enter a task name.";
	public static final String PATTERN_TIME_THREE = "^\\d{1,2}([a][m]|[p][m])$";
	public static final String PATTERN_TIME_TWO = "^\\d{1,2}([:.](\\d{2}))(([a][m])|([p][m]))$";
	public static final String PATTERN_MULTI_SPACE = "\\s+";

	// Punctuations and spaces
	public static final String SPACE = " ";
	public static final String COLON = ":";
	public static final String EMPTY_STRING = "";
	public static final String FULL_STOP = ".";
	public static final String DASH = "-";
	public static final String TO = "to";
	public static final String COMMA = ",";
	public static final String SLASH = "/";
	public static final char CHAR_COMMA = ',';
	public static final char CHAR_FULL_STOP = '.';
	public static final char CHAR_SLASH = '/';
	public static final char CHAR_DASH = '-';

	public static final String SUFFIX_THREE = "rd";
	public static final String SUFFIX_TWO = "nd";
	public static final String SUFFIX_ONE = "st";
	public static final String SUFFIX_OTHERS = "th";
	public static final int DAY_POSITION = 0;
	public static final int MONTH_POSITION = 1;
	public static final int YEAR_POSITION = 2;
	public static final int MAX_LENGTH = 3;
	
	// Commands
	public static final String ACTION_MIDWAY_DELETE = "still deleting";

	//Processors and formatters
	public static final String DATE_PROCESSOR = "Date processor";
	public static final String TIME_PROCESSOR = "Time processor";
	public static final String DATE_FORMATTER = "Date_Formatter";
	// List of commands
	public static final String[] LIST_ADD = { "add", ".a", "added", "adding" };
	public static final String[] LIST_DELETE = { "delete", ".d", "deleted",
			"deleting" };
	public static final String[] LIST_EDIT = { "edit", ".e", "edited",
			"editing" };
	public static final String[] LIST_SEARCH = { "search", ".s", "searched",
			"searching" };
	public static final String[] LIST_UNDO = { "undo", ".u", ".ud" };
	public static final String[] LIST_REDO = { "redo", ".r", ".rd" };
	public static final String[] LIST_SHOW_FLOATING = { "show floating",
			"show ft", ".sft" };
	public static final String[] LIST_EXIT = { "exit", ".e" };
	public static final String[] LIST_CLEAR = { "clear", ".c" };
	public static final String[] LIST_SHOW_ALL = { "show all", ".sha" };
	public static final String[] LIST_SHOW_TODAY = { "show today", ".sht" };
	public static final String[] LIST_SORT_IMPT = { "sort importance",
			"sort impt", "sort import", "sort important", ".si", ".sip" };
	public static final String[] LIST_SORT_ALPHA = { "sort alpha", ".sap",
			".sa" };
	public static final String[] LIST_DELETE_ALL = { "delete all", ".da" };
	public static final String[] LIST_DELETE_PAST = { "delete past", ".dp" };
	public static final String[] LIST_DELETE_TODAY = { "delete today", ".dtd" };
	public static final String[] LIST_SORT_TIME = { "sort time", "sort", ".st" };
	public static final String[] LIST_SHOW_DETAILS = { "show details", ".sd" };
	public static final String[] LIST_HIDE_DETAILS = { "hide details", ".hd" };
	public static final String[] LIST_VIEW_ARCHIVE = { "view archive", ".va",
			"show archive" };
	public static final String[] LIST_CLEAR_ARCHIVE = { "clear archive",
			"delete archive", ".ca" };
	public static final String[] LIST_DELETE_DATE = { "delete date", ".dd", };
	public static final String[] LIST_SHOW_THIS_WEEK = { "show this week",
			".stw" };
	public static final String[] LIST_SHOW_WEEK = { "show week", ".sw", };
	public static final String[] LIST_SHOW_NEXT_WEEK = { "show next week",
			".snw" };

	public static final String[] LIST_IMPORTANCE = { ".i", "/importance",
			"impt", "importance", "important" };
	public static final String[] LIST_DETAILS = { ".details", ".dtl" };
	// Lists
	public static final String[] LIST_MONTHS = { "january", "jan", "february",
			"feb", "march", "mar", "april", "apr", "may", "june", "jun",
			"july", "jul", "august", "aug", "september", "sept", "november",
			"oct", "october", "nov", "december", "dec" };
	public static final String[] LIST_DAYS = { "sunday", "sun", "saturday",
			"sat", "mon", "monday", "tuesday", "tues", "wed", "wednesday",
			"thurs", "thursday", "fri", "friday", "saturday", "sat" };
	// list of prepositions and determiners
	public static final String[] LIST_REMOVABLES = { "and", "about", "after",
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
	public final static String[] LIST_NO_TIME = { "no time", ".nt" };
	public final static String[] LIST_TODAY = { "today", "tdy" };
	public final static String[] LIST_TMR = { "tomorrow", "tmr" };
	public final static String[] LIST_YESTERDAY = { "yesterday", "ytd" };
	public final static String[] LIST_FLOATING_TASK = { "ft","floating task"};
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
	//parser error msg
	public static final String ERROR_INVALID_TIME = "invalid time";
	public static final String ERROR = "error";
	public static final String ERROR_INVALID_DATE = "invalid Date";
	public static final String ERROR_FOUND_TIME = "Invalid time found.";
	public static final String ERROR_TIME_RANGE = "Time range is invalid.";
	public static final String ERROR_SPELLED_DAY_FORMAT = "Error while parsing spelledDayFormat";
	public static final String ERROR_SPELLED_DATE_ONE_FORMAT = "Error while parsing SpelledDateOneFormatter";
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
			"dd/MM/yyyyHH:mm");
	public static final SimpleDateFormat spelledDayFormat = new SimpleDateFormat(
			"EEE");
	public static final SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
	public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	public static final SimpleDateFormat spelledMonthFormat = new SimpleDateFormat(
			"MMM");
	public static final SimpleDateFormat monthFormat = new SimpleDateFormat("MM");


	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String DATE_FT = "ft";
	public static final String NO_TIME = "no time";

	// Feedback
	public static final String MSG_ADD_SUCCESS = "Added to %s: \"%s\". Type .u to undo.";
	public static final String MSG_ADD_FAIL = "Unable to add line";
	public static final String MSG_DELETE_FAIL = "Unable to delete line";
	public static final String MSG_EDIT_FAIL = "Unable to edit line.";
	public static final String MSG_DELETE_SUCCESS = "deleted \'%d. %s\'";
	public static final String MSG_EDIT_SUCCESS = "Successfully edited.";
	public static final String MSG_NO_PREVIOUS_ACTION = "No previous action to be undone";
	public static final String MSG_NO_FUTURE_ACTION = "No previous action to be redone";
	public static final String MSG_DELETE_EMPTY = "Nothing to delete";
	public static final String MSG_UNDO_SUCCESS = "Previous action is undone";
	public static final String MSG_REDO_SUCCESS = "Previous action is done again";
	public static final String MSG_SORT_FAIL = "Sorting failed";
	public static final String MSG_SORT_SUCCESS = "Successfully sorted by %s.";
	public static final String MSG_NO_TASKS_TO_SORT = "Not enough tasks to sort";
	public static final String MSG_TIME_PASSED = "The time have passed";
	public static final String MSG_WRONG_FORMAT = "\"%s\" is wrong format";

	public static final String MSG_SCREEN_CLEARED = "Screen is cleared. Type show all, show today or show floating again.";
	public static final String MSG_EMPTY_FILE = "%s is empty.";
	public static final String MSG_EMPTY_TODAY = "No tasks for today!";
	public static final String MSG_EMPTY_ALL_DAYS = "No tasks for anyday!";
	public static final String MSG_EMPTY_FLOATING = "No floating tasks!";
	public static final String MSG_COMMAND_LINE = "Enter a command: ";
	public static final String MSG_FAIL_READ_FILE = "Unable to read file.";
	public static final String MSG_MISSING_FILE = "File not found.";
	public static final String MSG_INVALID_COMMAND = "Invalid command";
	public static final String MSG_CLEARED_FILE = "List is cleared";
	public static final String MSG_CLEAR_FAIL = "Nothing to clear!";
	public static final String MSG_BAD_INDEX = "%d is not a valid number.Valid range is %d to %d.";
	public static final String MSG_NO_ATTRIBUTES = "No attributes found.";
	public static final int ZERO = 0;
	public static final int INVALID_NUMBER = -1;
	

	// Storage
	public static final String DIVIDER_DATE = "//!@#DOUBLEUP_DIVIDER_DATE#@!//";
	public static final String DIVIDER_START_TIME = "//!@#DOUBLEUP_DIVIDER_START_TIME#@!//";
	public static final String DIVIDER_END_TIME = "//!@#DOUBLEUP_DIVIDER_END_TIME#@!//";
	public static final String DIVIDER_DETAILS = "//!@#DOUBLEUP_DIVIDER_DETAILS#@!//";
	public static final String DIVIDER_IMPORTANCE = "//!@#DOUBLEUP_DIVIDER_IMPORTANCE#@!//";

	// Exception
	public static final String INVALID_DATE_FORMAT = "Invalid date format in: %s";
	public static final String INVALID_TIME_FORMAT = "Invalid time format in: %s";
	public static final String INVALID_CHRONO_FORMAT = "Invalid date/ time format in: %s";

	public static final String SEARCH_TODAY = "search today";
	public static final String ERROR_PARA_RANGE = "Parameters range is invalid";
	public static final int LIMIT_RANGE_PARA = 2;

	// Attributes names
	public final static String DATE_NAME = "Date";
	public final static String TIME_NAME = "Time";
	public final static String TASK_NAME = "Task name";
	public final static String PARAMETERS_NAME = "Parameters";
	public final static String IMPT_NAME = "Importance level";
	public static final String COMMAND_NAME = "Command";
	// Numerics
	public static final int MAX_LENGTH_TIME = 2;
	public static final int MAX_TIME_TYPES = 2;
	public static final int MAX_TYPES = 9;
	public static final int HIGH_IMPT_LVL = 3;
	public static final int LOW_IMPT_LVL = 0;
	public static final int SHORT_LENGTH_YEAR = 2;
}
