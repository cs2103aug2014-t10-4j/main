import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Controller {

	private static final String MSG_FOUND_N_ITEMS = "Found %d items.";
	private static final String MSG_ITEM_TO_DELETE_NOT_FOUND = "item #%d is not found, ";
	private static final String MSG_CNT_DELETE_ZERO = "cannot delete index equal or below 0.";
	private static final String MSG_USER_CONFIRMED_NO = "Task is not %sed.";
	private static final String ACTION_EDIT = "edit";
	private static final String ACTION_ADD = "add";
	private static final String TITLE_JDIALOG_CLASH_FOUND = "Clash found";
	private static final String MSG_CLASH_FOUND = "Something is happening at the same time! Continue %sing?";
	private static final String DATE_FT = "ft";

	enum CommandType {
		ADD_TEXT, CLEAR_SCREEN, DELETE_ALL, DELETE_PAST, DELETE_TEXT, DELETE_TODAY, 
		EDIT, EXIT, HELP, INVALID, SEARCH, SHOW_ALL, SHOW_FLOATING, SHOW_TODAY, 
		SHOW_DETAILS, HIDE_DETAILS, SORT_TIME, SORT_ALPHA, SORT_IMPORTANCE, RESTORE, REDO, UNDO;
	};

	public static ResultOfCommand executeCommand(String userSentence, File file, File archive) {
		CommandType commandType;
		ResultOfCommand results = new ResultOfCommand();
		String[] splitCommand = Parser.parseInput(userSentence);
		String action = getCommandWord(splitCommand);
		commandType = determineCommandType(action);
		Task taskToExecute = new Task(splitCommand);
		switch (commandType) {
		case ADD_TEXT:
			ArrayList<Task> tasksFound = findClash(taskToExecute);
			if (tasksFound.size() == 0){
				results.setFeedback(Logic.add(ACTION_ADD, taskToExecute, file));
			} else {
				results.setListOfTasks(tasksFound);
				JFrame frame = new JFrame();
				int n = confirmClashIsOk(frame, ACTION_ADD);
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
		case DELETE_ALL:
			results.setFeedback(Logic.clearContent(file));
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel("All Tasks:");
			return results;
		case DELETE_TODAY:
			Task todayOnly = new Task();
			todayOnly.setDate(getTodayDate());
			return deleteDate(file, archive, results, todayOnly);
		case DELETE_TEXT:
			String params = taskToExecute.getParams();
			String feedback = "";
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
				for (int j = splitIndex.length - 1; j >= 0; j--){
					if (splitIndex[j] > Logic.getTempStorage().size()){
						feedback = String.format(MSG_ITEM_TO_DELETE_NOT_FOUND, splitIndex[j]) + feedback;
						continue; //Because cannot delete numbers larger than list size
					}
					if (splitIndex[j] <= 0){
						feedback += MSG_CNT_DELETE_ZERO;
						break; //Because cannot delete zero or negative number
					}
					Task oneOutOfMany = new Task();
					String userDeleteIndex = String.valueOf(splitIndex[j]); 
					oneOutOfMany.setParams(userDeleteIndex);
					feedback = Logic.delete("delete", splitIndex.length, oneOutOfMany, file, archive) +", " + feedback ;
				}
				feedback = capitalizeFirstLetter(feedback);
				feedback = endWithFulstop(feedback);
				results.setFeedback(feedback);
			} else { 
				results.setFeedback("You must add a number after delete");
			}
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		case EDIT:
			ArrayList<Task> clashFoundForEdit = findClash(taskToExecute);
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
			results.setListOfTasks(Logic.search(taskToExecute) );
			int numMatches = results.getListOfTasks().size();
			results.setFeedback(String.format(MSG_FOUND_N_ITEMS, numMatches));
			results.setTitleOfPanel("Search Results for \""+ getSearchTermOnly(taskToExecute) + "\"");
			return results;
		case SHOW_ALL:
			results.setFeedback("These are all your tasks.");
			results.setTitleOfPanel("All Tasks:");
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		case SHOW_FLOATING:
			Task dateFloating = new Task ();
			dateFloating.setDate(DATE_FT);
			results.setListOfTasks( Logic.search(dateFloating));
			results.setFeedback("These are your floating tasks.");
			results.setTitleOfPanel("Floating Tasks:");
			return results;
		case SHOW_TODAY:
			Task dateToday = new Task();
			dateToday.setDate(getTodayDate());
			results.setListOfTasks(Logic.search(dateToday));
			results.setFeedback("These are your tasks for the day.");
			results.setTitleOfPanel("Today Tasks:");
			return results;
		case SHOW_DETAILS:
			Task.setIsDetailsShown(true);
			results.setFeedback("Details are expanded.");
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel("All Tasks:");
			return results;
		case HIDE_DETAILS: 
			Task.setIsDetailsShown(false);
			results.setFeedback("Details are collapsed.");
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel("All Tasks:");
			return results;
		case SORT_TIME:
			Task.setSortedByTime(true);
			results.setFeedback(Logic.sortByDateAndTime(Logic.getTempStorage()));
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel("All Tasks:");
			return results;
		case SORT_ALPHA:
			Task.setSortedByTime(false);
			results.setFeedback(Logic.sortByAlphabet(Logic.getTempStorage()));
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel("All tasks by alphabetical order");
			return results;
		case SORT_IMPORTANCE:
			Task.setSortedByTime(false);
			results.setFeedback(Logic.sortByImportance(Logic.getTempStorage()));
			results.setListOfTasks(Logic.getTempStorage());
			results.setTitleOfPanel("All tasks by importance order");
			return results;
		case RESTORE:
			return results; //stub
		case UNDO: 
			Logic.undo(file, archive);
			results.setFeedback("Previous action is undone");
			results.setListOfTasks(Logic.getTempStorage());
			return results;
		case REDO:
			Logic.redo(file, archive); 
			results.setFeedback("Previous action is done again");
			results.setListOfTasks(Logic.getTempStorage());
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
		int [] splitIndexA = new int [allThoseTasks.size()];
		for (int j = 0 ; j < allThoseTasks.size(); j++){
			splitIndexA[j] = j+1;
		}
		String feedback1 ="";
		for (int j = splitIndexA.length - 1; j >= 0; j--){
			if (splitIndexA[j] > Logic.getTempStorage().size()){
				feedback1 = String.format(MSG_ITEM_TO_DELETE_NOT_FOUND, splitIndexA[j]) + feedback1;
				continue; //Because cannot delete numbers larger than list size
			}
			if (splitIndexA[j] <= 0){
				feedback1 += MSG_CNT_DELETE_ZERO;
				break; //Because cannot delete zero or negative number
			}
			Task oneOutOfMany = new Task();
			String userDeleteIndex = String.valueOf(splitIndexA[j]); 
			oneOutOfMany.setParams(userDeleteIndex);
			feedback1 = Logic.delete("delete", splitIndexA.length, oneOutOfMany, file, archive) +", " + feedback1 ;
		}
		feedback1 = capitalizeFirstLetter(feedback1);
		feedback1 = endWithFulstop(feedback1);
		results.setFeedback(feedback1);
		results.setListOfTasks(Logic.getTempStorage());
		return results;
	}

	private static String endWithFulstop(String feedback) {
		if (feedback.endsWith(", ")){
			feedback = feedback.substring(0, feedback.lastIndexOf(",")) + ".";
		}
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
			searchTerm += " " + task.getDate();
		}
		if (task.getTime() != null){
			searchTerm += " " + task.getTime();
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
		return Logic.search(tempTask);
	}

	// This method is used to determine the command types given the first word of the command.
	private static CommandType determineCommandType(String commandTypeString) {
		if (commandTypeString == null) {
			throw new Error("command type string cannot be null!");
		}
		if (commandTypeString.equalsIgnoreCase(ACTION_ADD)) {
			return CommandType.ADD_TEXT;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return CommandType.CLEAR_SCREEN;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return CommandType.DELETE_TEXT;
		} else if (commandTypeString.equalsIgnoreCase("delete all")) {
			return CommandType.DELETE_ALL;
		} else if (commandTypeString.equalsIgnoreCase("delete today")) {
			return CommandType.DELETE_TODAY;
		} else if (commandTypeString.equalsIgnoreCase(ACTION_EDIT)) {
			return CommandType.EDIT;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return CommandType.EXIT;
		} else if (commandTypeString.equalsIgnoreCase("help")) {
			return CommandType.HELP;
		} else if (commandTypeString.equalsIgnoreCase("hide details")) {
			return CommandType.HIDE_DETAILS;
		} else if (commandTypeString.equalsIgnoreCase("search")) {
			return CommandType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase("sort time")) {
			return CommandType.SORT_TIME;
		} else if (commandTypeString.equalsIgnoreCase("sort alpha")) {
			return CommandType.SORT_ALPHA;
		} else if (commandTypeString.equalsIgnoreCase("sort importance")) {
			return CommandType.SORT_IMPORTANCE;
		} else if (commandTypeString.equalsIgnoreCase("show all")) {
			return CommandType.SHOW_ALL;
		} else if (commandTypeString.equalsIgnoreCase("show floating")) {
			return CommandType.SHOW_FLOATING;
		} else if (commandTypeString.equalsIgnoreCase("show today")) {
			return CommandType.SHOW_TODAY;
		} else if (commandTypeString.equalsIgnoreCase("show details")) {
			return CommandType.SHOW_DETAILS;
		} else if (commandTypeString.equalsIgnoreCase("restore")) {
			return CommandType.RESTORE;
		} else if (commandTypeString.equalsIgnoreCase("redo")) {
			return CommandType.REDO;
		} else if (commandTypeString.equalsIgnoreCase("undo")) {
			return CommandType.UNDO;
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