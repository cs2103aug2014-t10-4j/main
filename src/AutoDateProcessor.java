//@author A0110937J
import java.util.ArrayList;

//The following class assigns today's date if no date is found

class AutoDateProcessor extends DateProcessor {
	@Override
	public void processAfter(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		putAllFullStop(input, fullStopArr);
		if (parsedInput[Constants.ERROR_MSG_POSITION] == null) {
			String possibleDate = null;
			assignDateIfPossible(input, parsedInput, index);
		}
	}

	private void assignDateIfPossible(String[] input, String[] parsedInput,
			Index index) {
		String possibleDate;
		if (parsedInput[Constants.DATE_POSITION] == null) {
			possibleDate = formatDate(index.getValue(), input,
					new DateFormatter());
			checkAndAssignDate(parsedInput, index, possibleDate);
			index.decrement();
		}
	}

	private void checkAndAssignDate(String[] parsedInput, Index index,
			String possibleDate) {
		if (validateDate(possibleDate)) {
			assignDate(parsedInput, index, possibleDate);
		}
	}
}