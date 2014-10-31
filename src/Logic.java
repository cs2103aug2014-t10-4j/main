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
		sortByDateAndTime(tempStorage);
		Storage.writeToFile(tempStorage, file);
		sortByDateAndTime(archiveStorage);
		Storage.writeToFile(archiveStorage, archive);
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

		Date currentDate = new Date();
		try {
			currentDate = Constants.dateFormat.parse(Constants.dateFormat.format(currentDate));
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
					Date dateOfCurrentTask = Constants.dateFormat.parse(tempStorage.get(i).getDate());
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
		if(undo.size()!= 0 && undo.peek().equals(Constants.COMMAND_SEARCH)){
			undo.pop();
		}
		if(command.equals(Constants.COMMAND_ADD)){
			Integer taskNumber = tempStorage.size()+1;
			task.setParams(taskNumber.toString());
			Task taskToAdd = new Task();
			taskToAdd.copyOfTask(task); 
			ArrayList<Task> addedTask = new ArrayList<Task>();
			addedTask.add(taskToAdd);

			returnMessage = addLineToFile(task,file,tempStorage.size());

			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			
			undo.push(Constants.COMMAND_ADD);
			undoTask.push(addedTask);

			redo.clear();
			redoTask.clear();

			return returnMessage;
		}

		if(command.equals(Constants. COMMAND_UNDO) || command.equals(Constants. COMMAND_REDO)){
			int taskToAddLocation = getIndex(task)+1;
			if (tempStorage.size()== Constants.INITIAL_VALUE){
				taskToAddLocation = Constants.INITIAL_VALUE;
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

		return Constants.MSG_FAIL_ADD;

	}

	private static String addLineToFile(Task task, File file, int taskLocation) {
		tempStorage.add(taskLocation, task);
		return String.format(Constants.ADD_MESSAGE, file.getName(), task.getName());
	}

	// First task to store in the ArrayList undoTask is the original task before
	// editing
	// Second task to store is the task after editing
	public static String edit(String command, Task detailsOfTask, File file) {
		String returnMessage;
		
		String lastCommand = null;
		if(undo.size()!= 0 && undo.peek().equals(Constants.COMMAND_SEARCH)){
			lastCommand = undo.pop();
		}
		

		if (lastCommand != null && lastCommand.equals(Constants.COMMAND_SEARCH)){

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
		if (command.equals(Constants.COMMAND_EDIT)){
			
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
		else if (command.equals(Constants. COMMAND_UNDO)||command.equals(Constants. COMMAND_REDO)){
			
			int taskNumber = getIndex(detailsOfTask);
			
			returnMessage = editUndoRedo( detailsOfTask, file, taskNumber);
			
			return returnMessage;
		}

		else {
			return Constants.MSG_FAIL_EDIT;
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
			
			else if (detailsOfTask.getDetails() != null && detailsOfTask.getDetails().equals("")){
				tempStorage.get(taskNumber).setDetails(null);
			}
			
			if (detailsOfTask.getImportance() != Constants.INITIAL_VALUE - 1) {
				tempStorage.get(taskNumber).setImportance(
						detailsOfTask.getImportance());
			} 
		
			return Constants.MSG_EDIT_SUCCESS;
	}
	
	private static String editUndoRedo(Task detailsOfTask, File file, int taskNumber) {
		tempStorage.get(taskNumber).setTime(detailsOfTask.getTime());
		tempStorage.get(taskNumber).setName(detailsOfTask.getName());
		tempStorage.get(taskNumber).setDate(detailsOfTask.getDate());
		tempStorage.get(taskNumber).setDetails(detailsOfTask.getDetails());
		tempStorage.get(taskNumber).setImportance(detailsOfTask.getImportance());
		return Constants.MSG_EDIT_SUCCESS;
	}
	

	public static String delete(String command, int numOfTaskToDelete, Task task, File file, File archive){
		String returnMessage;
		if (tempStorage.size() == 0) {
			if(undo.size()!=0 && undo.peek().equals(Constants.COMMAND_MIDWAY_DELETE)){
				undo.pop();
				undo.push(Constants.COMMAND_DELETE);
			}
			return Constants.MSG_NTH_DELETE;
			
		} 
		if(Integer.parseInt(task.getParams())<=0 && command.equals(Constants.COMMAND_DELETE)){
			if(undo.size()!=0 && undo.peek().equals(Constants.COMMAND_MIDWAY_DELETE)){
				undo.pop();
				undo.push(Constants.COMMAND_DELETE);
			}
			return Constants.MSG_FAIL_DELETE;
		}else if (command.equals(Constants.COMMAND_DELETE)){
			String commandCheck = null;
			
			if(undo.size()!=0 && undo.peek().equals(Constants.COMMAND_MIDWAY_DELETE)){
					commandCheck= undo.pop();
				}
			// DELETE FROM SEARCH LIST
			if(undo.size()!=0 && undo.peek().equals(Constants.COMMAND_SEARCH) && searchResults.size()!=0){
				int index = getIndex(task);
				Task taskToDelete = new Task();
				taskToDelete.copyOfTask(searchResults.get(index));
				returnMessage = deleteLineFromSearchList(task,searchResults,file,archive);

				if(commandCheck == null){
					ArrayList<Task> deletedTask = new ArrayList<Task>();
					deletedTask.add(taskToDelete);
					assert(deletedTask.size() <= numOfTaskToDelete);
					
					if(deletedTask.size()== numOfTaskToDelete){
						undo.pop(); // remove Constants.COMMAND_SEARCH
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
						
						undo.push(Constants.COMMAND_MIDWAY_DELETE);
						undoTask.push(deletedTask);
						return returnMessage;
					}
				}
				
				if(commandCheck.equals(Constants.COMMAND_MIDWAY_DELETE)){
					ArrayList<Task> deletedTask = undoTask.pop();
					deletedTask.add(taskToDelete);
					assert(deletedTask.size() <= numOfTaskToDelete);
					
					if(deletedTask.size()== numOfTaskToDelete){
						undo.pop(); // remove Constants.COMMAND_SEARCH
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
						undo.push(Constants.COMMAND_MIDWAY_DELETE);
						undoTask.push(deletedTask);
						return returnMessage;
					}
				}
			}
			// DELETE FROM NORMAL STORAGE
			else{
				if(undo.size()!=0 && undo.peek().equals(Constants.COMMAND_SEARCH) && searchResults.size()==0){
					undo.pop();
				}
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
						
						undo.push(Constants.COMMAND_MIDWAY_DELETE);
						undoTask.push(deletedTask);
						
						return returnMessage;
					}
				}
				
				if(commandCheck.equals(Constants.COMMAND_MIDWAY_DELETE)){
					
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
						
						undo.push(Constants.COMMAND_MIDWAY_DELETE);
						undoTask.push(deletedTask);
						return returnMessage;
					}
				}
			}
			
		}else if (command.equals(Constants. COMMAND_REDO)){
			int index = tempStorage.indexOf(task);
		
			returnMessage = deleteLineFromFile(index,task,file,archive);
			
			sortByDateAndTime(archiveStorage);
			
			Storage.writeToFile(archiveStorage, archive);
			
			return returnMessage;
			
		}else if (command.equals(Constants. COMMAND_UNDO)){
			int index = tempStorage.indexOf(task);
			returnMessage = deleteLineFromFile(index, task,file,archive);
			archiveStorage.remove(archiveStorage.indexOf(task));
			Storage.writeToFile(archiveStorage, archive);
			return returnMessage;
		} else {
			return Constants.MSG_DELETE_FAIL;
		}
		return Constants.MSG_DELETE_FAIL;
	}

	private static String deleteLineFromFile(int index, Task task, File file, File archive) {
		if (index == Constants.INVAILD_NUMBER) {
			return Constants.MSG_NTH_DELETE;
		}
		try {
			String returnMessage = String.format(Constants.MSG_DELETE_SUCCESS,index+1, tempStorage.get(index).getName());
			
			Task taskToDelete = tempStorage.remove(index);
			archiveStorage.add(taskToDelete);
			
			return returnMessage;
		} catch (IndexOutOfBoundsException e) {
			return String.format(Constants.BAD_INDEX_MESSAGE, index + 1, 1,
					tempStorage.size());
		}
	}
	
	private static String deleteLineFromSearchList(Task task,
			ArrayList<Task> searchResults, File file, File archive) {
		if (searchResults.size() == 0) {
			return Constants.MSG_NTH_DELETE;
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
		return String.format(Constants.MSG_DELETE_SUCCESS, index+1, searchResults.get(index).getName());

	}
	
	public static String clearContent(File file) {
		if(undo.size()!= 0 && undo.peek().equals(Constants.COMMAND_SEARCH)){
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
		
		undo.push(Constants.COMMAND_DELETE_ALL);
		undoTask.push(deletedTask);

		return Constants.MSG_CLEARED_FILE;
		
		}else{
			return Constants.NO_MESSAGE_CLEAR;
		}
	}
	
	public static String clearArchive(File file) {
		if(undo.size()!= 0 && undo.peek().equals(Constants.COMMAND_SEARCH)){
			undo.pop();
		} 
		if(archiveStorage.size()>0){

		archiveStorage.clear();
		Storage.writeToFile(new ArrayList<Task>(), file); 

		return Constants.MSG_CLEARED_FILE;
		
		}else{
			return Constants.NO_MESSAGE_CLEAR;
		}
	}

	private static int getIndex(Task task) {
		try {
			return Integer.parseInt(task.getParams()) - 1;
		} catch (NumberFormatException e) {
			System.out.println(String.format(Constants.WRONG_FORMAT, task.getParams()));
		}
		return Constants.INVAILD_NUMBER;
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
		undo.push(Constants.COMMAND_SEARCH);
		return searchResults;
	}
	
	public static ArrayList<Task> searchRangeOfDate(Task taskStartOfRange, Task taskEndOfRange) {
		searchResults.clear();
		Date dateStart = new Date();
		Date dateEnd = new Date();
		try {
			dateStart = Constants.dateFormat.parse(taskStartOfRange.getDate());
			dateEnd = Constants.dateFormat.parse(taskEndOfRange.getDate());
		
			assert tempStorage.size() >= 0 : "tempStorage.size() is negative";
		
			for (int i = 0; i < tempStorage.size(); i++) {
				if(tempStorage.get(i).getDate().equals(Constants.DATE_FT)){
					continue;
				}
				else{
					Date dateCheck = new Date();
					Task taskInList = tempStorage.get(i);
					dateCheck = Constants.dateFormat.parse(taskInList.getDate());
			
					if (dateCheck.compareTo(dateStart)>=0 
							&& dateCheck.compareTo(dateEnd)<=0) {
						searchResults.add(taskInList);
					}		
				}
			}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		undo.push(Constants.COMMAND_SEARCH);
		return searchResults;
	}
	
	
	public static String undo(File file, File archive) {
		if (undo.empty()) {
			return Constants.MSG_NO_PREVIOUS_ACTION;
		}else if(undo.size()!= 0 && undo.peek().equals(Constants.COMMAND_SEARCH)){
			undo.pop();
			return undo(file,archive);
		} else {

			String lastCommand = undo.pop();
			
			if(lastCommand.equals(Constants.COMMAND_ADD)){
				ArrayList<Task> taskToBeDeleted = undoTask.pop();
				Task taskToDelete = new Task();
				
				Integer taskLocation = tempStorage.indexOf(taskToBeDeleted.get(Constants.INITIAL_VALUE))+1;
				taskToDelete.setParams(taskLocation.toString());
				
				delete(Constants. COMMAND_UNDO,taskToBeDeleted.size(),taskToBeDeleted.get(Constants.INITIAL_VALUE),file,archive);
				
				redo.push(lastCommand);
				redoTask.push(taskToBeDeleted);
			}
			
			if(lastCommand.equals(Constants.COMMAND_DELETE)|| lastCommand.equals(Constants.COMMAND_DELETE_ALL)){
				ArrayList<Task> taskToBeAdded = undoTask.pop();
				
				
				for(int i=0;i<taskToBeAdded.size();i++){
					
					Task taskToAdd = new Task();
					taskToAdd.copyOfTask(taskToBeAdded.get(i));
					
					add(Constants. COMMAND_UNDO,taskToAdd,file);
				}

				redo.push(lastCommand);
				redoTask.push(taskToBeAdded);
			}
			
			if(lastCommand.equals(Constants.COMMAND_EDIT)){
				Task undoEditedTask = new Task();
				ArrayList<Task> taskToBeEdited = undoTask.pop();
				
				Integer taskNumber = tempStorage.indexOf(taskToBeEdited
						.get(Constants.INITIAL_VALUE + 1)) + 1;
				
				undoEditedTask.copyOfTask(taskToBeEdited.get(Constants.INITIAL_VALUE));
				undoEditedTask.setParams(taskNumber.toString());

				edit(Constants. COMMAND_UNDO,undoEditedTask,file);
				
				redo.push(lastCommand);
				redoTask.push(taskToBeEdited);

			}
			
			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			
			return Constants.MSG_UNDO_SUCCESS;
		}
	}
	
	public static String redo(File file, File archive) {
		if (redo.empty()) {
			return Constants.MSG_NO_FUTURE_ACTION;
		}else if(undo.size()!= 0 && undo.peek().equals(Constants.COMMAND_SEARCH)){
			undo.pop();
			return redo(file,archive);
		}  else {
			String lastCommand = redo.pop();
			
			if(lastCommand.equals(Constants.COMMAND_ADD)){
				ArrayList<Task> taskToBeAdded = redoTask.pop();
				add(Constants. COMMAND_REDO, taskToBeAdded.get(Constants.INITIAL_VALUE), file);
				undo.push("add");
				undoTask.push(taskToBeAdded);
			}
			
			if(lastCommand.equals(Constants.COMMAND_DELETE)){
				ArrayList<Task> taskToBeDeleted = redoTask.pop();
				
				for(int i=0;i<taskToBeDeleted.size();i++){

					
					delete(Constants. COMMAND_REDO,taskToBeDeleted.size(), taskToBeDeleted.get(i) ,file,archive);
				}
				
				undo.push(Constants.COMMAND_DELETE);
				undoTask.push(taskToBeDeleted);
			}
			
			if(lastCommand.equals(Constants.COMMAND_EDIT)){
				Task redoEditedTask = new Task();
				ArrayList<Task> taskToBeEdited = redoTask.pop();
				
				Integer taskNumber = tempStorage.indexOf(taskToBeEdited
						.get(Constants.INITIAL_VALUE)) + 1;
				
				redoEditedTask.copyOfTask(taskToBeEdited.get(Constants.INITIAL_VALUE+1));
				redoEditedTask.setParams(taskNumber.toString());

				edit(Constants. COMMAND_REDO,redoEditedTask,file);
				
				undo.push(lastCommand);
				undoTask.push(taskToBeEdited);
			}
			
			if(lastCommand.equals(Constants.COMMAND_DELETE_ALL)){
				clearContent(file);
			}
			
			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			
			return Constants.MSG_REDO_SUCCESS;
		}
	}
	//3 method of Sorting
	public static String sortByDateAndTime(ArrayList<Task> tempStorage) {
		Task.setSortedByTime(true);
		Date dateFirst = new Date();
		Date dateSecond = new Date();
		Date timeFirst = new Date();
		Date timeSecond = new Date();
		Constants.dateFormat.setLenient(false);
		Constants.timeFormatOne.setLenient(false);

		if (tempStorage.size() < 1) {
			return Constants.MSG_NO_TASKS_TO_SORT;
		} else {
			try {
				for (int i = 0; i < tempStorage.size(); i++) {
					boolean isSorted = true;

					for (int j = 0; j < tempStorage.size() - 1; j++) {


						int firstTaskOrder = Integer.parseInt(tempStorage.get(j).getParams());
						int secondTaskOrder = Integer.parseInt(tempStorage.get(j+1).getParams());

						if (tempStorage.get(j).getDate().equals(Constants.DATE_FT)
								&& !tempStorage.get(j + 1).getDate()
								.equals(Constants.DATE_FT)) {
							tempStorage.add(j + 2, tempStorage.get(j));
							tempStorage.remove(j);
							isSorted = false;
							continue;

						} else if (!tempStorage.get(j).getDate().equals(Constants.DATE_FT)
								&& tempStorage.get(j + 1).getDate()
								.equals(Constants.DATE_FT)) {
							continue;
						} else if( tempStorage.get(j).getDate().equals(Constants.DATE_FT)
								&& tempStorage.get(j + 1).getDate()
								.equals(Constants.DATE_FT)) {
							if(firstTaskOrder>secondTaskOrder){
								tempStorage.add(j + 2, tempStorage.get(j));
								tempStorage.remove(j);
								isSorted = false;
							}
							continue;
						}else {

							dateFirst = Constants.dateFormat.parse(tempStorage.get(
									j).getDate());

							dateSecond = Constants.dateFormat.parse(tempStorage
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


									timeFirst = Constants.timeFormatOne
											.parse(tempStorage.get(j).getTime());

									timeSecond = Constants.timeFormatOne
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
						Task.setSortedByTime(true);
						return  String.format(Constants.MSG_SORT_SUCCESS, "date and time");
					}
				}
				
			} catch (ParseException e) {
			}
			Task.setSortedByTime(true);
			return String.format(Constants.MSG_SORT_SUCCESS, "date and time");
		}
	}

	public static String sortByAlphabet(ArrayList<Task> tempStorage) {
		if (tempStorage.size() < 1) {
			return Constants.MSG_NO_TASKS_TO_SORT;
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
					return String.format(Constants.MSG_SORT_SUCCESS, "alphabetical order");
				}

			}
			return Constants. MSG_SORT_FAIL;
		}
	}

	public static String sortByImportance(ArrayList<Task> tempStorage) {
		if (tempStorage.size() < 1) {

			return Constants.MSG_NO_TASKS_TO_SORT;

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
					return String.format(Constants.MSG_SORT_SUCCESS, "importance level");
				}
			}
		}
		return Constants. MSG_SORT_FAIL;
	}
	
	//To remove the search in the undo list after searching for clash
	public static void undoPopForSearchClash(){
		undo.pop();
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
			Date currentDate = new Date();
			currentDate = removeTime(currentDate);
			for (int i = 0; i < tempStorage.size(); i++) {
				if (tempStorage.get(i).getDate().contains(Constants.DATE_FT)) {
					return i+1;
				} else {
					try {
						Date dateOfCurrentTask = Constants.dateFormat.parse(tempStorage.get(i).getDate());
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
