package uk.co.optimisticpanda.dropwizard.domain;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

public class MultipleChoiceQuestion extends Question<MultipleChoiceQuestion> {

	public static enum InputType {
		CHECKBOX
	}

	private final List<String> options = Lists.newArrayList();

	public MultipleChoiceQuestion(Long id, String question) {
		super(id, QuestionType.MULTIPLE_CHOICE, question);
	}
	public MultipleChoiceQuestion() {
	}

	public void addOptions(String option) {
		options.add(option);
	}

	public List<String> getOptions() {
		return Collections.unmodifiableList(options);
	}

	@Override
	public MultipleChoiceQuestion copy() {
		MultipleChoiceQuestion question = new MultipleChoiceQuestion(null, this.getQuestionText());
		question.options.addAll(this.options);
		question.setId(this.getId());
		return question;
	}
}
