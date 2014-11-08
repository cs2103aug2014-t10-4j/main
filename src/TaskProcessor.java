//@author A0110937J
import java.util.ArrayList;

//The following class checks and assigns task name to parsedInput
class TaskProcessor extends DetailsProcessor {
	public int getItemPosition() {
		return Constants.TASK_NAME_POSITION;
	}

	@Override
	public void processAfter(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		assignNull(parsedInput);
		if (parsedInput[getItemPosition()] != null) {
			parsedInput[getItemPosition()] = parsedInput[getItemPosition()]
					.trim();
			assignNull(parsedInput);
		}
	}

	@Override
	public void processBefore(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		parsedInput[getItemPosition()] = Constants.EMPTY_STRING;
	}

	@Override
	protected void checkAndremoveSlashes(String[] input, Index index) {
		if (hasSlashes(input, index)) {
			removeSlashes(input, index);
		}
	}

	private String removeSlashes(String[] input, Index index) {
		return input[index.getValue()] = input[index.getValue()].substring(1,
				input[index.getValue()].length());
	}

	private boolean hasSlashes(String[] input, Index index) {
		return input[index.getValue()].length() > 0
				&& input[index.getValue()].charAt(0) == Constants.CHAR_SLASH;
	}

	protected void checkAndAssignTask(String[] parsedInput, String taskName) {
		if (!isEmpty(taskName)) {
			parsedInput[Constants.TASK_NAME_POSITION] = taskName;
		}
	}

	@Override
	public String getItemName() {
		return Constants.TASK_NAME;
	}
}