
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class Logic {
	private static final String MSG_FAIL_ADD = "Unable to add line.";
	public static String ADD_MESSAGE = "added to %s: \"%s\"";
	
	private static ArrayList<Task> tempStorage = new ArrayList<Task>();
	private static ArrayList<Task> sortingStorage = new ArrayList<Task>();
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
		sortByDate(tempStorage);
		sortingStorage.add(task);
		Storage.writeToFile(tempStorage, file);
		
		return String.format(ADD_MESSAGE,file.getName(), task.getName());
	}

	public static ArrayList<Integer> init(File file) {
		 tempStorage = Storage.copyToArrayList(file, tempStorage);
		 sortingStorage = Storage.copyToArrayList(file, sortingStorage);
		 // stub = getNumTasks()
		 ArrayList<Integer> stub = new ArrayList<Integer>();
		 stub.add(1);
		return stub;
	}
	
	public static void sortByDate(ArrayList<Task> tempStorage){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if(tempStorage.size()<1){
			return;
		}
		else{
			for (int i=0;i<tempStorage.size();i++){
				boolean isSorted=true;
				for(int j=0;j<tempStorage.size()-1;j++){
					try{
					Date dateOfFirstTask = new Date();
					dateOfFirstTask = dateFormat.parse(tempStorage.get(j).getDate());
					
					Date dateOfSecondTask = new Date();
					dateOfSecondTask = dateFormat.parse(tempStorage.get(j+1).getDate());
					
						if(dateOfFirstTask.compareTo(dateOfSecondTask)>0){
							tempStorage.add(j+2,tempStorage.get(j));
							tempStorage.remove(j);
							isSorted= false;
							
						}
					}catch(Exception e){
					
					}
				}
				
				if (isSorted){
					return;
				}				
			}
		
		}
	}
	public static void sortByAlphabet(ArrayList<Task> sortingStorage){
		if(tempStorage.size()<1){
			return;
		}
		else{
			for (int i=0;i<tempStorage.size();i++){
				boolean isSorted=true;
				for(int j=0;j<tempStorage.size()-1;j++){
				
					if(sortingStorage.get(j).getName().compareToIgnoreCase(sortingStorage.get(j+1).getName())>0){
						sortingStorage.add(j+2,sortingStorage.get(j));
						sortingStorage.remove(j);
						isSorted= false;
							
					}
				}
				if (isSorted){
					return;
				}
			}				
		}
		
	}
	
	public static void sortByImportance(ArrayList<Task> sortingStorage){
		if(tempStorage.size()<1){
			return;
		}
		else{
			for (int i=0;i<tempStorage.size();i++){
				boolean isSorted=true;
				for(int j=0;j<tempStorage.size()-1;j++){
				
					if(sortingStorage.get(j).getImportance()<sortingStorage.get(j+1).getImportance()){
						sortingStorage.add(j+2,sortingStorage.get(j));
						sortingStorage.remove(j);
						isSorted= false;
							
					}
				}
				if (isSorted){
					return;
				}
			}				
		}
		
	}
}


