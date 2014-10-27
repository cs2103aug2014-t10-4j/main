import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;



public class LogicTest {
	/*
	this.name = splitTask[1];
	this.date = splitTask[2];
	this.time = splitTask[3];
	this.details = splitTask[4];
	if (splitTask[5] != null){
		this.importance = Integer.parseInt(splitTask[5]);

	this.error = splitTask[6];
	this.params = splitTask[7];
	
	string = string 
				+ tempStorage.get(i).getName() 
				+" "+ tempStorage.get(i).getDate()
				+" "+ tempStorage.get(i).getTime()
				+" "+ tempStorage.get(i).getDetails()
				+" "+ importance.toString()
				+" "+ tempStorage.get(i).getParams()
				+"\n";
*/
	
	
	

		public File testFile = new File("testingStorage.txt");
		public File testArchive = new File("testingArchive.txt");
		
		String taskInputFirst;
		String taskInputName;;
		String taskInputDate ;
		String taskInputTime;
		String taskInputDetails;
		String taskInputImportance ;
		String taskInputError ;
		String taskInputParams;
		String splitTask[] = new String [] {taskInputFirst, taskInputName,taskInputDate, taskInputTime,
				taskInputDetails, taskInputImportance, taskInputError, taskInputParams};;
		
				/*				
	@Test
	public void testOneItem() {
		Logic.init(testFile, testArchive);
		Logic.clearContent(testFile); 
		Logic.clearContent(testArchive);
		Logic.clearUndoRedo();
		
		// testing with one item only (minimum boundary case for a task)
		taskInputFirst = null; 
		taskInputName = "testing Logic";
		taskInputDate = "24/10/2015";
		taskInputTime = "2359";
		taskInputDetails = "yea";
		taskInputImportance = "-1";
		taskInputError = null;
		taskInputParams = null;
		
		splitTask[0] = taskInputFirst;		
		splitTask[1] = taskInputName; 
		splitTask[2] = taskInputDate; 
		splitTask[3] = taskInputTime;
		splitTask[4] = taskInputDetails; 
		splitTask[5] = taskInputImportance;
		splitTask[6] = taskInputError;
		splitTask[7] = taskInputParams;
		
		Task addTask = new Task(splitTask);

		Logic.add("add",addTask,testFile);
		assertEquals("testing of add","testing Logic 24/10/2015 2359 yea -1 1\n", Logic.printTempStorage());



		taskInputName = "testing edit";
		taskInputDate = "24/10/2015";
		taskInputTime = "2119";
		taskInputDetails = "changing items";
		taskInputImportance = "2";
		taskInputError = null;
		taskInputParams = "1";
		splitTask[1] = taskInputName; 
		splitTask[2] = taskInputDate; 
		splitTask[3] = taskInputTime;
		splitTask[4] = taskInputDetails; 
		splitTask[5] = taskInputImportance;
		splitTask[6] = taskInputError;
		splitTask[7] = taskInputParams;

		Task editTask = new Task(splitTask);
		Logic.edit("edit", editTask, testFile);
		assertEquals("testing of edit", "testing edit 24/10/2015 2119 changing items 2 1\n", Logic.printTempStorage());



		
		Task deleteTask = new Task(splitTask);
		deleteTask.setParams("1");
		Logic.delete("delete",1, deleteTask, testFile, testArchive);
		assertEquals("testing of delete", "", Logic.printTempStorage());
	}
	
	@Test
	public void testUndoRedoBoundary() {
		
		Logic.init(testFile, testArchive);
		Logic.clearContent(testFile); 
		Logic.clearContent(testArchive);
		Logic.clearUndoRedo();
		
		// same actions as before
		taskInputFirst = null; 
		taskInputName = "testing Logic";
		taskInputDate = "25/10/2015";
		taskInputTime = "2359";
		taskInputDetails = "yea";
		taskInputImportance = "-1";
		taskInputError = null;
		taskInputParams = null;
		
		splitTask[0] = taskInputFirst;		
		splitTask[1] = taskInputName; 
		splitTask[2] = taskInputDate; 
		splitTask[3] = taskInputTime;
		splitTask[4] = taskInputDetails; 
		splitTask[5] = taskInputImportance;
		splitTask[6] = taskInputError;
		splitTask[7] = taskInputParams;
		
		Task addTask = new Task(splitTask);

		Logic.add("add",addTask,testFile);
		System.out.println("add");
		assertEquals("testing of add","testing Logic 25/10/2015 2359 yea -1 1\n", Logic.printTempStorage());



		taskInputName = "testing edit";
		taskInputDate = "26/10/2015";
		taskInputTime = "2119";
		taskInputDetails = "changing items";
		taskInputImportance = "2";
		taskInputError = null;
		taskInputParams = "1";
		splitTask[1] = taskInputName; 
		splitTask[2] = taskInputDate; 
		splitTask[3] = taskInputTime;
		splitTask[4] = taskInputDetails; 
		splitTask[5] = taskInputImportance;
		splitTask[6] = taskInputError;
		splitTask[7] = taskInputParams;

		Task editTask = new Task(splitTask);
		Logic.edit("edit", editTask, testFile);
		System.out.println("edit");
		assertEquals("testing of edit", "testing edit 26/10/2015 2119 changing items 2 1\n", Logic.printTempStorage());



		taskInputName = null;
		taskInputDate = null;
		taskInputTime = null;
		taskInputDetails = null;
		taskInputImportance = "-1";
		taskInputError = null;
		taskInputParams = "1";
		splitTask[1] = taskInputName; 
		splitTask[2] = taskInputDate; 
		splitTask[3] = taskInputTime;
		splitTask[4] = taskInputDetails; 
		splitTask[5] = taskInputImportance;
		splitTask[6] = taskInputError;
		splitTask[7] = taskInputParams;
		Task deleteTask = new Task(splitTask);

		Logic.delete("delete",1, deleteTask, testFile, testArchive);
		System.out.println("delete");
		assertEquals("testing of delete", "", Logic.printTempStorage());
		
		// testing undo till lower boundary case( boundary case of undo is 0)

		Logic.undo(testFile, testArchive);
		System.out.println("undo-delete");
		assertEquals("testing of undo-delete one time","testing edit 26/10/2015 2119 changing items 2 1\n", Logic.printTempStorage());

		Logic.undo(testFile, testArchive);
		System.out.println("undo-edit");
		assertEquals("testing of undo-edit one time","testing Logic 25/10/2015 2359 yea -1 1\n", Logic.printTempStorage());

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
		assertEquals("testing of redo-add one time","testing Logic 25/10/2015 2359 yea -1 1\n", Logic.printTempStorage());

		Logic.redo(testFile,testArchive);
		System.out.println("redo-edit");
		assertEquals("testing of redo-edit one time", "testing edit 26/10/2015 2119 changing items 2 1\n", Logic.printTempStorage());

		Logic.redo(testFile,testArchive);
		System.out.println("redo-delete");
		assertEquals("testing of redo-delete one time", "", Logic.printTempStorage());

		Logic.redo(testFile,testArchive);
		System.out.println("no more redo");
		assertEquals("testing of redo till boundary case", "", Logic.printTempStorage());
	}
*/

