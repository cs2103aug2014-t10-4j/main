/**
 * This program implement the logic for the TextBuddy.
 * 
 */
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Logic {
	private static final String MSG_FAIL_ADD = "Unable to add line.";
	private static final String MSG_FAIL_DELETE = "Unable to delete line.";
	private static final String MSG_FAIL_EDIT = "Unable to edit line.";
	private static final String MSG_NO_PREVIOUS_ACTION = "Nothing to undo";
	private static final String MSG_NO_FUTURE_ACTION = "Nothing to redo";
	private static final String MSG_UNDO_SUCCESS = "Undo successful";
	private static final String MSG_REDO_SUCCESS = "Redo successful";
	private static final String DELETE_MESSAGE = "deleted from %s: \"%s\"";
	private static final String MSG_FAILED_SORT = "Sorting failed";
	private static final String MSG_SUCCESSFUL_SORT = "Successfully sorted by ";
	private static final String MSG_NO_TASKS_TO_SORT = "Not enough tasks to sort";
	private static final String WRONG_FORMAT = "\"%s\" is wrong format";
	private static final String BAD_INDEX_MESSAGE = "%d is not a valid number. Valid range is %d to %d.";
	public static String ADD_MESSAGE = "Added to %s: \"%s\". Type .u to undo.";
	private static final int INITIAL_VALUE = 0;
	private static final String NO_MESSAGE_DELETE = "Nothing to delete!";
	private static final int INVAILD_NUMBER = -1;
	private static final String MSG_CLEARED_FILE = "List is cleared";

	private static ArrayList<Task> tempStorage = new ArrayList<Task>();
	private static ArrayList<Task> archiveStorage = new ArrayList<Task>();
	private static ArrayList<Task> searchResults = new ArrayList<Task>();
	private static Stack<String> undo = new Stack<String>();
	private static Stack<String> redo = new Stack<String>();
	private static Stack<ArrayList<Task>> undoTask = new Stack<ArrayList<Task>>();
	private static Stack<ArrayList<Task>> redoTask = new Stack<ArrayList<Task>>();

	public static String add(String command, Task task, File file){
		String returnMessage;
		if (command.equals("add")){
			returnMessage = addLineToFile(task,file);
			redo.clear();
			redoTask.clear();

			undo.push(command);
			ArrayList<Task> addedTask = new ArrayList<Task>();
			addedTask.add(task);
			undoTask.push(addedTask);
			return returnMessage;
		}

		if (command.equals("undo") || command.equals("redo")){
			returnMessage = addLineToFile(task,file);
			return returnMessage;
		}

		else{
			return MSG_FAIL_ADD;
		}
	}

	public static String addLineToFile(Task task, File file) {
		if (task.getName() == null) {
			return "error";
		}
		tempStorage.add(task);
		sortByDateAndTime(tempStorage);
		Storage.writeToFile(tempStorage, file);
		return String.format(ADD_MESSAGE, file.getName(), task.getName());
	}

	public static String delete(String command, Task task, File file, File archive){
		String returnMessage;
		if (command.equals("delete") || command.equals(redo)){
			if(undo.size()!= 0 && undo.peek().equals("search")){
				System.out.println("here it is");
				returnMessage = deleteLineFromSearchList(task,searchResults,file,archive);
				undo.push(command);
				ArrayList<Task> deletedTask = new ArrayList<Task>();
				deletedTask.add(task);
				undoTask.push(deletedTask);
				return returnMessage;
			}
			else{
				returnMessage = deleteLineFromFile(task,file,archive);
				if(command.equals(redo)){
					return returnMessage;
				}
				else{
					undo.push(command);
					ArrayList<Task> deletedTask = new ArrayList<Task>();
					deletedTask.add(task);
					undoTask.push(deletedTask);
					return returnMessage;
				}
			}
		}

		else if (command.equals("undo")){
			returnMessage = deleteLineFromFile(task,file,archive);
			archiveStorage.remove(archiveStorage.indexOf(task));
			Storage.writeToFile(archiveStorage, archive);
			return returnMessage;
		}
		else{
			return MSG_FAIL_DELETE;
		}
	}

	public static String deleteLineFromFile(Task task, File file,File archive) {
		if (tempStorage.size() == 0) {
			return NO_MESSAGE_DELETE;
		}
		int index = getIndex(task);
		return removeText(index, task, file, archive);
	}

	public static int getIndex(Task task) {
		try {
			return Integer.parseInt(task.getParams()) - 1;
		} catch (NumberFormatException e) {
			System.out.println(String.format(WRONG_FORMAT, task.getParams()));
		}
		return INVAILD_NUMBER;
	}

	public static String removeText(int index, Task task, File file, File archive) {
		if (index == INVAILD_NUMBER) {
			return NO_MESSAGE_DELETE;
		}
		try {
			String temp = String.format(DELETE_MESSAGE, file.getName(),
					tempStorage.get(index).getName());
			archiveStorage.add(tempStorage.remove(index));
			sortByDateAndTime(tempStorage);
			sortByDateAndTime(archiveStorage);
			Storage.writeToFile(tempStorage, file);
			Storage.writeToFile(archiveStorage, archive);
			return temp;
		} catch (IndexOutOfBoundsException e) {
			return String.format(BAD_INDEX_MESSAGE, index + 1, 1,
					tempStorage.size());
		}
	}

	public static ArrayList<Task> search(Task taskToFind) {
		searchResults.clear();
		assert tempStorage.size() >= 0 : "tempStorage.size() is negative";
		for (int i = 0; i < tempStorage.size(); i++) {
			Task taskInList = tempStorage.get(i);
			//Filtering name
			if (taskToFind.getName() != null && taskInList.getName() != null 
					&& ! taskInList.getName().toLowerCase().contains(taskToFind.getName().toLowerCase())) {
				continue;
			}
			if (taskToFind.getDate() != null && taskInList.getDate() != null 
					&& !taskInList.getDate().equals(taskToFind.getDate()) ) {
				continue;
			}
			if (taskToFind.getDate() != null && taskInList.getDate() == null ){
				continue;
			}
			if (taskToFind.getTime() != null && taskInList.getTime() != null 
					&& ! taskInList.getTime().equals(taskToFind.getTime())) {
				continue;
			}
			//Because the one above will short circuit
			if (taskToFind.getTime() != null && taskInList.getTime() == null ){
				continue;
			}
			if (taskToFind.getDetails() != null && taskInList.getDetails() != null 
					&& !taskInList.getDetails().toLowerCase()
					.contains(taskToFind.getDetails().toLowerCase())) {
				continue;
			}
			searchResults.add(taskInList);
		}
		undo.push("search");
		return searchResults;
	}

	public static String deleteLineFromSearchList(Task task,
			ArrayList<Task> searchResults, File file, File archive) {
		if (searchResults.size() == 0) {
			return NO_MESSAGE_DELETE;
		}
		int index = getIndex(task);
		String name = searchResults.get(index).getName()
				+searchResults.get(index).getDate() +searchResults.get(index).getTime()
				+searchResults.get(index).getDetails()+searchResults.get(index).getImportance()
				+searchResults.get(index).getError() + searchResults.get(index).getParams();

		for (int i = 0; i < tempStorage.size(); i++) {
			String currentTask = tempStorage.get(i).getName()
					+tempStorage.get(i).getDate()+tempStorage.get(i).getTime()
					+tempStorage.get(i).getDetails()+tempStorage.get(i).getImportance()
					+tempStorage.get(i).getError() + tempStorage.get(i).getParams();
			if (currentTask.equals(name)) {
				System.out.println("does it reach here");
				archiveStorage.add(tempStorage.remove(i));
				sortByDateAndTime(tempStorage);
				sortByDateAndTime(archiveStorage);
				Storage.writeToFile(tempStorage, file);
				Storage.writeToFile(archiveStorage, archive);
			}
		}
		return String.format(DELETE_MESSAGE, file.getName(), searchResults.get(index).getName());

	}

	// for delete from searched list of tasks.
	// step 1 search from tempStorage display all the lists of tasks.
	// step 2 add these tasks one by one to the other temp storage(memory).
	// step3 get the contain for the delete task. delete the task use equals .

	public static String clearContent(File file) { //Changed by delvin, your file was not initialized without the parameters
		tempStorage.clear();
		Storage.writeToFile(new ArrayList<Task>(), file); //Changed by delvin. Using null will cause nullPointException.
		return MSG_CLEARED_FILE;
	}


	public static ArrayList<Integer> init(File file, File archive) {
		Storage.copyToArrayList(file, tempStorage);
		Storage.copyToArrayList(archive, archiveStorage); 
		ArrayList<Integer> numTask = new ArrayList<Integer>();
		getNumTasks(numTask, tempStorage);

		return numTask;
	}

	public static ArrayList<Task> getTempStorage(){
		return tempStorage;
	}

	private static void getNumTasks(ArrayList<Integer> numTask,
			ArrayList<Task> tempStorage) {
		// Need to ensure correct format of date (as returned by parser) is used
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date currentDate = new Date();
		try {
			currentDate = dateFormat.parse(dateFormat.format(currentDate));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		assert currentDate != null;
		try {
			int todayTask = 0;
			int overdueTask = 0;
			int tomorrowTask = 0;
			int floatingTask = 0;
			for (int i = 0; i < tempStorage.size(); i++) {
				if (tempStorage.get(i).getDate().contains("ft")) {
					floatingTask ++;
				} else {
					Date dateOfCurrentTask = dateFormat.parse(tempStorage.get(i).getDate());
					if (dateOfCurrentTask.compareTo(currentDate) == 0) {
						todayTask++;
					} else if (dateOfCurrentTask.compareTo(currentDate) == 1) {
						tomorrowTask++;
					} else {
						overdueTask++;						
					}
				}
			}
			numTask.add(todayTask);
			numTask.add(overdueTask);
			numTask.add(tomorrowTask);
			numTask.add(floatingTask);
		} catch (Exception e) {

		}
	}

	public static String sortByDateAndTime(ArrayList<Task> tempStorage){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat timeFormat = new SimpleDateFormat("HHmm");
		dateFormat.setLenient(false);
		timeFormat.setLenient(false);

		if(tempStorage.size()<1){
			return MSG_NO_TASKS_TO_SORT;
		}
		else{
			for (int i=0;i<tempStorage.size();i++){
				
				for(int j=0;j<tempStorage.size()-1;j++){
					try{
						if (tempStorage.get(j).getDate().equals("ft")&& !tempStorage.get(j+1).getDate().equals("ft")){
							tempStorage.add(j+2,tempStorage.get(j));
							tempStorage.remove(j);
						
						} else if (!tempStorage.get(j).getDate().equals("ft") && tempStorage.get(j+1).getDate().equals("ft")){
							continue;

						}
						else{

							Date dateOfFirstTask = new Date();

							dateOfFirstTask = dateFormat.parse(tempStorage.get(j).getDate());

							Date dateOfSecondTask = new Date();
							dateOfSecondTask = dateFormat.parse(tempStorage.get(j + 1).getDate());

							if (dateOfFirstTask.compareTo(dateOfSecondTask) > 0) {
								tempStorage.add(j + 2, tempStorage.get(j));
								tempStorage.remove(j);
							


							} else if (dateOfFirstTask.compareTo(dateOfSecondTask) == 0) {
								if (tempStorage.get(j).getTime()==null) {	
									continue;

								} else if (tempStorage.get(j).getTime() !=null
										&& tempStorage.get(j + 1).getTime() ==null) {
									tempStorage.add(j + 2, tempStorage.get(j));
									tempStorage.remove(j);
		

								} else {
									Date timeOfFirstTask = new Date();

									timeOfFirstTask = timeFormat
											.parse(tempStorage.get(j).getTime());

									Date timeOfSecondTask = new Date();
									timeOfSecondTask = timeFormat
											.parse(tempStorage.get(j + 1)
													.getTime());


									if (timeOfFirstTask
											.compareTo(timeOfSecondTask) > 0) {
										tempStorage.add(j + 2,
												tempStorage.get(j));
										tempStorage.remove(j);
									

									}
									if (timeOfFirstTask
											.compareTo(timeOfSecondTask) == 0) {
										continue;
									}
								}
							}
						}
					} catch(Exception e){
					}
				
				
				}

			
			}
			return MSG_SUCCESSFUL_SORT + "date and time";
		}
		
	}


	public static String sortByAlphabet(ArrayList<Task> tempStorage) {
		if (tempStorage.size() < 1) {
			return MSG_NO_TASKS_TO_SORT;
		} else {
			for (int i = 0; i < tempStorage.size(); i++) {
				boolean isSorted = true;
				for (int j = 0; j < tempStorage.size() - 1; j++) {
					if (tempStorage
							.get(j)
							.getName()
							.compareToIgnoreCase(
									tempStorage.get(j + 1).getName()) > 0) {
						tempStorage.add(j + 2, tempStorage.get(j));
						tempStorage.remove(j);
						isSorted = false;
					}
				}
				if (isSorted) {
					return MSG_SUCCESSFUL_SORT + "alphabetical order.";
				}
			}
			return MSG_FAILED_SORT;
		}
	}

	public static String sortByImportance(ArrayList<Task> tempStorage) {
		if (tempStorage.size() < 1) {
			return MSG_NO_TASKS_TO_SORT;
		} else {
			for (int i = 0; i < tempStorage.size(); i++) {
				boolean isSorted = true;
				for (int j = 0; j < tempStorage.size() - 1; j++) {
					if (tempStorage.get(j).getImportance() < tempStorage.get(
							j + 1).getImportance()) {
						tempStorage.add(j + 2, tempStorage.get(j));
						tempStorage.remove(j);
						isSorted = false;
					}
				}
				if (isSorted) {
					return MSG_SUCCESSFUL_SORT + "importance level.";
				}
			}
		}
		return MSG_FAILED_SORT;
	}

	// First task to store in the ArrayList undoTask is the original task before editing
	// Second task to store is the task after editing
	public static String edit(String command, Task detailsOfTask, File file) {
		String returnMessage;
		if (command.equals("edit")){
			int taskNumber = getIndex(detailsOfTask);
			ArrayList<Task> taskEdited = new ArrayList<Task>();
			taskEdited.add(tempStorage.get(taskNumber));
			returnMessage = editTask(detailsOfTask, file, taskNumber);
			taskEdited.add(tempStorage.get(taskNumber));

			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			undo.push(command);
			undoTask.push(taskEdited);

			return returnMessage;
		}
		else if (command.equals("undo")||command.equals(redo)){
			int taskNumber = getIndex(detailsOfTask);
			returnMessage = editTask(detailsOfTask, file, taskNumber);
			return returnMessage;
		}

		else{
			return MSG_FAIL_EDIT;
		}

	}
	public static String editTask(Task detailsOfTask, File file, int taskNumber) {

		if (detailsOfTask.getName() != null) {
			tempStorage.get(taskNumber).setName(detailsOfTask.getName());
		}
		if (detailsOfTask.getDate() !=null) {
			tempStorage.get(taskNumber).setDate(detailsOfTask.getDate());
		}
		System.out.println(detailsOfTask.getTime());
		if (detailsOfTask.getTime()!=null) {
			tempStorage.get(taskNumber).setTime(detailsOfTask.getTime());
		}
		if (detailsOfTask.getDetails() != null) {
			tempStorage.get(taskNumber).setDetails(detailsOfTask.getDetails());
		}

		if (detailsOfTask.getImportance() != INITIAL_VALUE - 1) {
			tempStorage.get(taskNumber).setImportance(
					detailsOfTask.getImportance());
		}

		return "success";

	}

	public static String undo(File file, File archive){
		if (undo.empty()){
			return MSG_NO_PREVIOUS_ACTION;
		}
		else{
			String command = "undo";
			String lastCommand = undo.pop();

			if(lastCommand.equals("add")){
				ArrayList<Task> taskToBeDeleted = new ArrayList<Task>();
				taskToBeDeleted = undoTask.pop();

				Integer taskNumber = tempStorage.indexOf(taskToBeDeleted.get(INITIAL_VALUE))+ 1;
				taskToBeDeleted.get(INITIAL_VALUE).setParams(taskNumber.toString());

				delete(command,taskToBeDeleted.get(INITIAL_VALUE),file,archive);
				redo.push("add");
				redoTask.push(taskToBeDeleted);

			}

			if(lastCommand.equals("delete")){
				ArrayList<Task> taskToBeAdded = new ArrayList<Task>();
				taskToBeAdded = undoTask.pop();

				for(int i=0;i<taskToBeAdded.size();i++){
					add(command,taskToBeAdded.get(i),file);
				}
				for(int i=INITIAL_VALUE;i<taskToBeAdded.size();i++){
					Integer taskNumber = tempStorage.indexOf(taskToBeAdded.get(i))+ 1;
					taskToBeAdded.get(i).setParams(taskNumber.toString());
				}
				redo.push("delete");
				redoTask.push(taskToBeAdded);

			}

			if(lastCommand.equals("edit")){
				ArrayList<Task> taskToBeEdited = new ArrayList<Task>();
				taskToBeEdited = undoTask.pop();

				Integer taskNumber = tempStorage.indexOf(taskToBeEdited.get(INITIAL_VALUE+1))+ 1;
				taskToBeEdited.get(INITIAL_VALUE).setParams(taskNumber.toString());

				edit(command,taskToBeEdited.get(INITIAL_VALUE),file);
				redo.push("edit");
				taskToBeEdited.add(taskToBeEdited.remove(INITIAL_VALUE));
				redoTask.push(taskToBeEdited);

			}
			return MSG_UNDO_SUCCESS;
		}

	}

	public static String redo(File file, File archive){
		if (redo.empty()){
			return MSG_NO_PREVIOUS_ACTION;
		}
		else{
			String command = "redo";
			String lastCommand = redo.pop();
			if(lastCommand.equals("add")){

				ArrayList<Task> taskToBeAdded = new ArrayList<Task>();
				taskToBeAdded = redoTask.pop();
				add(command,taskToBeAdded.get(INITIAL_VALUE),file);

				undo.push("add");
				undoTask.push(taskToBeAdded);

			}

			if(lastCommand.equals("delete")){
				ArrayList<Task> taskToBeDeleted = new ArrayList<Task>();
				taskToBeDeleted = redoTask.pop();

				for(int i=0;i<taskToBeDeleted.size();i++){
					add(command,taskToBeDeleted.get(i),file);
				}
				delete(command,taskToBeDeleted.get(INITIAL_VALUE),file,archive);
				undo.push("delete");
				undoTask.push(taskToBeDeleted);
			}

			if(lastCommand.equals("edit")){
				ArrayList<Task> taskToBeEdited = new ArrayList<Task>();
				taskToBeEdited = undoTask.pop();

				Integer taskNumber = tempStorage.indexOf(taskToBeEdited.get(INITIAL_VALUE+1))+ 1;
				taskToBeEdited.get(INITIAL_VALUE).setParams(taskNumber.toString());

				edit(command,taskToBeEdited.get(INITIAL_VALUE),file);
				redo.push("edit");
				taskToBeEdited.add(taskToBeEdited.remove(INITIAL_VALUE));
				redoTask.push(taskToBeEdited);

			}
			return MSG_UNDO_SUCCESS;
		}

	}
}

