//@author A0110937J
//Command object for search
class ParserSearch extends ParserCommand {

	@Override
	public void parse(String[] parsedInput, String[] input, Index index) {
		processType(parsedInput, input, index, new DateProcessor(),
				new NaturalProcessor());
		processType(parsedInput, input, index, new TaskProcessor(),
				new NaturalProcessor());
		processEmpty(parsedInput);
	}

}
