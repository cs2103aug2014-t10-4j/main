//@author A0110930X
/*
 * This class is used to create an object to store the results after executing 
 * a command. The ResultOfCommand object is to be returned to the UI and be 
 *  used to set the display on the UI.
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ResultOfCommand {
	private ArrayList<Task> listOfTasks;
	private String feedback;
	private String titleOfPanel;
	
	public ResultOfCommand () {
		listOfTasks = new ArrayList<Task>();
		feedback = "";
		titleOfPanel = Constants.TITLE_ALL_TASKS;
	}

	//Accessors
	public ArrayList<Task> getListOfTasks (){
		return listOfTasks;
	}

	public String getFeedback(){
		return feedback;
	}
	public String getTitleOfPanel(){
		return titleOfPanel;
	}

	//Mutators
	public void setListOfTasks(ArrayList<Task> list){
		listOfTasks = list;
	}

	public void setFeedback(String result){
		this.feedback = result;
	}
	public void setTitleOfPanel(String newTitle){
		this.titleOfPanel = newTitle;
	}

	//Other methods
	public String printArrayList(){
		if (listOfTasks.size() !=0){
			return printTasks();
		} else {
			return Constants.MSG_EMPTY_TYPES;
		}
	}

	private String printTasks() {
		String toPrint = "";
		assert listOfTasks.size() >0;
		String previousDate = listOfTasks.get(0).getDate();
		toPrint = printDateHeader(toPrint, previousDate);
		for (int j = 0; j < listOfTasks.size() ; j ++){
			String dateOfCurrentTask = listOfTasks.get(j).getDate();
			if ( dateOfCurrentTask != null && !dateOfCurrentTask.equals(previousDate)){
				if (Task.getIsSortedByTime()){
					toPrint += "<br>";
				}
				toPrint = printDateHeader(toPrint, dateOfCurrentTask);
			}
			toPrint += String.format("%2d. ", j+1) + listOfTasks.get(j).toString() + "<br>";
			previousDate = dateOfCurrentTask;
		}
		return toPrint;
	}

	//Prints the date header if list is sorted by time.
	private String printDateHeader(String toPrint, String dateOfCurrentTask) {
		if (Task.getIsSortedByTime()){
			String dayOfWeek = "";
			if (isOverdue(dateOfCurrentTask)){
				dayOfWeek = getDayOfWeek(dateOfCurrentTask);
				toPrint += String.format(Constants.DATE_WITH_LINE, Constants.COLOR_PINKISH_RED, 
						dateOfCurrentTask, ", " + dayOfWeek);
			} else if  (dateOfCurrentTask.equals(getTodayDate())){
				dayOfWeek = getDayOfWeek(dateOfCurrentTask);
				toPrint += String.format(Constants.DATE_WITH_LINE_TODAY, Constants.COLOR_DARK_BLUE, 
						dateOfCurrentTask, ", " + dayOfWeek + " (Today)");
			} else if (dateOfCurrentTask.equals("ft")){
				toPrint += String.format(Constants.DATE_WITH_LINE, Constants.COLOR_BLOOD_RED, 
						"Floating Tasks", dayOfWeek);
			} else {
				dayOfWeek = getDayOfWeek(dateOfCurrentTask);
				toPrint += String.format(Constants.DATE_WITH_LINE, Constants.COLOR_DARK_GREEN, 
						dateOfCurrentTask, ", " + dayOfWeek); 
			}
		}
		return toPrint;
	}

	//Get today date and return it as a String
	private static String getTodayDate() {
		DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date date = new Date();
		String reportDate = dateFormat.format(date);
		return reportDate;
	}

	//Return the day of the week
	private static String getDayOfWeek(String date){
		try {
			Date mydate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH).parse(date);
			return new SimpleDateFormat("EEE").format(mydate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	//Returns whether a date is overdue.
	private static boolean isOverdue (String date) {
		Date currentDate = new Date();
		currentDate = removeTime(currentDate);
		try {
			Date dateOfCurrentTask = Constants.dateFormat.parse(date);
			dateOfCurrentTask = removeTime(dateOfCurrentTask);
			if (dateOfCurrentTask.compareTo(currentDate) < 0) {
				return true;
			}
		} catch (ParseException e) {
		}
		return false;
	}

	//For use internally to check whether date is overdue.
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
