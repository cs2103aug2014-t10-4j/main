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
	
	public Task(String[] splitTask){
		this.name = splitTask[1];
		this.date = splitTask[2];
		this.time = splitTask[3];
		this.details = splitTask[4];
		if (splitTask[5] != null){
			this.importance = Integer.parseInt(splitTask[5]);
		}
	}
	
	public Task(){
		this.name = null;
		this.date = null;
		this.time = null;
		this.details = null;
		this.importance = 0;
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
	
	//This method can be used to print tasks for debugging.
	@Override
	public String toString(){
		String sentence =  " [" + this.getTime() +"] " + this.getName();
		if (importance !=0) {
			sentence += sentence + " [" + printImportance(importance) + "] ";
		}
		if (details != null){
			sentence += sentence + "[+]";
		}
		return sentence;
	}
	
	//This overrode method can perhaps be used for search and other methods.
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Task){
			Task task = (Task) obj; 
			return this.equals(task.getName()) && this.equals(task.getDate()) && this.equals(task.getTime()) &&
					this.equals(task.getDetails()) && this.getImportance() == task.getImportance();
		} else {
			return false;
		}
	}
	//Return a number of ! based on importance number
	private String printImportance (int num){
		String toPrint = null;
		for (int j=0; j < num; j++){
			toPrint += "!";
		}
		return toPrint;
	}
}
