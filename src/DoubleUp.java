import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
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
	private static final String MSG_COMMAND_LINE = "Enter a command: ";
	private static final String MSG_RESULT = "Result: ";

	private static JTextField textFieldCmdIn, textFieldResultsOut;
	private static JTextArea displayPanelTodayTasks, displayPanelFloatingTasks, displayPanelAllTasks;

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

		// Today panel
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0.0;
		c.ipady = 40;
		c.gridwidth = 3;
		JPanel middleRow = new JPanel();
		displayPanelTodayTasks = new JTextArea(10,50);
		displayPanelTodayTasks.setEditable(false);
		displayPanelTodayTasks.setText(Controller.printTodayList(Controller.createTodayList()));
		JScrollPane scroll  = new JScrollPane(displayPanelTodayTasks);
		middleRow.add(scroll);
		middleRow.setOpaque(true);
		middleRow.setBorder(BorderFactory.createTitledBorder("To-do Today, " + getCurrentDate()));
		cp.add(middleRow, c);

		//everything tasks panel
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0.0;
		c.ipady = 40;
		c.gridwidth = 3;
		JPanel everythingRow = new JPanel();
		displayPanelAllTasks = new JTextArea(10,50);
		displayPanelAllTasks.setEditable(false);
		displayPanelAllTasks.setText(Controller.printEveryTask());
		JScrollPane scroll2 = new JScrollPane(displayPanelAllTasks);
		everythingRow.add(scroll2);
		everythingRow.setOpaque(true);
		everythingRow.setBorder(BorderFactory.createTitledBorder("All tasks"));
		cp.add(everythingRow, c);

		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 5;
		c.weightx = 0.0;
		c.ipady = 40;
		c.gridwidth = 3;
		JPanel thirdRow = new JPanel();
		displayPanelFloatingTasks = new JTextArea(5,50);
		displayPanelFloatingTasks.setEditable(false);
		displayPanelFloatingTasks.setText(Controller.printFloatingList());
		JScrollPane scroll3 = new JScrollPane(displayPanelFloatingTasks);
		thirdRow.add(scroll3);
		thirdRow.setOpaque(true);
		thirdRow.setBorder(BorderFactory.createTitledBorder("Floating tasks:"));
		cp.add(thirdRow, c);

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
				String result = Controller.executeCommand(userSentence, file, archive);
				textFieldCmdIn.setText("");  // clear input TextField
				displayPanelTodayTasks.setText(Controller.printTodayList(Controller.createTodayList()));
				displayPanelAllTasks.setText(Controller.printEveryTask());
				displayPanelFloatingTasks.setText(Controller.printFloatingList());
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
}