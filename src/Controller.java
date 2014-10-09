import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


public class Controller {

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

	enum CommandType {
		ADD_TEXT, DISPLAY_TEXT, DELETE_TEXT, CLEAR_SCREEN, EDIT, EXIT, INVALID, SEARCH, SORT, HELP;
	};

	public static String executeCommand(String[] splitCommand, File file, File archive) {
		String action = getFirstWord(splitCommand);
		CommandType commandType = determineCommandType(action);
		Task taskToExecute = new Task(splitCommand);
		switch (commandType) {
		case ADD_TEXT:
			Task tempTask = new Task();
			tempTask.setDate(taskToExecute.getDate());
			tempTask.setTime(taskToExecute.getTime());
			//ArrayList<Task> tasksFound = search(tempTask, file);
			ArrayList<Task> tasksFound = new ArrayList<Task>(); //stub to swop with above line
			if (tasksFound.size() == 0){
				return Logic.addLineToFile(taskToExecute, file);
			} /*else {
					String matchedList = printArrayList(tasksFound);
					displayList.setText(matchedList);
					//textFieldResultsOut.setText("Clashes found!");
					 JFrame frame = new JFrame();
    				 Object result = JOptionPane.showInputDialog(frame, "The task you are about to add have the same 
						date and time as the above items. Do you want to continue? (y/n)?"");
					 if (result.equalsIgnoreCase("y") || results.equalsIgnoreCase("yes"){
					 	return Logic.addLineToFile(taskToExecute, file);
					 } else {
					 	return;
					 }
				}*/
		case DISPLAY_TEXT:
			return printArrayList(createTodayList());
		case DELETE_TEXT:
			return Logic.deleteLineFromFile(taskToExecute, file, archive);
		case EDIT:
			return Logic.edit(taskToExecute, file);
		case CLEAR_SCREEN:
			// return Logic.clearContent(file);
			return "clear"; // stub
		case SEARCH:
			return printArrayList ( Logic.search(taskToExecute) );
			//return "search"; // stub
		case SORT:
			/*String sortParams = splitCommand[7s];
			if (sortParams.equals("alpha"){
				return sortByAlphabet(file);
			} else if (sortParams.equals("importance")){
				return sortByImportance(file);
			} else {
				return sortByDateAndTime(file);
			}*/
			return "sort"; // stub
		case HELP:
			return showHelp();
		case EXIT:
			System.exit(0);
		default:
			return MSG_INVALID_COMMAND;
		}
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
			return CommandType.CLEAR_SCREEN;
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

	private static String showHelp() {
		File helpFile = new File("help.txt");
		Scanner sc;
		String toPrint = "";
		try {
			sc = new Scanner(helpFile);
			while (sc.hasNext()) {
				String sentence = sc.nextLine();
				String[] result = sentence.split(" ### ");
				toPrint += String.format("%-16s%-56s%-50s%n", result[0], result[1], result[2]);
			} 
			sc.close();
		}catch (FileNotFoundException e) {
		}
		return toPrint;
	}

	public static String printTodayList (ArrayList<Task> listOfTasks){
		if (listOfTasks.size() ==0){
			return MSG_EMPTY_TODAY;
		} else {
			return printArrayList(listOfTasks);
		}
	}
	public static String printFloatingList (){
		ArrayList<Task> allTasks = Logic.getTempStorage();
		ArrayList <Task> listOfFloating = new ArrayList<Task>();
		for (int j=0; j < allTasks.size(); j++){
			if (allTasks.get(j).getDate().equals("ft")){
				listOfFloating.add(allTasks.get(j));
			}
		}
		if (listOfFloating.size() == 0){
			return MSG_EMPTY_FLOATING;
		} else {
			return printArrayList(listOfFloating);
		}
	}

	public static String printArrayList(ArrayList<Task> listOfTasks){
		String toPrint ="";
		for (int j = 0; j < listOfTasks.size() ; j ++){
			toPrint += (j+1) + ". " + listOfTasks.get(j).toString() + "\n";
		}
		return toPrint;
	}
	public static String printEveryTask(){
		String toPrint = "";
		ArrayList<Task> everyTask = Logic.getTempStorage();
		if (everyTask.size() !=0){
			String date = everyTask.get(0).getDate();
			toPrint += createHorizLine("=", 20) + date + " " + createHorizLine("=", 20) + "\n";
			for (int j = 0; j < everyTask.size() ; j ++){
				String dateOfCurrentTask = everyTask.get(j).getDate();
				if (! dateOfCurrentTask.equals(date)){
					toPrint += "\n";
					toPrint += createHorizLine("=", 20) + dateOfCurrentTask + " " + createHorizLine("=", 20)+ "\n" ;
				}
				toPrint += (j+1) + ". " + everyTask.get(j).toString() + "\n";
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
	
	public static ArrayList<Task> createTodayList() {
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
