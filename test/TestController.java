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
		results = Controller.executeCommand("wahaha", testFile, archiveFile);
		assertEquals(String.format(Constants.MSG_ADD_SUCCESS, "testFile.txt", "wahaha"), results.getFeedback());
		//Unsuccessful add
		results = Controller.executeCommand("add ", testFile, archiveFile);
		assertEquals(String.format(Constants.ERROR_EMPTY_ITEM, "Task name"), results.getFeedback());
		
		//Testing delete 
		//0 is a boundary case
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
	}

}
