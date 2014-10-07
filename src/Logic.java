/**
 * This program implement the logic for the TextBuddy.
 * 
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Logic {
	private static final String MSG_FAIL_ADD = "Unable to add line.";
	private static final String DELETE_MESSAGE = "deleted from %s: \"%s\"";
	private static final String WRONG_FORMAT = "\"%s\" is wrong format";
	private static final String BAD_INDEX_MESSAGE = "%d is not a valid number.Valid range is %d to %d.";
	public static String ADD_MESSAGE = "added to %s: \"%s\"";
	private static final int INITIAL_VALUE = 0;
	private static final String NO_MESSAGE_DELETE = "nothing to delete!";
	private static final int INVAILD_NUMBER = -1;

	private static ArrayList<Task> tempStorage = new ArrayList<Task>();
	private static ArrayList<Task> memory = new ArrayList<Task>();
	private static ArrayList<Task> searchTask;
	private static ArrayList<Task> searchResults = new ArrayList<Task>();
	private File file;

	/*
	 * 
	 * public static String delete(int number) {
	 * 
	 * String deleteTask; try { deleteTask = tempStorage.remove(number); } catch
	 * (Exception e) { deleteTask = null; } return deleteTask;
	 * 
	 * }
	 * 
	 * public void sort() { Collections.sort(tempStorage); writeTextFile(null,
	 * file); }
	 * 
	 * public ArrayList<String> search(String word) { ArrayList<String> result =
	 * new ArrayList<String>(); for (String task : tempStorage) { if
	 * (task.contains(word)) { result.add(task); } } return result; }
	 * 
	 * public void clear() { tempStorage.clear(); writeTextFile(null, file); }
	 * 
	 * public static void readFile(String FileName) throws IOException {
	 * BufferedReader reader = null; try { reader = new BufferedReader(new
	 * FileReader(FileName)); String text; while ((text = reader.readLine()) !=
	 * null) { tempStorage.add(text.substring(3, text.length())); } } catch
	 * (IOException e) { e.printStackTrace(); } finally { try { reader.close();
	 * } catch (IOException e) { e.printStackTrace(); } } }
	 * 
	 * private static void writeTextFile(String inputText, File file) {
	 * BufferedWriter outputFile; try { outputFile = new BufferedWriter( new
	 * FileWriter(file.getName(), true)); if (!isEmpty(file))
	 * outputFile.write(inputText); outputFile.close(); } catch (IOException e)
	 * { System.out.println("Error: " + e.getMessage()); } }
	 * 
	 * private static boolean isEmpty(File file) { return file.length() <= 0; }
	 */

	public static String addLineToFile(Task task, File file) {
		if (task.getName() == null) {
			return "error";
		}
		tempStorage.add(task);
		sortByDateAndTime(tempStorage);
		Storage.writeToFile(tempStorage, file);

		return String.format(ADD_MESSAGE, file.getName(), task.getName());
	}

	public static String deleteLineFromFile(Task task, File file) {
		if (tempStorage.size() == 0) {
			return NO_MESSAGE_DELETE;
		}
		int index = getIndex(task);
		return removeText(index, task, file);
	}

	public static int getIndex(Task task) {
		try {
			return Integer.parseInt(task.getParams()) - 1;
		} catch (NumberFormatException e) {
			System.out.println(String.format(WRONG_FORMAT, task.getParams()));
		}
		return INVAILD_NUMBER;
	}

	public static String removeText(int index, Task task, File file) {
		if (index == INVAILD_NUMBER) {
			return NO_MESSAGE_DELETE;
		}
		try {
			String temp = String.format(DELETE_MESSAGE, file.getName(),
					tempStorage.remove(index));
			sortByDateAndTime(tempStorage);
			Storage.writeToFile(tempStorage, file);
			return temp;
		} catch (IndexOutOfBoundsException e) {
			return String.format(BAD_INDEX_MESSAGE, index, 1,
					tempStorage.size());
		}
	}

	public static ArrayList<Task> search(Task task) {
		for (int i = 0; i < tempStorage.size(); i++) {
			if (!task.getName().equals("null")
					&& !tempStorage.get(i).getName().contains(task.getName())) {
				continue;
			}
			if (!task.getDate().equals("null")
					&& !tempStorage.get(i).getDate().contains(task.getDate())) {
				continue;
			}
			if (!task.getTime().equals("null")
					&& !tempStorage.get(i).getTime().contains(task.getTime())) {
				continue;
			}
			if (!task.getDetails().equals("null")
					&& !tempStorage.get(i).getDetails()
							.contains(task.getDetails())) {
				continue;
			}
			if (task.getImportance() != -1
					&& tempStorage.get(i).getImportance() != task
							.getImportance()) {
				continue;
			}
			searchResults.add(tempStorage.get(i));
		}

		return searchResults;
	}

	/*
	 * public static String deleteLineFromSearchList (Task task, ArrayList<Task>
	 * memory ){ if (memory.size() == 0) { return NO_MESSAGE_DELETE; } int index
	 * = getIndex(task); return removeText (index, task, memory);
	 * 
	 * }
	 */

	// for delete from searched list of tasks.
	// step 1 search from tempStorage display all the lists of tasks.
	// step 2 add these tasks one by one to the other temp storage(memory).
	// step3 get the contain for the delete task. delete the task use equals .

	public void clearContent() {
		tempStorage.clear();
		Storage.writeToFile(null, file);
	}

	public static ArrayList<Integer> init(File file) {
		tempStorage = Storage.copyToArrayList(file, tempStorage);
		// stub = getNumTasks()
		ArrayList<Integer> numTask = new ArrayList<Integer>();
		getNumTasks(numTask, tempStorage);

		return numTask;
	}

	private static void getNumTasks(ArrayList<Integer> numTask,
			ArrayList<Task> tempStorage2) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date currentDate = new Date();

		try {
			int todayTask = INITIAL_VALUE;
			int tomorrowTask = INITIAL_VALUE;
			for (int i = 0; i < tempStorage.size(); i++) {
				Date dateOfCurrentTask = new Date();
				dateOfCurrentTask = dateFormat.parse(tempStorage.get(i)
						.getDate());
				if (dateOfCurrentTask.compareTo(currentDate) == INITIAL_VALUE) {
					todayTask++;
				} else if (dateOfCurrentTask.compareTo(currentDate) == INITIAL_VALUE + 1) {
					tomorrowTask++;
				} else {
					break; // During init, the tempStorage is already sorted by
							// date and time.
				}
			}
			numTask.add(todayTask);
			numTask.add(tomorrowTask);
		} catch (Exception e) {

		}
	}

	public static void sortByDateAndTime(ArrayList<Task> tempStorage) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		if (tempStorage.size() < 1) {
			return;
		} else {
			for (int i = 0; i < tempStorage.size(); i++) {
				boolean isSorted = true;
				for (int j = 0; j < tempStorage.size() - 1; j++) {
					try {
						Date dateAndTimeOfFirstTask = new Date();
						dateAndTimeOfFirstTask = dateFormat.parse(tempStorage
								.get(j).getDate()
								+ tempStorage.get(j).getTime());

						Date dateAndTimeOfSecondTask = new Date();
						dateAndTimeOfSecondTask = dateFormat.parse(tempStorage
								.get(j + 1).getDate()
								+ tempStorage.get(j + 1).getTime());

						if (dateAndTimeOfFirstTask
								.compareTo(dateAndTimeOfSecondTask) > 0) {
							tempStorage.add(j + 2, tempStorage.get(j));
							tempStorage.remove(j);
							isSorted = false;

						}
					} catch (Exception e) {

					}
				}

				if (isSorted) {
					return;
				}
			}

		}
	}

	public static void sortByAlphabet(ArrayList<Task> tempStorage) {
		if (tempStorage.size() < 1) {
			return;
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
					return;
				}
			}
		}

	}

	public static void sortByImportance(ArrayList<Task> tempStorage) {
		if (tempStorage.size() < 1) {
			return;
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
					return;
				}

			}
		}
	}

	public static void edit(Task detailsOfTask, Task taskToBeEdited,
			ArrayList<Task> tempStorage) {
		int counter = exactMatchCounter(taskToBeEdited, tempStorage);
		if (detailsOfTask.getName() != null) {
			tempStorage.get(counter).setName(detailsOfTask.getName());
		}
		if (detailsOfTask.getDate() != null) {
			tempStorage.get(counter).setDate(detailsOfTask.getDate());
		}
		if (detailsOfTask.getTime() != null) {
			tempStorage.get(counter).setTime(detailsOfTask.getTime());
		}
		if (detailsOfTask.getDetails() != null) {
			tempStorage.get(counter).setDetails(detailsOfTask.getDetails());
		}
		// IMPORTANCE LEVEL MIGHT NOT HAVE CHANGED!! BEST IF SET IN THE DETAILS
		// OF TASK TO BE A NEGATIVE NUMBER
		// if(detailsOfTask.getImportance()!=
		// tempStorage.get(counter).getImportance()){
		// tempStorage.get(counter).setImportance(detailsOfTask.getImportance());
		// }

	}

	private static int exactMatchCounter(Task taskToBeEdited,
			ArrayList<Task> tempStorage) {
		int counter = INITIAL_VALUE;

		for (int i = 0; i < tempStorage.size(); i++) {
			if (taskToBeEdited.getName().equals(tempStorage.get(i).getName())
					&& taskToBeEdited.getDate().equals(
							tempStorage.get(i).getDate())
					&& taskToBeEdited.getTime().equals(
							tempStorage.get(i).getTime())
					&& taskToBeEdited.getDetails().equals(
							tempStorage.get(i).getDetails())
					&& taskToBeEdited.getImportance() == tempStorage.get(i)
							.getImportance()) {
				break;
			} else {

				counter++;
			}
		}
		return counter;
	}

}
