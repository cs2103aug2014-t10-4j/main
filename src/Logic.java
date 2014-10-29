/**
 * This program implement the logic for the TextBuddy.
 * 
 */
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

public class Logic {

	private static final String BAD_INDEX_MESSAGE = "%d is not a valid number to delete because valid range is %d to %d";
	private static final String DATE_FT = "ft";
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	private static final String TIME_FORMAT = "HHmm";
	private static final String MSG_FAIL_ADD = "Unable to add line";
	private static final String MSG_FAIL_DELETE = "Unable to delete line";
	private static final String MSG_FAIL_EDIT = "Unable to edit line.";
	
	private static final String MSG_ADD_FAIL = "Unable to add line";
	private static final String MSG_ADD_SUCCESS = "Added to %s: \"%s\". Type .u to undo.";
	private static final String MSG_CLEAR_FILE_SUCCESS = "List is cleared";
	private static final String MSG_CLEAR_FILE_FAIL = "Nothing to clear!";
	private static final String MSG_DELETE_FAIL = "Unable to delete line";
	private static final String MSG_DELETE_SUCCESS = "deleted from your list: \"%s\"";
	private static final String MSG_EDIT_FAIL = "Unable to edit line.";
	private static final String MSG_EDIT_SUCCESS = "Successfully edited.";
	private static final String MSG_NO_PREVIOUS_ACTION = "Nothing to undo";
	private static final String MSG_NO_FUTURE_ACTION = "Nothing to redo";
	private static final String MSG_NTH_DELETE = "nothing to delete";
	
	private static final String MSG_UNDO_SUCCESS = "Undo successful";
	private static final String MSG_REDO_SUCCESS = "Redo successful";
	private static final String MSG_SORT_FAIL = "Sorting failed";
	private static final String MSG_SORT_SUCCESS = "Successfully sorted by %s.";
	private static final String MSG_NO_TASKS_TO_SORT = "Not enough tasks to sort";
	
	private static final String MSG_TIME_PASSED = "The time have passed";
	private static final String WRONG_FORMAT = "\"%s\" is wrong format";
	public static String ADD_MESSAGE = "Added to %s: \"%s\". Type .u to undo.";
	
	private static final String MSG_WRONG_FORMAT = "\"%s\" is wrong format";
	
	private static final int INITIAL_VALUE = 0;
	private static final int INVAILD_NUMBER = -1;
	
	private static final String MSG_CLEARED_FILE = "List is cleared";
	private static final String NO_MESSAGE_CLEAR = "Nothing to clear!";
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_REDO = "redo";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_DELETE_ALL = "delete all";
	private static final String COMMAND_MIDWAY_DELETE = "still deleting";

	private static ArrayList<Task> tempStorage = new ArrayList<Task>();
	private static ArrayList<Task> archiveStorage = new ArrayList<Task>();
	private static ArrayList<Task> searchResults = new ArrayList<Task>();
	private static Stack<String> undo = new Stack<String>();
	private static Stack<String> redo = new Stack<String>();
	private static Stack<ArrayList<Task>> undoTask = new Stack<ArrayList<Task>>();
	private static Stack<ArrayList<Task>> redoTask = new Stack<ArrayList<Task>>();
	

	//Methods required during start-up
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
	