	@Test
	public void testSort() {

		Logic.clearContent(testFile);
		Logic.clearContent(testArchive);
		Logic.clearUndoRedo();
		
		//testing of 10 tasks
		taskInputName = "testing task 1";
		taskInputDate = "22/10/2015";
		taskInputTime = "1045";
		taskInputDetails = "still testing";
		taskInputImportance = "2";
		taskInputError = null;
		taskInputParams = null;
		splitTask[1] = taskInputName; 
		splitTask[2] = taskInputDate; 
		splitTask[3] = taskInputTime;
		splitTask[4] = taskInputDetails; 
		splitTask[5] = taskInputImportance;
		splitTask[6] = taskInputError;
		splitTask[7] = taskInputParams;
		Task taskOne = new Task(splitTask);
		Logic.add("add",taskOne,testFile);
		
		taskInputName = "testing task 2";
		taskInputDate = "24/10/2015";
		taskInputTime = "2212";
		taskInputDetails = "how long is the limit of this?";
		taskInputImportance = "1";
		taskInputError = null;
		taskInputParams = null;
		splitTask[1] = taskInputName; 
		splitTask[2] = taskInputDate; 
		splitTask[3] = taskInputTime;
		splitTask[4] = taskInputDetails; 
		splitTask[5] = taskInputImportance;
		splitTask[6] = taskInputError;
		splitTask[7] = taskInputParams;
		Task taskTwo = new Task(splitTask);
		Logic.add("add",taskTwo,testFile);
		
		taskInputName = "testing task 3";
		taskInputDate = "24/01/2015";
		taskInputTime = "0100";
		taskInputDetails = "lala";
		taskInputImportance = "-1";
		taskInputError = null;
		taskInputParams = null;
		splitTask[1] = taskInputName; 
		splitTask[2] = taskInputDate; 
		splitTask[3] = taskInputTime;
		splitTask[4] = taskInputDetails; 
		splitTask[5] = taskInputImportance;
		splitTask[6] = taskInputError;
		splitTask[7] = taskInputParams;
		Task taskThree = new Task(splitTask);
		Logic.add("add",taskThree,testFile);
		
		taskInputName = "testing task 4";
		taskInputDate = "24/11/2014";
		taskInputTime = "0000";
		taskInputDetails = "still testing?";
		taskInputImportance = "2";
		taskInputError = null;
		taskInputParams = null;
		splitTask[1] = taskInputName; 
		splitTask[2] = taskInputDate; 
		splitTask[3] = taskInputTime;
		splitTask[4] = taskInputDetails; 
		splitTask[5] = taskInputImportance;
		splitTask[6] = taskInputError;
		splitTask[7] = taskInputParams;
		Task taskFour = new Task(splitTask);
		Logic.add("add",taskFour,testFile);
		
		taskInputName = "testing task 5";
		taskInputDate = "24/11/2014";
		taskInputTime = "0000";
		taskInputDetails = "is this behind?";
		taskInputImportance = "3";
		taskInputError = null;
		taskInputParams = null;
		splitTask[1] = taskInputName; 
		splitTask[2] = taskInputDate; 
		splitTask[3] = taskInputTime;
		splitTask[4] = taskInputDetails; 
		splitTask[5] = taskInputImportance;
		splitTask[6] = taskInputError;
		splitTask[7] = taskInputParams;
		Task taskFive = new Task(splitTask);
		Logic.add("add",taskFive,testFile);
		
		assertEquals("after adding all five", "testing task 4 24/11/2014 0000 still testing? 2 4\n"
						+"testing task 5 24/11/2014 0000 is this behind? 3 5\n"
						+"testing task 3 24/01/2015 0100 lala -1 3\n"
						+"testing task 1 22/10/2015 1045 still testing 2 1\n"
						+"testing task 2 24/10/2015 2212 how long is the limit of this? 1 2\n"
						,Logic.printTempStorage());
		
		Logic.sortAlpha();
		assertEquals("sortAlpha", "testing task 1 22/10/2015 1045 still testing 2 1\n"
				+"testing task 2 24/10/2015 2212 how long is the limit of this? 1 2\n"
				+"testing task 3 24/01/2015 0100 lala -1 3\n"
				+"testing task 4 24/11/2014 0000 still testing? 2 4\n"
				+"testing task 5 24/11/2014 0000 is this behind? 3 5\n"
				,Logic.printTempStorage());
		
		Logic.sortImportance();
		assertEquals("sortImport", "testing task 5 24/11/2014 0000 is this behind? 3 5\n"
				+"testing task 1 22/10/2015 1045 still testing 2 1\n"
				+"testing task 4 24/11/2014 0000 still testing? 2 4\n"
				+"testing task 2 24/10/2015 2212 how long is the limit of this? 1 2\n"
				+"testing task 3 24/01/2015 0100 lala -1 3\n"
				,Logic.printTempStorage());
		
		Logic.sortChrono();
		assertEquals("sortChrono", "testing task 4 24/11/2014 0000 still testing? 2 4\n"
				+"testing task 5 24/11/2014 0000 is this behind? 3 5\n"
				+"testing task 3 24/01/2015 0100 lala -1 3\n"
				+"testing task 1 22/10/2015 1045 still testing 2 1\n"
				+"testing task 2 24/10/2015 2212 how long is the limit of this? 1 2\n"
				,Logic.printTempStorage());
		
		
		Logic.sortImportance();
		Logic.undo(testFile, testArchive);
		assertEquals("sortImport", "testing task 4 24/11/2014 0000 still testing? 2 4\n"
				+"testing task 3 24/01/2015 0100 lala -1 3\n"
				+"testing task 1 22/10/2015 1045 still testing 2 1\n"
				+"testing task 2 24/10/2015 2212 how long is the limit of this? 1 2\n"
				,Logic.printTempStorage());
		Logic.undo(testFile, testArchive);
		
	}
	
		private static final String MSG_CLEARED_FILE = "List is cleared";
		private static final String MSG_SORT_SUCCESS = "Successfully sorted by alphabetical order.";
		private static final String MSG_SORT_SUCCESS_1 = "Successfully sorted by importance level.";
		
		String[] splitTask4 = {null,"testing", "ft", null, null, null, null , null};
		Task task = new Task(splitTask4);
		String[] splitTask1 = {null,"testing task","22/10/2014","0000", "some many works need to do", "1", 
				null , null};
		Task task1 = new Task(splitTask1);
		
		String[] splitTask2 = {null,"apple","ft", null, null,"2", null, null};
		Task task2 = new Task(splitTask2);

		String[] splitTask3 = {null,"banana", "ft", null, null,"3", null, null};
		Task task3 = new Task(splitTask3);
		
		//File testFile = new File("test.txt");
		//File testArchive = new File("testingArchive.txt");
		
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
	
