import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Controller {

	private static final String MSG_DELETE_PAST_FAIL = "There are no past tasks to be deleted.";
	private static final String MSG_DELETE_PAST_SUCCESS = "All past tasks have been deleted.";
	private static final String MSG_NO_TASK_FOR_DATE = "There is no task found for %s.";
	private static final String DATE_FT = "ft";
	private static final String ERROR_NULL_COMMAND = "command type string cannot be null!";
	private static final String ACTION_VIEW_ARCHIVE = "view archive";
	private static final String ACTION_UNDO = "undo";
	private static final String ACTION_REDO = "redo";
	private static final String ACTION_RESTORE = "restore";
	private static final String ACTION_SHOW_DETAILS = "show details";
	private static final String ACTION_SHOW_TODAY = "show today";
	private static final String ACTION_SHOW_FLOATING = "show floating";
	private static final String ACTION_SHOW_ALL = "show all";
	private static final String ACTION_SORT_IMPORTANCE = "sort importance";
	private static final String ACTION_SORT_ALPHA = "sort alpha";
	private static final String ACTION_SORT_TIME = "sort time";
	private static final String ACTION_SEARCH = "search";
	private static final String ACTION_HIDE_DETAILS = "hide details";
	private static final String ACTION_HELP = "help";
	private static final String ACTION_EXIT = "exit";
	private static final String ACTION_EDIT = "edit";
	private static final String ACTION_DELETE_DATE = "delete date";
	private static final String ACTION_DELETE_PAST= "delete past";
	private static final String ACTION_DELETE_TODAY = "delete today";
	private static final String ACTION_DELETE_ALL = "delete all";
	private static final String ACTION_DELETE = "delete";
	private static final String ACTION_CLEAR_ARCHIVE = "clear archive";
	private static final String ACTION_CLEAR = "clear";
	private static final String ACTION_ADD = "add";

	private static final String TITLE_ARCHIVED_TASKS = "Archived Tasks (view-only)";
	private static final String MSG_ARCHIVED_TASKS = "These are all your completed and archived tasks.";
	private static final String MSG_CLASH_FOUND = "Something is happening at the same time! Continue %sing?";
	private static final String MSG_DELETE_NO_INDEX = "You must add a number after delete";
	private static final String MSG_DELETED_TODAY = "All today tasks have been cleared.";
	private static final String MSG_FOUND_N_ITEMS = "Found %d items.";
	private static final String MSG_HIDE_DETAILS_SUCCESS = "Details are collapsed.";
	private static final String MSG_ITEM_TO_DELETE_NOT_FOUND = "item #%d is not found, ";
	private static final String MSG_USER_CONFIRMED_NO = "Task is not %sed.";
	private static final String MSG_SHOW_FLOATING_SUCCESS = "These are your floating tasks.";
	private static final String MSG_SHOW_TODAY_SUCCESS = "These are your tasks for the day.";
	private static final String MSG_SHOW_DETAILS_SUCCESS = "Details are expanded.";
	private static final String MSG_SHOW_ALL_SUCCESS = "These are all your tasks.";

	private static final String TITLE_ALL_TASKS = "All Tasks:";
	private static final String TITLE_ALPHABETICAL_ORDER = "All tasks by alphabetical order";
	private static final String TITLE_FLOATING_TASKS = "Floating Tasks:";
	private static final String TITLE_IMPORTANCE_ORDER = "All tasks by importance order";
	private static final String TITLE_JDIALOG_CLASH_FOUND = "Clash found";
	private static final String TITLE_SEARCH_RESULTS = "Search Results for \"%s\"";
	private static final String TITLE_TODAY_TASKS = "Today Tasks:";
	private static final int MAX_LEN_FEEDBACK = 300;

	enum CommandType {
		ADD_TEXT, CLEAR_SCREEN, CLEAR_ARCHIVE, DELETE_ALL, DELETE_DATE, DELETE_PAST, DELETE_TEXT, 
		DELETE_TODAY, EDIT, EXIT, HELP, HIDE_DETAILS, INVALID, SEARCH, SHOW_ALL, SHOW_FLOATING, 
		SHOW_TODAY, SHOW_DETAILS, SORT_TIME, SORT_ALPHA, SORT_IMPORTANCE, RESTORE, 
		REDO, UNDO, VIEW_ARCHIVE;
	};

	public static ResultOfCommand executeCommand(String userSentence, File file, File archive) {
		CommandType commandType;
		ResultOfCommand results = new ResultOfCommand();
		String[] splitCommand = Parser.parseInput(userSentence);
		String action = getCommandWord(splitCommand);
		commandType = determineCommandType(action);
		Task taskToExecute = new Task(splitCommand);
		String feedback = "";
		switch (commandType) {
		case ADD_TEXT:
			ArrayList<Task> tasksFound = findClash(taskToExecute);
			if (tasksFound.size() == 0){
				results.setFeedback(Logic.add(ACTION_ADD, taskToExecute, file));
			} else {
				results.setListOfTasks(tasksFound);
				JFrame frame = new JFrame();
				int n = confirmClashIsOk(frame, ACTION_ADD);
				UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
				if (n == JOptionPane.YES_OPTION){
					results.setFeedback(Logic.add(ACTION_ADD,taskToExecute, file));			
				} else {
					results.setFeedback(String.format(MSG_USER_CONFIRMED_NO, ACTION_ADD));
				}
			}
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		case CLEAR_SCREEN:
			results.setFeedback(Constants.MSG_SCREEN_CLEARED);
			results.setListOfTasks(new ArrayList<Task>());
			return results;
		case CLEAR_ARCHIVE:
			results.setFeedback(Logic.clearArchive(archive)); 
			results.setListOfTasks(Logic.getArchiveStorage());
			results.setTitleOfPanel(TITLE_ARCHIVED_TASKS);
			return results;
		case DELETE_ALL:
			results.setFeedback(Logic.clearContent(file));
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel(TITLE_ALL_TASKS);
			return results;
		case DELETE_DATE:
			Task taskWithThisDate = new Task();
			taskWithThisDate.setDate(taskToExecute.getDate());
			return deleteDate(file, archive, results, taskWithThisDate);
		case DELETE_PAST:
			//Force sort by time first
			Task.setSortedByTime(true);
			Logic.sortByDateAndTime(Logic.getTempStorage());
			int indexToDeleteBefore = Logic.getFirstNotOverdueInList();
			try {
				if (indexToDeleteBefore > 1){
					int [] indexesToDelete = new int [indexToDeleteBefore-1]; 
					for (int j = 0; j < indexesToDelete.length; j++){
						indexesToDelete[j]= j+1;
					}
					deleteMultiple(file, archive, results, feedback, indexesToDelete);
					results.setFeedback(MSG_DELETE_PAST_SUCCESS);
					results.setListOfTasks(Logic.getTempStorage());
					return results;
				} else {
					results.setFeedback(MSG_DELETE_PAST_FAIL);
					results.setListOfTasks(Logic.getTempStorage());
					return results;
				}
			} catch (NegativeArraySizeException e) {
				results.setFeedback(MSG_DELETE_PAST_FAIL);
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			}
		case DELETE_TODAY:
			Task todayOnly = new Task();
			todayOnly.setDate(getTodayDate());
			deleteDate(file, archive, results, todayOnly);
			results.setFeedback(MSG_DELETED_TODAY);
			return results;
		case DELETE_TEXT:
			String params = taskToExecute.getParams();
			//Because multiple deletions is handled by Controller.
			if (params != null){
				String [] splitParams = params.split("\\s+");
				int [] splitIndex = new int [splitParams.length];
				for (int j = 0; j < splitParams.length; j ++){
					int indexToDelete = Integer.parseInt(splitParams[j]);
					if (indexToDelete > 0){
						splitIndex[j] = indexToDelete;
					} 
				}
				sortIndex(splitIndex);
				deleteMultiple(file, archive, results, feedback, splitIndex);
			} else { 
				results.setFeedback(MSG_DELETE_NO_INDEX);
			}
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		case EDIT:
			ArrayList<Task> clashFoundForEdit = findClash(taskToExecute);
			if (taskToExecute.getDetails() !=null && taskToExecute.getDetails().equals("")){
				Task.setIsDetailsShown(false);
			}
			if (clashFoundForEdit.size() == 0){
				results.setFeedback(Logic.edit(ACTION_EDIT, taskToExecute, file));
			} else {
				results.setListOfTasks(clashFoundForEdit);
				JFrame frame = new JFrame();
				int n = confirmClashIsOk(frame, ACTION_EDIT);
				if (n == JOptionPane.YES_OPTION){
					results.setFeedback(Logic.edit(ACTION_EDIT, taskToExecute, file));			 
				} else {
					results.setFeedback(String.format(MSG_USER_CONFIRMED_NO, ACTION_EDIT));
				}
			}
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		case EXIT:
			System.exit(0);
		case SEARCH:
			results.setListOfTasks(Logic.search(taskToExecute));
			int numMatches = results.getListOfTasks().size();
			results.setFeedback(String.format(MSG_FOUND_N_ITEMS, numMatches));
			results.setTitleOfPanel(String.format(TITLE_SEARCH_RESULTS, getSearchTermOnly(taskToExecute)));
			return results;
		case SHOW_ALL:
			results.setFeedback(MSG_SHOW_ALL_SUCCESS);
			results.setTitleOfPanel(TITLE_ALL_TASKS);
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		case SHOW_FLOATING:
			Task dateFloating = new Task ();
			dateFloating.setDate(DATE_FT);
			results.setListOfTasks( Logic.search(dateFloating));
			results.setFeedback(MSG_SHOW_FLOATING_SUCCESS);
			results.setTitleOfPanel(TITLE_FLOATING_TASKS);
			return results;
		case SHOW_TODAY:
			Task dateToday = new Task();
			dateToday.setDate(getTodayDate());
			results.setListOfTasks(Logic.search(dateToday));
			results.setFeedback(MSG_SHOW_TODAY_SUCCESS);
			results.setTitleOfPanel(TITLE_TODAY_TASKS);
			return results;
		case SHOW_DETAILS:
			Task.setIsDetailsShown(true);
			results.setFeedback(MSG_SHOW_DETAILS_SUCCESS);
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel(TITLE_ALL_TASKS);
			return results;
		case HIDE_DETAILS: 
			Task.setIsDetailsShown(false);
			results.setFeedback(MSG_HIDE_DETAILS_SUCCESS);
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel(TITLE_ALL_TASKS);
			return results;
		case SORT_TIME:
			Task.setSortedByTime(true);
			results.setFeedback(Logic.sortByDateAndTime(Logic.getTempStorage()));
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel(TITLE_ALL_TASKS);
			return results;
		case SORT_ALPHA:
			Task.setSortedByTime(false);
			results.setFeedback(Logic.sortByAlphabet(Logic.getTempStorage()));
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel(TITLE_ALPHABETICAL_ORDER);
			return results;
		case SORT_IMPORTANCE:
			Task.setSortedByTime(false);
			results.setFeedback(Logic.sortByImportance(Logic.getTempStorage()));
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel(TITLE_IMPORTANCE_ORDER);
			return results;
		case RESTORE:
			return results; //stub
		case UNDO: 
			results.setFeedback(Logic.undo(file, archive));
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		case REDO:
			results.setFeedback(Logic.redo(file, archive));
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		case VIEW_ARCHIVE:
			results.setTitleOfPanel(TITLE_ARCHIVED_TASKS);
			results.setFeedback(MSG_ARCHIVED_TASKS);
			results.setListOfTasks(Logic.getArchiveStorage());
			return results;
		default:
			try {
				results.setFeedback(taskToExecute.getError());
			} catch (NullPointerException e){
				results.setFeedback(Constants.MSG_INVALID_COMMAND);
			}
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		}
	}

	private static void deleteMultiple(File file, File archive,
			ResultOfCommand results, String feedback, int[] splitIndex) {
		for (int j = splitIndex.length - 1; j >= 0; j--){
			if (splitIndex[j] > Logic.getTempStorage().size()){
				feedback = String.format(MSG_ITEM_TO_DELETE_NOT_FOUND, splitIndex[j]) + feedback;
				continue; //Because cannot delete numbers larger than list size
			}
			/*if (splitIndex[j] <= 0){
				feedback += MSG_CNT_DELETE_ZERO;
				return; //Because cannot delete zero or negative number
			}*/
			Task oneOutOfMany = new Task();
			String userDeleteIndex = String.valueOf(splitIndex[j]); 
			oneOutOfMany.setParams(userDeleteIndex);
			feedback = Logic.delete(ACTION_DELETE, splitIndex.length, oneOutOfMany, file, archive) + "," + feedback ;
		}
		String firstPart = "";
		String secondPart = "";
		if (feedback.length() > MAX_LEN_FEEDBACK){
			firstPart = feedback.substring(0, MAX_LEN_FEEDBACK);
			//feedback = feedback.substring(0, MAX_LEN_FEEDBACK);
			int lastCommaIndex = firstPart.lastIndexOf(",");
			if (lastCommaIndex != -1){
				firstPart = firstPart.substring(0, lastCommaIndex) + ", ...";
			}
			secondPart = feedback.substring(MAX_LEN_FEEDBACK);
			int lastCommaSecondPart = secondPart.lastIndexOf(",", secondPart.length()- 2);
			if (lastCommaSecondPart != -1){
				secondPart = secondPart.substring(lastCommaSecondPart);
			}
			feedback = firstPart + secondPart;
		}
		feedback = capitalizeFirstLetter(feedback);
		feedback = endWithFulstop(feedback);
		feedback = feedback.replace("deleted", "");
		results.setFeedback(feedback);
	}

	private static int confirmClashIsOk(JFrame frame, String action) {
		int n = JOptionPane.showConfirmDialog(
				frame,
				String.format(MSG_CLASH_FOUND, action),
				TITLE_JDIALOG_CLASH_FOUND,
				JOptionPane.YES_NO_OPTION);
		return n;
	}

	private static ResultOfCommand deleteDate(File file, File archive,
			ResultOfCommand results, Task withParticularDate) {
		ArrayList<Task> allThoseTasks= Logic.search(withParticularDate);
		if (allThoseTasks.isEmpty()){
			results.setFeedback(String.format(MSG_NO_TASK_FOR_DATE, withParticularDate.getDate()));
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		}
		int [] splitIndexA = new int [allThoseTasks.size()];
		for (int j = 0 ; j < allThoseTasks.size(); j++){
			splitIndexA[j] = j+1;
		}
		String feedback1 = "";
		deleteMultiple(file, archive, results, feedback1, splitIndexA);
		results.setListOfTasks(Logic.getTempStorage());
		return results;
	}

	private static String endWithFulstop(String feedback) {
		if (feedback.endsWith(",")){
			feedback = feedback.substring(0, feedback.lastIndexOf(",")) + " from your list.";
		}
		if (feedback.endsWith("..."))
			feedback = feedback + " from your list.";
		return feedback;
	}

	private static String capitalizeFirstLetter(String feedback) {
		if (!feedback.isEmpty()){
			feedback = feedback.substring(0,1).toUpperCase() + feedback.substring(1); // Capitalize first letter
		}
		return feedback;
	}

	private static String getSearchTermOnly(Task task) {
		String searchTerm = "";
		if (task.getName() != null){
			searchTerm += task.getName();
		}
		if (task.getDate() != null){
			if (searchTerm.length()>0){
				searchTerm += " ";
			}
			searchTerm += task.getDate();
		}
		if (task.getTime() != null){
			if (searchTerm.length()>0){
				searchTerm += " ";
			}
			searchTerm += task.getTime();
		}
		return searchTerm;
	}

	//Sort index from smallest to largest for multiple deletion.
	private static void sortIndex(int[] splitIndex) {
		int n = splitIndex.length;
		int temp = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 1; j < (n - i); j++) {
				if (splitIndex[j - 1] > splitIndex[j]) {
					temp = splitIndex[j - 1];
					splitIndex[j - 1] = splitIndex[j];
					splitIndex[j] = temp;
				}
			}
		}
	}

	//Return any tasks with the same date and time as taskToExecute
	private static ArrayList<Task> findClash(Task taskToExecute) {
		//Do not check for clash if is floating, because it will always clash
		if (taskToExecute.getDate() != null && taskToExecute.getDate().equals(DATE_FT)){
			return new ArrayList<Task>();
		}
		//If time is null, means there is no time allocated for that task today
		if (taskToExecute.getTime() == null){
			return new ArrayList<Task>();
		}
		Task tempTask = new Task();
		tempTask.setDate(taskToExecute.getDate());
		tempTask.setTime(taskToExecute.getTime());
		ArrayList<Task> searchResult = Logic.search(tempTask);
		Logic.undoPopForSearchClash();
		return searchResult;
	}

	// This method is used to determine the command types given the first word of the command.
	private static CommandType determineCommandType(String commandTypeString) {
		if (commandTypeString == null) {
			throw new Error(ERROR_NULL_COMMAND);
		}
		if (commandTypeString.equalsIgnoreCase(ACTION_ADD)) {
			return CommandType.ADD_TEXT;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_CLEAR)) {
			return CommandType.CLEAR_SCREEN;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_CLEAR_ARCHIVE)) {
			return CommandType.CLEAR_ARCHIVE;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_DELETE)) {
			return CommandType.DELETE_TEXT;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_DELETE_ALL)) {
			return CommandType.DELETE_ALL;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_DELETE_DATE)) {
			return CommandType.DELETE_DATE;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_DELETE_PAST)) {
			return CommandType.DELETE_PAST;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_DELETE_TODAY)) {
			return CommandType.DELETE_TODAY;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_EDIT)) {
			return CommandType.EDIT;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_EXIT)) {
			return CommandType.EXIT;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_HELP)) {
			return CommandType.HELP;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_HIDE_DETAILS)) {
			return CommandType.HIDE_DETAILS;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_SEARCH)) {
			return CommandType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_SORT_TIME)) {
			return CommandType.SORT_TIME;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_SORT_ALPHA)) {
			return CommandType.SORT_ALPHA;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_SORT_IMPORTANCE)) {
			return CommandType.SORT_IMPORTANCE;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_SHOW_ALL)) {
			return CommandType.SHOW_ALL;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_SHOW_FLOATING)) {
			return CommandType.SHOW_FLOATING;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_SHOW_TODAY)) {
			return CommandType.SHOW_TODAY;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_SHOW_DETAILS)) {
			return CommandType.SHOW_DETAILS;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_RESTORE)) {
			return CommandType.RESTORE;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_REDO)) {
			return CommandType.REDO;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_UNDO)) {
			return CommandType.UNDO;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_VIEW_ARCHIVE)) {
			return CommandType.VIEW_ARCHIVE;
		} else {
			return CommandType.INVALID;
		}
	}

	private static String getCommandWord(String[] userCommand) {
		assert userCommand.length >0;
		String firstWord = userCommand[0];
		return firstWord;
	}

	private static String getTodayDate() {
		DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date date = new Date();
		String reportDate = dateFormat.format(date);
		return reportDate;
	}
}