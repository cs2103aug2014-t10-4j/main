import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class DoubleUp extends JFrame {

	private static final String MSG_WELCOME = "Welcome to DoubleUp!\n";
	private static final String MSG_PROGRESS_BAR = "You have %d tasks due today, %d tasks due tomorrow and %d free tasks.\n";
	private static final String MSG_QOTD = "QOTD: \n";
	private static final String MSG_GOAL = "Your goal is: ";
	private static final String MSG_HELP = "Type /help to view all the commands for various actions. Happy doubling up!\n";
	private static final String MSG_EMPTY_FILE = "%s is empty.";
	private static final String MSG_COMMAND_LINE = "Enter a command: ";
	private static final String MSG_RESULT = "Result: ";
	private static final String MSG_FAIL_READ_FILE = "Unable to read file.";
	private static final String MSG_MISSING_FILE = "File not found.";
	private static final String MSG_INVALID_COMMAND = "Invalid command";

	private static JTextField textFieldCmdIn, textFieldResultsOut;
	private static JTextArea displayList;
	final static boolean shouldFill = true;
	final static boolean shouldWeightX = true;
	final static boolean RIGHT_TO_LEFT = false;

	public static File file;
	private static final int LENGTH_OF_PAGE = 80;

	enum CommandType {
		ADD_TEXT, DISPLAY_TEXT, DELETE_TEXT, CLEAR_SCREEN, EXIT, INVALID, SEARCH, SORT, HELP;
	};

	public static void createAndShowGUI() {
		//Create and set up the window
		JFrame frame = new JFrame("DoubleUp To-do-List");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentsToPane(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}

	public static void addComponentsToPane(Container cp){
		cp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		//Top pane
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		JPanel topRow = new JPanel();
		topRow.add(new JLabel(MSG_COMMAND_LINE));
		textFieldCmdIn = new JTextField(30);
		topRow.add(textFieldCmdIn);
		cp.add(topRow,c);

		//second panel
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0.0;
		c.ipady = 40;
		c.gridwidth = 3;
		JPanel middleRow = new JPanel();
		displayList = new JTextArea(10,50);
		displayList.setEditable(false);
		displayList.setText(displayOnScreen(file));
		middleRow.add(displayList);
		middleRow.setOpaque(true);
		middleRow.setBorder(BorderFactory.createTitledBorder("To-do Today, " + getCurrentDate()));
		cp.add(middleRow, c);

		c.fill = GridBagConstraints.BOTH;
		c.ipady = 00;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridx = 0;
		c.gridy = 10;
		JPanel lastRow = new JPanel();
		lastRow.add(new JLabel(MSG_RESULT));
		textFieldResultsOut = new JTextField(30);
		textFieldResultsOut.setEditable(false);  // read-only
		lastRow.add(textFieldResultsOut);
		cp.add(lastRow, c);

		// Allocate an anonymous instance of an anonymous inner class that
		//  implements ActionListener as ActionEvent listener
		textFieldCmdIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userSentence = textFieldCmdIn.getText();
				String[] splitCommand = Parser.parseInput(userSentence);
				String result = executeCommand(splitCommand, file);
				textFieldCmdIn.setText("");  // clear input TextField
				displayList.setText(displayOnScreen(file));
				textFieldResultsOut.setText(result); // display results of command on the output TextField
			}
		});
	}

	public static void main(String[] args) {
		String fileName = "DoubleUp.txt";
		file = openFile(fileName);
		//ArrayList<Integer> numOfTask = Logic.init(file);
		ArrayList<Integer> numOfTask = new ArrayList<Integer>();
		numOfTask.add(5);
		numOfTask.add(0);
		numOfTask.add(1);


		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
		//messageToUser(createWelcomeMessage(numOfTask));
		//messageToUser(createTodayList(file));
	}

	private static String executeCommand(String[] splitCommand, File file) {
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
			return displayOnScreen(file);
		case DELETE_TEXT:
			// return deleteLineFromFile(taskToExecute, file);
			return "delete"; // stub
		case CLEAR_SCREEN:
			// return Logic.clearContent(file);
			return "clear"; // stub
		case SEARCH:
			// return printArrayList ( search(taskToExecute, file) );
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

	private static String printArrayList(ArrayList<Task> listOfTasks){
		String toPrint ="";
		for (int j = 0; j < listOfTasks.size() ; j ++){
			toPrint += (j+1) + ". " + listOfTasks.get(j).toString() + "\n";
		}
		return toPrint;
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
		File settings = new File("settings.txt");
		try {
			Scanner sc = new Scanner(settings);
			String personalGoal = sc.nextLine();
			goal += personalGoal + "\n";
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
				stringBuilder.append("END");
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
		displayList.setText(text);
	}
}