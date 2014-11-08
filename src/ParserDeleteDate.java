//@author A0110937J
//Command object for delete date
class ParserDeleteDate extends ParserCommand {

	@Override
	public void parse(String[] parsedInput, String[] input, Index index) {

		processType(parsedInput, input, index, new DateProcessor(),
				new StrictNaturalProcessor());
		processType(parsedInput, input, index, new TaskProcessor(),
				new AntiNaturalProcessor());
	}

}