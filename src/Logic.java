
/**
 * This program implement the logic for the TextBuddy.
 * 
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Logic {
	private static final String MSG_FAIL_ADD = "Unable to add line.";
	public static String ADD_MESSAGE = "added to %s: \"%s\"";
	
	private static ArrayList<Task> tempStorage;
	private File file;

	
	/*
	
	public static String delete(int number) {
		
		String deleteTask;
		try {
			deleteTask = tempStorage.remove(number);
		} catch (Exception e) {
			deleteTask = null;
		}
		return deleteTask;
		
	}

	public void sort() {
		Collections.sort(tempStorage);
		writeTextFile(null, file);
	}

	public ArrayList<String> search(String word) {
		ArrayList<String> result = new ArrayList<String>();
		for (String task : tempStorage) {
			if (task.contains(word)) {
				result.add(task);
			}
		}
		return result;
	}

	public void clear() {
		tempStorage.clear();
		writeTextFile(null, file);
	}

	public static void readFile(String FileName) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(FileName));
			String text;
			while ((text = reader.readLine()) != null) {
				tempStorage.add(text.substring(3, text.length()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void writeTextFile(String inputText, File file) {
		BufferedWriter outputFile;
		try {
			outputFile = new BufferedWriter(
					new FileWriter(file.getName(), true));
			if (!isEmpty(file))
				outputFile.write(inputText);
			outputFile.close();
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private static boolean isEmpty(File file) {
		return file.length() <= 0;
	}
*/
	public static String addLineToFile(Task task, File file) {

		if (task.getName() == null) {
			return "error";
		}
		tempStorage.add(task);
		
		return String.format(ADD_MESSAGE,file.getName(), task.getName());
	}

	public static ArrayList<Integer> init(File file) {
		 Storage.copyToArrayList(file, tempStorage);
		 
		 // stub = getNumTasks()
		 ArrayList<Integer> stub = new ArrayList<Integer>();
		 stub.add(1);
		return stub;
	}

}
