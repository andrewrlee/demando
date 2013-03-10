package uk.co.optimisticpanda.dropwizard.domain;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

public class RadioButtonQuestion extends Question<RadioButtonQuestion> {

	private final List<String> options = Lists.newArrayList();

	public RadioButtonQuestion(Long id, String question) {
		super(id, QuestionType.MULTIPLE_CHOICE, question);
	}
	public RadioButtonQuestion() {
	}

	public void addOptions(String option) {
		options.add(option);
	}

	public List<String> getOptions() {
		return Collections.unmodifiableList(options);
	}

	@Override
	public RadioButtonQuestion copy() {
		RadioButtonQuestion question = new RadioButtonQuestion(null, this.getQuestionText());
		question.options.addAll(this.options);
		question.setId(this.getId());
		return question;
	}
}
