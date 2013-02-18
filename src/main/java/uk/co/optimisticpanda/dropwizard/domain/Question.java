package uk.co.optimisticpanda.dropwizard.domain;


public abstract class Question<THIS extends Question<THIS>> {

	private Long id;
	private QuestionType type;
	private String questionText;
	
	public Question(Long id, QuestionType type, String question){
		this.type = type;
		this.questionText = question;
	}
	public Question(){
	}

	public Question(THIS question){
		this.type = question.type;
		this.questionText = question.questionText;
	}
	
	public String getQuestionText() {
		return questionText;
	}
	
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public QuestionType getType() {
		return type;
	}
	
	public abstract THIS copy();
	
	@Override
	public String toString() {
		return type + ":\t" + questionText;
	}
}

