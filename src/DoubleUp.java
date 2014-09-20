import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class DoubleUp {
	private static Scanner scanner = new Scanner(System.in);
	private static final String MSG_WELCOME = "Welcome to DoubleUp!";
	private static final String MSG_COMMAND_LINE = "Enter Command: ";
	private static final String MSG_FAIL_READ_FILE = "Unable to read file.";
	private static final String MSG_FAIL_ADD = "Unable to add line.";
	private static final String MSG_MISSING_FILE = "File not found.";

	public static void main(String[] args) {
		String fileName = "DoubleUp.txt";
		File file= openFile(fileName);

		messageToUser(createWelcomeMessage());
		while(true) {
			messageToUser(MSG_COMMAND_LINE);
			String command = scanner.nextLine();
			/*		
			String[] command = parseCommand();
			String operation = command[0];
			//Create Task object.
			Task taskToExecute = new Task(command);
			String result = executeCommand(operation, taskToExecute, textFile);
			 */
			String result = executeCommand(command, file);
			messageToUser(result);
		}
	}
	
	private static String executeCommand(String command, File file) {
		return null;
	}

	//Concats the different messages to form the welcome message for the welcome screen
	private static String createWelcomeMessage() {
		//showToUser(getProgressBar);
		//showToUser(getQOTD);
		//showToUser(getGoal);
		//showToUser(getHelpMessage);
		return MSG_WELCOME;
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