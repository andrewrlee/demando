package uk.co.optimisticpanda.dropwizard.domain.builder;

import static uk.co.optimisticpanda.dropwizard.QuestionEventService.ChangeType.CREATE;
import static uk.co.optimisticpanda.dropwizard.QuestionEventService.ChangeType.UPDATE;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import uk.co.optimisticpanda.dropwizard.QuestionEventService.ChangeType;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.event.Event;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;

public class EventGenerator implements Supplier<List<Event<Question<?>>>>{

	private static final NumberFormat format = new DecimalFormat("000");
	private final Supplier<Date> dates;
	private final Supplier<List<Question<?>>> questions;

	public EventGenerator(Supplier<Date> dates, Supplier<List<Question<?>>> questions) throws IOException {
		this.dates = dates;
		this.questions = Suppliers.memoize(questions);
	}

	public List<Event<Question<?>>> get() {
		long id = 0;
		List<Event<Question<?>>> events = Lists.newArrayList();
		
		for (Question<?> question : questions.get()) {
			Question<?> payload = getPayload(question.copy(), CREATE);
			events.add(new Event<Question<?>>(id++, CREATE.name(), payload, dates.get()));
		}
		for (Question<?> question : questions.get()) {
			Question<?> payload = getPayload(question.copy(), UPDATE);
			events.add(new Event<Question<?>>(id++, UPDATE.name(), payload, dates.get()));
		}
		
		return events;
	}
	

	private Question<?> getPayload(Question<?> question, ChangeType changeType) {
		String prefix = changeType == CREATE ? "" : format.format(question.getId()) + "_"; 
 		question.setQuestionText(prefix + question.getQuestionText());
		return question;
	}


}