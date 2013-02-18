package uk.co.optimisticpanda.dropwizard.domain.builder;

import static uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao.Change.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao.Change;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.event.Event;
import uk.co.optimisticpanda.dropwizard.util.RandomDateProvider.DateVisitor;

import com.google.common.collect.Lists;

public class EventGenerator implements DateVisitor {

	private static final NumberFormat format = new DecimalFormat("000");
	private Iterator<Question<?>> iterator;
	private List<Event<Question<?>, Change>> events = Lists.newArrayList();
	private List<Question<?>> questions;
	private Change currentChange = Change.CREATE;
	private AtomicLong id = new AtomicLong();

	public EventGenerator() throws IOException {
		questions = new QuestionGenerator().gather();
		iterator = questions.iterator();
	}

	public boolean visit(Date randomDate) {
		if (!iterator.hasNext()) {
			if (currentChange == CREATE) {
				currentChange = UPDATE;
				iterator = questions.iterator();
				return true;
			}
			return false;
		}
		events.add(new Event<Question<?>, Change>(id.getAndIncrement(), currentChange, getPayload(), randomDate));
		return true;
	}

	private Question<?> getPayload() {
		Question<?> question = iterator.next().copy();
		String prefix = currentChange == CREATE ? "" : format.format(question.getId()) + "_"; 
		question.setQuestionText(prefix + question.getQuestionText());
		return question;
	}

	public List<Event<Question<?>, Change>> getEvents() {
		return events;
	}

}