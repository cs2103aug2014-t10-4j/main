import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Storage {
	private static final String DIVIDER_DATE = "//!@#DOUBLEUP_DIVIDER_DATE#@!//";
	private static final String DIVIDER_TIME = "//!@#DOUBLEUP_DIVIDER_TIME#@!//";
	private static final String DIVIDER_DETAILS = "//!@#DOUBLEUP_DIVIDER_DETAILS#@!//";
	private static final String DIVIDER_IMPORTANCE = "//!@#DOUBLEUP_DIVIDER_IMPORTANCE#@!//";
	
	private static final String MSG_MISSING_FILE = "File not found.";
	
	public static ArrayList<Task> copyToArrayList(File file, ArrayList<Task> tempStorage) {
		
		
		Scanner input;
		try {
			input = new Scanner(file);

			if (!input.hasNext()) {
				input.close();
			} else {
				while (input.hasNext()) {
					Task task = new Task();
					String currentTask = input.nextLine();
					task.setName(currentTask.substring(0,
							currentTask.indexOf(DIVIDER_DATE)));
					currentTask.replace(
							currentTask.substring(0,
									currentTask.indexOf(DIVIDER_DATE)), "");

					task.setDate(currentTask.substring(0,
							currentTask.indexOf(DIVIDER_TIME)));
					currentTask.replace(
							currentTask.substring(0,
									currentTask.indexOf(DIVIDER_TIME)), "");

					task.setTime(currentTask.substring(0,
							currentTask.indexOf(DIVIDER_DETAILS)));
					currentTask.replace(
							currentTask.substring(0,
									currentTask.indexOf(DIVIDER_DETAILS)), "");

					task.setDetails(currentTask.substring(0,
							currentTask.indexOf(DIVIDER_IMPORTANCE)));
					currentTask.replace(
							currentTask.substring(0,
									currentTask.indexOf(DIVIDER_IMPORTANCE)),
							"");

					task.setImportance(Integer.parseInt(currentTask));
					tempStorage.add(task);

				}
				input.close();
			}
		} catch (FileNotFoundException e) {
			return null;
		}
		return tempStorage;
	}

	public static boolean writeToFile(Task task, File file) {
		BufferedWriter fileWritten;
		String toWriteInFile;
		try {
			fileWritten = new BufferedWriter(new FileWriter(file.getName(),
					true));
			if (numberOfLine(file) > 0) {
				fileWritten.newLine();
			}
			toWriteInFile = task.getName() + DIVIDER_DATE + task.getDate()
					+ DIVIDER_TIME + task.getTime() + DIVIDER_DETAILS
					+ task.getDetails() + DIVIDER_IMPORTANCE
					+ task.getImportance();

			fileWritten.write(toWriteInFile);
			fileWritten.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	// The function below serves to count the number of lines of text present in
		// the file.
		public static Integer numberOfLine(File file) {
			Scanner input;
			int lineNum = 0;
			try {
				input = new Scanner(file);

				if (!input.hasNext()) {
					input.close();
					return lineNum;
				} else {

					while (input.hasNext()) {
						input.nextLine();
						lineNum++;
					}
					input.close();
				}
			} catch (FileNotFoundException e) {
				return null;
			}
			return lineNum;
		}

}
