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
	private static boolean isSortedByTime;

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
		isSortedByTime = true;
	}

	public Task(){
		this.name = null;
		this.date = null;
		this.time = null;
		this.details = null;
		this.importance = -1;
		this.error = null;
		this.params = null;
		isSortedByTime = true;
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

	//This method can be used to print tasks for debugging.
	@Override
	public String toString() {
		String sentence = "";
		if (this.getTime() == null || this.getTime().equals("null")) {
			sentence +=  " [ **** ]   "; 
		} else {
			sentence +=  " [" + this.getTime() +"]   "; 
		}
		if (!isSortedByTime) {
			if (this.getDate() != null && !date.equals("null")) {
				if (!this.getDate().equals("ft")){
					sentence += "(" + printDateNicely() + ")   ";
				} else {
					sentence += "(" + this.date + ")   ";
				}
			}
		}
		sentence += this.getName();
		if (importance >0) {
			sentence += " [" + printImportance(importance) + "]";
		}
		if (details != null && !details.equals("null")){
			sentence += " [+] ";
		}
		return sentence;
	}

	//This overrode method can perhaps be used for search and other methods.
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Task){
			Task task = (Task) obj; 
			return compareStrings(this.getName(),task.getName()) && compareStrings(this.getDate(),task.getDate()) && 
					compareStrings(this.getTime(),task.getTime()) && compareStrings(this.getDetails(),task.getDetails()) && 
					this.getImportance() == task.getImportance();
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

	private String printDateNicely (){
		String temp = date;
		String toPrint = temp.substring(0, 2);
		toPrint += "/";
		toPrint += temp.substring(2,4);
		toPrint += "/";
		toPrint += temp.substring(4);

		return toPrint;
	}
}
