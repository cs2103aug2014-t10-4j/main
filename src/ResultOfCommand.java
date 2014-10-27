import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/*
 * This class is used to store the results after executing a command.
 * It is to be used for display on the UI.
 */
public class ResultOfCommand {
	private ArrayList<Task> listOfTasks;
	private String feedback;
	private String titleOfPanel;
	private static final String MSG_EMPTY_TYPES = "No tasks for these types!";
	private static final String DATE_WITH_LINE = " ========================= %s %s ========================= \n";


	public ResultOfCommand () {
		listOfTasks = new ArrayList<Task>();
		feedback = "";
		titleOfPanel="All Tasks";
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
		String toPrint ="";
		if (listOfTasks.size() !=0){
			String date = listOfTasks.get(0).getDate();
			toPrint = printDateHeaders(toPrint, date);
			return printTasks(toPrint, date);
		} else {
			return MSG_EMPTY_TYPES;
		}
	}

	private String printTasks(String toPrint, String date) {
		for (int j = 0; j < listOfTasks.size() ; j ++){
			String dateOfCurrentTask = listOfTasks.get(j).getDate();
			if ( dateOfCurrentTask != null && !dateOfCurrentTask.equals(date)){
				if (Task.getIsSortedByTime()){
					toPrint += "\n";
					if (dateOfCurrentTask.equals("ft")){
						toPrint += String.format(DATE_WITH_LINE, "Floating Tasks", "");
					} else if  (dateOfCurrentTask.equals(getTodayDate())){
						toPrint += String.format(DATE_WITH_LINE, dateOfCurrentTask, ", Today");
					} else {
						toPrint += String.format(DATE_WITH_LINE, dateOfCurrentTask, ", " + getDayOfWeek(dateOfCurrentTask));
					}
				}
			}
			toPrint += String.format("%2d.   ", j+1) + listOfTasks.get(j).toString() + "\n";
			date = dateOfCurrentTask;
		}
		return toPrint;
	}

	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}
	
	private String printDateHeaders(String toPrint, String date) {
		if (Task.getIsSortedByTime()){
			if (date.equals(getTodayDate())){
				toPrint += String.format(DATE_WITH_LINE, date, ", Today");
			} else if (date.equalsIgnoreCase("ft")){
				toPrint += String.format(DATE_WITH_LINE, "Floating Tasks", "");
			} else {	
				toPrint += String.format(DATE_WITH_LINE, date, ", " + getDayOfWeek(date)); 
			}
		}
		return toPrint;
	}

	//Same function as getCurrentDate except date is in another format
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
}
