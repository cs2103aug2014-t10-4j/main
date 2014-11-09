import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
public class TestLogic {
	
	String[] splitTask4 = {null,"testing", "ft", null, null,null, null, null , null};
	Task task = new Task(splitTask4);
	
	String[] splitTask1 = {null,"testing task","22/12/2014","0000","null", "some many works need to do", "1", 
			null , null};
	Task task1 = new Task(splitTask1);	
	
	String[] splitTask2 = {null,"apple","ft",null, null, null,"2", null, null};
	Task task2 = new Task(splitTask2);

	String[] splitTask3 = {null,"banana", "ft", null,null, null,"3", null, null};
	Task task3 = new Task(splitTask3);
	
	public File testFile = new File("testingStorage.txt");
	public File testArchive = new File("testingArchive.txt");
	
	private static ArrayList<Task> List = new ArrayList<Task>();
	private static ArrayList<Task> List1 = new ArrayList<Task>();

	@Test
	public void Test_clearContent() {
		String expected = String.format(Constants.MSG_CLEARED_FILE, "");
		String result = Logic.clearContent(testFile, testArchive);
		assertEquals(expected, result);
	}

	@Test
	public void Test_add() {
		assertEquals("Added to testingStorage.txt: \"testing task\". Type .u to undo.",
				Logic.add("add", task1, testFile));
		Logic.undo(testFile, testArchive);
	}

	@Test
	/* This is a boundary case for the ArrayList task size partition */
	public void Test_DeleteTask() {
		Logic.add("add", task1, testFile);
		Logic.add("add",task2,testFile);
		/*test delete first valid index number*/
		assertEquals("deleted \'1. testing task\'",
				Logic.delete("delete", 1, task1, testFile, testArchive));
		/*test delete a upper boundary case*/
		assertEquals("deleted \'1. apple\'",
				Logic.delete("delete", 2, task1, testFile, testArchive));
	}
	
	@Test
	public void Test_search() {
		String[] splitTask = {null,"testing",null,null, null, null, null, null , null};
		Task task = new Task(splitTask);
		Logic.clearContent(testFile,testArchive);
		Logic.add("add", task1, testFile);	
		List.clear();
		List.add(task1);
		assertEquals(List, Logic.search(task));
		Logic.clearContent(testFile, testArchive);
	}
	@Test
	public void test_edit() {
		/*edit time */
		Logic.add("add", task1, testFile);
		String[] splitTask = {null,null,null,"1pm", "2pm", null, null, null , "1"};
		Task task5 = new Task(splitTask);
		assertEquals(Constants.MSG_EDIT_SUCCESS, Logic.edit("edit",task5,testFile));
		/*edit date*/
		String[] splitTask1 = {null,null,null,"1pm", "2pm", null, null, null , "1"};
		Task task6 = new Task(splitTask1);
		assertEquals(Constants.MSG_EDIT_SUCCESS, Logic.edit("edit",task6,testFile));
		/*edit task name*/
		String[] splitTask2 = {null,"CS2103 develop guide",null,null, null, null, null, null , "1"};
		Task task7 = new Task(splitTask2);
		assertEquals(Constants.MSG_EDIT_SUCCESS, Logic.edit("edit",task7,testFile));
		/*edit date*/
		String[] splitTask3 = {null,null,"tomorrow",null, null, null, null, null , "1"};
		Task task8 = new Task(splitTask3);
		assertEquals(Constants.MSG_EDIT_SUCCESS, Logic.edit("edit",task8,testFile));
		/*edit importance level*/
		String[] splitTask4 = {null,null,null,null, null, null, "3", null , "1"};
		Task task9 = new Task(splitTask4);
		assertEquals(Constants.MSG_EDIT_SUCCESS, Logic.edit("edit",task9,testFile));
		/*edit timed task to floating task*/
		String[] splitTask5= {null,null,"ft",null, null, null, null, null , "1"};
		Task task10 = new Task(splitTask5);
		assertEquals(Constants.MSG_EDIT_SUCCESS, Logic.edit("edit",task10,testFile));
		Logic.clearAll(testFile);
		Logic.clearAll(testArchive);	
	}

