import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;


public class TestController {
	private static final String MSG_ADD_SUCCESS = "Added to %s: \"%s\". Type .u to undo.";
	private static final String MSG_ADD_FAIL = "Unable to add line";
	private static final String MSG_CNT_DELETE_ZERO = "Cannot delete index equal or below 0.";
	private static final String MSG_ITEM_TO_DELETE_NOT_FOUND = "Item #%d is not found.";
	private static final String MSG_DELETE_SUCCESS = "Deleted from your list: \"%s\".";
	private static final String MSG_SORT_FAIL = "Sorting failed";
	private static final String MSG_SORT_SUCCESS = "Successfully sorted by %s.";
	private static final String MSG_NO_TASKS_TO_SORT = "Not enough tasks to sort";
	private static final String MSG_CLEAR_FILE_SUCCESS = "List is cleared";
	private static final String MSG_FOUND_N_ITEMS = "Found %d items.";
	private static File testFile = new File("testFile.txt");
	private static File archiveFile = new File("testArchive.txt");

	@Test
	public void testExecuteCommand() {
		String[] splitCommand = {"A", "24/12/2014", "1900", "details", "3", 
				null , null};
		//Testing add
		//Successful with command
		ResultOfCommand results = Controller.executeCommand("add abc", testFile, archiveFile);
		assertEquals(String.format(MSG_ADD_SUCCESS, "testFile.txt", "abc"), results.getFeedback());
		//Successful without command
		results = Controller.executeCommand("wahaha", testFile, archiveFile);
		assertEquals(String.format(MSG_ADD_SUCCESS, "testFile.txt", "wahaha"), results.getFeedback());
		//Unsuccessful
		//results = Controller.executeCommand("add ", testFile, archiveFile);
		//assertEquals(String.format(Constants.EMPTY_TASKNAME_MSG, "testFile.txt", "abc"), results.getFeedback());
		
		//Testing delete 
		//0 is a boundary case
		results = Controller.executeCommand("delete 0", testFile, archiveFile);
		assertEquals(MSG_CNT_DELETE_ZERO, results.getFeedback());
		//Delete index -100 
		results = Controller.executeCommand("delete -100", testFile, archiveFile);
		assertEquals(MSG_CNT_DELETE_ZERO, results.getFeedback());
		Controller.executeCommand("add abc", testFile, archiveFile);
		Controller.executeCommand("add def", testFile, archiveFile);
		Controller.executeCommand("add ghi", testFile, archiveFile);
		//Delete index 10 when there are only three items.
		results = Controller.executeCommand("delete 10", testFile, archiveFile);
		assertEquals(String.format(MSG_ITEM_TO_DELETE_NOT_FOUND, 10), results.getFeedback());
		//Delete index 5 - successful case
		results = Controller.executeCommand("delete 5", testFile, archiveFile);
		assertEquals(String.format(MSG_DELETE_SUCCESS, "ghi"), results.getFeedback());
		
		//Testing sort
		results = Controller.executeCommand("sort time", testFile, archiveFile);
		assertEquals(String.format(MSG_SORT_SUCCESS, "date and time"), results.getFeedback());
		results = Controller.executeCommand("sort alpha", testFile, archiveFile);
		assertEquals(String.format(MSG_SORT_SUCCESS, "alphabetical order"), results.getFeedback());
		results = Controller.executeCommand("sort importance", testFile, archiveFile);
		assertEquals(String.format(MSG_SORT_SUCCESS, "importance level"), results.getFeedback());
		
		//Testing search- boundary case: cannot find anything
		results = Controller.executeCommand("search ghi", testFile, archiveFile);
		assertEquals(0, results.getListOfTasks().size());
		//Successful search- found two "abc"
		results = Controller.executeCommand("search abc", testFile, archiveFile);
		assertEquals(2, results.getListOfTasks().size());
		//Testing delete all
		results = Controller.executeCommand("delete all", testFile, archiveFile);
		assertEquals(MSG_CLEAR_FILE_SUCCESS, results.getFeedback());
		//Boundary case - search empty list
		results = Controller.executeCommand("search abc", testFile, archiveFile);
		assertEquals(String.format(MSG_FOUND_N_ITEMS,0), results.getFeedback());
	}

}
