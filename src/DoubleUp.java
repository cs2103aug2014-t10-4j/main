import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.UIManager.*;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class DoubleUp extends JFrame implements NativeKeyListener , WindowListener{
	private static final String MSG_PREVIOUS_INSTANCE = "DoubleUp is already running.";
	private static final String ACTION_SHOW_ALL = "show all";
	private static final String TITLE_MAIN_WINDOW = "DoubleUp To-do-List";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MSG_WELCOME = "Welcome to DoubleUp! ";
	private static final String MSG_PROGRESS_BAR = "You have %d tasks due today, %d overdue tasks, %d tasks due eventually and %d floating tasks.\n";
	private static final String MSG_QOTD = "QOTD: \n";
	private static final String MSG_GOAL = "Your goal is: ";
	private static final String MSG_HELP = "Press F2 to view all the commands. Happy doubling up!";
	private static final String MSG_ENTER_COMMAND = "Enter a command: ";
	private static final String MSG_RESULT = "Result: ";
	private static final String FILE_TASK = "DoubleUp.txt";
	private static final String FILE_ARCHIVE = "Archive.txt";
	private static final String FILE_LOCK = "Lock.txt";

	private static JTextField textFieldCmdIn;
	private static JTextArea displayPanelTodayTasks, textFieldResultsOut;
	private static JPanel middleRow;
	private static JFrame frame;

	private static Stack <String> backwardsUserInput = new Stack<String>();
	private static Stack <String> forwardUserInput = new Stack<String>();

	private static File file, archive;
	private static Logger logger = Logger.getLogger("myLogger");

	public DoubleUp() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		setTitle(TITLE_MAIN_WINDOW);
		//setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentsToPane(getContentPane());
		setMinimumSize(new Dimension(650,600));
		setVisible(true);
		addWindowListener(this);
		logger.log(Level.INFO, "Successfully create GUI");
	}

	public static void main(String[] args) {
		if (lockInstance()){
			file = Storage.openFile(FILE_TASK);
			archive = Storage.openFile(FILE_ARCHIVE);
			ArrayList<Integer> overview = Logic.init(file, archive);
			new DoubleUp();
			initSystemTray(overview);
		} else {
			JOptionPane.showMessageDialog(frame, MSG_PREVIOUS_INSTANCE);
		}
	}

	private static void initSystemTray(ArrayList<Integer> overview) {
		if (SystemTray.isSupported()) {
			TrayIcon icon = new TrayIcon(getImage(), "DoubleUp", createPopupMenu());
			try {
				SystemTray.getSystemTray().add(icon);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			icon.displayMessage(MSG_WELCOME, String.format(MSG_PROGRESS_BAR, overview.get(0), overview.get(1),overview.get(2), overview.get(3)), 
					TrayIcon.MessageType.INFO);
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		//Initialze native hook.
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
		GlobalScreen.getInstance().addNativeKeyListener(this);
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		GlobalScreen.unregisterNativeHook();
		System.runFinalization();
		System.exit(0);
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
		if (e.getKeyCode() == NativeKeyEvent.VC_SPACE && 
				NativeInputEvent.getModifiersText(e.getModifiers()).equals(
						"Ctrl")) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					controlSpace();
				}
			});
		}
	}

	private static Image getImage() throws HeadlessException {
		Icon defaultIcon = MetalIconFactory.getTreeHardDriveIcon();
		Image img = new BufferedImage(defaultIcon.getIconWidth(), 
				defaultIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		defaultIcon.paintIcon(new Panel(), img.getGraphics(), 0, 0);
		return img;
	}

	private static PopupMenu createPopupMenu() throws HeadlessException {
		PopupMenu menu = new PopupMenu();
		MenuItem aboutUs = new MenuItem("About DoubleUp");
		MenuItem help = new MenuItem("Help Contents");
		MenuItem exit = new MenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(aboutUs);
		menu.add(help);
		menu.add(exit);
		return menu;
	}

	public static void createAndShowGUI() {
		frame = new JFrame(TITLE_MAIN_WINDOW);
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
		displayPanelTodayTasks.setLineWrap(true);
		displayPanelTodayTasks.setWrapStyleWord(true);
		displayPanelTodayTasks.setMargin(new Insets(5,5,5,5));
		ResultOfCommand results = Controller.executeCommand(ACTION_SHOW_ALL, file, archive);
		displayPanelTodayTasks.setText(results.printArrayList());
		JScrollPane scroll  = new JScrollPane(displayPanelTodayTasks,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		middleRow.add(scroll, BorderLayout.CENTER);
		middleRow.setBorder(BorderFactory.createTitledBorder(results.getTitleOfPanel()));
		cp.add(middleRow, BorderLayout.CENTER);

		//Feedback field below
		JPanel lastRow = new JPanel();
		lastRow.add(new JLabel(MSG_RESULT));
		textFieldResultsOut = new JTextArea(0, 43);
		textFieldResultsOut.setLineWrap(true);
		textFieldResultsOut.setWrapStyleWord(true);
		textFieldResultsOut.setMargin(new Insets(5,5,5,5));
		textFieldResultsOut.setEditable(false);  // read-only
		textFieldResultsOut.setText(MSG_WELCOME + MSG_HELP);
		//textFieldResultsOut.setBackground(Color.YELLOW);

		lastRow.add(textFieldResultsOut);
		cp.add(lastRow, BorderLayout.SOUTH);
		
		UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);

		Action showHelp = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				showHelp();
			}
			private void showHelp() {
				String helpfile = "/res/helpV2.txt";
				InputStream inputStream = this.getClass().getResourceAsStream(helpfile);
				assert inputStream != null;

				String theString = convertStreamToString(inputStream);
				displayPanelTodayTasks.setText(theString);
				textFieldResultsOut.setText("Press ESC to return to All Tasks");
				middleRow.setBorder(BorderFactory.createTitledBorder("Help Screen:"));
			}

			String convertStreamToString(java.io.InputStream is) {
				Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
				return s.hasNext() ? s.next() : "";
			}
		};
		
		Action showAll = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				showAll();
			}
			private void showAll() {
				ResultOfCommand results = Controller.executeCommand("show all", file, archive);
				displayPanelTodayTasks.setText(results.printArrayList());
				middleRow.setBorder(BorderFactory.createTitledBorder(results.getTitleOfPanel()));
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

		//This is to add listener for textfield at top
		textFieldCmdIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userSentence = textFieldCmdIn.getText().trim();
				ResultOfCommand results = new ResultOfCommand();
				if (userSentence.equalsIgnoreCase("help") || userSentence.equalsIgnoreCase(".h")){
					showHelp(results);
				} else { 
					results = Controller.executeCommand(userSentence, file, archive);	
					assert results != null;
					displayPanelTodayTasks.setText(results.printArrayList());
					middleRow.setBorder(BorderFactory.createTitledBorder(results.getTitleOfPanel()));
					textFieldResultsOut.setText(results.getFeedback());
				}
				backwardsUserInput.push(userSentence);
				textFieldCmdIn.setText("");  // clear input TextField
			}

			private void showHelp(ResultOfCommand results) {
				String helpfile = "/res/helpV2.txt";
				InputStream inputStream = this.getClass().getResourceAsStream(helpfile);
				assert inputStream != null;

				String theString = convertStreamToString(inputStream);
				displayPanelTodayTasks.setText(theString);
				textFieldResultsOut.setText("Press ESC to return to All Tasks");
				middleRow.setBorder(BorderFactory.createTitledBorder("Help Screen:"));
				results.setTitleOfPanel("Help Screen:");
				results.setFeedback("Press ESC to return to Today Tasks");
			}

			String convertStreamToString(java.io.InputStream is) {
				Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
				return s.hasNext() ? s.next() : "";
			}
		});
	}

	// Concats the different messages to form the welcome message for the welcome screen
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



	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {

	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
	}


	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}





	private void controlSpace() {
		if (isFocused()){
			setState(Frame.ICONIFIED);
		} else { 
			setState(Frame.NORMAL);
			setVisible(true);
			toFront();
			textFieldCmdIn.requestFocus();
			repaint();
		}
		repaint();
	}
	//Windows has an issue where it will not allow application windows to steal focus.
	//It will only flash the icon.
	@Override 
	public void toFront() {
		int sta = super.getExtendedState() &~ JFrame.ICONIFIED & JFrame.NORMAL;
		super.setExtendedState(sta);
		super.setAlwaysOnTop(true);
		super.toFront();
		super.requestFocus();
		super.setAlwaysOnTop(false);
	}

	private static boolean lockInstance() {
		try {
			final File lockfile = new File(FILE_LOCK);
			final RandomAccessFile randomAccessFile = new RandomAccessFile(lockfile, "rw");
			final FileLock fileLock = randomAccessFile.getChannel().tryLock();
			if (fileLock != null) {
				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run() {
						try {
							fileLock.release();
							randomAccessFile.close();
							lockfile.delete();
						} catch (Exception e) {
							logger.log(Level.WARNING, "Unable to remove lock file: " + FILE_LOCK, e);
						}
					}
				});
				return true;
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "Unable to create and/or lock file: " + FILE_LOCK, e);
		}
		return false;
	}



}