	@Test
	public void Test_sortByAlphabet() {
		List1.clear();
		List1.add(task1);
		List1.add(task2);
		List.add(task3);
		Logic.add("add", task1, testFile);	
		Logic.add("add", task2, testFile);	
		Logic.add("add", task3, testFile);	
		String expected = String.format(Constants.MSG_SORT_SUCCESS,
				"alphabetical order");
		assertEquals(expected, Logic.sortByAlphabet(List1));
		List1.clear();
		Logic.clearContent(testFile, testArchive);
		/*nothing to sort*/
		assertEquals(Constants.MSG_NO_TASKS_TO_SORT, Logic.sortByImportance(List1));	
	}
	@Test
	public void Test_sortByImportance(){
		List1.clear();
		List1.add(task1);
		List1.add(task2);
		List.add(task3);
		Logic.add("add", task1, testFile);	
		Logic.add("add", task2, testFile);	
		Logic.add("add", task3, testFile);	
		String expected = String.format(Constants.MSG_SORT_SUCCESS,
				"importance level");
		assertEquals(expected, Logic.sortByImportance(List1));
		List1.clear();
		/*nothing to sort*/
		Logic.clearContent(testFile, testArchive);	
		assertEquals(Constants.MSG_NO_TASKS_TO_SORT, Logic.sortByImportance(List1));	
	}
			
	@Test
	public void testOneItem() {
		Logic.init(testFile, testArchive);
		Logic.clearAll(testFile); 
		Logic.clearAll(testArchive);
		Logic.clearUndoRedo();
		
		// testing with one item only (minimum boundary case for a task)

		
		String[] splitTaskAdd = {null,"testing Logic", "24/10/2015", "23:59", null , "yea", "-1",
				null , null};
		
		Task addTask = new Task(splitTaskAdd);

		Logic.add("add",addTask,testFile);
		assertEquals("testing of add","testing Logic 24/10/2015 23:59 null yea -1 1\n", Logic.printTempStorage());




		String[] splitTaskEdit = {null,"testing edit", "24/10/2015", "21:19", "22:10" , "changing items", "2",
				null , "1"};
		Task editTask = new Task(splitTaskEdit);
		Logic.edit("edit", editTask, testFile);
		assertEquals("testing of edit", "testing edit 24/10/2015 21:19 22:10 changing items 2 1\n", Logic.printTempStorage());



		String[] splitTaskDelete = {null,null, null, null, null , null, null,
				null , "1"};
		Task deleteTask = new Task(splitTaskDelete);
		deleteTask.setParams("1");
		Logic.delete("delete",1, deleteTask, testFile, testArchive);
		assertEquals("testing of delete", "", Logic.printTempStorage());
	}
	
	@Test
	public void testUndoRedoBoundary() {
		
		Logic.init(testFile, testArchive);
		Logic.clearAll(testFile); 
		Logic.clearAll(testArchive);
		Logic.clearUndoRedo();
		
		// same actions as before
		String[] splitTaskAdd = {null,"testing Logic", "24/10/2015", "23:59", null , "yea", "-1",
				null , null};
		
		Task addTask = new Task(splitTaskAdd);

		Logic.add("add",addTask,testFile);
		assertEquals("testing of add","testing Logic 24/10/2015 23:59 null yea -1 1\n", Logic.printTempStorage());




		String[] splitTaskEdit = {null,"testing edit", "24/10/2015", "21:19", "22:10" , "changing items", "2",
				null , "1"};
		Task editTask = new Task(splitTaskEdit);
		Logic.edit("edit", editTask, testFile);
		assertEquals("testing of edit", "testing edit 24/10/2015 21:19 22:10 changing items 2 1\n", Logic.printTempStorage());



		String[] splitTaskDelete = {null,null, null, null, null , null, null,
				null , "1"};
		Task deleteTask = new Task(splitTaskDelete);
		deleteTask.setParams("1");
		Logic.delete("delete",1, deleteTask, testFile, testArchive);
		assertEquals("testing of delete", "", Logic.printTempStorage());

		Logic.delete("delete",1, deleteTask, testFile, testArchive);
		System.out.println("delete");
		assertEquals("testing of delete", "", Logic.printTempStorage());
		
		// testing undo till lower boundary case( boundary case of undo is 0)

		Logic.undo(testFile, testArchive);
		System.out.println("undo-delete");
		assertEquals("testing of undo-delete one time","testing edit 24/10/2015 21:19 22:10 changing items 2 1\n", Logic.printTempStorage());

		Logic.undo(testFile, testArchive);
		System.out.println("undo-edit");
		assertEquals("testing of undo-edit one time","testing Logic 24/10/2015 23:59 null yea -1 1\n", Logic.printTempStorage());

		// Last action done
		Logic.undo(testFile, testArchive);
		System.out.println("undo-add");
		assertEquals("testing of undo-add one time","", Logic.printTempStorage()); 

		// No more previous case
		Logic.undo(testFile, testArchive);
		System.out.println("no more undo");
		assertEquals("testing of undo- boundary case","", Logic.printTempStorage()); 

		// testing redo till upper boundary case( boundary case of redo is the number just after the number of actions done)

		Logic.redo(testFile,testArchive);
		System.out.println("redo-add");
		assertEquals("testing of redo-add one time","testing Logic 24/10/2015 23:59 null yea -1 1\n", Logic.printTempStorage());

		Logic.redo(testFile,testArchive);
		System.out.println("redo-edit");
		assertEquals("testing of redo-edit one time", "testing edit 24/10/2015 21:19 22:10 changing items 2 1\n", Logic.printTempStorage());

		Logic.redo(testFile,testArchive);
		System.out.println("redo-delete");
		assertEquals("testing of redo-delete one time", "", Logic.printTempStorage());

		Logic.redo(testFile,testArchive);
		System.out.println("no more redo");
		assertEquals("testing of redo till boundary case", "", Logic.printTempStorage());
	}


