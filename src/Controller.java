import java.awt.Font;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Controller {

	private static final String MSG_EMPTY_TODAY = "No tasks for today!";
	private static final String MSG_EMPTY_ALL_DAYS = "No tasks for anyday!";
	private static final String MSG_INVALID_COMMAND = "Invalid command";
    protected static final int PRESET_TYPE_DATE = Font.BOLD;//
	protected static final int PRESET_TYPE_TIME = Font.BOLD;//
	enum CommandType {
		ADD_TEXT, DISPLAY_TEXT, DELETE_TEXT, CLEAR_FILE, EDIT, EXIT, INVALID, SEARCH, SORT, HELP, REDO, UNDO;
	};

	public static ResultOfCommand executeCommand(String userSentence, File file, File archive) {
		CommandType commandType;
		ResultOfCommand results = new ResultOfCommand();
		if (userSentence.contains(" ")){
			String[] splitCommand = Parser.parseInput(userSentence);
			String action = getFirstWord(splitCommand);
			commandType = determineCommandType(action);
			Task taskToExecute = new Task(splitCommand);
			switch (commandType) {
			case ADD_TEXT:
				ArrayList<Task> tasksFound = findClash(taskToExecute);
				if (tasksFound.size() == 0){
					results.setFeedback(Logic.add("add",taskToExecute, file));
					results.setListOfTasks(Logic.getTempStorage());
					return results;
				} else {
					results.setListOfTasks(tasksFound);
					JFrame frame = new JFrame();
					int n = JOptionPane.showConfirmDialog(
							frame,
							"Continue adding?",
							"Something is happening at the same time!",
							JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION){
						results.setFeedback(Logic.add("add",taskToExecute, file));			
					} else {
						results.setFeedback("Task is not added.");
					}
					results.setListOfTasks(Logic.getTempStorage());
					return results;
				}
				/*results.setListOfTasks(Logic.getTempStorage());
				results.setFeedback(Logic.addLineToFile(taskToExecute, file));
				return results;*/
			case DELETE_TEXT:
				String params = taskToExecute.getParams();
				String feedback = "";
				if (params != null){
					String [] splitParams = params.split("\\s+");
					int [] splitIndex = new int [splitParams.length];
					for (int j = 0; j < splitParams.length; j ++){
						splitIndex[j] = Integer.parseInt(splitParams[j]);
					}
					sortIndex(splitIndex);
					for (int j = splitIndex.length - 1; j >= 0; j--){
						Task oneOutOfMany = new Task();
						String userDeleteIndex = String.valueOf(splitIndex[j]); 
						oneOutOfMany.setParams(userDeleteIndex);
						feedback += Logic.delete("delete", splitIndex.length, oneOutOfMany, file, archive) + ", ";
					}
					results.setFeedback(feedback);
				} else { 
					results.setFeedback("You must add a number after /delete");
				}
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			case EDIT:
				results.setFeedback(Logic.edit("edit" /*stub*/, taskToExecute, file));
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			case SEARCH:
				results.setListOfTasks(Logic.search(taskToExecute) );
				results.setFeedback("This is what is found.");
				results.setTitleOfPanel("Search Results for \""+ getSearchTermOnly(userSentence) + "\"");
				return results;
			/*case UNDO: 
				Logic.undo(file, archive); //ACTIVATE THIS METHOD WHEN YOU THINK IT CAN WORK
				results.setFeedback("Previous action is undone");
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			case REDO:
				Logic.redo(file, archive); //ACTIVATE THIS METHOD WHEN YOU THINK IT CAN WORK
				results.setFeedback("Previous action is done again");
				results.setListOfTasks(Logic.getTempStorage());
				return results;*/ 
				//Activate all these above after Parser can accept one word commands
			default:
				results.setFeedback(MSG_INVALID_COMMAND);
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			}
		} else { 
			if (userSentence.equalsIgnoreCase("exit")) {
				System.exit(0);
			} else if (userSentence.equalsIgnoreCase("deleteall")) {
				results.setFeedback(Logic.clearContent(file));
				results.setListOfTasks(Logic.getTempStorage());
				results.setTitleOfPanel("All Tasks:");
				return results;
			} else if (userSentence.equalsIgnoreCase("sort")) {
				Task.setSortedByTime(true);
				results.setFeedback(Logic.sortByDateAndTime(Logic.getTempStorage()));
				results.setListOfTasks(Logic.getTempStorage());
				results.setTitleOfPanel("All Tasks:");
				return results;
			} else if (userSentence.equalsIgnoreCase("sortalpha")) {
				Task.setSortedByTime(false);
				results.setFeedback(Logic.sortByAlphabet(Logic.getTempStorage()));
				results.setListOfTasks(Logic.getTempStorage());
				results.setTitleOfPanel("All tasks by alphabetical order");
				return results;
			} else if (userSentence.equalsIgnoreCase("sortimpt")) {
				Task.setSortedByTime(false);
				results.setFeedback(Logic.sortByImportance(Logic.getTempStorage()));
				results.setListOfTasks(Logic.getTempStorage());
				results.setTitleOfPanel("All tasks by importance order");
				return results;
			} else if (userSentence.equalsIgnoreCase("showfloating")) {
				// Cannot delete from floating list or today list now because
				// number in floating list is different from tempStorage.
				// Deleting and editing after search will be written with cmdHistory.
				Task dateFloating = new Task ();
				dateFloating.setDate("ft");
				results.setListOfTasks( Logic.search(dateFloating));
				results.setFeedback("These are your floating tasks.");
				results.setTitleOfPanel("Floating Tasks:");
				return results;
			} else if (userSentence.equalsIgnoreCase("showtoday")) {
				//Need to ignore importance? Set to null?
				Task dateToday = new Task();
				dateToday.setDate(getTodayDate());
				results.setListOfTasks(Logic.search(dateToday));
				results.setFeedback("These are your tasks for the day.");
				results.setTitleOfPanel("Today Tasks:");
				return results;
			} else if (userSentence.equalsIgnoreCase("showall")) {
				results.setFeedback("These are all your tasks.");
				results.setTitleOfPanel("All Tasks:");
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			} else if (userSentence.equalsIgnoreCase("clear")) {
				results.setFeedback("Screen is cleared. Type showall, showtoday or showfloating again.");
				results.setListOfTasks(new ArrayList<Task>());
				return results;
			} else if (userSentence.equalsIgnoreCase("redo")) {
				Logic.redo(file, archive);
				results.setFeedback("Redo is called");
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			} else if (userSentence.equalsIgnoreCase("undo")) {
				Logic.undo(file, archive);
				results.setFeedback("Undo is called");
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			} else {
				results.setFeedback(MSG_INVALID_COMMAND);
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			}
		}
		return results;
	}

	private static String getSearchTermOnly(String userSentence) {
		String firstWord = userSentence.trim().split("\\s+")[0];
		return userSentence.replace(firstWord , "").trim();
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
		if (taskToExecute.getDate().equals("ft")){
			return new ArrayList<Task>();
		}
		//If time is null, means there is no time allocated for that task
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
		if (commandTypeString.equalsIgnoreCase("add")) {
			return CommandType.ADD_TEXT;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return CommandType.DISPLAY_TEXT;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return CommandType.DELETE_TEXT;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return CommandType.CLEAR_FILE;
		} else if (commandTypeString.equalsIgnoreCase("edit")) {
			return CommandType.EDIT;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return CommandType.EXIT;
		} else if (commandTypeString.equalsIgnoreCase("search")) {
			return CommandType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase("sort")) {
			return CommandType.SORT;
		} else if (commandTypeString.equalsIgnoreCase("help")) {
			return CommandType.HELP;
		} else if (commandTypeString.equalsIgnoreCase("redo")) {
			return CommandType.REDO;
		} else if (commandTypeString.equalsIgnoreCase("undo")) {
			return CommandType.UNDO;
		} else {
			return CommandType.INVALID;
		}
	}

	private static String getFirstWord(String[] userCommand) {
		String firstWord = userCommand[0];
		return firstWord;
	}

	public static String printTodayList (ArrayList<Task> listOfTasks){
		if (listOfTasks.size() ==0){
			return MSG_EMPTY_TODAY;
		} else {
			return printArrayList(listOfTasks);
		}
	}

	public static ArrayList<Task> getFloatingList (){
		ArrayList<Task> allTasks = Logic.getTempStorage();
		ArrayList <Task> listOfFloating = new ArrayList<Task>();
		for (int j=0; j < allTasks.size(); j++){
			if (allTasks.get(j).getDate().equals("ft")){
				listOfFloating.add(allTasks.get(j));
			}
		}
		return listOfFloating;
	} 

	public static String printArrayList(ArrayList<Task> listOfTasks){
		String toPrint ="";
		for (int j = 0; j < listOfTasks.size() ; j ++){
			toPrint += " " + (j+1) + ". " + listOfTasks.get(j).toString() + "\n";
		}
		return toPrint;
	}

	public static String printEveryTask(){
		String toPrint = "";
		ArrayList<Task> everyTask = Logic.getTempStorage();
		if (everyTask.size() !=0){
			String date = everyTask.get(0).getDate();
		
			if (Task.getIsSortedByTime()){
				if (date.equals(getTodayDate())){
					toPrint += " " + createHorizLine("=", 20) + " " + date + " , Today " + createHorizLine("=", 20) + "\n";
				} else {	
					toPrint += " " + createHorizLine("=", 20) + " " + date + " " + createHorizLine("=", 20) + "\n";
				}
			}
			for (int j = 0; j < everyTask.size() ; j ++){
				String dateOfCurrentTask = everyTask.get(j).getDate();
				if (! dateOfCurrentTask.equals(date)){
					toPrint += "\n";
					if (Task.getIsSortedByTime()){
						if (dateOfCurrentTask.equals("ft")){
							toPrint += " " + createHorizLine("=", 20) + " Floating Tasks "  + createHorizLine("=", 20)+ "\n" ;
						} else if  (dateOfCurrentTask.equals(getTodayDate())){
							toPrint += " " + createHorizLine("=", 20) + " " + dateOfCurrentTask + " , Today " + createHorizLine("=", 20) + "\n";
						} else {
							toPrint += " " + createHorizLine("=", 20) + " " + dateOfCurrentTask + " " + createHorizLine("=", 20)+ "\n" ;
						}
					}
				}
				toPrint += " " + (j+1) + ". " + everyTask.get(j).toString() + "\n";
				date = dateOfCurrentTask;
			}
			return toPrint;
		} else {
			return MSG_EMPTY_ALL_DAYS;
		}
	}

	//Creates a horizontal line for formatting the User Interface.
	private static String createHorizLine(String charseq, int numToDraw){
		String line = "";
		for (int i=0; i < numToDraw; i++){
			line += charseq;
		}
		return line;
	}

	public static ArrayList<Task> getTodayList() {
		ArrayList<Task> allTasks = Logic.getTempStorage();
		ArrayList<Task> todayTasks = new ArrayList<Task>();
		for (int j = 0 ; j< allTasks.size() ; j++){
			if (allTasks.get(j).getDate().equals(getTodayDate())){
				todayTasks.add(allTasks.get(j));
			} 
		}
		return todayTasks;
	}

	//Same function as getCurrentDate except date is in another format
	private static String getTodayDate() {
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		Date date = new Date();
		String reportDate = dateFormat.format(date);
		return reportDate;
	}
}