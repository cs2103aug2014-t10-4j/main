

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

	private static ArrayList<String> texts;
	private File file;
	public Logic() {
		this.file= null;
		texts = new ArrayList<String>();	
	}
	
	public Logic( File file) throws IOException {
		this.file = file;
		texts = new ArrayList<String>();
		writeTextFile(null,file);	
	}
	
	public File getFile(){
		return file;
	}
	
	public boolean add(String content) {
	if (content == null) {
		return false;		
	}	
	boolean result = texts.add(content);
	writeTextFile(null,file);
	return result;	
	}
	
	public static String delete(int number) {
		String deleteTask;
		try {
			deleteTask = texts.remove(number);
		} catch (Exception e) {
			deleteTask = null;
		}	
		return deleteTask;
	}
	
	public void sort() {
		Collections.sort(texts);
		writeTextFile(null,file);
	}
	
	public ArrayList<String> search (String word) {
		ArrayList<String> result = new ArrayList<String>();
		for (String task : texts) {
			if(task.contains(word)) {
				result.add(task);
			}
		}
		return result;
	}
	
	public void clear() {
		texts.clear();
		writeTextFile(null,file);		
	}
	
	public static void readFile(String FileName) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(FileName));
			String text;
			while ((text = reader.readLine()) != null) {
				texts.add(text.substring(3, text.length()));
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

	private static boolean isEmpty (File file) {
		return file.length() <= 0;
	}

}
