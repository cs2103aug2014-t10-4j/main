/**
 * This program implement the logic for the TextBuddy.
 * 
 */
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Logic {

	private static ArrayList<Task> tempStorage = new ArrayList<Task>();
	private static ArrayList<Task> archiveStorage = new ArrayList<Task>();
	private static ArrayList<Task> searchResults = new ArrayList<Task>();
	private static Stack<String> undo = new Stack<String>();
	private static Stack<String> redo = new Stack<String>();
	private static Stack<ArrayList<Task>> undoTask = new Stack<ArrayList<Task>>();
	private static Stack<ArrayList<Task>> redoTask = new Stack<ArrayList<Task>>();
	private static Logger logger = Logger.getLogger("Logic");

	// Methods required during start-up
	public static ArrayList<Integer> init(File file, File archive) {
		while (!Storage.copyToArrayList(file, tempStorage));
		while (!Storage.copyToArrayList(archive, archiveStorage));
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

	private static void getNumTasks(ArrayList<Integer> numTask,
			ArrayList<Task> tempStorage) {
		Date currentDate = new Date();
		try {
			currentDate = Constants.dateFormat.parse(Constants.dateFormat
					.format(currentDate));
		} catch (ParseException e1) {
			logger.log(Level.WARNING, String.format(Constants.INVALID_DATE_FORMAT, "getNumTasks"));
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
					Date dateOfCurrentTask = Constants.dateFormat
							.parse(tempStorage.get(i).getDate());
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
		} catch (ParseException e) {
			logger.log(Level.WARNING, String.format(Constants.INVALID_DATE_FORMAT, "getNumTasks"));
		}
	}

	// Methods to manipulate task
	public static String add(String command, Task task, File file) {
		String returnMessage;
		if (undo.size() != 0 && undo.peek().equals(Constants.COMMAND_SEARCH)) {
			undo.pop();
		}
		if (command.equals(Constants.COMMAND_ADD)) {
			Integer taskNumber = tempStorage.size() + 1;
			task.setParams(taskNumber.toString());
			Task taskToAdd = new Task();
			taskToAdd.copyOfTask(task);
			ArrayList<Task> addedTask = new ArrayList<Task>();
			addedTask.add(taskToAdd);
			returnMessage = addLineToFile(task, file, tempStorage.size());
			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			updateUndo(Constants.COMMAND_ADD, addedTask);
			clearRedo();
			return returnMessage;
		}

		if (command.equals(Constants.COMMAND_UNDO)
				|| command.equals(Constants.COMMAND_REDO)) {
			int taskToAddLocation = getIndex(task) + 1;
			if (tempStorage.size() == Constants.ZERO) {
				taskToAddLocation = Constants.ZERO;
			} else {
				for (int i = 0; i < tempStorage.size(); i++) {
					int currentTaskParams = getIndex(tempStorage.get(i)) + 1;
					if (taskToAddLocation < currentTaskParams) {
						taskToAddLocation = i;
						break;
					} else if (taskToAddLocation > currentTaskParams
							&& i == tempStorage.size() - 1) {
						taskToAddLocation = tempStorage.size();
					}
				}
			}
			returnMessage = addLineToFile(task, file, taskToAddLocation);
			return returnMessage;
		}

		return Constants.MSG_FAIL_ADD;

	}

	private static String addLineToFile(Task task, File file, int taskLocation) {
		tempStorage.add(taskLocation, task);
		return String.format(Constants.ADD_MESSAGE, file.getName(),
				task.getName());
	}

	public static String edit(String command, Task detailsOfTask, File file) {
		String returnMessage;
		String lastCommand = null;
		if (undo.size() != 0 && undo.peek().equals(Constants.COMMAND_SEARCH)) {
			lastCommand = undo.pop();
		}
		if (lastCommand != null && lastCommand.equals(Constants.COMMAND_SEARCH)) {
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
					updateUndo(command, tasksEdited);
					clearRedo();
					return returnMessage;
				}
			}

		}
		if (command.equals(Constants.COMMAND_EDIT)) {
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
			updateUndo(command, tasksEdited);
			clearRedo();

			return returnMessage;
		} else if (command.equals(Constants.COMMAND_UNDO)
				|| command.equals(Constants.COMMAND_REDO)) {
			int taskNumber = getIndex(detailsOfTask);
			returnMessage = editUndoRedo(detailsOfTask, file, taskNumber);
			return returnMessage;
		} else {
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
		if (detailsOfTask.getStartTime() != null
				&& detailsOfTask.getEndTime() != null) {
			if (detailsOfTask.getStartTime().equals(Constants.NO_TIME)) {
				detailsOfTask.setStartTime(null);
			}
			tempStorage.get(taskNumber).setStartTime(
					detailsOfTask.getStartTime());
			tempStorage.get(taskNumber).setEndTime(detailsOfTask.getEndTime());
		} else if (detailsOfTask.getStartTime() != null
				&& detailsOfTask.getEndTime() == null) {
			if (detailsOfTask.getStartTime().equals(Constants.NO_TIME)) {
				detailsOfTask.setStartTime(null);
			}
			tempStorage.get(taskNumber).setStartTime(
					detailsOfTask.getStartTime());
			tempStorage.get(taskNumber).setEndTime(null);
		}

		if (detailsOfTask.getDetails() != null) {
			tempStorage.get(taskNumber).setDetails(detailsOfTask.getDetails());
		} else if (detailsOfTask.getDetails() != null
				&& detailsOfTask.getDetails().equals("")) {
			tempStorage.get(taskNumber).setDetails(null);
		}

		if (detailsOfTask.getImportance() != Constants.ZERO - 1) {
			tempStorage.get(taskNumber).setImportance(
					detailsOfTask.getImportance());
		}
		return Constants.MSG_EDIT_SUCCESS;
	}

	private static String editUndoRedo(Task detailsOfTask, File file,
			int taskNumber) {
		tempStorage.get(taskNumber).setStartTime(detailsOfTask.getStartTime());
		tempStorage.get(taskNumber).setEndTime(detailsOfTask.getEndTime());
		tempStorage.get(taskNumber).setName(detailsOfTask.getName());
		tempStorage.get(taskNumber).setDate(detailsOfTask.getDate());
		tempStorage.get(taskNumber).setDetails(detailsOfTask.getDetails());
		tempStorage.get(taskNumber)
				.setImportance(detailsOfTask.getImportance());
		return Constants.MSG_EDIT_SUCCESS;
	}

	public static String delete(String command, int numOfTaskToDelete,
			Task task, File file, File archive) {
		String returnMessage;
		if (tempStorage.size() == 0) {
			if (undo.size() != 0
					&& undo.peek().equals(Constants.COMMAND_MIDWAY_DELETE)) {
				undo.pop();
				undo.push(Constants.COMMAND_DELETE);
			}
			return Constants.MSG_NTH_DELETE;
		}
		if (Integer.parseInt(task.getParams()) <= 0
				&& command.equals(Constants.COMMAND_DELETE)) {
			if (undo.size() != 0
					&& undo.peek().equals(Constants.COMMAND_MIDWAY_DELETE)) {
				undo.pop();
				undo.push(Constants.COMMAND_DELETE);
			}
			return Constants.MSG_FAIL_DELETE;
		} else if (command.equals(Constants.COMMAND_DELETE)) {
			String commandCheck = null;
			if (undo.size() != 0
					&& undo.peek().equals(Constants.COMMAND_MIDWAY_DELETE)) {
				commandCheck = undo.pop();
			}
			// DELETE FROM SEARCH LIST
			if (undo.size() != 0
					&& undo.peek().equals(Constants.COMMAND_SEARCH)
					&& searchResults.size() != 0) {
				int index = getIndex(task);
				Task taskToDelete = new Task();
				taskToDelete.copyOfTask(searchResults.get(index));
				returnMessage = deleteLineFromSearchList(task, searchResults,
						file, archive);
				if (commandCheck == null) {
					ArrayList<Task> deletedTask = new ArrayList<Task>();
					deletedTask.add(taskToDelete);
					assert (deletedTask.size() <= numOfTaskToDelete);
					if (deletedTask.size() == numOfTaskToDelete || tempStorage.size()== Constants.ZERO || index == Constants.ZERO) {
						undo.pop(); // remove Constants.COMMAND_SEARCH
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						updateUndo(command, deletedTask);
						clearRedo();
						return returnMessage;
					} else {
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						updateUndo(Constants.COMMAND_MIDWAY_DELETE, deletedTask);
						return returnMessage;
					}
				}
				if (commandCheck.equals(Constants.COMMAND_MIDWAY_DELETE)) {
					ArrayList<Task> deletedTask = undoTask.pop();
					deletedTask.add(taskToDelete);
					assert (deletedTask.size() <= numOfTaskToDelete);
					if (deletedTask.size() == numOfTaskToDelete || tempStorage.size()== Constants.ZERO || index == Constants.ZERO) {
						undo.pop(); // remove Constants.COMMAND_SEARCH
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						updateUndo(command, deletedTask);
						clearRedo();
						return returnMessage;
					} else {
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						updateUndo(Constants.COMMAND_MIDWAY_DELETE, deletedTask);
						return returnMessage;
					}
				}
			} else { // DELETE FROM NORMAL STORAGE
				if (undo.size() != 0
						&& undo.peek().equals(Constants.COMMAND_SEARCH)
						&& searchResults.size() == 0) {
					undo.pop();
				}
				int index = getIndex(task);
				Task taskToDelete = new Task();
				taskToDelete.copyOfTask(tempStorage.get(index));
				returnMessage = deleteLineFromFile(index, task, file, archive);
				if (commandCheck == null) {
					ArrayList<Task> deletedTask = new ArrayList<Task>();
					deletedTask.add(taskToDelete);
					assert (deletedTask.size() <= numOfTaskToDelete || tempStorage.size()== Constants.ZERO || index == Constants.ZERO );
					if (deletedTask.size() == numOfTaskToDelete) {
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						updateUndo(command, deletedTask);
						clearRedo();
						return returnMessage;
					} else {
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						updateUndo(Constants.COMMAND_MIDWAY_DELETE, deletedTask);
						return returnMessage;
					}
				}
				if (commandCheck.equals(Constants.COMMAND_MIDWAY_DELETE)) {
					ArrayList<Task> deletedTask = undoTask.pop();
					deletedTask.add(taskToDelete);
					assert (deletedTask.size() <= numOfTaskToDelete);
					if (deletedTask.size() == numOfTaskToDelete || tempStorage.size()== Constants.ZERO || index == Constants.ZERO) {
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						updateUndo(command, deletedTask);
						clearRedo();
						return returnMessage;
					} else {
						sortByDateAndTime(tempStorage);
						sortByDateAndTime(archiveStorage);
						Storage.writeToFile(tempStorage, file);
						Storage.writeToFile(archiveStorage, archive);
						updateUndo(Constants.COMMAND_MIDWAY_DELETE, deletedTask);
						return returnMessage;
					}
				}
			}

		} else if (command.equals(Constants.COMMAND_REDO)) {
			int index = tempStorage.indexOf(task);
			returnMessage = deleteLineFromFile(index, task, file, archive);
			sortByDateAndTime(archiveStorage);
			Storage.writeToFile(archiveStorage, archive);
			return returnMessage;
		} else if (command.equals(Constants.COMMAND_UNDO)) {
			int index = tempStorage.indexOf(task);
			returnMessage = deleteLineFromFile(index, task, file, archive);
			archiveStorage.remove(archiveStorage.indexOf(task));
			Storage.writeToFile(archiveStorage, archive);
			return returnMessage;
		} else {
			return Constants.MSG_DELETE_FAIL;
		}
		return Constants.MSG_DELETE_FAIL;
	}

	private static String deleteLineFromFile(int index, Task task, File file,
			File archive) {
		if (index == Constants.INVAILD_NUMBER) {
			return Constants.MSG_NTH_DELETE;
		}
		try {
			String returnMessage = String.format(Constants.MSG_DELETE_SUCCESS,
					index + 1, tempStorage.get(index).getName());

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
		return String.format(Constants.MSG_DELETE_SUCCESS, index + 1,
				searchResults.get(index).getName());
	}

	public static String clearContent(File file, File archive) {
		if (undo.size() != 0 && undo.peek().equals(Constants.COMMAND_SEARCH)) {
			undo.pop();
		}
		if (tempStorage.size() > 0) {
			ArrayList<Task> deletedTask = new ArrayList<Task>();
			for (int i = 0; i < tempStorage.size(); i++) {
				Task taskToDelete = new Task();
				taskToDelete.copyOfTask(tempStorage.get(i));
				deletedTask.add(taskToDelete);
				Task taskToArchive = new Task();
				taskToArchive.copyOfTask(tempStorage.get(i));
				archiveStorage.add(taskToArchive);
			}
			tempStorage.clear();
			Storage.writeToFile(new ArrayList<Task>(), file);
			sortByDateAndTime(archiveStorage);
			Storage.writeToFile(archiveStorage, archive);
			updateUndo(Constants.COMMAND_DELETE_ALL, deletedTask);
			return Constants.MSG_CLEARED_FILE;
		} else {
			return Constants.NO_MESSAGE_CLEAR;
		}
	}

	public static String clearArchive(File file) {
		if (undo.size() != 0 && undo.peek().equals(Constants.COMMAND_SEARCH)) {
			undo.pop();
		}
		if (archiveStorage.size() > 0) {
			archiveStorage.clear();
			Storage.writeToFile(new ArrayList<Task>(), file);
			return Constants.MSG_CLEARED_FILE;
		} else {
			return Constants.NO_MESSAGE_CLEAR;
		}
	}

	private static int getIndex(Task task) {
		try {
			return Integer.parseInt(task.getParams()) - 1;
		} catch (NumberFormatException e) {
			System.out.println(String.format(Constants.WRONG_FORMAT,
					task.getParams()));
		}
		return Constants.INVAILD_NUMBER;
	}


	//@author A0108380L
	// This function searches for task in tempStorage
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
			searchResults.add(taskInList);
		}
		undo.push(Constants.COMMAND_SEARCH);
		return searchResults;
	}

	// This function searches for tasks in tempStorage which occurs on the same date and time as the taskToFind
	public static ArrayList<Task> searchForCheckClash(Task taskToFind) {
		searchResults.clear();

		assert tempStorage.size() >= 0 : "tempStorage.size() is negative";
		if (taskToFind.getStartTime() != null
				&& taskToFind.getStartTime().equals(Constants.NO_TIME)) {
			return searchResults;
		}
		if (taskToFind.getDate() == null) {
			int taskLocation = getIndex(taskToFind);
			taskToFind.setDate(tempStorage.get(taskLocation).getDate());
		}
		try {
			for (int i = 0; i < tempStorage.size(); i++) {
				Task taskInList = tempStorage.get(i);
				// Filtering name
				if (taskToFind.getDate() != null
						&& taskInList.getDate() != null
						&& !taskInList.getDate().equals(taskToFind.getDate())) {
					continue;
				}
				if (taskToFind.getDate() != null
						&& taskInList.getDate() == null) {
					continue;
				}

				if (taskToFind.getStartTime() != null
						&& taskToFind.getEndTime() != null) {
					Date timeFindStart = Constants.timeFormatOne
							.parse(taskToFind.getStartTime());
					Date timeFindEnd = Constants.timeFormatOne.parse(taskToFind
							.getEndTime());
					if (taskInList.getStartTime() != null
							&& taskInList.getEndTime() != null) {
						Date timeInListStart = Constants.timeFormatOne
								.parse(taskInList.getStartTime());
						Date timeInListEnd = Constants.timeFormatOne
								.parse(taskInList.getEndTime());

						if (timeFindStart.compareTo(timeInListEnd) >= 0
								|| timeFindEnd.compareTo(timeInListStart) <= 0) {
							continue;
						} else {
							searchResults.add(taskInList);
							continue;
						}
					} else if (taskInList.getStartTime() != null
							&& taskInList.getEndTime() == null) {
						Date timeInListStart = Constants.timeFormatOne
								.parse(taskInList.getStartTime());
						if (timeFindStart.compareTo(timeInListStart) > 0
								|| timeFindEnd.compareTo(timeInListStart) <= 0) {
							continue;
						} else {
							searchResults.add(taskInList);
						}
					}
				} else if (taskToFind.getStartTime() != null
						&& taskToFind.getEndTime() == null) {
					Date timeFindStart = Constants.timeFormatOne
							.parse(taskToFind.getStartTime());

					if (taskInList.getStartTime() != null
							&& taskInList.getEndTime() != null) {
						Date timeInListStart = Constants.timeFormatOne
								.parse(taskInList.getStartTime());
						Date timeInListEnd = Constants.timeFormatOne
								.parse(taskInList.getEndTime());
						if (timeFindStart.compareTo(timeInListEnd) >= 0
								|| timeFindStart.compareTo(timeInListStart) < 0) {
							continue;
						} else {
							searchResults.add(taskInList);
							continue;
						}
					} else if (taskInList.getStartTime() != null
							&& taskInList.getEndTime() == null) {
						Date timeInListStart = Constants.timeFormatOne
								.parse(taskInList.getStartTime());
						if (timeFindStart.compareTo(timeInListStart) != 0) {
							continue;
						} else {
							searchResults.add(taskInList);
							continue;
						}
					}
				}

				if (taskToFind.getStartTime() != null
						&& taskInList.getStartTime() == null) {
					continue;
				}
				searchResults.add(taskInList);
			}
		} catch (ParseException e) {
			logger.log(Level.WARNING, String.format(Constants.INVALID_TIME_FORMAT, "searchForCheckClash"));
		}
		return searchResults;
	}

	// This function search for the tasks in tempStorage within a range of dates.
	public static ArrayList<Task> searchRangeOfDate(Task taskStartOfRange,
			Task taskEndOfRange) {
		searchResults.clear();
		Date dateStart = new Date();
		Date dateEnd = new Date();
		try {
			dateStart = Constants.dateFormat.parse(taskStartOfRange.getDate());
			dateEnd = Constants.dateFormat.parse(taskEndOfRange.getDate());

			assert tempStorage.size() >= 0 : "tempStorage.size() is negative";

			for (int i = 0; i < tempStorage.size(); i++) {
				if (tempStorage.get(i).getDate().equals(Constants.DATE_FT)) {
					continue;
				} else {
					Date dateCheck = new Date();
					Task taskInList = tempStorage.get(i);
					dateCheck = Constants.dateFormat
							.parse(taskInList.getDate());

					if (dateCheck.compareTo(dateStart) >= 0
							&& dateCheck.compareTo(dateEnd) <= 0) {
						searchResults.add(taskInList);
					}
				}
			}
		} catch (ParseException e) {
			logger.log(Level.WARNING, String.format(Constants.INVALID_DATE_FORMAT, "searchRangeOfDate"));
		}
		undo.push(Constants.COMMAND_SEARCH);
		return searchResults;
	}

	// This function undo the last action done by the user.
	public static String undo(File file, File archive) {
		String current= undo.peek();
		System.out.println (current);
		if (undo.empty()) {
			return Constants.MSG_NO_PREVIOUS_ACTION;
		} else if (undo.size() != 0
				&& undo.peek().equals(Constants.COMMAND_SEARCH)) {
			undo.pop();
			return undo(file, archive);
		} else if (undo.peek().equals(Constants.COMMAND_MIDWAY_DELETE)){ 
			undo.pop();
			undo.push(Constants.COMMAND_DELETE);
			return undo(file, archive);
		}else {

			String lastCommand = undo.pop();

			if (lastCommand.equals(Constants.COMMAND_ADD)) {
				ArrayList<Task> taskToBeDeleted = undoTask.pop();
				Task taskToDelete = new Task();
				Integer taskLocation = tempStorage.indexOf(taskToBeDeleted
						.get(Constants.ZERO)) + 1;
				taskToDelete.setParams(taskLocation.toString());
				delete(Constants.COMMAND_UNDO, taskToBeDeleted.size(),
						taskToBeDeleted.get(Constants.ZERO), file,
						archive);
				updateRedo(lastCommand, taskToBeDeleted);
			}
			else if (lastCommand.equals(Constants.COMMAND_DELETE)
					|| lastCommand.equals(Constants.COMMAND_DELETE_ALL)) {
				ArrayList<Task> taskToBeAdded = undoTask.pop();
				for (int i = 0; i < taskToBeAdded.size(); i++) {
					Task taskToAdd = new Task();
					taskToAdd.copyOfTask(taskToBeAdded.get(i));
					add(Constants.COMMAND_UNDO, taskToAdd, file);
					if (archiveStorage.size() > 0) {
						if (archiveStorage.contains(taskToAdd)) {
							archiveStorage.remove(archiveStorage
									.indexOf(taskToAdd));
						}
					}
				}
				sortByDateAndTime(archiveStorage);
				Storage.writeToFile(archiveStorage, archive);
				updateRedo(lastCommand, taskToBeAdded);
			}
			else if (lastCommand.equals(Constants.COMMAND_EDIT)) {
				Task undoEditedTask = new Task();
				ArrayList<Task> taskToBeEdited = undoTask.pop();
				Integer taskNumber = tempStorage.indexOf(taskToBeEdited
						.get(Constants.ZERO + 1)) + 1;
				undoEditedTask.copyOfTask(taskToBeEdited
						.get(Constants.ZERO));
				undoEditedTask.setParams(taskNumber.toString());
				edit(Constants.COMMAND_UNDO, undoEditedTask, file);
				updateRedo(lastCommand, taskToBeEdited);
			}
			else{
				assert false; // Execution should never reach this point
			}
			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			return Constants.MSG_UNDO_SUCCESS;
		}
	}

	// This function redo the last action undone by the user
	public static String redo(File file, File archive) {
		if (redo.empty()) {
			return Constants.MSG_NO_FUTURE_ACTION;
		} else if (undo.size() != 0
				&& undo.peek().equals(Constants.COMMAND_SEARCH)) {
			undo.pop();
			return redo(file, archive);
		} else {
			String lastCommand = redo.pop();
			if (lastCommand.equals(Constants.COMMAND_ADD)) {
				ArrayList<Task> taskToBeAdded = redoTask.pop();
				add(Constants.COMMAND_REDO,
						taskToBeAdded.get(Constants.ZERO), file);
				updateUndo(lastCommand, taskToBeAdded);

			}

			else if (lastCommand.equals(Constants.COMMAND_DELETE)) {
				ArrayList<Task> taskToBeDeleted = redoTask.pop();
				for (int i = 0; i < taskToBeDeleted.size(); i++) {
					delete(Constants.COMMAND_REDO, taskToBeDeleted.size(),
							taskToBeDeleted.get(i), file, archive);
				}
				updateUndo(lastCommand, taskToBeDeleted);
			}
			else if (lastCommand.equals(Constants.COMMAND_EDIT)) {
				Task redoEditedTask = new Task();
				ArrayList<Task> taskToBeEdited = redoTask.pop();
				Integer taskNumber = tempStorage.indexOf(taskToBeEdited
						.get(Constants.ZERO)) + 1;
				redoEditedTask.copyOfTask(taskToBeEdited
						.get(Constants.ZERO + 1));
				redoEditedTask.setParams(taskNumber.toString());
				edit(Constants.COMMAND_REDO, redoEditedTask, file);
			
				updateUndo(lastCommand, taskToBeEdited);
			}
			else if (lastCommand.equals(Constants.COMMAND_DELETE_ALL)) {
				clearContent(file, archive);
			}
			else{
				assert false; // Execution should never reach this point
			}
			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			return Constants.MSG_REDO_SUCCESS;
		}
	}

	/* This method sort the ArrayList based on Date and Time
	 * Task with dates will be in front of the list and floating task will be at the end
	 * Task without time will be in front of the task with time in the same day.
	 * If task have same date and time, it will be based on the order of being added.
	 */
	public static String sortByDateAndTime(ArrayList<Task> listOfTask) {
		Task.setSortedByTime(true);
		Date dateFirst = new Date();
		Date dateSecond = new Date();
		Date timeFirst = new Date();
		Date timeSecond = new Date();
		Constants.dateFormat.setLenient(false);
		Constants.timeFormatOne.setLenient(false);
		if (listOfTask.size() < 1) {
			return Constants.MSG_NO_TASKS_TO_SORT;
		} else {
			try {
				for (int i = 0; i < listOfTask.size(); i++) {
					boolean isSorted = true;

					for (int j = 0; j < listOfTask.size() - 1; j++) {
						assert(listOfTask.get(j).getDate()!=null);
						assert(listOfTask.get(j + 1).getDate()!=null);
						int firstTaskOrder = Integer.parseInt(listOfTask
								.get(j).getParams());
						int secondTaskOrder = Integer.parseInt(listOfTask.get(
								j + 1).getParams());
						if (listOfTask.get(j).getDate()
								.equals(Constants.DATE_FT)
								&& !listOfTask.get(j + 1).getDate()
										.equals(Constants.DATE_FT)) {
							listOfTask.add(j + 2, listOfTask.get(j));
							listOfTask.remove(j);
							isSorted = false;
							continue;
						} else if (!listOfTask.get(j).getDate()
								.equals(Constants.DATE_FT)
								&& listOfTask.get(j + 1).getDate()
										.equals(Constants.DATE_FT)) {
							continue;
						} else if (listOfTask.get(j).getDate()
								.equals(Constants.DATE_FT)
								&& listOfTask.get(j + 1).getDate()
										.equals(Constants.DATE_FT)) {
							if (firstTaskOrder > secondTaskOrder) {
								listOfTask.add(j + 2, listOfTask.get(j));
								listOfTask.remove(j);
								isSorted = false;
							}
							continue;
						} else {
							dateFirst = Constants.dateFormat.parse(listOfTask
									.get(j).getDate());
							dateSecond = Constants.dateFormat.parse(listOfTask
									.get(j + 1).getDate());
							if (dateFirst.compareTo(dateSecond) > 0) {
								listOfTask.add(j + 2, listOfTask.get(j));
								listOfTask.remove(j);
								isSorted = false;
								continue;
							} else if (dateFirst.compareTo(dateSecond) == 0) {
								if (listOfTask.get(j).getStartTime() == null) {
									continue;
								} else if (listOfTask.get(j).getStartTime() != null
										&& listOfTask.get(j + 1)
												.getStartTime() == null) {
									listOfTask.add(j + 2, listOfTask.get(j));
									listOfTask.remove(j);
									isSorted = false;
									continue;
								} else {
									timeFirst = Constants.timeFormatOne
											.parse(listOfTask.get(j)
													.getStartTime());
									timeSecond = Constants.timeFormatOne
											.parse(listOfTask.get(j + 1)
													.getStartTime());
									if (timeFirst.compareTo(timeSecond) > 0) {
										listOfTask.add(j + 2,
												listOfTask.get(j));
										listOfTask.remove(j);
										isSorted = false;
										continue;
									} else if (timeFirst.compareTo(timeSecond) == 0) {

										if (firstTaskOrder > secondTaskOrder) {
											listOfTask.add(j + 2,
													listOfTask.get(j));
											listOfTask.remove(j);
											isSorted = false;
										}
										continue;
									} else {
										continue;
									}
								}
							} else {
								continue;
							}
						}
					}
					if (isSorted) {
						Task.setSortedByTime(true);
						return String.format(Constants.MSG_SORT_SUCCESS,
								"date and time");
					}
				}

			} catch (ParseException e) {
				logger.log(Level.WARNING, String.format(Constants.INVALID_CHRONO_FORMAT, "sortByDateAndTime"));
			}
			Task.setSortedByTime(true);
			return String.format(Constants.MSG_SORT_SUCCESS, "date and time");
		}
	}

	// This method is to sort the ArrayList in alphabetical order.
	public static String sortByAlphabet(ArrayList<Task> listOfTask) {
		if (listOfTask.size() < 1) {
			return Constants.MSG_NO_TASKS_TO_SORT;
		} else {
			for (int i = 0; i < listOfTask.size(); i++) {
				boolean isSorted = true;
				for (int j = 0; j < listOfTask.size() - 1; j++) {
					assert (listOfTask.get(j).getName()!= null);
					if (listOfTask
							.get(j)
							.getName()
							.compareToIgnoreCase(
									listOfTask.get(j + 1).getName()) > 0) {
						listOfTask.add(j + 2, listOfTask.get(j));
						listOfTask.remove(j);
						isSorted = false;
					}
				}
				if (isSorted) {
					return String.format(Constants.MSG_SORT_SUCCESS,
							"alphabetical order");
				}
			}
			return Constants.MSG_SORT_FAIL;
		}
	}

	// This method is to sort the ArrayList based on importance from most important to least.
	public static String sortByImportance(ArrayList<Task> listOfTask) {
		if (listOfTask.size() < 1) {
			return Constants.MSG_NO_TASKS_TO_SORT;
		} else {
			for (int i = 0; i < listOfTask.size(); i++) {
				boolean isSorted = true;
				for (int j = 0; j < listOfTask.size() - 1; j++) {
					if (listOfTask.get(j).getImportance() < listOfTask.get(
							j + 1).getImportance()) {
						listOfTask.add(j + 2, listOfTask.get(j));
						listOfTask.remove(j);
						isSorted = false;
					}
				}
				if (isSorted) {
					return String.format(Constants.MSG_SORT_SUCCESS,
							"importance level");
				}
			}
		}
		return Constants.MSG_SORT_FAIL;
	}

	// This method is to update the undo stack and undoTask stack.
	private static void updateUndo(String command, ArrayList<Task> listOfTask) {
		undo.push(command);
		undoTask.push(listOfTask);
	}

	// This method is to update the redo stack and redoTask stack.
	private static void updateRedo(String command, ArrayList<Task> listOfTask) {
		redo.push(command);
		redoTask.push(listOfTask);
	}

	// This method is to clear all information in the redo stack and redoTask stack
	private static void clearRedo() {
		redo.clear();
		redoTask.clear();
	}

	// Following methods are used for junit testing.
	// This method is used to check the content of tempStorage for junit testing.
	public static String printTempStorage() {
		String string = "";
		for (int i = 0; i < tempStorage.size(); i++) {
			Integer importance = tempStorage.get(i).getImportance();
			string = string + tempStorage.get(i).getName() + " "
					+ tempStorage.get(i).getDate() + " "
					+ tempStorage.get(i).getStartTime() + " "
					+ tempStorage.get(i).getEndTime() + " "
					+ tempStorage.get(i).getDetails() + " "
					+ importance.toString() + " "
					+ tempStorage.get(i).getParams() + "\n";
		}
		return string;
	}

	// This method is used to check the content of archiveStorage for junit testing.
	public static String printTempArchive() {
		String string = "";
		for (int i = 0; i < archiveStorage.size(); i++) {
			Integer importance = archiveStorage.get(i).getImportance();
			string = string + archiveStorage.get(i).getName() + " "
					+ archiveStorage.get(i).getDate() + " "
					+ archiveStorage.get(i).getStartTime() + " "
					+ archiveStorage.get(i).getEndTime() + " "
					+ archiveStorage.get(i).getDetails() + " "
					+ importance.toString() + " "
					+ archiveStorage.get(i).getParams() + "\n";
		}
		return string;
	}

	// This method is used to clear the undo and redo stack at the start of each junit test.
	public static void clearUndoRedo() {
		undo.clear();
		undoTask.clear();
		clearRedo();
	}

	// This method is used to clear the file at the start of each junit test.
	public static void clearAll(File file) {
		Storage.writeToFile(new ArrayList<Task>(), file);
	}

	// This method is to call sortByAlphabet for junit test
	public static void sortAlpha() {
		sortByAlphabet(tempStorage);
	}

	// This method is to call sortByDateAndTime for junit test
	public static void sortChrono() {
		sortByDateAndTime(tempStorage);
	}

	// This method is to call sortByImporance for junit test
	public static void sortImportance() {
		sortByImportance(tempStorage);
	}

	// Returns first index of non-overdue. Use in deleting past tasks before
	// that index.
	public static int getFirstNotOverdueInList() {
		Date currentDate = new Date();
		currentDate = removeTime(currentDate);
		for (int i = 0; i < tempStorage.size(); i++) {
			if (tempStorage.get(i).getDate().contains(Constants.DATE_FT)) {
				return i + 1;
			} else {
				try {
					Date dateOfCurrentTask = Constants.dateFormat
							.parse(tempStorage.get(i).getDate());
					dateOfCurrentTask = removeTime(dateOfCurrentTask);
					if (dateOfCurrentTask.compareTo(currentDate) >= 0) {
						return i + 1;
					}
				} catch (ParseException e) {
					logger.log(Level.WARNING, String.format(Constants.INVALID_DATE_FORMAT, "getFirstNotOverdueInList"));

				}
			}
		}
		return tempStorage.size() + 1;
	}

	// For use in delete past. Need to remove time of all dates for comparison.
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
