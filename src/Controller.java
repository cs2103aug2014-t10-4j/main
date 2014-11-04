//@author Low Zheng Yang A0110930X

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Controller {


	enum CommandType {
		ADD_TEXT, CLEAR_SCREEN, CLEAR_ARCHIVE, DELETE_ALL, DELETE_DATE, DELETE_PAST, 
		DELETE_TEXT, DELETE_TODAY, EDIT, EXIT, HELP, HIDE_DETAILS, INVALID, 
		SEARCH, SHOW_ALL, SHOW_FLOATING, SHOW_TODAY, SHOW_DETAILS, SHOW_THIS_WEEK, 
		SHOW_WEEK, SORT_TIME, SORT_ALPHA, SORT_IMPORTANCE, RESTORE, REDO, UNDO, 
		VIEW_ARCHIVE;
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
				results.setFeedback(Logic.add(Constants.ACTION_ADD, taskToExecute, file));
			} else {
				results.setListOfTasks(tasksFound);
				JFrame frame = new JFrame();
				int n = confirmClashIsOk(frame, Constants.ACTION_ADD);
				UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
				if (n == JOptionPane.YES_OPTION){
					results.setFeedback(Logic.add(Constants.ACTION_ADD,taskToExecute, file));			
				} else {
					results.setFeedback(String.format(Constants.MSG_USER_CONFIRMED_NO, Constants.ACTION_ADD));
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
			results.setTitleOfPanel(Constants.TITLE_ARCHIVED_TASKS);
			return results;
		case DELETE_ALL:
			results.setFeedback(Logic.clearContent(file, archive));
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel(Constants.TITLE_ALL_TASKS);
			return results;
		case DELETE_DATE:
			Task taskWithThisDate = new Task();
			taskWithThisDate.setDate(taskToExecute.getDate());
			return deleteTasksForDate(file, archive, results, taskWithThisDate);
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
					results.setFeedback(Constants.MSG_DELETE_PAST_SUCCESS);
					results.setListOfTasks(Logic.getTempStorage());
					return results;
				} else {
					results.setFeedback(Constants.MSG_DELETE_PAST_FAIL);
					results.setListOfTasks(Logic.getTempStorage());
					return results;
				}
			} catch (NegativeArraySizeException e) {
				results.setFeedback(Constants.MSG_DELETE_PAST_FAIL);
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			}
		case DELETE_TODAY:
			Task todayOnly = new Task();
			todayOnly.setDate(getTodayDate());
			deleteTasksForDate(file, archive, results, todayOnly);
			results.setFeedback(Constants.MSG_DELETED_TODAY);
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
				results.setFeedback(Constants.MSG_DELETE_NO_INDEX);
			}
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		case EDIT:
			ArrayList<Task> clashFoundForEdit = findClash(taskToExecute);
			if (taskToExecute.getDetails() !=null && taskToExecute.getDetails().equals("")){
				Task.setIsDetailsShown(false);
			}
			if (clashFoundForEdit.size() == 0){
				results.setFeedback(Logic.edit(Constants.ACTION_EDIT, taskToExecute, file));
			} else {
				results.setListOfTasks(clashFoundForEdit);
				JFrame frame = new JFrame();
				int n = confirmClashIsOk(frame, Constants.ACTION_EDIT);
				if (n == JOptionPane.YES_OPTION){
					results.setFeedback(Logic.edit(Constants.ACTION_EDIT, taskToExecute, file));			 
				} else {
					results.setFeedback(String.format(Constants.MSG_USER_CONFIRMED_NO, 
							Constants.ACTION_EDIT));
				}
			}
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		case EXIT:
			System.exit(0);
		case SEARCH:
			results.setListOfTasks(Logic.search(taskToExecute));
			int numMatches = results.getListOfTasks().size();
			results.setFeedback(String.format(Constants.MSG_FOUND_N_ITEMS, numMatches));
			results.setTitleOfPanel(String.format(Constants.TITLE_SEARCH_RESULTS, getSearchTermOnly(taskToExecute)));
			return results;
		case SHOW_ALL:
			results.setFeedback(Constants.MSG_SHOW_ALL_SUCCESS);
			results.setTitleOfPanel(Constants.TITLE_ALL_TASKS);
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		case SHOW_FLOATING:
			Task dateFloating = new Task ();
			dateFloating.setDate(Constants.DATE_FT);
			results.setListOfTasks( Logic.search(dateFloating));
			results.setFeedback(Constants.MSG_SHOW_FLOATING_SUCCESS);
			results.setTitleOfPanel(Constants.TITLE_FLOATING_TASKS);
			return results;
		case SHOW_TODAY:
			Task dateToday = new Task();
			dateToday.setDate(getTodayDate());
			results.setListOfTasks(Logic.search(dateToday));
			results.setFeedback(Constants.MSG_SHOW_TODAY_SUCCESS);
			results.setTitleOfPanel(Constants.TITLE_TODAY_TASKS);
			return results;
		case SHOW_DETAILS:
			Task.setIsDetailsShown(true);
			results.setFeedback(Constants.MSG_SHOW_DETAILS_SUCCESS);
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel(Constants.TITLE_ALL_TASKS);
			return results;
		case HIDE_DETAILS: 
			Task.setIsDetailsShown(false);
			results.setFeedback(Constants.MSG_HIDE_DETAILS_SUCCESS);
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel(Constants.TITLE_ALL_TASKS);
			return results;
		case SHOW_WEEK:
			// Set the date today
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			// Calculates the start date of the week
			Calendar firstDay = (Calendar) calendar.clone();
			// and add six days to the end date
			Calendar lastDay = (Calendar) firstDay.clone();
			lastDay.add(Calendar.DAY_OF_YEAR, 6);

			DateFormat dateFormat1 = new SimpleDateFormat(Constants.DATE_FORMAT);
			Task startOfSevenDays = new Task();
			Task endOfSevenDays = new Task();
			startOfSevenDays.setDate(dateFormat1.format(firstDay.getTime()));
			endOfSevenDays.setDate(dateFormat1.format(lastDay.getTime()));
			String rangeOfSevenDays = getRangeOfWeek(startOfSevenDays, endOfSevenDays);
			results.setListOfTasks(Logic.searchRangeOfDate(startOfSevenDays, endOfSevenDays));
			results.setFeedback(Constants.MSG_SHOW_SEVEN_DAYS_SUCCESS);
			results.setTitleOfPanel(String.format(Constants.TITLE_SHOW_SEVEN_DAYS, rangeOfSevenDays));
			return results;
		case SHOW_THIS_WEEK:
			// Set the date today
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			// Calculates the start date of the week
			Calendar first = (Calendar) cal.clone();
			first.add(Calendar.DAY_OF_WEEK, 
					first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));
			// and add six days to the end date
			Calendar last = (Calendar) first.clone();
			last.add(Calendar.DAY_OF_YEAR, 6);

			DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
			Task startOfWeekTask = new Task();
			Task endOfWeekTask = new Task();
			startOfWeekTask.setDate(dateFormat.format(first.getTime()));
			endOfWeekTask.setDate(dateFormat.format(last.getTime()));
			String rangeOfWeek = getRangeOfWeek(startOfWeekTask, endOfWeekTask);
			results.setListOfTasks(Logic.searchRangeOfDate(startOfWeekTask, endOfWeekTask));
			results.setFeedback(Constants.MSG_SHOW_THIS_WEEK_SUCCESS);
			results.setTitleOfPanel(String.format(Constants.TITLE_SHOW_WEEK, rangeOfWeek));
			return results;
		case SORT_TIME:
			Task.setSortedByTime(true);
			results.setFeedback(Logic.sortByDateAndTime(Logic.getTempStorage()));
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel(Constants.TITLE_ALL_TASKS);
			return results;
		case SORT_ALPHA:
			Task.setSortedByTime(false);
			results.setFeedback(Logic.sortByAlphabet(Logic.getTempStorage()));
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel(Constants.TITLE_ALPHABETICAL_ORDER);
			return results;
		case SORT_IMPORTANCE:
			Task.setSortedByTime(false);
			results.setFeedback(Logic.sortByImportance(Logic.getTempStorage()));
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel(Constants.TITLE_IMPORTANCE_ORDER);
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
			results.setTitleOfPanel(Constants.TITLE_ARCHIVED_TASKS);
			results.setFeedback(Constants.MSG_ARCHIVED_TASKS);
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

	private static String getRangeOfWeek(Task startOfWeekTask,
			Task endOfWeekTask) {
		return String.format(Constants.MSG_RANGE_OF_WEEK,
				getDayOfWeek(startOfWeekTask.getDate()),  
				startOfWeekTask.getDate(), 
				getDayOfWeek(endOfWeekTask.getDate()),
				endOfWeekTask.getDate());
	}

	// This method is used to determine the command types given the first word of the command.
	private static CommandType determineCommandType(String commandTypeString) {
		if (commandTypeString == null) {
			throw new Error(Constants.ERROR_NULL_COMMAND);
		}
		if (commandTypeString.equalsIgnoreCase(Constants.ACTION_ADD)) {
			return CommandType.ADD_TEXT;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_CLEAR)) {
			return CommandType.CLEAR_SCREEN;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_CLEAR_ARCHIVE)) {
			return CommandType.CLEAR_ARCHIVE;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_DELETE)) {
			return CommandType.DELETE_TEXT;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_DELETE_ALL)) {
			return CommandType.DELETE_ALL;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_DELETE_DATE)) {
			return CommandType.DELETE_DATE;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_DELETE_PAST)) {
			return CommandType.DELETE_PAST;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_DELETE_TODAY)) {
			return CommandType.DELETE_TODAY;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_EDIT)) {
			return CommandType.EDIT;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_EXIT)) {
			return CommandType.EXIT;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_HELP)) {
			return CommandType.HELP;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_HIDE_DETAILS)) {
			return CommandType.HIDE_DETAILS;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_SEARCH)) {
			return CommandType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_SORT_TIME)) {
			return CommandType.SORT_TIME;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_SORT_ALPHA)) {
			return CommandType.SORT_ALPHA;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_SORT_IMPORTANCE)) {
			return CommandType.SORT_IMPORTANCE;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_SHOW_ALL)) {
			return CommandType.SHOW_ALL;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_SHOW_FLOATING)) {
			return CommandType.SHOW_FLOATING;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_SHOW_TODAY)) {
			return CommandType.SHOW_TODAY;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_SHOW_WEEK)) {
			return CommandType.SHOW_WEEK;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_SHOW_THIS_WEEK)) {
			return CommandType.SHOW_THIS_WEEK;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_SHOW_DETAILS)) {
			return CommandType.SHOW_DETAILS;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_RESTORE)) {
			return CommandType.RESTORE;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_REDO)) {
			return CommandType.REDO;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_UNDO)) {
			return CommandType.UNDO;
		} else if (commandTypeString.equalsIgnoreCase(Constants.ACTION_VIEW_ARCHIVE)) {
			return CommandType.VIEW_ARCHIVE;
		} else {
			return CommandType.INVALID;
		}
	}

	//Get first word in the command position
	private static String getCommandWord(String[] userCommand) {
		assert userCommand.length >0;
		String firstWord = userCommand[Constants.COMMAND_POSITION];
		return firstWord;
	}

	private static void deleteMultiple(File file, File archive,
			ResultOfCommand results, String feedback, int[] splitIndex) {
		boolean isMoreThanSizeOfList = false;
		for (int j = splitIndex.length - 1; j >= 0; j--){
			if (splitIndex[j] > Logic.getTempStorage().size()){
				isMoreThanSizeOfList = true;
				continue; //Because cannot delete numbers larger than list size
			}
			Task oneOutOfMany = new Task();
			String userDeleteIndex = String.valueOf(splitIndex[j]); 
			oneOutOfMany.setParams(userDeleteIndex);
			if (splitIndex[j] > 0){
				feedback = Logic.delete(Constants.ACTION_DELETE, splitIndex.length, oneOutOfMany, 
						file, archive) + "," + feedback ;
			} else  {
				feedback = capitalizeFirstLetter(feedback);
				feedback = Logic.delete(Constants.ACTION_DELETE, splitIndex.length, oneOutOfMany, 
						file, archive) + ". " + feedback ;

			}
		}
		String firstPart = "";
		String secondPart = "";
		if (feedback.length() > Constants.MAX_LEN_FEEDBACK){
			firstPart = feedback.substring(0, Constants.MAX_LEN_FEEDBACK);
			//feedback = feedback.substring(0, MAX_LEN_FEEDBACK);
			int lastCommaIndex = firstPart.lastIndexOf(",");
			if (lastCommaIndex != -1){
				firstPart = firstPart.substring(0, lastCommaIndex) + ", ...";
			}
			secondPart = feedback.substring(Constants.MAX_LEN_FEEDBACK);
			int lastCommaSecondPart = secondPart.lastIndexOf(",", secondPart.length()- 2);
			if (lastCommaSecondPart != -1){
				secondPart = secondPart.substring(lastCommaSecondPart);
			}
			feedback = firstPart + secondPart;
		}
		feedback = capitalizeFirstLetter(feedback);
		feedback = endWithFulstop(feedback);
		feedback = feedback.replace("deleted", "");
		if (isMoreThanSizeOfList){
			feedback = feedback + " You tried to delete non-existent tasks." ;
			feedback = feedback.trim();
		}
		results.setFeedback(feedback);
	}

	private static int confirmClashIsOk(JFrame frame, String action) {
		int n = JOptionPane.showConfirmDialog(
				frame, String.format(Constants.MSG_CLASH_FOUND, action),
				Constants.TITLE_JDIALOG_CLASH_FOUND,
				JOptionPane.YES_NO_OPTION);
		return n;
	}

	private static ResultOfCommand deleteTasksForDate(File file, File archive,
			ResultOfCommand results, Task withParticularDate) {
		ArrayList<Task> allThoseTasks= Logic.search(withParticularDate);
		if (allThoseTasks.isEmpty()){
			results.setFeedback(String.format(Constants.MSG_NO_TASK_FOR_DATE, withParticularDate.getDate()));
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
		if (feedback.endsWith(",") || (feedback.endsWith(", "))){
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
		if (task.getStartTime() != null){
			if (searchTerm.length()>0){
				searchTerm += " ";
			}
			searchTerm += task.getStartTime();
		}
		if (task.getEndTime() != null){
			if (searchTerm.length()>0){
				searchTerm += " - ";
			}
			searchTerm += task.getEndTime();
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
		if (taskToExecute.getDate() != null && taskToExecute.getDate().equals(Constants.DATE_FT)){
			return new ArrayList<Task>();
		}
		//If time is null, means there is no time allocated for that task today
		if (taskToExecute.getStartTime() == null){
			return new ArrayList<Task>();
		}
		Task tempTask = new Task();
		tempTask.setDate(taskToExecute.getDate());
		tempTask.setStartTime(taskToExecute.getStartTime());
		tempTask.setEndTime(taskToExecute.getEndTime());
		tempTask.setParams(taskToExecute.getParams());
		ArrayList<Task> searchResult = Logic.searchForCheckClash(tempTask);
		return searchResult;
	}

	private static String getTodayDate() {
		DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date date = new Date();
		String reportDate = dateFormat.format(date);
		return reportDate;
	}

	//Return the day of the week
	private static String getDayOfWeek(String date){
		try {
			Date mydate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).parse(date);
			return new SimpleDateFormat("EEE").format(mydate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
}