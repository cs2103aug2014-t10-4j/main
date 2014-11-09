//@author A0110930X
//The following class is used for integration testing from Controller
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class TestController {
	private static File testFile = new File("testFile.txt");
	private static File archiveFile = new File("testArchive.txt");

	@Test
	public void testExecuteCommand() {
		//Testing add
		//Successful with add command
		ResultOfCommand results = Controller.executeCommand("add abc", testFile, archiveFile);
		assertEquals(String.format(Constants.MSG_ADD_SUCCESS, "testFile.txt", "abc"), results.getFeedback());
		//Successful without add command
		results = Controller.executeCommand("this is something new", testFile, archiveFile);
		assertEquals(String.format(Constants.MSG_ADD_SUCCESS, "testFile.txt", "this is something new"), results.getFeedback());
		//Unsuccessful add
		results = Controller.executeCommand("add ", testFile, archiveFile);
		assertEquals(String.format(Constants.ERROR_EMPTY_ITEM, "Task name"), results.getFeedback());
		
		//Testing delete 
		//Delete index 0 is a boundary case
		results = Controller.executeCommand("delete 0", testFile, archiveFile);
		assertEquals(Constants.MSG_DELETE_FAIL + ". ", results.getFeedback());
		//Delete index -100 
		results = Controller.executeCommand("delete -100", testFile, archiveFile);
		assertEquals(Constants.ERROR_INVALID_PARAMETER, results.getFeedback());
		Controller.executeCommand("add abc", testFile, archiveFile);
		Controller.executeCommand("add def", testFile, archiveFile);
		Controller.executeCommand("add ghi", testFile, archiveFile);
		//Delete index 10 when there are only three items.
		results = Controller.executeCommand("delete 10", testFile, archiveFile);
		assertEquals(Constants.MSG_DELETE_NON_EXISTENT.trim(), results.getFeedback());
		//Delete index 5 - successful case
		results = Controller.executeCommand("delete 5", testFile, archiveFile);
		assertEquals("Deleted '5. ghi' from your list.", results.getFeedback());
		
		//Testing sort
		results = Controller.executeCommand("sort time", testFile, archiveFile);
		assertEquals(String.format(Constants.MSG_SORT_SUCCESS, "date and time"), results.getFeedback());
		results = Controller.executeCommand("sort alpha", testFile, archiveFile);
		assertEquals(String.format(Constants.MSG_SORT_SUCCESS, "alphabetical order"), results.getFeedback());
		results = Controller.executeCommand("sort importance", testFile, archiveFile);
		assertEquals(String.format(Constants.MSG_SORT_SUCCESS, "importance level"), results.getFeedback());
		
		//Testing search- boundary case: cannot find anything
		results = Controller.executeCommand("search ghi", testFile, archiveFile);
		assertEquals(0, results.getListOfTasks().size());
		//Successful search- found two "abc"
		results = Controller.executeCommand("search abc", testFile, archiveFile);
		assertEquals(2, results.getListOfTasks().size());
		//Testing delete all
		results = Controller.executeCommand("delete all", testFile, archiveFile);
		assertEquals(Constants.MSG_CLEARED_FILE, results.getFeedback());
		//Boundary case - search empty list
		results = Controller.executeCommand("search abc", testFile, archiveFile);
		assertEquals(String.format(Constants.MSG_FOUND_N_ITEMS,0), results.getFeedback());
		
		//Testing delete multiple
		Controller.executeCommand("add abc", testFile, archiveFile);
		Controller.executeCommand("add def", testFile, archiveFile);
		results = Controller.executeCommand("delete 1 2", testFile, archiveFile);
		assertEquals("Deleted '1. abc', '2. def' from your list.", results.getFeedback());
		
		//Testing edit
		//Boundary case: wrong type of parameters
		results = Controller.executeCommand("edit abc", testFile, archiveFile);
		assertEquals(String.format(Constants.ERROR_EMPTY_ITEM,"Parameters"), results.getFeedback());
		//Successful edit
		Controller.executeCommand("add abc", testFile, archiveFile);
		results = Controller.executeCommand("edit 1 def", testFile, archiveFile);
		assertEquals(Constants.MSG_EDIT_SUCCESS, results.getFeedback());
		//Unsuccessful edit - editing item #2 when there is only one item
		results = Controller.executeCommand("edit 2 def", testFile, archiveFile);
		assertEquals(String.format(Constants.MSG_BAD_INDEX, 2 , 1 ,1), results.getFeedback());
		
		//Testing show and hide details
		results = Controller.executeCommand("show details", testFile, archiveFile);
		assertEquals(Constants.MSG_SHOW_DETAILS_SUCCESS, results.getFeedback());
		results = Controller.executeCommand("hide details", testFile, archiveFile);
		assertEquals(Constants.MSG_HIDE_DETAILS_SUCCESS, results.getFeedback());
		
		//Testing undo and redo
		results = Controller.executeCommand("undo", testFile, archiveFile);
		assertEquals(Constants.MSG_UNDO_SUCCESS, results.getFeedback());
		results = Controller.executeCommand("redo", testFile, archiveFile);
		assertEquals(Constants.MSG_REDO_SUCCESS, results.getFeedback());
		
		//Testing show all
		results = Controller.executeCommand("show all", testFile, archiveFile);
		assertEquals(Constants.MSG_SHOW_ALL_SUCCESS, results.getFeedback());
	}

}
