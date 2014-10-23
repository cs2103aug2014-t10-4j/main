import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

public class LogicTest {
	private static final String MSG_CLEARED_FILE = "List is cleared";
	private static final String MSG_SORT_SUCCESS = "Successfully sorted by alphabetical order.";
	private static final String MSG_SORT_SUCCESS_1 = "Successfully sorted by importance level.";
	
	String[] splitTask = {null,"testing", null, null, null, null, null , null};
	Task task = new Task(splitTask);
	String[] splitTask1 = {null,"testing task","22/10/2014","0000", "some many works need to do", "1", 
			null , null};
	Task task1 = new Task(splitTask1);
	
	String[] splitTask2 = {null,"apple",null, null, null,"2", null, null};
	Task task2 = new Task(splitTask2);

	String[] splitTask3 = {null,"banana", null, null, null,"3", null, null};
	Task task3 = new Task(splitTask3);
	
	File testFile = new File("test.txt");
	File testArchive = new File("testingArchive.txt");
	
	private static ArrayList<Task> List = new ArrayList<Task>();
	private static ArrayList<Task> List1 = new ArrayList<Task>();

	@Test
	public void Test_clearCommand() {
		String expected = String.format(MSG_CLEARED_FILE, "");
		String result = Logic.clearContent(testFile);
		assertEquals(expected, result);
	}

	@Test
	public void Test_add() {
		assertEquals("Added to test.txt: \"testing task\". Type .u to undo.",
				Logic.add("add", task1, testFile));
	}

	@Test
	/* This is a boundary case for the ArrayList task size partition */
	public void Test_DeleteTask() {
		Logic.add("add", task1, testFile);
		Logic.add("add",task2,testFile);
		/*test delete first valid index number*/
		assertEquals("deleted from your list: \"testing task\"",
				Logic.delete("delete", 1, task1, testFile, testArchive));
		/*test delete invalid cases*/
		assertEquals("Unable to delete line",
				Logic.delete("delete", -1, task1, testFile, testArchive));
		//assertEquals("Unable to delete line",
				//Logic.delete("delete", 3, task1, testFile, testArchive));
		/*test delete a upper boundary case*/
		assertEquals("deleted from your list: \"apple\"",
				Logic.delete("delete", 2, task1, testFile, testArchive));
	}
	
	@Test
	public void Test_search() {
		String[] splitTask = {null,"testing", null, null, null, null, null , null};
		Task task = new Task(splitTask);
		//String[] splitTask4 = {null,null, null, null, null, null, null , null};
		//Task task4 = new Task(splitTask4);	
		Logic.clearContent(testFile);
		Logic.add("add", task1, testFile);		
		List.add(task1);
		//List1.add(task4);
		assertEquals(List, Logic.search(task));
		//assertEquals(null, Logic.search(task4));
	}

	@Test
	public void Test_sortByAlphabet() {
		List1.clear();
		List1.add(task1);
		List1.add(task2);
		String expected = String.format(MSG_SORT_SUCCESS);
		assertEquals(expected, Logic.sortByAlphabet(List1));		
	}
	@Test
	public void Test_sortByImportance(){
		String expected = String.format(MSG_SORT_SUCCESS_1);
		assertEquals(expected, Logic.sortByImportance(List1));	
	}

}