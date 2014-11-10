//@author A0110937J
import java.util.ArrayList;

//The following class checks and assign valid command
class CommandProcessor extends Processor {

	public void process(String[] parsedInput, String[] input, Index index) {
		if (isPartOfList(input[index.getValue()], Constants.LIST_DETAILS)) {
			index.setValue(input.length);
		} else {
			assignIfPossible(parsedInput, input, index);
		}
	}

	private void assignAddIfPossible(String[] parsedInput, String[] input,
			Index index) {
		if (parsedInput[Constants.COMMAND_POSITION] == null) {
			assignCommand(parsedInput, input, index,
					getFirstMember(Constants.LIST_ADD));
		}
	}

	public void processAfter(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		putAllFullStop(input, fullStopArr);
		assignAddIfPossible(parsedInput, input, index);
	}

	private void assignIfPossible(String[] parsedInput, String[] input,
			Index index) {
		if (isIndexValid(index.getValue(), input)) {
			if (parsedInput[Constants.COMMAND_POSITION] == null) {
				String command = getCommand(input, index, parsedInput);
				if (command != null) {
					assignCommand(parsedInput, input, index, command);
				}
			}
		}
	}

	private void assignCommand(String[] parsedInput, String[] input,
			Index index, String command) {
		assert index.getValue() < input.length;
		parsedInput[Constants.COMMAND_POSITION] = command;
		index.increment();
	}

	private String getCommand(String[] input, Index index, String[] parsedInput) {

		if (isPartOfList(input[index.getValue()], Constants.LIST_ADD)) {
			return getFirstMember(Constants.LIST_ADD);
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_EDIT)) {
			return getFirstMember(Constants.LIST_EDIT);
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_SEARCH)) {
			return getFirstMember(Constants.LIST_SEARCH);
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_UNDO)) {
			return getFirstMember(Constants.LIST_UNDO);
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_REDO)) {
			return getFirstMember(Constants.LIST_REDO);
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_EXIT)) {
			return getFirstMember(Constants.LIST_EXIT);
		} else if (isSpecialCommand(input, index, Constants.LIST_CLEAR_ARCHIVE,
				parsedInput)) {
			return getFirstMember(Constants.LIST_CLEAR_ARCHIVE);
		} else if (isSpecialCommand(input, index, Constants.LIST_CLEAR,
				parsedInput)) {
			return getFirstMember(Constants.LIST_CLEAR);
		} else if (isSpecialCommand(input, index, Constants.LIST_SHOW_FLOATING,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_FLOATING);
		} else if (isSpecialCommand(input, index, Constants.LIST_SHOW_ALL,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_ALL);
		} else if (isSpecialCommand(input, index, Constants.LIST_SHOW_DETAILS,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_DETAILS);
		} else if (isSpecialCommand(input, index, Constants.LIST_HIDE_DETAILS,
				parsedInput)) {
			return getFirstMember(Constants.LIST_HIDE_DETAILS);
		} else if (isSpecialCommand(input, index, Constants.LIST_SORT_IMPT,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SORT_IMPT);
		} else if (isSpecialCommand(input, index, Constants.LIST_SORT_ALPHA,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SORT_ALPHA);
		} else if (isSpecialCommand(input, index, Constants.LIST_SORT_TIME,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SORT_TIME);
		} else if (isSpecialCommand(input, index, Constants.LIST_DELETE_ALL,
				parsedInput)) {
			return getFirstMember(Constants.LIST_DELETE_ALL);
		} else if (isSpecialCommand(input, index, Constants.LIST_DELETE_PAST,
				parsedInput)) {
			return getFirstMember(Constants.LIST_DELETE_PAST);
		} else if (isSpecialCommand(input, index, Constants.LIST_DELETE_TODAY,
				parsedInput)) {
			return getFirstMember(Constants.LIST_DELETE_TODAY);
		} else if (isPartOfList(input, index, Constants.LIST_DELETE_DATE)) {
			return getFirstMember(Constants.LIST_DELETE_DATE);
		} else if (isSpecialCommand(input, index, Constants.LIST_SHOW_TODAY,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_TODAY);
		} else if (isSpecialCommand(input, index, Constants.LIST_SHOW_WEEK,
				parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_WEEK);
		} else if (isSpecialCommand(input, index,
				Constants.LIST_SHOW_THIS_WEEK, parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_THIS_WEEK);
		} else if (isSpecialCommand(input, index,
				Constants.LIST_SHOW_NEXT_WEEK, parsedInput)) {
			return getFirstMember(Constants.LIST_SHOW_NEXT_WEEK);
		} else if (isSpecialCommand(input, index, Constants.LIST_VIEW_ARCHIVE,
				parsedInput)) {
			return getFirstMember(Constants.LIST_VIEW_ARCHIVE);
		} else if (isPartOfList(input[index.getValue()], Constants.LIST_DELETE)) {
			return getFirstMember(Constants.LIST_DELETE);
		}
		return null;
	}

	// check if a String is part of the list
	private boolean isSpecialCommand(String[] input, Index index,
			String[] list, String[] parsedInput) {
		for (int i = 0; i < list.length; i++) {
			if (input == null) {
				return false;
			}
			if (isFirstInstance(index.getValue())
					&& isMatchString(input, index, list[i])) {
				if (isRestEmpty(input, index.getValue())) {
					return true;
				} else {
					assignErrorMsg(parsedInput, Constants.ERROR_MSG_SPECIAL_COM);
				}
			}
		}
		return false;
	}

	private boolean isFirstInstance(int index) {
		return index == 0;
	}

	private boolean isRestEmpty(String[] input, int index) {
		if ((input.length - 1) == index) {
			return true;
		}
		return false;
	}

	@Override
	public int getItemPosition() {
		return Constants.COMMAND_POSITION;
	}

	@Override
	public String getItemName() {
		return Constants.COMMAND_NAME;
	}

}
