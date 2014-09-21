import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;

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
	private static final String MSG_HELP = "Type -help to view all the commands for various actions. Happy doubling up!\n";
	private static final String MSG_COMMAND_LINE = "Enter Command: ";
	private static final String MSG_FAIL_READ_FILE = "Unable to read file.";
	private static final String MSG_FAIL_ADD = "Unable to add line.";
	private static final String MSG_MISSING_FILE = "File not found.";

	public static final String ERROR_INVALID_COMMAND = "Invalid command";
	
	private static Scanner scanner = new Scanner(System.in);

	enum CommandType {
		ADD_TEXT, DISPLAY_TEXT, DELETE_TEXT, CLEAR_SCREEN, EXIT, INVALID, SEARCH, SORT, HELP;
	};

	public static void main(String[] args) {
		String fileName = "DoubleUp.txt";
		File file= openFile(fileName);

		messageToUser(createWelcomeMessage());
		while(true) {
			messageToUser(MSG_COMMAND_LINE);
			String userSentence = scanner.nextLine();
					
			String[] splitCommand = parseCommand(userSentence);
			String action = splitCommand[0];
			Task taskToExecute = new Task(splitCommand);
			String result = executeCommand(action, taskToExecute, file);
			messageToUser(result);
		}
	}

	//Return a string array with 6 fields: command, task name, date, time, details, importance level.
	private static String[] parseCommand(String userSentence) {
		// TODO Auto-generated method stub
		String [] arr = {"add", "assignment", null, null, null, "0"};
		return arr;
	}

	private static String executeCommand(String command, Task task, File file) {
		String commandTypeString = getFirstWord(command);
		CommandType commandType = determineCommandType(commandTypeString);
		switch (commandType) {
		case ADD_TEXT:
			//return addLineToFile(task, file);
			return "add"; //stub
		case DISPLAY_TEXT:
			//return displayOnScreen(file);
			return "display"; //stub
		case DELETE_TEXT:
			//return deleteLineFromFile(task, file);
			return "delete"; //stub
		case CLEAR_SCREEN:
			//return clearContent(file);
			return "clear"; //stub
		case EXIT:
			System.exit(0);
		case SEARCH:
			//return search(task, file);
			return "search"; //stub
		case SORT:
			//return sort(file);
			return "sort"; //stub
		case HELP:
			//return showHelp();
			return "help"; //stub
		default:
			return ERROR_INVALID_COMMAND;
		}
	}

	//This method is used to determine the command types given the first word of the command.
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

	private static String getFirstWord(String userCommand) {
		return userCommand.trim().split("\\s+")[0];
	}

	//Concats the different messages to form the welcome message for the welcome screen
	private static String createWelcomeMessage() {
		String welcomeMessage = MSG_WELCOME;
		welcomeMessage += "\n" + "\t" + String.format(MSG_PROGRESS_BAR, 3,0,1);
		welcomeMessage += "\n" + "\t" + MSG_QOTD;
		welcomeMessage += "\n" + "\t" + MSG_GOAL;
		welcomeMessage += "\n" + getHelpMessage();
		return welcomeMessage;
	}
	private static String getHelpMessage() {
		return MSG_HELP;
	}

	public static void writeToFile(String lineToAdd, File file) {
		BufferedWriter fileWritten;
		try {
			fileWritten = new BufferedWriter(new FileWriter (file.getName(),true));
			if(numberOfLine(file)>0){
				fileWritten.newLine();
			}
			fileWritten.write(lineToAdd);
			fileWritten.close();
		} catch (IOException e) {
			messageToUser(MSG_FAIL_ADD);
		}
	}

	// The function below serves to count the number of lines of text present in the file.
	public static int numberOfLine(File file) {
		Scanner input;
		int lineNum = 0;
		try {
			input = new Scanner(file);

			if(!input.hasNext()) {
				input.close();
				return lineNum;	
			} else{

				while(input.hasNext()) {
					input.nextLine();
					lineNum++;
				}
				input.close();	
			}
		} catch (FileNotFoundException e) {
			messageToUser(MSG_MISSING_FILE);
		}
		return lineNum;
	}
	private static File openFile(String fileName) {
		File file = new File(fileName);
		try{
			if(!file.exists()){
				file.createNewFile();
			}
		}
		catch(IOException e){
			System.out.println(MSG_FAIL_READ_FILE);
			System.exit(1);
		}
		return file;
	}

	public static void messageToUser(String text){
		System.out.println(text);
	}
}