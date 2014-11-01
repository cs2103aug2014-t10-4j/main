/**
 * This Task class is to be used to create Task objects that can be added or deleted from the to-do-list.
 * @author Low Zheng Yang
 *
 */
class Task {
	private String name;
	private String date;
	private String time;
	private String details;
	private int importance;
	private String error;
	private String params;
	private static boolean isSortedByTime = true;
	private static boolean isDetailsShown = false;

	public Task(String[] splitTask){
		this.name = splitTask[1];
		this.date = splitTask[2];
		this.time = splitTask[3];
		this.details = splitTask[4];
		if (splitTask[5] != null){
			this.importance = Integer.parseInt(splitTask[5]);
		} else {
			this.importance = -1;
		}
		this.error = splitTask[6];
		this.params = splitTask[7];
	}

	public Task(){
		this.name = null;
		this.date = null;
		this.time = null;
		this.details = null;
		this.importance = -1;
		this.error = null;
		this.params = null;
	}

	//Accessors
	public String getName(){
		return name;
	}
	public String getDate(){
		return date;
	}
	public String getTime(){
		return time;
	}
	public String getDetails(){
		return details;
	}
	public int getImportance(){
		return importance;
	}
	public String getError(){
		return error;
	}
	public String getParams(){
		return params;
	}
	public static boolean getIsSortedByTime(){
		return isSortedByTime;
	}

	public static boolean getIsDetailsShown(){
		return isDetailsShown;
	}

	//Mutators
	public void setName(String newName){
		this.name = newName;
	}
	public void setDate(String newDate){
		this.date = newDate;
	}
	public void setTime(String newTime){
		this.time= newTime;
	}
	public void setDetails(String newDetails){
		this.details = newDetails;
	}
	public void setImportance(int newImportance){
		this.importance = newImportance;
	}
	public void setError(String newError){
		this.error = newError;
	}
	public void setParams(String newParams){
		this.params = newParams;
	}
	public static void setSortedByTime(boolean sortedByTime){
		isSortedByTime = sortedByTime;
	}
	public static void setIsDetailsShown(boolean shouldShowDetails){
		isDetailsShown = shouldShowDetails;
	}

	//This method can be used to print tasks for display.
	@Override
	public String toString() {
		String taskSentence = "";
		if (isSortedByTime){
			taskSentence = String.format("%s %s %s %s ",
					printTime(), printName(), printImportanceLevel()
					, printDetails());
		} else {
			taskSentence = String.format("%s %s %s %s %s ",
					printDate(), printTime(), printName(), 
					printImportanceLevel(), printDetails());
		}
		return taskSentence;
	}

	private String printTime() {
		String time = "";
		if (this.getDate().equalsIgnoreCase("ft")){
			time += "&nbsp &nbsp &nbsp&nbsp";
		} else if (this.getTime() == null || this.getTime().equals("null")) {
			time +=  "[****]"; 
		} else {
			time +=  "[" + this.getTime() +"]"; 
		}
		time = padRight(time,7);
		return "<span class=\"time\">" + time + "</span>";
	}

	private String printDate() {
		String dateOfTask = "";
		if (!isSortedByTime) {
			if (this.getDate() != null && !date.equals("null")) {
				if (!this.getDate().equals("ft")){
					dateOfTask += "(" + date + ")";
					dateOfTask = padRight(dateOfTask, 13);
				} else {
					dateOfTask += "(Floating) ";
					//dateOfTask = padRight(dateOfTask, 13);
				}
			}
		}
		return "<span class=\"date\">" + dateOfTask + "</span>";
	}

	private String printName() {
		return "<span class=\"name\">" + this.getName() + "</span>";
	}

	private String printImportanceLevel() {
		String importanceOfTask = "";
		if (importance >0) {
			importanceOfTask += " [" + printImportance(importance) + "]";
		}
		return "<span class=\"importance\">" + importanceOfTask + "</span>";
	}

	private String printDetails() {
		String detailsOfTask = "";
		if (details != null && details.equals("")){
			return detailsOfTask;
		}
		if (details != null && !details.equals("null") && !isDetailsShown){
			detailsOfTask += " [+] ";
		}
		if (details != null && !details.equals("null") && isDetailsShown){
			detailsOfTask += "<br>" + "&#09" + " [-] " + details;
		}
		return "<span class=\"details\">" + detailsOfTask + "</span>";
	}

	//This override method can perhaps be used for search and other methods.
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Task){
			Task task = (Task) obj; 
			return compareStrings(this.getName(),task.getName()) && compareStrings(this.getDate(),task.getDate()) && 
					compareStrings(this.getTime(),task.getTime()) && compareStrings(this.getDetails(),task.getDetails()) && 
					this.getImportance() == task.getImportance() && compareStrings(this.getParams(),task.getParams()) && 
					compareStrings(this.getError(),task.getError());
		} else {
			return false;
		}
	}

	private boolean compareStrings (String firstLine, String secondLine){
		if(firstLine !=null && secondLine != null){
			return firstLine.equals(secondLine);
		}
		else if (firstLine == null && secondLine == null){
			return true;
		}
		else{
			return false;
		}
	}
	//Return a number of ! based on importance number
	private String printImportance (int num){
		String toPrint = "";
		for (int j=0; j < num; j++){
			toPrint += "!";
		}
		return toPrint;
	}

	public void copyOfTask( Task task){
		this.setName(task.getName());
		this.setDate(task.getDate());
		this.setTime(task.getTime());
		this.setDetails(task.getDetails());
		this.setImportance(task.getImportance());
		this.setParams(task.getParams());
	}

	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);  
	}
}
