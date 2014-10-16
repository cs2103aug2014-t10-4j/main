import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
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
	private static final String MSG_HELP = "Press F2 to view all the commands. Happy doubling up!\n";
	private static final String MSG_ENTER_COMMAND = "Enter a command: ";
	private static final String MSG_RESULT = "Result: ";
	private static final String FILE_TASK = "DoubleUp.txt";
	private static final String FILE_ARCHIVE = "Archive.txt";

	private static JTextField textFieldCmdIn, textFieldResultsOut;
	private static JTextArea displayPanelTodayTasks;
	private static JPanel middleRow;

	private static Stack <String> backwardsUserInput, forwardUserInput;

	public static File file, archive;

	public static void main(String[] args) {
		file = Storage.openFile(FILE_TASK);
		archive = Storage.openFile(FILE_ARCHIVE);
		backwardsUserInput = new Stack<String>();
		forwardUserInput = new Stack<String>();
		Logic.init(file, archive);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void createAndShowGUI() {
		JFrame frame = new JFrame("DoubleUp To-do-List");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentsToPane(frame.getContentPane());
		frame.setMinimumSize(new Dimension(650,600));
		frame.setVisible(true);
		Logger logger = Logger.getLogger("myLogger");
		logger.log(Level.INFO, "Successfully create GUI");
	}

	public static void addComponentsToPane(Container cp){
		cp.setLayout(new BorderLayout());
		//Top panel for Command
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
		displayPanelTodayTasks.setText(Controller.printEveryTask());
		JScrollPane scroll  = new JScrollPane(displayPanelTodayTasks,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		middleRow.add(scroll, BorderLayout.CENTER);
		middleRow.setBorder(BorderFactory.createTitledBorder("All Tasks: "));
		cp.add(middleRow, BorderLayout.CENTER);

		//feedback
		JPanel lastRow = new JPanel();
		lastRow.add(new JLabel(MSG_RESULT));
		textFieldResultsOut = new JTextField(43);
		textFieldResultsOut.setEditable(false);  // read-only
		textFieldResultsOut.setText(MSG_WELCOME + MSG_HELP);
		lastRow.add(textFieldResultsOut);
		cp.add(lastRow, BorderLayout.SOUTH);

		Action showHelp = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				showHelp();
			}
			private void showHelp() {
				try 
				{
					FileReader fr = new FileReader("help.txt");
					BufferedReader br = new BufferedReader(fr);
					displayPanelTodayTasks.read(br, null);
					br.close();
				} catch (IOException e1) {
					System.out.println("Help.txt not found.");
				}
				textFieldResultsOut.setText("Press ESC to return to All Tasks");
				middleRow.setBorder(BorderFactory.createTitledBorder("Help Screen:"));
			}
		};
		Action showAll = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				showAll();
			}
			private void showAll() {
				displayPanelTodayTasks.setText(Controller.printEveryTask());
				middleRow.setBorder(BorderFactory.createTitledBorder("All Tasks: "));
				textFieldResultsOut.setText("Press F2 for help.");
			}
		};

		Action goBack = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
			private void goBack() {
				try {
					if (!backwardsUserInput.empty()){
						forwardUserInput.push(backwardsUserInput.pop());
						textFieldCmdIn.setText(forwardUserInput.peek());
					}
				} catch (EmptyStackException e) {
					e.printStackTrace();
				}
			}
		};
		Action goForward = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				goForward();
			}
			private void goForward() {
				try {
					if (!forwardUserInput.empty()){
						backwardsUserInput.push(forwardUserInput.pop());
						textFieldCmdIn.setText(forwardUserInput.peek());
					}
				} catch (EmptyStackException e) {
					textFieldCmdIn.setText("");
				}
			}
		};

		textFieldCmdIn.getInputMap().put(KeyStroke.getKeyStroke("F2"), "showHelp");
		textFieldCmdIn.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "showall");
		textFieldCmdIn.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "goBack");
		textFieldCmdIn.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "goForward");
		textFieldCmdIn.getActionMap().put("showHelp", showHelp);
		textFieldCmdIn.getActionMap().put("showall", showAll);
		textFieldCmdIn.getActionMap().put("goBack", goBack);
		textFieldCmdIn.getActionMap().put("goForward", goForward);

		textFieldCmdIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userSentence = textFieldCmdIn.getText().trim();
				ResultOfCommand results = new ResultOfCommand();
				if (userSentence.equalsIgnoreCase("/help")){
					showHelp(results);
				} else { 
					results = Controller.executeCommand(userSentence, file, archive);	
					assert results != null;
					displayPanelTodayTasks.setText(results.printArrayList());
					middleRow.setBorder(BorderFactory.createTitledBorder(results.getTitleOfPanel()));
					backwardsUserInput.push(userSentence);
					textFieldResultsOut.setText(results.getFeedback());
					textFieldCmdIn.setText("");  // clear input TextField
				}
			}

			private void showHelp(ResultOfCommand results) {
				try 
				{
					FileReader fr = new FileReader("help.txt");
					BufferedReader br = new BufferedReader(fr);
					displayPanelTodayTasks.read(br, null);
					br.close();
				} catch (IOException e1) {
					System.out.println("Help.txt is not found");
				}
				results.setTitleOfPanel("Help Screen:");
				results.setFeedback("Press ESC to return to Today Tasks");
			}
		});
	}

	// Concats the different messages to form the welcome message for the
	// welcome screen
	private static String createWelcomeMessage(ArrayList<Integer> numOfTask) {
		assert numOfTask != null;
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

}