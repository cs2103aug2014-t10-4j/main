//@author A0110937J
import java.util.ArrayList;

//The following class creates a loop for attribute processors to process the string

class NaturalProcessor {

	public void process(String[] parsedInput, String[] input, Index index,
			Processor processor) {
		index.reset();
		ArrayList<Integer> fullStopArr = new ArrayList<Integer>();

		processor.processBefore(input, fullStopArr, parsedInput, index);
		if (isCollectProcessors(processor)) {
			collectProcess(parsedInput, input, index, processor);
		} else if (isBackProcessors(processor)) {
			backwardProcess(parsedInput, input, index, processor);
		} else {

			forwardProcess(parsedInput, input, index, processor);
		}
		processor.processAfter(input, fullStopArr, parsedInput, index);
		assignErrorMsg(parsedInput, processor);
	}

	private void forwardProcess(String[] parsedInput, String[] input,
			Index index, Processor processor) {
		while (isIndexValid(index.getValue(), input)) {
			int startPosition = index.getValue();
			processor.process(parsedInput, input, index);
			if (parsedInput[Constants.ERROR_MSG_POSITION] != null) {
				break;
			} else if (parsedInput[processor.getItemPosition()] != null) {
				assignNull(input, index, startPosition);
				cleanWord(input, index, processor, startPosition);
				break;
			}
			index.increment();
		}
	}

	private void assignNull(String[] input, Index index, int startPosition) {
		for (int i = startPosition; i < index.getValue(); i++) {
			input[i] = null;
		}
	}

	private void cleanWord(String[] input, Index index, Processor processor,
			int startPosition) {
		cleanWordBackward(input, startPosition - 1, processor);
		cleanWordForward(input, index.getValue(), processor);
	}

	// Processors that process from the end of string
	private boolean isBackProcessors(Processor processor) {
		return processor instanceof DateProcessor
				|| processor instanceof TimeProcessor;
	}

	// Processors that process attributes that could appear more than once in
	// string
	private boolean isCollectProcessors(Processor processor) {
		return processor instanceof TaskProcessor
				|| processor instanceof DetailsProcessor
				|| processor instanceof MultiParaProcessor;
	}

	private void cleanWordForward(String[] input, int index, Processor processor) {
		if (processor instanceof CommandProcessor) {
			while (isIndexValid(index, input) && input[index] != null
					&& isPartOfList(input[index], Constants.LIST_REMOVABLES)) {
				input[index] = null;
				index++;
			}
		}
	}
	//Create loop that allow processors to process till the end of string 
	//even if a valid attribute is found
	private void collectProcess(String[] parsedInput, String[] input,
			Index index, Processor processor) {
		while (isIndexValid(index.getValue(), input)) {
			processor.process(parsedInput, input, index);
			index.increment();
		}
	}
	//Creates loop that process from behind
	private void backwardProcess(String[] parsedInput, String[] input,
			Index index, Processor processor) {
		index.setValue(input.length - 1);
		while (isIndexValid(index.getValue(), input)) {
			int startPosition = index.getValue();
			processor.process(parsedInput, input, index);
			if (parsedInput[Constants.ERROR_MSG_POSITION] != null) {
				break;
			} else if (parsedInput[processor.getItemPosition()] != null) {
				if (index.getValue() <= startPosition) {
					cleanWordBackward(input, index.getValue(), processor);
				} else {
					assignNull(input, index, startPosition);
					cleanWordBackward(input, startPosition - 1, processor);
					break;
				}
			}
			index.decrement();
		}
	}

	protected void assignErrorMsg(String[] parsedInput, Processor processor) {
	}
	
	private void cleanWordBackward(String[] input, int index,
			Processor processor) {
		if (isValidBackCleanProcessor(processor)) {
			while (hasRemovable(input, index)) {
				input[index] = null;
				index--;
			}
		}
	}

	private boolean hasRemovable(String[] input, int index) {
		return isIndexValid(index, input)
				&& isPartOfList(input[index], Constants.LIST_REMOVABLES)
				&& input[index] != null;
	}

	private boolean isValidBackCleanProcessor(Processor processor) {
		return processor instanceof DateProcessor
				|| processor instanceof TimeProcessor
				|| processor instanceof ImportanceProcessor
				|| processor instanceof CommandProcessor;
	}

	protected boolean isIndexValid(int index, String[] input) {
		if (index >= input.length || index < 0) {
			return false;
		} else {
			return true;
		}
	}

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
}
