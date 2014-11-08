//@author A0110937J
import java.util.ArrayList;

//The following class checks and assigns details

class DetailsProcessor extends Processor {
	private static final String NAME_DETAILS = null;

	@Override
	public int getItemPosition() {
		return Constants.DETAILS_POSITION;
	}

	@Override
	public void processBefore(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		while (isIndexValid(index.getValue(), input)) {
			if (isPartOfList(input[index.getValue()], Constants.LIST_DETAILS)) {
				assingInputNull(input, index);
				parsedInput[getItemPosition()] = Constants.EMPTY_STRING;
				index.increment();
				break;
			}
			index.increment();
		}
	}

	@Override
	public void processAfter(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		if (parsedInput[getItemPosition()] != null) {
			trimString(parsedInput);
		}
	}

	private void trimString(String[] parsedInput) {
		parsedInput[getItemPosition()] = parsedInput[getItemPosition()]
				.trim();
	}

	@Override
	public void process(String[] parsedInput, String[] input, Index index) {
		if (input[index.getValue()] != null) {
			checkAndremoveSlashes(input, index);
			assignDetails(parsedInput, input, index);
			assingInputNull(input, index);
		}
	}

	private void assingInputNull(String[] input, Index index) {
		input[index.getValue()] = null;
	}

	private void assignDetails(String[] parsedInput, String[] input, Index index) {
		parsedInput[getItemPosition()] = parsedInput[getItemPosition()]
				+ input[index.getValue()] + Constants.SPACE;
	}

	protected void checkAndremoveSlashes(String[] input, Index index) {
	}

	@Override
	public String getItemName() {
		return NAME_DETAILS;
	}
}