import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.*;

public class Storage {
	private static final String DIVIDER_DATE = "//!@#DOUBLEUP_DIVIDER_DATE#@!//";
	private static final String DIVIDER_TIME = "//!@#DOUBLEUP_DIVIDER_TIME#@!//";
	private static final String DIVIDER_DETAILS = "//!@#DOUBLEUP_DIVIDER_DETAILS#@!//";
	private static final String DIVIDER_IMPORTANCE = "//!@#DOUBLEUP_DIVIDER_IMPORTANCE#@!//";
	private static final String MSG_FAIL_READ_FILE = "Unable to read file.";
	private static Logger logger = Logger.getLogger("Storage");


	// This function serves to write all the task in the text file into temp storage.
	public static void copyToArrayList(File file, ArrayList<Task> tempStorage) {
		logger.log(Level.INFO, "going to start writing to tempStorage / archive");
		Scanner input;
		try {
			input = new Scanner(file);

			if (!input.hasNext()) {
				input.close();
			} else {
				while (input.hasNext()) {
					Task task = new Task();
					String currentTask = input.nextLine();
					task.setName(currentTask.substring(0, currentTask.indexOf(DIVIDER_DATE)));


					currentTask = currentTask.replaceFirst(currentTask.substring(0,currentTask.indexOf(DIVIDER_DATE)),"");
					currentTask = currentTask.replaceFirst(DIVIDER_DATE,"");


					task.setDate(currentTask.substring(0,currentTask.indexOf(DIVIDER_TIME)));
					currentTask = currentTask.replaceFirst(currentTask.substring(0,currentTask.indexOf(DIVIDER_TIME)),"");
					currentTask = currentTask.replaceFirst((DIVIDER_TIME),"");

					task.setTime(currentTask.substring(0,currentTask.indexOf(DIVIDER_DETAILS)));
					currentTask = currentTask.replaceFirst(currentTask.substring(0,currentTask.indexOf(DIVIDER_DETAILS)),"");
					currentTask = currentTask.replaceFirst((DIVIDER_DETAILS),"");


					task.setDetails(currentTask.substring(0,currentTask.indexOf(DIVIDER_IMPORTANCE)));
					currentTask = currentTask.replaceFirst(currentTask.substring(0,currentTask.indexOf(DIVIDER_IMPORTANCE)),"");
					currentTask = currentTask.replaceFirst((DIVIDER_IMPORTANCE),"");


					task.setImportance(Integer.parseInt(currentTask));
					tempStorage.add(task);

				}
				input.close();
			}
		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, "no storage.txt");

		}
		logger.log(Level.INFO, "end of writing to tempStorage / archive");
	}

	// This function serves to write all the task in the tempStorage into the text file.
	public static boolean writeToFile(ArrayList<Task> tempStorage, File file) {
		logger.log(Level.INFO, "going to start writing to Storage.txt / archive.txt");
		BufferedWriter fileWritten;
		String toWriteInFile;
		try {
			clear(file);
			fileWritten = new BufferedWriter(new FileWriter(file.getName(),true));

			for(int i=0; i<tempStorage.size();i++){

				toWriteInFile = tempStorage.get(i).getName() + DIVIDER_DATE + tempStorage.get(i).getDate()
						+ DIVIDER_TIME + tempStorage.get(i).getTime() + DIVIDER_DETAILS
						+ tempStorage.get(i).getDetails() + DIVIDER_IMPORTANCE
						+ tempStorage.get(i).getImportance()+"\n";

				fileWritten.write(toWriteInFile);
			}
			fileWritten.close();
		} catch (IOException e) {
			logger.log(Level.INFO, "unable to write to Storage.txt / archive.txt");
			return false;
		}
		logger.log(Level.INFO, "end of writing to Storage.txt / archive.txt");
		return true;

	}


	// This function serves to clear the file.
	private static void clear(File file) {
		try {
			BufferedWriter bw =new BufferedWriter(new FileWriter (file,false));
			bw.write("");
			bw.close();
		} catch (IOException e) {

		}
	}

	// Creates a text file if the text file is missing or for first time usage.
	public static File openFile(String fileName) {
		File file = new File(fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			System.out.println(MSG_FAIL_READ_FILE);
		}
		return file;
	}

}