	public static ArrayList<Task> getArchiveStorage() {
		return archiveStorage;
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

	// Methods to manipulate task
	public static String add(String command, Task task, File file){
		String returnMessage;
		if(undo.size()!= 0 && undo.peek().equals(COMMAND_SEARCH)){
			undo.pop();
		}
		if(command.equals(COMMAND_ADD)){
			Integer taskNumber = tempStorage.size()+1;
			task.setParams(taskNumber.toString());
			Task taskToAdd = new Task();
			taskToAdd.copyOfTask(task); 
			ArrayList<Task> addedTask = new ArrayList<Task>();
			addedTask.add(taskToAdd);

			returnMessage = addLineToFile(task,file,tempStorage.size());

			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			
			undo.push(COMMAND_ADD);
			undoTask.push(addedTask);

			redo.clear();
			redoTask.clear();

			return returnMessage;
		}

		if(command.equals(COMMAND_UNDO) || command.equals(COMMAND_REDO)){
			int taskToAddLocation = getIndex(task)+1;
			if (tempStorage.size()== INITIAL_VALUE){
				taskToAddLocation = INITIAL_VALUE;
			}
			else{
				for (int i=0; i<tempStorage.size(); i++){
					int currentTaskParams = getIndex(tempStorage.get(i))+1;
					if (taskToAddLocation<currentTaskParams){
						taskToAddLocation = i;
						break;
					}
					else if (taskToAddLocation>currentTaskParams
							&& i == tempStorage.size()-1){
						taskToAddLocation = tempStorage.size();
					}
				}
			}
			returnMessage = addLineToFile(task,file,taskToAddLocation);
			return returnMessage;
		}

		return MSG_FAIL_ADD;

	}

	private static String addLineToFile(Task task, File file, int taskLocation) {
		tempStorage.add(taskLocation, task);
		return String.format(ADD_MESSAGE, file.getName(), task.getName());
	}

	// First task to store in the ArrayList undoTask is the original task before
	// editing
	// Second task to store is the task after editing
	public static String edit(String command, Task detailsOfTask, File file) {
		String returnMessage;
		String lastCommand = null;
		if(undo.size()!= 0 && undo.peek().equals(COMMAND_SEARCH)){
			lastCommand = undo.pop();
		}
		
		if (lastCommand!= null && lastCommand.equals(COMMAND_SEARCH)){
			int taskNumber = getIndex(detailsOfTask);
			
			for (int i = 0; i < tempStorage.size(); i++) {

				if (searchResults.get(taskNumber).equals(tempStorage.get(i))) {
					
					ArrayList<Task> tasksEdited = new ArrayList<Task>();
					Task originalTask = new Task();
					originalTask.copyOfTask(tempStorage.get(i));
					tasksEdited.add(originalTask);
					
					returnMessage = editTask(detailsOfTask, file, i);
					
					Task editedTask = new Task();
					editedTask.copyOfTask(tempStorage.get(i));
					tasksEdited.add(editedTask);

					sortByDateAndTime(tempStorage);
					Storage.writeToFile(tempStorage, file);
					
					undo.push(command);
					undoTask.push(tasksEdited);
					redo.clear();
					redoTask.clear();

					return returnMessage;
				}
			}

			
		}
		if (command.equals(COMMAND_EDIT)){
			
			int taskNumber = getIndex(detailsOfTask);
			
			ArrayList<Task> tasksEdited = new ArrayList<Task>();
			Task originalTask = new Task();
			originalTask.copyOfTask(tempStorage.get(taskNumber));
			tasksEdited.add(originalTask);

			returnMessage = editTask(detailsOfTask, file, taskNumber);
			
			Task editedTask = new Task();
			editedTask.copyOfTask(tempStorage.get(taskNumber));
			tasksEdited.add(editedTask);

			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			
			undo.push(command);
			undoTask.push(tasksEdited);
			redo.clear();
			redoTask.clear();

			return returnMessage;
		}
		else if (command.equals(COMMAND_UNDO)||command.equals(COMMAND_REDO)){
			
			int taskNumber = getIndex(detailsOfTask);
			
			returnMessage = editUndoRedo( detailsOfTask, file, taskNumber);
			
			return returnMessage;
		}

		else {
			return MSG_FAIL_EDIT;
		}

	}

	private static String getTodayDateAndTime() {
		DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT+Constants.TIME_FORMAT);
		Date date = new Date();
		String reportCurrent = dateFormat.format(date);
		return reportCurrent;
	}