	@Test
	public void testSort() {

		Logic.clearAll(testFile);
		Logic.clearAll(testArchive);
		Logic.clearUndoRedo();
		
		//testing with 5 tasks
		
		
		
		String[] splitTaskOne = {null,"testing task 1","22/10/2015","10:45","null", "still testing", "2", 
				null , null};
		Task taskOne = new Task(splitTaskOne);
		Logic.add("add",taskOne,testFile);
		
		
		String[] splitTaskTwo = {null,"testing task 2","24/10/2015","22:12","null", "how long is the limit of this?", "1", 
				null , null};
		Task taskTwo = new Task(splitTaskTwo);
		Logic.add("add",taskTwo,testFile);
		
		
		String[] splitTaskThree = {null,"testing task 3","24/01/2015","01:00","null", "lala", "-1", 
				null , null};
		Task taskThree = new Task(splitTaskThree);
		Logic.add("add",taskThree,testFile);
		
		
		String[] splitTaskFour = {null,"testing task 4","24/11/2014","00:00","null", "still testing?", "2", 
				null , null};
		Task taskFour = new Task(splitTaskFour);
		Logic.add("add",taskFour,testFile);
		
		
		String[] splitTaskFive = {null,"testing task 5","24/11/2014","00:00" ,"null", "is this behind?", "3", 
				null , null};
		Task taskFive = new Task(splitTaskFive);
		Logic.add("add",taskFive,testFile);
		
		assertEquals("after adding all five", "testing task 4 24/11/2014 00:00 null still testing? 2 4\n"
						+"testing task 5 24/11/2014 00:00 null is this behind? 3 5\n"
						+"testing task 3 24/01/2015 01:00 null lala -1 3\n"
						+"testing task 1 22/10/2015 10:45 null still testing 2 1\n"
						+"testing task 2 24/10/2015 22:12 null how long is the limit of this? 1 2\n"
						,Logic.printTempStorage());
		
		Logic.sortAlpha();
		assertEquals("sortAlpha", "testing task 1 22/10/2015 10:45 null still testing 2 1\n"
				+"testing task 2 24/10/2015 22:12 null how long is the limit of this? 1 2\n"
				+"testing task 3 24/01/2015 01:00 null lala -1 3\n"
				+"testing task 4 24/11/2014 00:00 null still testing? 2 4\n"
				+"testing task 5 24/11/2014 00:00 null is this behind? 3 5\n"
				,Logic.printTempStorage());
		
		Logic.sortImportance();
		assertEquals("sortImport", "testing task 5 24/11/2014 00:00 null is this behind? 3 5\n"
				+"testing task 1 22/10/2015 10:45 null still testing 2 1\n"
				+"testing task 4 24/11/2014 00:00 null still testing? 2 4\n"
				+"testing task 2 24/10/2015 22:12 null how long is the limit of this? 1 2\n"
				+"testing task 3 24/01/2015 01:00 null lala -1 3\n"
				,Logic.printTempStorage());
		
		Logic.sortChrono();
		assertEquals("sortChrono", "testing task 4 24/11/2014 00:00 null still testing? 2 4\n"
				+"testing task 5 24/11/2014 00:00 null is this behind? 3 5\n"
				+"testing task 3 24/01/2015 01:00 null lala -1 3\n"
				+"testing task 1 22/10/2015 10:45 null still testing 2 1\n"
				+"testing task 2 24/10/2015 22:12 null how long is the limit of this? 1 2\n"
				,Logic.printTempStorage());
		
		
		Logic.sortImportance();
		Logic.undo(testFile, testArchive);
		assertEquals("sortImport", "testing task 4 24/11/2014 00:00 null still testing? 2 4\n"
				+"testing task 3 24/01/2015 01:00 null lala -1 3\n"
				+"testing task 1 22/10/2015 10:45 null still testing 2 1\n"
				+"testing task 2 24/10/2015 22:12 null how long is the limit of this? 1 2\n"
				,Logic.printTempStorage());
		Logic.undo(testFile, testArchive);
		
	}
	}
	
