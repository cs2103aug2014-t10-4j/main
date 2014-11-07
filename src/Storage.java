/**
 * This Storage class is to be used to create the text file and extract or store information into it.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.*;

public class Storage {
	private static Logger logger = Logger.getLogger("Storage");

	//@author A0108380L
	// This function serves to write all the task in the text file into temp storage.
	public static boolean copyToArrayList(File file, ArrayList<Task> tempStorage) {
		Scanner input;
		try {
			input = new Scanner(file);

			if (!input.hasNext()) {
				input.close();
			} else {
				for(Integer i =0; input.hasNext(); i++) {
					Task task = new Task();
					String currentTask = input.nextLine();
					assignValuesToTask(i, task, currentTask);
					
					tempStorage.add(task);

				}
				input.close();
			}
		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, "no storage.txt");
			openFile(file.getName());
			return false;

		}
		logger.log(Level.INFO, "end of writing to temporary storage");
		return true;
	}
	
	// This function serves to extract the information from the string and assign to corresponding portion in task.
	private static void assignValuesToTask(Integer taskNumber, Task task,
			String currentTask) {
		task.setName(currentTask.substring(0, currentTask.indexOf(Constants.DIVIDER_DATE)));

		currentTask = currentTask.substring(currentTask.indexOf(Constants.DIVIDER_DATE));
		currentTask = currentTask.replaceFirst(Constants.DIVIDER_DATE,"");

		task.setDate(currentTask.substring(0,currentTask.indexOf(Constants.DIVIDER_START_TIME)));
		currentTask = currentTask.substring(currentTask.indexOf(Constants.DIVIDER_START_TIME));
		currentTask = currentTask.replaceFirst((Constants.DIVIDER_START_TIME),"");
		
		String taskStartTime = currentTask.substring(0,currentTask.indexOf(Constants.DIVIDER_END_TIME));
		if (taskStartTime.equals("null")){
			task.setStartTime(null);
		}
		else{
			task.setStartTime(taskStartTime);
		}
		
		currentTask = currentTask.substring(currentTask.indexOf(Constants.DIVIDER_END_TIME));
		currentTask = currentTask.replaceFirst((Constants.DIVIDER_END_TIME),"");
		String taskEndTime = currentTask.substring(0,currentTask.indexOf(Constants.DIVIDER_DETAILS));
		if (taskEndTime.equals("null")){
			task.setEndTime(null);
		}
		else{
			task.setEndTime(taskEndTime);
		}
		
		currentTask = currentTask.substring(currentTask.indexOf(Constants.DIVIDER_DETAILS));
		currentTask = currentTask.replaceFirst((Constants.DIVIDER_DETAILS),"");


		String taskDetails = currentTask.substring(0,currentTask.indexOf(Constants.DIVIDER_IMPORTANCE));
		if (taskDetails.equals("null")){
			task.setDetails(null);
		}
		else{
			task.setDetails(taskDetails);
		}
		currentTask = currentTask.substring(currentTask.indexOf(Constants.DIVIDER_IMPORTANCE));
		currentTask = currentTask.replaceFirst((Constants.DIVIDER_IMPORTANCE),"");


		task.setImportance(Integer.parseInt(currentTask));
		task.setParams(taskNumber.toString());
	}

	// This function serves to write all the task in the tempStorage into the text file.
	public static boolean writeToFile(File file, ArrayList<Task> tempStorage) {
		logger.log(Level.INFO, "going to start writing to writing to " + file.getName());
		BufferedWriter fileWritten;
		String toWriteInFile;
		try {
			clear(file);
			fileWritten = new BufferedWriter(new FileWriter(file.getName(),true));

			for(int i=0; i<tempStorage.size();i++){

				Task currentTask = tempStorage.get(i);
				toWriteInFile = taskToString(currentTask);

				fileWritten.write(toWriteInFile);
			}
			fileWritten.close();
		} catch (IOException e) {
			logger.log(Level.INFO, "unable to write to Storage.txt / archive.txt");
			
			return false;
		}
		logger.log(Level.INFO, "end of writing to " + file.getName());
		return true;

	}
	
	// This function serves to convert the task into a string with dividers in between each detail of the task.
	private static String taskToString(Task currentTask) {
		return currentTask.getName() 
				+ Constants.DIVIDER_DATE + currentTask.getDate()
				+ Constants.DIVIDER_START_TIME + currentTask.getStartTime() 
				+ Constants.DIVIDER_END_TIME + currentTask.getEndTime()
				+ Constants.DIVIDER_DETAILS + currentTask.getDetails() 
				+ Constants.DIVIDER_IMPORTANCE + currentTask.getImportance()+"\n";
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
			System.out.println(Constants.MSG_FAIL_READ_FILE);
		}
		return file;
	}
}

