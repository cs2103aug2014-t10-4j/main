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
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final String MSG_FAIL_ADD = "Unable to add line";
	private static final String MSG_FAIL_DELETE = "Unable to delete line";
	private static final String MSG_FAIL_EDIT = "Unable to edit line.";
	private static final String MSG_NO_PREVIOUS_ACTION = "Nothing to undo";
	private static final String MSG_NO_FUTURE_ACTION = "Nothing to redo";
	private static final String MSG_UNDO_SUCCESS = "Undo successful";
	private static final String MSG_REDO_SUCCESS = "Redo successful";
	private static final String MSG_DELETE_SUCCESS = "deleted from your list: \"%s\"";
	private static final String MSG_SORT_FAILED = "Sorting failed";
	private static final String MSG_SORT_SUCCESS = "Successfully sorted by ";
	private static final String MSG_NO_TASKS_TO_SORT = "Not enough tasks to sort";
	private static final String WRONG_FORMAT = "\"%s\" is wrong format";
	private static final String BAD_INDEX_MESSAGE = "%d is not a valid number to delete because valid range is %d to %d";
	public static String ADD_MESSAGE = "Added to %s: \"%s\". Type .u to undo.";
	private static final int INITIAL_VALUE = 0;
	private static final String NO_MESSAGE_DELETE = "nothing to delete";
	private static final int INVAILD_NUMBER = -1;
	private static final String MSG_CLEARED_FILE = "List is cleared";
	private static final String NO_MESSAGE_CLEAR = "Nothing to clear!";

	private static ArrayList<Task> tempStorage = new ArrayList<Task>();
	private static ArrayList<Task> archiveStorage = new ArrayList<Task>();
	private static ArrayList<Task> searchResults = new ArrayList<Task>();
	private static Stack<String> undo = new Stack<String>();
	private static Stack<String> redo = new Stack<String>();
	private static Stack<ArrayList<Task>> undoTask = new Stack<ArrayList<Task>>();
	private static Stack<ArrayList<Task>> redoTask = new Stack<ArrayList<Task>>();

	public static String add(String command, Task task, File file) {
		String returnMessage;
		if(undo.size()!= 0 && undo.peek().equals("search")){
			undo.pop();
		}
		if (command.equals("add")){
			Integer taskNumber = tempStorage.size();
			task.setParams(taskNumber.toString());
			returnMessage = addLineToFile(task,file,taskNumber);
			redo.clear();
			redoTask.clear();
			undo.push(command);
			ArrayList<Task> addedTask = new ArrayList<Task>();
			addedTask.add(task);
			undoTask.push(addedTask);
			return returnMessage;
		}
		if (command.equals("undo")){
			returnMessage = addLineToFile(task,file,getIndex(task)+1);
			return returnMessage;
		}
		if (command.equals("redo")){
			returnMessage = addLineToFile(task,file,getIndex(task));
			return returnMessage;
		} else {
			return MSG_FAIL_ADD;
		}
	}

	private static String addLineToFile(Task task, File file, int taskLocation) {
		if (task.getName() == null) {
			return "error";
		}
		tempStorage.add(taskLocation, task);
		sortByDateAndTime(tempStorage);
		Storage.writeToFile(tempStorage, file);
		return String.format(ADD_MESSAGE, file.getName(), task.getName());
	}

	public static String delete(String command, int numOfTaskToDelete, Task task, File file, File archive){
		String returnMessage;
		if (tempStorage.size() == 0) {
			return NO_MESSAGE_DELETE;
		} else if (command.equals("delete") || command.equals("redo")){
			String commandCheck;
			if(undo.size()!=0){
				if (undo.peek().equals("deleting process")){
					commandCheck= undo.pop();
				} else{
					commandCheck = null;
				}
			} else{
				commandCheck = null;
			}
			
			if(undo.size()!= 0 && undo.peek().equals("search")){
				if (commandCheck != null ){
					undo.push(commandCheck);
				}

				int index = getIndex(task);
				Task taskToDelete = new Task();
				taskToDelete.copyOfTask(searchResults.get(index));
				
				returnMessage = deleteLineFromSearchList(task,searchResults,file,archive);

				if(undo.empty() || !undo.peek().equals("deleting process")){
					ArrayList<Task> deletedTask = new ArrayList<Task>();
					deletedTask.add(taskToDelete);
					
					assert(deletedTask.size() <= numOfTaskToDelete);
					if(deletedTask.size()== numOfTaskToDelete){
						if(undo.peek().equals("search")){
							undo.pop();
						}
						undo.push(command);
						undoTask.push(deletedTask);
						redo.clear();
						redoTask.clear();
						
						return returnMessage;
					}
					else if(deletedTask.size() < numOfTaskToDelete){
						undo.push("deleting process");
						undoTask.push(deletedTask);
						return returnMessage;
					}
				}
				else if(undo.peek().equals("deleting process")){
					ArrayList<Task> deletedTask = undoTask.pop();
					deletedTask.add(taskToDelete);
					
					assert(deletedTask.size() <= numOfTaskToDelete);
					if(deletedTask.size()== numOfTaskToDelete){
						undo.pop();
						if(undo.peek().equals("search")){
							undo.pop();
						}
						undo.push(command);
						undoTask.push(deletedTask);
						redo.clear();
						redoTask.clear();
					}
					else if(deletedTask.size() < numOfTaskToDelete){
						undoTask.push(deletedTask);
						return returnMessage;
					}
					

				}
			}
			else{
				
				if (commandCheck != null){
					undo.push(commandCheck);
				}
				int index = getIndex(task);
				Task taskToDelete = new Task();
				taskToDelete.copyOfTask(tempStorage.get(index));
				returnMessage = deleteLineFromFile(index,task,file,archive);
				if(command.equals("redo")){
					return returnMessage;
				}
				else{
					if(undo.empty() || !undo.peek().equals("deleting process")){
						ArrayList<Task> deletedTask = new ArrayList<Task>();
						deletedTask.add(taskToDelete);
						
						assert(deletedTask.size() <= numOfTaskToDelete);
						if(deletedTask.size()== numOfTaskToDelete){
							undo.push(command);
							undoTask.push(deletedTask);
							redo.clear();
							redoTask.clear();
							return returnMessage;
						}
						else if(deletedTask.size() < numOfTaskToDelete){
							undo.push("deleting process");
							undoTask.push(deletedTask);
							return returnMessage;
						}
					}
					else if(undo.peek().equals("deleting process")){
						ArrayList<Task> deletedTask = undoTask.pop();
						deletedTask.add(taskToDelete);
						
						assert(deletedTask.size() <= numOfTaskToDelete);
						if(deletedTask.size()== numOfTaskToDelete){
							undo.pop();
							undo.push(command);
							undoTask.push(deletedTask);
							redo.clear();
							redoTask.clear();
						}
						else if(deletedTask.size() < numOfTaskToDelete){
							undoTask.push(deletedTask);
							return returnMessage;
						}
					}

				}
			}
		}

		else if (command.equals("undo")){
			int index = getIndex(task)+1;
			returnMessage = deleteLineFromFile(index, task,file,archive);
			archiveStorage.remove(archiveStorage.indexOf(task));
			Storage.writeToFile(archiveStorage, archive);
			

			
			return returnMessage;
		}

		else{
			return MSG_FAIL_DELETE;
		}
		return MSG_FAIL_DELETE;
	}

	private static int getIndex(Task task) {
		try {
			return Integer.parseInt(task.getParams()) - 1;
		} catch (NumberFormatException e) {
			System.out.println(String.format(WRONG_FORMAT, task.getParams()));
		}
		return INVAILD_NUMBER;
	}

	private static String deleteLineFromFile(int index, Task task, File file, File archive) {
		if (index == INVAILD_NUMBER) {
			return NO_MESSAGE_DELETE;
		}
		try {
			String temp = String.format(MSG_DELETE_SUCCESS, tempStorage.get(index).getName());
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
			// Filtering name
			if (taskToFind.getName() != null
					&& taskInList.getName() != null
					&& !taskInList.getName().toLowerCase()
							.contains(taskToFind.getName().toLowerCase())) {
				continue;
			}
			if (taskToFind.getDate() != null && taskInList.getDate() != null
					&& !taskInList.getDate().equals(taskToFind.getDate())) {
				continue;
			}
			if (taskToFind.getDate() != null && taskInList.getDate() == null) {
				continue;
			}
			if (taskToFind.getTime() != null && taskInList.getTime() != null
					&& !taskInList.getTime().equals(taskToFind.getTime())) {
				continue;
			}
			// Because the one above will short circuit
			if (taskToFind.getTime() != null && taskInList.getTime() == null) {
				continue;
			}
			if (taskToFind.getDetails() != null
					&& taskInList.getDetails() != null
					&& !taskInList.getDetails().toLowerCase()
							.contains(taskToFind.getDetails().toLowerCase())) {
				continue;
			}
			searchResults.add(taskInList);
		}
		undo.push("search");
		return searchResults;
	}

	private static String deleteLineFromSearchList(Task task,
			ArrayList<Task> searchResults, File file, File archive) {
		if (searchResults.size() == 0) {
			return NO_MESSAGE_DELETE;
		}
		int index = getIndex(task);
		String name = searchResults.get(index).getName()
				+ searchResults.get(index).getDate()
				+ searchResults.get(index).getTime()
				+ searchResults.get(index).getDetails()
				+ searchResults.get(index).getImportance()
				+ searchResults.get(index).getError()
				+ searchResults.get(index).getParams();

		for (int i = 0; i < tempStorage.size(); i++) {
			String currentTask = tempStorage.get(i).getName()
					+ tempStorage.get(i).getDate()
					+ tempStorage.get(i).getTime()
					+ tempStorage.get(i).getDetails()
					+ tempStorage.get(i).getImportance()
					+ tempStorage.get(i).getError()
					+ tempStorage.get(i).getParams();
			if (currentTask.equals(name)) {

				archiveStorage.add(tempStorage.remove(i));
				sortByDateAndTime(tempStorage);
				sortByDateAndTime(archiveStorage);
				Storage.writeToFile(tempStorage, file);
				Storage.writeToFile(archiveStorage, archive);
			}
		}
		return String.format(MSG_DELETE_SUCCESS, searchResults.get(index).getName());

	}

	// for delete from searched list of tasks.
	// step 1 search from tempStorage display all the lists of tasks.
	// step 2 add these tasks one by one to the other temp storage(memory).
	// step3 get the contain for the delete task. delete the task use equals .

	public static String clearContent(File file) {
		if(tempStorage.size()>0){
		ArrayList<Task> deletedTask = new ArrayList<Task>();
		for(int i =0; i<tempStorage.size(); i++){
			Task taskToDelete = new Task();	
			taskToDelete.copyOfTask( tempStorage.get(i));
			deletedTask.add(taskToDelete);
		}
		
		tempStorage.clear();
		Storage.writeToFile(new ArrayList<Task>(), file); 
		undo.push("clear all");
		undoTask.push(deletedTask);

		return MSG_CLEARED_FILE;
		}
		else{
			return NO_MESSAGE_CLEAR;
		}
	}

	public static ArrayList<Integer> init(File file, File archive) {
		while(!Storage.copyToArrayList(file, tempStorage));
		while(!Storage.copyToArrayList(archive, archiveStorage)); 
		ArrayList<Integer> numTask = new ArrayList<Integer>();
		getNumTasks(numTask, tempStorage);
		return numTask;
	}

	public static ArrayList<Task> getTempStorage() {
		return tempStorage;
	}

	private static void getNumTasks(ArrayList<Integer> numTask, ArrayList<Task> tempStorage) {
		// Need to ensure correct format of date (as returned by parser) is used
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		Date currentDate = new Date();
		try {
			currentDate = dateFormat.parse(dateFormat.format(currentDate));
		} catch (ParseException e1) {
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
					floatingTask++;
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

	public static String sortByDateAndTime(ArrayList<Task> tempStorage) {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		DateFormat timeFormat = new SimpleDateFormat("HHmm");
		dateFormat.setLenient(false);
		timeFormat.setLenient(false);
		if (tempStorage.size() < 1) {
			return MSG_NO_TASKS_TO_SORT;
		} else {
			for (int i = 0; i < tempStorage.size(); i++) {
				for (int j = 0; j < tempStorage.size() - 1; j++) {
					try {
						if (tempStorage.get(j).getDate().equals("ft")
								&& !tempStorage.get(j + 1).getDate()
										.equals("ft")) {
							tempStorage.add(j + 2, tempStorage.get(j));
							tempStorage.remove(j);

						} else if (!tempStorage.get(j).getDate().equals("ft")
								&& tempStorage.get(j + 1).getDate()
										.equals("ft")) {
							continue;
						} else {
							Date dateOfFirstTask = new Date();
							dateOfFirstTask = dateFormat.parse(tempStorage.get(
									j).getDate());
							Date dateOfSecondTask = new Date();
							dateOfSecondTask = dateFormat.parse(tempStorage
									.get(j + 1).getDate());
							if (dateOfFirstTask.compareTo(dateOfSecondTask) > 0) {
								tempStorage.add(j + 2, tempStorage.get(j));
								tempStorage.remove(j);
							} else if (dateOfFirstTask
									.compareTo(dateOfSecondTask) == 0) {
								if (tempStorage.get(j).getTime() == null) {
									continue;
								} else if (tempStorage.get(j).getTime() != null
										&& tempStorage.get(j + 1).getTime() == null) {
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
					} catch (Exception e) {
					}
				}
			}
			return MSG_SORT_SUCCESS + "date and time";
		}
	}

	public static String sortByAlphabet(ArrayList<Task> tempStorage) {
		if (tempStorage.size() < 1) {
			return MSG_NO_TASKS_TO_SORT;
		} else {
			for (int i = 0; i < tempStorage.size(); i++) {
				boolean isSorted = true;
				for (int j = 0; j < tempStorage.size() - 1; j++) {
					if (tempStorage.get(j).getName().compareToIgnoreCase(
									tempStorage.get(j + 1).getName()) > 0) {
						tempStorage.add(j + 2, tempStorage.get(j));
						tempStorage.remove(j);
						isSorted = false;
					}
				}
				if (isSorted) {
					return MSG_SORT_SUCCESS + "alphabetical order.";
				}
			}
			return MSG_SORT_FAILED;
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
					return MSG_SORT_SUCCESS + "importance level.";
				}
			}
		}
		return MSG_SORT_FAILED;
	}

	// First task to store in the ArrayList undoTask is the original task before
	// editing
	// Second task to store is the task after editing
	public static String edit(String command, Task detailsOfTask, File file) {
		String returnMessage;
		if(undo.size()!= 0 && undo.peek().equals("search")){
			undo.pop();
		}
		if (command.equals("edit")){
			int taskNumber = getIndex(detailsOfTask);
			ArrayList<Task> tasksEdited = new ArrayList<Task>();
			Task originalTask = new Task();
			originalTask.copyOfTask(tempStorage.get(taskNumber));
			tasksEdited.add(originalTask);
			
			
			returnMessage = editTask(detailsOfTask, file, taskNumber);
			Task editedTask = tempStorage.get(taskNumber);

			tasksEdited.add(editedTask);

			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			undo.push(command);
			undoTask.push(tasksEdited);
			
			redo.clear();
			redoTask.clear();

			return returnMessage;
		}
		else if (command.equals("undo")){
			int taskNumber = getIndex(detailsOfTask);
			returnMessage = editTask(detailsOfTask, file, taskNumber);
			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			return returnMessage;
		}
		else if (command.equals("redo")){
			int taskNumber = getIndex(detailsOfTask)+1;
			returnMessage = editTask(detailsOfTask, file, taskNumber);
			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			return returnMessage;
		}

		else {
			return MSG_FAIL_EDIT;
		}

	}
	
	private static String editTask(Task detailsOfTask, File file, int taskNumber) {
		if (detailsOfTask.getName() != null) {
			tempStorage.get(taskNumber).setName(detailsOfTask.getName());
		}
		if (detailsOfTask.getDate() != null) {
			tempStorage.get(taskNumber).setDate(detailsOfTask.getDate());
		}
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

	public static String undo(File file, File archive) {
		if (undo.empty()) {
			return MSG_NO_PREVIOUS_ACTION;
		} else {
			String command = "undo";
			String lastCommand = undo.pop();
			if (lastCommand.equals("add")) {
				ArrayList<Task> taskToBeDeleted = new ArrayList<Task>();
				taskToBeDeleted = undoTask.pop();
				delete(command,taskToBeDeleted.size(),taskToBeDeleted.get(INITIAL_VALUE),file,archive);
				redo.push(lastCommand);
				redoTask.push(taskToBeDeleted);
			}
			if(lastCommand.equals("delete") || lastCommand.equals("clear all")){
				ArrayList<Task> taskToBeAdded = new ArrayList<Task>();
				taskToBeAdded = undoTask.pop();
				for (int i = 0; i < taskToBeAdded.size(); i++) {
					boolean isSorted = true;
					for (int j = 0; j < taskToBeAdded.size() - 1; j++) {
						if (Integer.parseInt(taskToBeAdded.get(j).getParams()) > Integer.parseInt(taskToBeAdded.get(
								j + 1).getParams())) {
							taskToBeAdded.add(j + 2, taskToBeAdded.get(j));
							taskToBeAdded.remove(j);
							isSorted = false;
						}
					}
					if (isSorted) {
					}
				}
				
				for(int i=0;i<taskToBeAdded.size();i++){
					add(command,taskToBeAdded.get(i),file);
				}
				redo.push(lastCommand);
				redoTask.push(taskToBeAdded);
			}

			if (lastCommand.equals("edit")) {
				ArrayList<Task> taskToBeEdited = new ArrayList<Task>();
				taskToBeEdited = undoTask.pop();
				Integer taskNumber = tempStorage.indexOf(taskToBeEdited
						.get(INITIAL_VALUE + 1)) + 1;
				taskToBeEdited.get(INITIAL_VALUE).setParams(
						taskNumber.toString());

				Task undoEditedTask = new Task();
				undoEditedTask.copyOfTask( taskToBeEdited.get(INITIAL_VALUE+1));

				edit(command,taskToBeEdited.get(INITIAL_VALUE),file);
				redo.push(lastCommand);

				taskToBeEdited.remove(INITIAL_VALUE+1);
				taskToBeEdited.add(undoEditedTask);

				redoTask.push(taskToBeEdited);

			}
			
			return MSG_UNDO_SUCCESS;
		}
	}
	
	public static String redo(File file, File archive) {
		if (redo.empty()) {
			return MSG_NO_PREVIOUS_ACTION;
		} else {
			String command = "redo";
			String lastCommand = redo.pop();
			if (lastCommand.equals("add")) {
				ArrayList<Task> taskToBeAdded = new ArrayList<Task>();
				taskToBeAdded = redoTask.pop();
				add(command, taskToBeAdded.get(INITIAL_VALUE), file);
				undo.push("add");
				undoTask.push(taskToBeAdded);
			}
			if (lastCommand.equals("delete")) {
				ArrayList<Task> taskToBeDeleted = new ArrayList<Task>();
				taskToBeDeleted = redoTask.pop();
				for(int i=0;i<taskToBeDeleted.size();i++){
					Integer index = Integer.parseInt(taskToBeDeleted.get(i).getParams())+1;
					Task taskToDelete = new Task();
					taskToDelete.setParams(Integer.toString(index));
					delete(command,taskToBeDeleted.size(), taskToDelete ,file,archive);
				}
				undo.push("delete");
				undoTask.push(taskToBeDeleted);
			}
			if (lastCommand.equals("edit")) {
				ArrayList<Task> taskToBeEdited = new ArrayList<Task>();
				taskToBeEdited = redoTask.pop();
				Task redoEditedTask = new Task();
				redoEditedTask.copyOfTask(taskToBeEdited.get(INITIAL_VALUE));

				edit(command,taskToBeEdited.get(INITIAL_VALUE+1),file);
				undo.push("edit");

				taskToBeEdited.remove(INITIAL_VALUE);
				taskToBeEdited.add(INITIAL_VALUE,redoEditedTask);
				
				undoTask.push(taskToBeEdited);
			}
			if(lastCommand.equals("clear all")){
				clearContent(file);
			}
			return MSG_REDO_SUCCESS;
		}

	}
}
