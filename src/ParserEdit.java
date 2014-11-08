//@author A0110937J
//Command object for edit

class ParserEdit extends ParserCommand {

	@Override
	public void parse(String[] parsedInput, String[] input, Index index) {
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