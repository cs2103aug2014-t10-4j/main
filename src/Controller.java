import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Controller {

	private static final String MSG_EMPTY_TODAY = "No tasks for today!";
	private static final String MSG_EMPTY_ALL_DAYS = "No tasks for anyday!";
	private static final String MSG_EMPTY_FLOATING = "No floating tasks!";
	private static final String MSG_INVALID_COMMAND = "Invalid command";

	enum CommandType {
		ADD_TEXT, DISPLAY_TEXT, DELETE_TEXT, CLEAR_FILE, EDIT, EXIT, INVALID, SEARCH, SORT, HELP;
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
					results.setListOfTasks(Logic.getTempStorage());
					results.setFeedback(Logic.addLineToFile(taskToExecute, file));
					return results;
				} else {
					results.setListOfTasks(tasksFound);
					results.setFeedback("Clashes found!");
					JFrame frame = new JFrame();
					int n = JOptionPane.showConfirmDialog(
							frame,
							"Continue adding?",
							"Something is happening at the same time!",
							JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION){
						results.setFeedback(Logic.addLineToFile(taskToExecute, file));			
					} else {
						results.setFeedback("Task is not added.");
					}
					results.setListOfTasks(Logic.getTempStorage());
					return results;
				}
			case DELETE_TEXT:
				results.setFeedback(Logic.deleteLineFromFile(taskToExecute, file, archive));
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			case EDIT:
				results.setFeedback(Logic.edit(taskToExecute, file));
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			case SEARCH:
				results.setListOfTasks(Logic.search(taskToExecute) );
				results.setFeedback("This is what is found.");
				results.setTitleOfPanel("Search Results for \""+ userSentence + "\"");
				return results;
			default:
				results.setFeedback(MSG_INVALID_COMMAND);
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			}
		} else { 
			switch (userSentence) {
			case "/display" :
				results.setListOfTasks(Logic.getTempStorage());
				results.setFeedback("These are all your tasks");
				results.setTitleOfPanel("All Tasks:");
				return results;
			case "/exit":
				System.exit(0);
			case "/deleteall":
				results.setFeedback(Logic.clearContent());
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			case "/sort":
				results.setFeedback(Logic.sortByDateAndTime(Logic.getTempStorage()));
				results.setListOfTasks(Logic.getTempStorage());
				results.setTitleOfPanel("All Tasks:");
				return results;
			case "/sortalpha":
				results.setFeedback(Logic.sortByAlphabet(Logic.getTempStorage()));
				results.setListOfTasks(Logic.getTempStorage());
				results.setTitleOfPanel("All tasks by alphabetical order");
				return results;
			case "/sortimpt":
				results.setFeedback(Logic.sortByImportance(Logic.getTempStorage()));
				results.setListOfTasks(Logic.getTempStorage());
				results.setTitleOfPanel("All tasks by importance order");
				return results;
			case "/showfloating":
				results.setFeedback("These are your floating tasks.");
				results.setTitleOfPanel("Floating Tasks:");
				results.setListOfTasks(getFloatingList());
				return results;
			case "/showtoday":
				results.setFeedback("These are your tasks for the day.");
				results.setTitleOfPanel("Today Tasks:");
				results.setListOfTasks(getTodayList());
				return results;
			case "/showall":
				results.setFeedback("These are all your tasks.");
				results.setTitleOfPanel("All Tasks:");
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			case "/clear":
				results.setFeedback("Screen is cleared. Type /showall, /showtoday or /showfloating again.");
				results.setListOfTasks(new ArrayList<Task>());
				return results;
			default:
				results.setFeedback(MSG_INVALID_COMMAND);
				results.setListOfTasks(Logic.getTempStorage());
				return results;
			}
		}
	}

	//Return any tasks with the same date and time as taskToExecute
	private static ArrayList<Task> findClash(Task taskToExecute) {
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
		} else {
			return CommandType.INVALID;
		}
	}

	private static String getFirstWord(String[] userCommand) {
		return userCommand[0];
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
			if (date.equals(getTodayDate())){
				toPrint += " " + createHorizLine("=", 20) + " " + date + " , Today " + createHorizLine("=", 20) + "\n";
			} else {	
				toPrint += " " + createHorizLine("=", 20) + " " + date + " " + createHorizLine("=", 20) + "\n";
			}
			for (int j = 0; j < everyTask.size() ; j ++){
				String dateOfCurrentTask = everyTask.get(j).getDate();
				if (! dateOfCurrentTask.equals(date)){
					toPrint += "\n";
					if (dateOfCurrentTask.equals("ft")){
						toPrint += " " + createHorizLine("=", 20) + " Floating Tasks "  + createHorizLine("=", 20)+ "\n" ;
					} else if  (dateOfCurrentTask.equals(getTodayDate())){
						toPrint += " " + createHorizLine("=", 20) + " " + dateOfCurrentTask + " , Today " + createHorizLine("=", 20) + "\n";
					} else {
						toPrint += " " + createHorizLine("=", 20) + " " + dateOfCurrentTask + " " + createHorizLine("=", 20)+ "\n" ;
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