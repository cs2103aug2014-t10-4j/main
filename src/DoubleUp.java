import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DoubleUp {

	public static final int ADD_TEXT = 1;
	public static final int DISPLAY_TEXT = 2;
	public static final int DELETE_TEXT = 3;
	public static final int CLEAR_SCREEN = 4;
	public static final int EXIT = 5;
	public static final int SEARCH = 6;
	public static final int SORT = 7;
	public static final int HELP = 8;

	private static final String MSG_WELCOME = "Welcome to DoubleUp!\n";
	private static final String MSG_PROGRESS_BAR = "You have %d tasks due today, %d tasks due tomorrow and %d free tasks.\n";
	private static final String MSG_QOTD = "QOTD: \n";
	private static final String MSG_GOAL = "Your goal is to: \n";
	private static final String MSG_HELP = "Type /help to view all the commands for various actions. Happy doubling up!\n";
	private static final String MSG_EMPTY_FILE = "%s is empty.";
	private static final String MSG_COMMAND_LINE = "Enter Command: ";
	private static final String MSG_FAIL_READ_FILE = "Unable to read file.";
	private static final String MSG_MISSING_FILE = "File not found.";
	private static final String MSG_INVALID_COMMAND = "Invalid command";

	private static final int LENGTH_OF_PAGE = 80;

	private static Scanner scanner = new Scanner(System.in);

	enum CommandType {
		ADD_TEXT, DISPLAY_TEXT, DELETE_TEXT, CLEAR_SCREEN, EXIT, INVALID, SEARCH, SORT, HELP;
	};

	public static void main(String[] args) {
		String fileName = "DoubleUp.txt";
		File file = openFile(fileName);
		//ArrayList<Integer> numOfTask = Logic.init(file);
		ArrayList<Integer> numOfTask = new ArrayList<Integer>();
		numOfTask.add(5);
		numOfTask.add(0);
		numOfTask.add(1);

		messageToUser(createWelcomeMessage(numOfTask));
		messageToUser(createTodayList(file));
		while (true) {
			messageToUser(MSG_COMMAND_LINE);
			String userSentence = scanner.nextLine();
			String[] splitCommand = Parser.parseInput(userSentence);
			String result = executeCommand(splitCommand, file);
			messageToUser(result);
		}
	}

	private static String executeCommand(String[] splitCommand, File file) {
		String action = getFirstWord(splitCommand);
		CommandType commandType = determineCommandType(action);
		Task taskToExecute = new Task(splitCommand);
		switch (commandType) {
		case ADD_TEXT:
			return Logic.addLineToFile(taskToExecute, file);
		case DISPLAY_TEXT:
			return displayOnScreen(file);
		case DELETE_TEXT:
			// return deleteLineFromFile(taskToExecute, file);
			return "delete"; // stub
		case CLEAR_SCREEN:
			// return clearContent(file);
			return "clear"; // stub
		case SEARCH:
			// return search(taskToExecute, file);
			return "search"; // stub
		case SORT:
			/*String sortParams = splitCommand[6];
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

	private static String showHelp() {
		File helpFile = new File("help.txt");
		Scanner sc;
		String toPrint = "";
		try {
			sc = new Scanner(helpFile);
			while (sc.hasNext()) {
				String sentence = sc.nextLine();
				String[] result = sentence.split(" ### ");
				//toPrint += result[0] + result[1] + result[2];
				toPrint += String.format("%-16s%-56s%-50s%n", result[0], result[1], result[2]);
			} 
			sc.close();
		}catch (FileNotFoundException e) {

		}
		return toPrint;
	}

	// This method is used to determine the command types given the first word
	// of the command.
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

	// Concats the different messages to form the welcome message for the
	// welcome screen
	private static String createWelcomeMessage(ArrayList<Integer> numOfTask) {
		String welcomeMessage = MSG_WELCOME;
		welcomeMessage += "\n" + "\t" + String.format(MSG_PROGRESS_BAR, numOfTask.get(0), 
				numOfTask.get(1), numOfTask.get(2));
		welcomeMessage += createQOTD();
		welcomeMessage += createGoalMsg();
		welcomeMessage += createHelpMsg();
		welcomeMessage += "\n" + createHorizLine("*", LENGTH_OF_PAGE);
		return welcomeMessage;
	}
	//Returns Quote of the day.
	private static String createQOTD(){
		String quote = "\n" + "\t" + MSG_QOTD;
		return quote;
	}

	//Returns goal message.
	private static String createGoalMsg(){
		String goal = "\n" + "\t" + MSG_GOAL;
		return goal;
	}

	//Returns help message.
	private static String createHelpMsg(){
		String help = "\n" + MSG_HELP;
		return help;
	}

	private static String createTodayList(File file) {
		//String allTodayTasks = fetchTodayTask();
		return getCurrentDate() + "(Today):" + "\n" + displayOnScreen(file) + createHorizLine("-",LENGTH_OF_PAGE/2);
	}

	private static String getHelpMessage() {
		return MSG_HELP;
	}

	//Creates a horizontal line for formatting the User Interface.
	private static String createHorizLine(String charseq, int numToDraw){
		String line = "";
		for (int i=0; i < numToDraw; i++){
			line += charseq;
		}
		line += "\n";
		return line;
	}

	// This function serves to display on the task in the text file.
	private static String displayOnScreen(File file) {
		Scanner input;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			input = new Scanner(file);
			if (!input.hasNext()) {
				input.close();
				return (String.format(MSG_EMPTY_FILE, file.getName()));
			}

			else {
				int listNum = 1;
				while (input.hasNext()) {
					stringBuilder.append(listNum + ". " + input.nextLine() + "\n");
					listNum++;
				}
				input.close();
			}
		} catch (FileNotFoundException e) {
			return (MSG_MISSING_FILE);
		}
		return stringBuilder.toString();
	}

	private static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String reportDate = dateFormat.format(date);
		return reportDate;
	}

	// Creates a text file if the text file is missing or for first time usage.
	private static File openFile(String fileName) {
		File file = new File(fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			System.out.println(MSG_FAIL_READ_FILE);
			System.exit(1);
		}
		return file;
	}

	public static void messageToUser(String text) {
		System.out.println(text);
	}

}