public abstract class ParserCommand {
	private final int ERROR_MSG_POSITION = 7;
	private final int COMMAND_POSITION = 0;
	private final String ERROR = "error";

	public abstract void parse(String[] parsedInput, String[] input, Index index);
/*
	protected static void processNatDetails(String[] parsedInput,
			String[] input, Index index, DetailsProcessor processor) {
		processor.processNatural(parsedInput, input, index);
	}
*/
	protected void processType(String[] parsedInput, String[] input,
			Index index, Processor processor, NaturalProcessor natProcessor) {
		natProcessor.process(parsedInput, input, index, processor);
	}
/*
	protected void processDirect(String[] parsedInput, String[] input,
			Index index, Processor processor) {
		processor.process(parsedInput, input, index);
	}
*/
	protected void processEmpty(String[] parsedInput) {
		if (isAllNull(parsedInput)) {
			assignErrorMsg(parsedInput, "No type found.");

		}
	}

	protected void assignErrorMsg(String[] parsedInput, String message) {
		parsedInput[ERROR_MSG_POSITION] = message;
		parsedInput[COMMAND_POSITION] = ERROR;
	}

	private boolean isAllNull(String[] parsedInput) {
		for (int i = 1; i < parsedInput.length; i++) {
			if (parsedInput[i] != null) {
				return false;
			}
		}
		return true;
	}
}

class ParserAdd extends ParserCommand {
	@Override
	public void parse(String[] parsedInput, String[] input, Index index) {
		//processNatDetails(parsedInput, input, index, new DetailsProcessor());
		processType(parsedInput, input, index, new DetailsProcessor(),
				new NaturalProcessor());
		processType(parsedInput, input, index, new AutoDateProcessor(),
				new NaturalProcessor());
		processType(parsedInput, input, index, new TimeProcessor(),
				new NaturalProcessor());
		processType(parsedInput, input, index, new ImportanceProcessor(),
				new NaturalProcessor());
		processType(parsedInput, input, index, new TaskProcessor(),
				new StrictNaturalProcessor());
	}
}

class ParserDelete extends ParserCommand {

	@Override
	public void parse(String[] parsedInput, String[] input, Index index) {
		//processDirect(parsedInput, input, index, new MultiParaProcessor());
		processType(parsedInput, input, index, new MultiParaProcessor(),
				new StrictNaturalProcessor());
	processType(parsedInput, input, index, new TaskProcessor(),
				new AntiNaturalProcessor());

	}

}
class ParserDeleteDate extends ParserCommand {

	@Override
	public void parse(String[] parsedInput, String[] input, Index index) {
		
		processType(parsedInput, input, index, new DateProcessor(),
				new StrictNaturalProcessor());
	processType(parsedInput, input, index, new TaskProcessor(),
				new AntiNaturalProcessor());

	}

}

class ParserSearch extends ParserCommand {

	@Override
	public void parse(String[] parsedInput, String[] input, Index index) {
		/*processNatDetails(parsedInput, input, index, new DetailsProcessor());*/
		processType(parsedInput, input, index, new DateProcessor(),
				new NaturalProcessor());
		/*processType(parsedInput, input, index, new TimeProcessor(),
				new NaturalProcessor());
		/*processType(parsedInput, input, index, new ImportanceProcessor(),
				new NaturalProcessor());*/
		processType(parsedInput, input, index, new TaskProcessor(),
				new NaturalProcessor());
		processEmpty(parsedInput);
	}

}

class ParserEdit extends ParserCommand {

	@Override
	public void parse(String[] parsedInput, String[] input, Index index) {
		//processNatDetails(parsedInput, input, index, new DetailsProcessor());
		processType(parsedInput, input, index, new DetailsProcessor(),
				new NaturalProcessor());
		processType(parsedInput, input, index, new DateProcessor(),
				new NaturalProcessor());
		processType(parsedInput, input, index, new TimeProcessor(),
				new NaturalProcessor());
		processType(parsedInput, input, index, new ImportanceProcessor(),
				new NaturalProcessor());
		processType(parsedInput, input, index, new SingleParaProcessor(),
				new StrictNaturalProcessor());
		processType(parsedInput, input, index, new TaskProcessor(),
				new NaturalProcessor());
	}

}


