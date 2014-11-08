//@author A0110937J
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//The following class is the abstract class for attribute processor

public abstract class Processor {

	public abstract void process(String[] parsedInput, String[] input,
			Index index);

	protected boolean isRegexMatch(String input, String regexPattern) {
		Pattern pattern = Pattern.compile(regexPattern);
		try {
			Matcher matchPattern = pattern.matcher(input);
			matchPattern.find();
			if (matchPattern.group().length() != 0) {
				return true;
			}
		} catch (IllegalStateException | NullPointerException e) {
			return false;
		}
		return false;
	}

	protected void assignNull(String[] parsedInput) {
		if (parsedInput[getItemPosition()].equals(Constants.EMPTY_STRING)) {
			parsedInput[getItemPosition()] = null;
		}
	}

	// check if a String is part of the list
	protected boolean isPartOfList(String[] input, Index index, String[] list) {
		for (int i = 0; i < list.length; i++) {
			if (input == null) {
				return false;
			}
			if (isMatchString(input, index, list[i])) {
				return true;
			}
		}
		return false;
	}

	// returns first member of list
	protected String getFirstMember(String[] list) {
		return list[0];
	}

	protected boolean isMatchString(String[] input, Index index, String command) {
		String[] possibleCommand = command.split(Constants.SPACE);
		int startIndex = index.getValue();
		for (int i = 0; i < possibleCommand.length; i++) {
			if (!isIndexValid(index.getValue(), input)) {
				index.setValue(startIndex);
				return false;
			}
			if (!possibleCommand[i].equalsIgnoreCase(input[index.getValue()])) {
				index.setValue(startIndex);
				return false;
			}
			if (i == possibleCommand.length - 1) {
				break;
			}
			index.increment();
		}
		return true;
	}

	protected boolean isIndexValid(int index, String[] input) {
		if (index >= input.length || index < 0) {
			return false;
		} else {
			return true;
		}
	}

	protected void assignErrorMsg(String[] parsedInput, String message) {
		parsedInput[Constants.ERROR_MSG_POSITION] = message;
		parsedInput[Constants.COMMAND_POSITION] = Constants.ERROR;
	}

	// check if a String is part of the list
	protected boolean isPartOfList(String input, String[] list) {
		for (int i = 0; i < list.length; i++) {
			if (input == null) {
				return false;
			}
			if (input.equalsIgnoreCase(list[i])) {
				return true;
			}
		}
		return false;
	}

	protected boolean isEmpty(String taskName) {
		return taskName.length() == 0;
	}

	protected boolean isNull(String str) {
		if (str == null) {
			return true;
		} else {
			return false;
		}
	}

	public abstract int getItemPosition();

	public abstract String getItemName();

	// sets up the string before the processors process
	public void processAfter(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		putAllFullStop(input, fullStopArr);
	}

	protected static void putAllFullStop(String[] input,
			ArrayList<Integer> fullStopArr) {
		int origin;
		for (int i = fullStopArr.size() - 1; i >= 0; i--) {
			int position = fullStopArr.get(i);
			origin = position;
			if (input[position] == null) {
				position = getPossibleLastWord(input, position);
				if (position < 0) {
					break;
				}
				i = removeCollision(fullStopArr, i, position);
				putFullStop(input, position, origin);
			} else {
				putFullStop(input, position, origin);
			}
		}
	}

	private static int getPossibleLastWord(String[] input, int position) {
		while (position >= 0 && input[position] == null) {
			position--;
		}
		return position;
	}

	private static int removeCollision(ArrayList<Integer> fullStopArr, int i,
			int position) {
		for (int j = i - 1; j >= 0; j--) {
			if (fullStopArr.get(j) >= position) {
				fullStopArr.remove(j);
				i--;
			}
		}
		return i;
	}

	private static void putFullStop(String[] input, int position, int origin) {
		if (origin == position) {
			input[position] = input[position] + Constants.FULL_STOP;
		} else if (!isLastWord(input, position)) {
			input[position] = input[position] + Constants.FULL_STOP;
		}

	}

	protected static void removeAllFullStop(String[] input,
			ArrayList<Integer> fullStopArr) {
		for (int i = 0; i < input.length; i++) {
			if (isLastWord(input, i)) {
				removeFullStop(input, i);
				fullStopArr.add(i);
			}
		}
	}

	private static boolean isLastWord(String[] input, int index) {
		if (input[index] == null) {
			return false;
		} else if (input[index].length() == 0) {
			return false;
		} else if (input[index].charAt(input[index].length() - 1) == Constants.CHAR_FULL_STOP) {
			return true;
		} else {
			return false;
		}
	}

	private static void removeFullStop(String[] input, int index) {
		input[index] = input[index].substring(0, input[index].length() - 1);
		if (input[index].length() == 0) {
			input[index] = null;
		}
	}

	public void processBefore(String[] input, ArrayList<Integer> fullStopArr,
			String[] parsedInput, Index index) {
		removeAllFullStop(input, fullStopArr);
	}

}






