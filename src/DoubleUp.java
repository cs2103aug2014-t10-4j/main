import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class DoubleUp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MSG_WELCOME = "Welcome to DoubleUp!\n";
	private static final String MSG_PROGRESS_BAR = "You have %d tasks due today, %d tasks due tomorrow and %d free tasks.\n";
	private static final String MSG_QOTD = "QOTD: \n";
	private static final String MSG_GOAL = "Your goal is: ";
	private static final String MSG_HELP = "Type /help to view all the commands for various actions. Happy doubling up!\n";
	private static final String MSG_ENTER_COMMAND = "Enter a command: ";
	private static final String MSG_RESULT = "Result: ";

	private static JTextField textFieldCmdIn, textFieldResultsOut;
	private static JTextArea displayPanelTodayTasks, displayPanelFloatingTasks, displayPanelAllTasks;
	private static JPanel middleRow;

	public static File file, archive;

	public static void main(String[] args) {
		String fileName = "DoubleUp.txt";
		String archiveName = "Archive.txt";
		file = Storage.openFile(fileName);
		archive = Storage.openFile(archiveName);
		ArrayList<Integer> numOfTask = Logic.init(file, archive);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
		//messageToUser(createWelcomeMessage(numOfTask));
		//messageToUser(createTodayList(file));
	}

	public static void createAndShowGUI() {
		//Create and set up the window
		JFrame frame = new JFrame("DoubleUp To-do-List");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentsToPane(frame.getContentPane());
		//frame.pack();
		frame.setMinimumSize(new Dimension(500,500));
		frame.setVisible(true);
	}

	public static void addComponentsToPane(Container cp){
		cp.setLayout(new BorderLayout());

		//Top pane
		JPanel topRow = new JPanel();
		topRow.add(new JLabel(MSG_ENTER_COMMAND));
		textFieldCmdIn = new JTextField(30);
		topRow.add(textFieldCmdIn);
		cp.add(topRow, BorderLayout.NORTH);

		// Today panel
		middleRow = new JPanel();
		middleRow.setLayout(new BorderLayout());
		displayPanelTodayTasks = new JTextArea();
		displayPanelTodayTasks.setEditable(false);
		displayPanelTodayTasks.setText(Controller.printTodayList(Controller.createTodayList()));
		JScrollPane scroll  = new JScrollPane(displayPanelTodayTasks,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		middleRow.add(scroll, BorderLayout.CENTER);
		middleRow.setBorder(BorderFactory.createTitledBorder("To-do Today, " + getCurrentDate()));
		cp.add(middleRow, BorderLayout.CENTER);
		
		JPanel lastRow = new JPanel();
		lastRow.add(new JLabel(MSG_RESULT));
		textFieldResultsOut = new JTextField(30);
		textFieldResultsOut.setEditable(false);  // read-only
		lastRow.add(textFieldResultsOut);
		cp.add(lastRow, BorderLayout.SOUTH);

		// Allocate an anonymous instance of an anonymous inner class that
		//  implements ActionListener as ActionEvent listener
		textFieldCmdIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userSentence = textFieldCmdIn.getText();
				String result;
				switch (userSentence){
				case "/showhelp":
					try 
					{
						FileReader fr = new FileReader("help1.txt");
						BufferedReader br = new BufferedReader(fr);
						displayPanelTodayTasks.read(br, null);
						br.close();
					}catch (IOException e1) {
	                 }
	              
					middleRow.setBorder(BorderFactory.createTitledBorder("Help Screen"));
					result = "Press ESC to return to Today Tasks";
					break;
				case "/showfloating":
					result = "These are your floating tasks.";
					middleRow.setBorder(BorderFactory.createTitledBorder("Floating Tasks:"));
					displayPanelTodayTasks.setText(Controller.printFloatingList());
					break;
				case "/showtoday":
					result = "These are your tasks for the day.";
					middleRow.setBorder(BorderFactory.createTitledBorder("To-do Today, " + getCurrentDate()));
					displayPanelTodayTasks.setText(Controller.printTodayList(Controller.createTodayList()));
					break;
				case "/showall":
					result = "These are all your tasks.";
					middleRow.setBorder(BorderFactory.createTitledBorder("All Tasks:"));
					displayPanelTodayTasks.setText(Controller.printEveryTask());
					break;
				case "/clear":
					result ="Screen cleared.";
					displayPanelTodayTasks.setText("");
					break;
				default:
					result = Controller.executeCommand(userSentence, file, archive);					
					displayPanelTodayTasks.setText(Controller.printTodayList(Controller.createTodayList()));
					middleRow.setBorder(BorderFactory.createTitledBorder("To-do Today, " + getCurrentDate()));
				}
				textFieldCmdIn.setText("");  // clear input TextField
				textFieldResultsOut.setText(result); // display results of command on the output TextField
			}
		});
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

	private static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String reportDate = dateFormat.format(date);
		return reportDate;
	}

	public static void messageToUser(String text) {
		displayPanelTodayTasks.setText(text);
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
				toPrint += String.format("%-40s%-56s%-50s%n", result[0], result[1], result[2]);
			} 
			sc.close();
		}catch (FileNotFoundException e) {
		}
		return toPrint;
	}
}