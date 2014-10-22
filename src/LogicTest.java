import static org.junit.Assert.*;

import org.junit.Test;
import java.io.File;



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
	String taskInputFirst = null; 
	String taskInputName = "testing Logic";
	String taskInputDate = "23/10/2014";
	String taskInputTime = "2359";
	String taskInputDetails = "yea";
	String taskInputImportance = "-1";
	String taskInputError = null;
	String taskInputParams = null;
	String splitTask[] = new String [] {taskInputFirst, taskInputName,taskInputDate, taskInputTime,
			taskInputDetails, taskInputImportance, taskInputError, taskInputParams};
	Task task = new Task(splitTask);
	
	File testFile = new File("testingStorage.txt");
	File testArchive = new File("testingArchive.txt");
	
	@Test
	public void testPrintTempStorage() {
		

		Logic.init(testFile, testArchive);
		Logic.clearContent(testFile);
		Logic.clearContent(testArchive);
		
		// testing with one item only
		Logic.add("add",task,testFile);
		assertEquals("testing of add","testing Logic 23/10/2014 2359 yea -1 0\n", Logic.printTempStorage());
		Logic.undo(testFile, testArchive);
		assertEquals("testing of undo-add one time","", Logic.printTempStorage());
		Logic.redo(testFile,testArchive);
		assertEquals("testing of redo-add one time","testing Logic 23/10/2014 2359 yea -1 0\n", Logic.printTempStorage());
		
		
		taskInputName = "testing edit";
		taskInputDate = "24/10/2014";
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
		assertEquals("testing of edit", "testing edit 24/10/2014 2119 changing items 2 0\n", Logic.printTempStorage());
		Logic.undo(testFile, testArchive);
		assertEquals("testing of undo-edit one time","testing Logic 23/10/2014 2359 yea -1 0\n", Logic.printTempStorage());
		Logic.redo(testFile,testArchive);
		assertEquals("testing of redo-edit one time", "testing edit 24/10/2014 2119 changing items 2 0\n", Logic.printTempStorage());
	}

}
