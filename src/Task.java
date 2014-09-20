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
		this.importance = Integer.parseInt(splitTask[5]);
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
		return "[" + this.getTime() +"]" + this.getName();
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
}
