package uk.co.optimisticpanda.dropwizard.domain;

public class FreeTextQuestion extends Question<FreeTextQuestion>{

	public static enum InputType{ FIELD, AREA }
	private InputType inputType;
	private int minChars = 0 ;
	private int maxChars = 255;
	
	public FreeTextQuestion(Long id, InputType type, String question) {
		super(id, QuestionType.FREE_TEXT, question);
		this.inputType = type;
	}
	public FreeTextQuestion() {
	}
	
	public int getMinChars() {
		return minChars;
	}

	public void setMinChars(int minChars) {
		this.minChars = minChars;
	}

	public int getMaxChars() {
		return maxChars;
	}

	public void setMaxChars(int maxChars) {
		this.maxChars = maxChars;
	}
	
	public InputType getInputType() {
		return inputType;
	}
	
	@Override
	public FreeTextQuestion copy() {
		return new FreeTextQuestion(this.getId(), this.inputType, this.getQuestionText());
	}
}
