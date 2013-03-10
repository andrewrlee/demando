package uk.co.optimisticpanda.dropwizard.domain;

import uk.co.optimisticpanda.dropwizard.event.Resource;


public abstract class Question<THIS extends Question<THIS>> implements Entry,Resource<Long>{

	private Long id;
	private QuestionType type;
	private String questionText;
	
	public Question(Long id, QuestionType type, String question){
		this.type = type;
		this.questionText = question;
		this.id = id;
	}
	public Question(){
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
		return "ID: " +id + ", TYPE: " + type + ", QUESTION TEXT:" + questionText;
	}
}

