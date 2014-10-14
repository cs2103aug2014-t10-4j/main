import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * This class is used to store the results after executing a command.
 * It is to be used for display on the UI.
 */
public class ResultOfCommand {
	private ArrayList<Task> listOfTasks;
	private String feedback;
	private String titleOfPanel;
	private static final String MSG_EMPTY_TYPES = "No tasks for these types!";

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
			if (date.equals(getTodayDate())){
				toPrint += " " + createHorizLine("=", 20) + " " + date + " , Today " + createHorizLine("=", 20) + "\n";
			} else {	
				toPrint += " " + createHorizLine("=", 20) + " " + date + " " + createHorizLine("=", 20) + "\n";
			}
			for (int j = 0; j < listOfTasks.size() ; j ++){
				String dateOfCurrentTask = listOfTasks.get(j).getDate();
				if ( dateOfCurrentTask != null && !dateOfCurrentTask.equals(date)){
					toPrint += "\n";
					if (dateOfCurrentTask.equals("ft")){
						toPrint += " " + createHorizLine("=", 20) + " Floating Tasks "  + createHorizLine("=", 20)+ "\n" ;
					} else if  (dateOfCurrentTask.equals(getTodayDate())){
						toPrint += " " + createHorizLine("=", 20) + " " + dateOfCurrentTask + " , Today " + createHorizLine("=", 20) + "\n";
					} else {
						toPrint += " " + createHorizLine("=", 20) + " " + dateOfCurrentTask + " " + createHorizLine("=", 20)+ "\n" ;
					}
				}
				toPrint += " " + (j+1) + ". " + listOfTasks.get(j).toString() + "\n";
				date = dateOfCurrentTask;
			}
			return toPrint;
		} else {
			return MSG_EMPTY_TYPES;
		}
	}

	//Creates a horizontal line for formatting the User Interface.
	private static String createHorizLine(String charseq, int numToDraw){
		String line = "";
		for (int i=0; i < numToDraw; i++){
			line += charseq;
		}
		return line;
	}
	//Same function as getCurrentDate except date is in another format
		private static String getTodayDate() {
			DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
			Date date = new Date();
			String reportDate = dateFormat.format(date);
			return reportDate;
		}
}
