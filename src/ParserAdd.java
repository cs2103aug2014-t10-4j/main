//@author A0110937J
//Command object for add

class ParserAdd extends ParserCommand {
	
	@Override
	public void parse(String[] parsedInput, String[] input, Index index) {
		// processNatDetails(parsedInput, input, index, new DetailsProcessor());
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
