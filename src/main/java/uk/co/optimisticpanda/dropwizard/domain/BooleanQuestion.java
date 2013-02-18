package uk.co.optimisticpanda.dropwizard.domain;

public class BooleanQuestion extends Question<BooleanQuestion>{

	public BooleanQuestion(Long id, String questionText) {
		super(id, QuestionType.BOOLEAN, questionText);
	}
	public BooleanQuestion() {
	}
	@Override
	public BooleanQuestion copy() {
		return new BooleanQuestion(this.getId(), this.getQuestionText());
	}
}