	private static String editTask(Task detailsOfTask, File file, int taskNumber) {
		String todayDate = getTodayDateAndTime();
		System.out.println(todayDate);
		try {
			if (detailsOfTask.getTime()!=null) {

				Date taskDateAndTime = new SimpleDateFormat(Constants.DATE_FORMAT+Constants.TIME_FORMAT).parse(
						tempStorage.get(taskNumber).getDate()+detailsOfTask.getTime());
				
				Date currentDateAndTime = new SimpleDateFormat(Constants.DATE_FORMAT+Constants.TIME_FORMAT).parse(todayDate);
				
				if(taskDateAndTime.compareTo(currentDateAndTime)<0){
					return MSG_TIME_PASSED;
				}

				tempStorage.get(taskNumber).setTime(detailsOfTask.getTime());

			}
			if (detailsOfTask.getName() != null) {
				tempStorage.get(taskNumber).setName(detailsOfTask.getName());
			}
			if (detailsOfTask.getDate() != null) {
				tempStorage.get(taskNumber).setDate(detailsOfTask.getDate());
			}

			if (detailsOfTask.getDetails() != null) {
				tempStorage.get(taskNumber).setDetails(detailsOfTask.getDetails());
			}
			
			else if (detailsOfTask.getDetails() != null && detailsOfTask.getDetails().equals("")){
				tempStorage.get(taskNumber).setDetails(null);
			}
			

			if (detailsOfTask.getImportance() != INITIAL_VALUE - 1) {
				tempStorage.get(taskNumber).setImportance(
						detailsOfTask.getImportance());
			} 
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return MSG_EDIT_SUCCESS;
	}
	
	private static String editUndoRedo(Task detailsOfTask, File file, int taskNumber) {
		tempStorage.get(taskNumber).setTime(detailsOfTask.getTime());
		tempStorage.get(taskNumber).setName(detailsOfTask.getName());
		tempStorage.get(taskNumber).setDate(detailsOfTask.getDate());
		tempStorage.get(taskNumber).setDetails(detailsOfTask.getDetails());
		tempStorage.get(taskNumber).setImportance(detailsOfTask.getImportance());
		return MSG_EDIT_SUCCESS;
	}
	

	public static String delete(String command, int numOfTaskToDelete, Task task, File file, File archive){
		String returnMessage;
		if (tempStorage.size() == 0) {
			if(undo.size()!=0 && undo.peek().equals(COMMAND_MIDWAY_DELETE)){
				undo.pop();
				undo.push(COMMAND_DELETE);
			}
			return MSG_NTH_DELETE;
			
		} 
		if(Integer.parseInt(task.getParams())<=0 && command.equals(COMMAND_DELETE)){
			if(undo.size()!=0 && undo.peek().equals(COMMAND_MIDWAY_DELETE)){
				undo.pop();
				undo.push(COMMAND_DELETE);
			}
			return MSG_FAIL_DELETE;
		}else if (command.equals(COMMAND_DELETE)){
			String commandCheck = null;
			
			if(undo.size()!=0 && undo.peek().equals(COMMAND_MIDWAY_DELETE)){
					commandCheck= undo.pop();
				}
			// DELETE FROM SEARCH LIST
			if(undo.size()!=0 && undo.peek().equals(COMMAND_SEARCH)){
				int index = getIndex(task);
				Task taskToDelete = new Task();
				taskToDelete.copyOfTask(searchResults.get(index));
				returnMessage = deleteLineFromSearchList(task,searchResults,file,archive);

				if(commandCheck == null){
					ArrayList<Task> deletedTask = new ArrayList<Task>();
					deletedTask.add(taskToDelete);
					assert(deletedTask.size() <= numOfTaskToDelete);
					
					if(deletedTask.size()== numOfTaskToDelete){
						undo.pop(); // remove COMMAND_SEARCH
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						undo.push(command);
						undoTask.push(deletedTask);
						redo.clear();
						redoTask.clear();
						return returnMessage;
					}
					else{
						
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						
						undo.push(COMMAND_MIDWAY_DELETE);
						undoTask.push(deletedTask);
						return returnMessage;
					}
				}
				
				if(commandCheck.equals(COMMAND_MIDWAY_DELETE)){
					ArrayList<Task> deletedTask = undoTask.pop();
					deletedTask.add(taskToDelete);
					assert(deletedTask.size() <= numOfTaskToDelete);
					
					if(deletedTask.size()== numOfTaskToDelete){
						undo.pop(); // remove COMMAND_SEARCH
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						undo.push(command);
						undoTask.push(deletedTask);
						redo.clear();
						redoTask.clear();
						return returnMessage;
					}
					
					else{
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						undo.push(COMMAND_MIDWAY_DELETE);
						undoTask.push(deletedTask);
						return returnMessage;
					}
				}
			}
			// DELETE FROM NORMAL STORAGE
			else{
				int index = getIndex(task);
				Task taskToDelete = new Task();
				taskToDelete.copyOfTask(tempStorage.get(index));

				returnMessage = deleteLineFromFile(index,task,file,archive);
				
				if( commandCheck == null){
					ArrayList<Task> deletedTask = new ArrayList<Task>();
					deletedTask.add(taskToDelete);
					
					assert(deletedTask.size() <= numOfTaskToDelete);
					
					if(deletedTask.size()== numOfTaskToDelete){
						
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						
						undo.push(command);
						undoTask.push(deletedTask);
					
						redo.clear();
						redoTask.clear();
						
						return returnMessage;
					}
					else{ 
						
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						
						undo.push(COMMAND_MIDWAY_DELETE);
						undoTask.push(deletedTask);
						
						return returnMessage;
					}
				}
				
				if(commandCheck.equals(COMMAND_MIDWAY_DELETE)){
					
					ArrayList<Task> deletedTask = undoTask.pop();
					deletedTask.add(taskToDelete);
					
					assert(deletedTask.size() <= numOfTaskToDelete);
					if(deletedTask.size()== numOfTaskToDelete){ 
						
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						
						undo.push(command);
						undoTask.push(deletedTask);
						
						redo.clear();
						redoTask.clear();
						return returnMessage;
					}
					else {
						
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						
						undo.push(COMMAND_MIDWAY_DELETE);
						undoTask.push(deletedTask);
						return returnMessage;
					}
				}
			}
			
		}else if (command.equals(COMMAND_REDO)){
			int index = tempStorage.indexOf(task);
		
			returnMessage = deleteLineFromFile(index,task,file,archive);
			
			sortByDateAndTime(archiveStorage);
			
			Storage.writeToFile(archiveStorage, archive);
			
			return returnMessage;
			
		}else if (command.equals(COMMAND_UNDO)){
			int index = tempStorage.indexOf(task);
			returnMessage = deleteLineFromFile(index, task,file,archive);
			archiveStorage.remove(archiveStorage.indexOf(task));
			Storage.writeToFile(archiveStorage, archive);
			return returnMessage;
		} else {
			return MSG_DELETE_FAIL;
		}
		return MSG_DELETE_FAIL;
	}

	private static String deleteLineFromFile(int index, Task task, File file, File archive) {
		if (index == INVAILD_NUMBER) {
			return MSG_NTH_DELETE;
		}
		try {
			String returnMessage = String.format(MSG_DELETE_SUCCESS, tempStorage.get(index).getName());
			
			Task taskToDelete = tempStorage.remove(index);
			archiveStorage.add(taskToDelete);
			
			return returnMessage;
		} catch (IndexOutOfBoundsException e) {
			return String.format(BAD_INDEX_MESSAGE, index + 1, 1,
					tempStorage.size());
		}
	}
	
	private static String deleteLineFromSearchList(Task task,
			ArrayList<Task> searchResults, File file, File archive) {
		if (searchResults.size() == 0) {
			return MSG_NTH_DELETE;
		}
		int index = getIndex(task);

		for (int i = 0; i < tempStorage.size(); i++) {

			if (searchResults.get(index).equals(tempStorage.get(i))) {
				
				Task taskToDelete = tempStorage.remove(i);
				archiveStorage.add(taskToDelete);
				
				sortByDateAndTime(tempStorage);
				sortByDateAndTime(archiveStorage);
				
				Storage.writeToFile(tempStorage, file);
				Storage.writeToFile(archiveStorage, archive);
				break;
			}
		}
		return String.format(MSG_DELETE_SUCCESS, searchResults.get(index).getName());

	}
	
	public static String clearContent(File file) {
		if(undo.size()!= 0 && undo.peek().equals(COMMAND_SEARCH)){
			undo.pop();
		} 
		if(tempStorage.size()>0){
			
		ArrayList<Task> deletedTask = new ArrayList<Task>();
		
		for(int i =0; i<tempStorage.size(); i++){
			
			Task taskToDelete = new Task();	
			taskToDelete.copyOfTask( tempStorage.get(i));
			deletedTask.add(taskToDelete);
		}
		
		tempStorage.clear();
		Storage.writeToFile(new ArrayList<Task>(), file); 
		
		undo.push(COMMAND_DELETE_ALL);
		undoTask.push(deletedTask);

		return MSG_CLEARED_FILE;
		
		}else{
			return NO_MESSAGE_CLEAR;
		}
	}
	
	public static String clearArchive(File file) {
		if(undo.size()!= 0 && undo.peek().equals(COMMAND_SEARCH)){
			undo.pop();
		} 
		if(archiveStorage.size()>0){

		archiveStorage.clear();
		Storage.writeToFile(new ArrayList<Task>(), file); 

		return MSG_CLEARED_FILE;
		
		}else{
			return NO_MESSAGE_CLEAR;
		}
	}

	private static int getIndex(Task task) {
		try {
			return Integer.parseInt(task.getParams()) - 1;
		} catch (NumberFormatException e) {
			System.out.println(String.format(WRONG_FORMAT, task.getParams()));
		}
		return INVAILD_NUMBER;
	}

	public static ArrayList<Task> search(Task taskToFind) {
		searchResults.clear();
		
		assert tempStorage.size() >= 0 : "tempStorage.size() is negative";
		for (int i = 0; i < tempStorage.size(); i++) {
			Task taskInList = tempStorage.get(i);
			// Filtering name
			
			if (taskToFind.getName() != null
					&& taskInList.getDetails() != null
					&& taskInList.getDetails().toLowerCase()
							.contains(taskToFind.getName().toLowerCase())) {
				searchResults.add(taskInList);
				continue;
			}
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
			searchResults.add(taskInList);
		}
		undo.push(Constants.COM_SEARCH);
		return searchResults;
	}
	
	
	public static String undo(File file, File archive) {
		if (undo.empty()) {
			return MSG_NO_PREVIOUS_ACTION;
		}else if(undo.size()!= 0 && undo.peek().equals(COMMAND_SEARCH)){
			undo.pop();
			return undo(file,archive);
		} else {

			String lastCommand = undo.pop();
			
			if(lastCommand.equals(COMMAND_ADD)){
				ArrayList<Task> taskToBeDeleted = undoTask.pop();
				Task taskToDelete = new Task();
				
				Integer taskLocation = tempStorage.indexOf(taskToBeDeleted.get(INITIAL_VALUE))+1;
				taskToDelete.setParams(taskLocation.toString());
				
				delete(COMMAND_UNDO,taskToBeDeleted.size(),taskToBeDeleted.get(INITIAL_VALUE),file,archive);
				
				redo.push(lastCommand);
				redoTask.push(taskToBeDeleted);
			}
			
			if(lastCommand.equals(COMMAND_DELETE)|| lastCommand.equals(COMMAND_DELETE_ALL)){
				ArrayList<Task> taskToBeAdded = undoTask.pop();
				
				
				for(int i=0;i<taskToBeAdded.size();i++){
					
					Task taskToAdd = new Task();
					taskToAdd.copyOfTask(taskToBeAdded.get(i));
					
					add(COMMAND_UNDO,taskToAdd,file);
				}

				redo.push(lastCommand);
				redoTask.push(taskToBeAdded);
			}
			
			if(lastCommand.equals(COMMAND_EDIT)){
				Task undoEditedTask = new Task();
				ArrayList<Task> taskToBeEdited = undoTask.pop();
				
				Integer taskNumber = tempStorage.indexOf(taskToBeEdited
						.get(INITIAL_VALUE + 1)) + 1;
				
				undoEditedTask.copyOfTask(taskToBeEdited.get(INITIAL_VALUE));
				undoEditedTask.setParams(taskNumber.toString());

				edit(COMMAND_UNDO,undoEditedTask,file);
				
				redo.push(lastCommand);
				redoTask.push(taskToBeEdited);

			}
			
			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			
			return MSG_UNDO_SUCCESS;
		}
	}
	
	public static String redo(File file, File archive) {
		if (redo.empty()) {
			return MSG_NO_FUTURE_ACTION;
		}else if(undo.size()!= 0 && undo.peek().equals(COMMAND_SEARCH)){
			undo.pop();
			return redo(file,archive);
		}  else {
			String lastCommand = redo.pop();
			
			if(lastCommand.equals(COMMAND_ADD)){
				ArrayList<Task> taskToBeAdded = redoTask.pop();
				add(COMMAND_REDO, taskToBeAdded.get(INITIAL_VALUE), file);
				undo.push("add");
				undoTask.push(taskToBeAdded);
			}
			
			if(lastCommand.equals(COMMAND_DELETE)){
				ArrayList<Task> taskToBeDeleted = redoTask.pop();
				
				for(int i=0;i<taskToBeDeleted.size();i++){

					
					delete(COMMAND_REDO,taskToBeDeleted.size(), taskToBeDeleted.get(i) ,file,archive);
				}
				
				undo.push(COMMAND_DELETE);
				undoTask.push(taskToBeDeleted);
			}
			
			if(lastCommand.equals(COMMAND_EDIT)){
				Task redoEditedTask = new Task();
				ArrayList<Task> taskToBeEdited = redoTask.pop();
				
				Integer taskNumber = tempStorage.indexOf(taskToBeEdited
						.get(INITIAL_VALUE)) + 1;
				
				redoEditedTask.copyOfTask(taskToBeEdited.get(INITIAL_VALUE+1));
				redoEditedTask.setParams(taskNumber.toString());

				edit(COMMAND_REDO,redoEditedTask,file);
				
				undo.push(lastCommand);
				undoTask.push(taskToBeEdited);
			}
			
			if(lastCommand.equals(COMMAND_DELETE_ALL)){
				clearContent(file);
			}
			
			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			
			return MSG_REDO_SUCCESS;
		}
	}
	//3 method of Sorting
	public static String sortByDateAndTime(ArrayList<Task> tempStorage) {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
		Date dateFirst = new Date();
		Date dateSecond = new Date();
		Date timeFirst = new Date();
		Date timeSecond = new Date();
		dateFormat.setLenient(false);
		timeFormat.setLenient(false);

		if (tempStorage.size() < 1) {
			return MSG_NO_TASKS_TO_SORT;
		} else {
			try {
				for (int i = 0; i < tempStorage.size(); i++) {

					boolean isSorted = true;

					for (int j = 0; j < tempStorage.size() - 1; j++) {


						int firstTaskOrder = Integer.parseInt(tempStorage.get(j).getParams());
						int secondTaskOrder = Integer.parseInt(tempStorage.get(j+1).getParams());

						if (tempStorage.get(j).getDate().equals("ft")
								&& !tempStorage.get(j + 1).getDate()
								.equals("ft")) {
							tempStorage.add(j + 2, tempStorage.get(j));
							tempStorage.remove(j);
							isSorted = false;
							continue;

						} else if (!tempStorage.get(j).getDate().equals(DATE_FT)
								&& tempStorage.get(j + 1).getDate()
								.equals("ft")) {
							continue;
						} else if( tempStorage.get(j).getDate().equals("ft")
								&& tempStorage.get(j + 1).getDate()
								.equals("ft")) {
							if(firstTaskOrder>secondTaskOrder){
								tempStorage.add(j + 2, tempStorage.get(j));
								tempStorage.remove(j);
								isSorted = false;
							}
							continue;
						}else {

							dateFirst = dateFormat.parse(tempStorage.get(
									j).getDate());

							dateSecond = dateFormat.parse(tempStorage
									.get(j + 1).getDate());

							if (dateFirst.compareTo(dateSecond) > 0) {
								tempStorage.add(j + 2, tempStorage.get(j));
								tempStorage.remove(j);
								isSorted = false;
								continue;

							} else if (dateFirst
									.compareTo(dateSecond) == 0) {

								if (tempStorage.get(j).getTime() == null) {
									continue;

								} else if (tempStorage.get(j).getTime() != null
										&& tempStorage.get(j + 1).getTime() == null) {

									tempStorage.add(j + 2, tempStorage.get(j));
									tempStorage.remove(j);
									isSorted = false;
									continue;

								} else {


									timeFirst = timeFormat
											.parse(tempStorage.get(j).getTime());

									timeSecond = timeFormat
											.parse(tempStorage.get(j + 1)
													.getTime());

									if (timeFirst
											.compareTo(timeSecond) > 0) {
										tempStorage.add(j + 2,
												tempStorage.get(j));
										tempStorage.remove(j);
										isSorted = false;
										continue;
									}else if (timeFirst.compareTo(timeSecond) == 0){

										if(firstTaskOrder>secondTaskOrder){
											tempStorage.add(j + 2, tempStorage.get(j));
											tempStorage.remove(j);
											isSorted = false;
										}
										continue;
									}
									else {
										continue;
									}
								}
							}
							else{
								continue;
							}
						}
					}
					if (isSorted) {
						return  MSG_SORT_SUCCESS + "date and time";
					}
				}
			} catch (ParseException e) {
			}
			return String.format(MSG_SORT_SUCCESS, "date and time");
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
					return String.format(MSG_SORT_SUCCESS, "alphabetical order");
				}

			}
			return MSG_SORT_FAIL;
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
					return String.format(MSG_SORT_SUCCESS, "importance level");
				}
			}
		}
		return MSG_SORT_FAIL;
	}

	// Following methods are used for junit testing.
	public static String printTempStorage(){
		String string="";
		for (int i = 0; i< tempStorage.size(); i++){
			Integer importance= tempStorage.get(i).getImportance();
			string = string 
					+ tempStorage.get(i).getName() 
					+" "+ tempStorage.get(i).getDate()
					+" "+ tempStorage.get(i).getTime()
					+" "+ tempStorage.get(i).getDetails()
					+" "+ importance.toString()
					+" "+ tempStorage.get(i).getParams()
					+"\n";
		}
		
		return string;
	}

	public static String printTempArchive(){
		String string="";
		for (int i = 0; i< archiveStorage.size(); i++){
			Integer importance= archiveStorage.get(i).getImportance();
			string = string 
					+ archiveStorage.get(i).getName() 
					+" "+ archiveStorage.get(i).getDate()
					+" "+ archiveStorage.get(i).getTime()
					+" "+ archiveStorage.get(i).getDetails()
					+" "+ importance.toString()
					+" "+ archiveStorage.get(i).getParams()
					+"\n";
		}
		return string;
	}

	public static void clearUndoRedo(){
		undo.clear();
		undoTask.clear();
		redo.clear();
		redoTask.clear();
	}
	
	public static void sortAlpha(){
		sortByAlphabet(tempStorage);
	}
	public static void sortChrono(){
		sortByDateAndTime(tempStorage);
	}
	public static void sortImportance(){
		sortByImportance(tempStorage);
	}
	
	//Returns first index of non-overdue. Use in deleting past tasks before that index.
		public static int getFirstNotOverdueInList() {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			Date currentDate = new Date();
			currentDate = removeTime(currentDate);
			for (int i = 0; i < tempStorage.size(); i++) {
				if (tempStorage.get(i).getDate().contains(DATE_FT)) {
					return i+1;
				} else {
					try {
						Date dateOfCurrentTask = dateFormat.parse(tempStorage.get(i).getDate());
						dateOfCurrentTask = removeTime(dateOfCurrentTask);
						if (dateOfCurrentTask.compareTo(currentDate) >= 0) {
							return i+1;
						}
					} catch (ParseException e) {
					}
				}
			}
			return tempStorage.size()+1;
		}
		
		//For use in delete past. Need to remove time of all dates for comparison.
		private static Date removeTime(Date date) {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        cal.set(Calendar.HOUR_OF_DAY, 0);
	        cal.set(Calendar.MINUTE, 0);
	        cal.set(Calendar.SECOND, 0);
	        cal.set(Calendar.MILLISECOND, 0);
	        return cal.getTime();
	    }
}
