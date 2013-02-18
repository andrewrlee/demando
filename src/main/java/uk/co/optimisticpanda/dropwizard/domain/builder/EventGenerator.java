package uk.co.optimisticpanda.dropwizard.domain.builder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao.Change;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.domain.builder.DateGenerator.DateVisitor;
import uk.co.optimisticpanda.dropwizard.event.Event;

import com.google.common.collect.Lists;

public class EventGenerator implements  DateVisitor{

	private Iterator<Question<?>> iterator;
	private List<Event<Question<?>, Change>> events = Lists.newArrayList();
	private List<Question<?>> questions;
	private Change currentCategory = Change.CREATE;
	private AtomicInteger counter = new AtomicInteger();
	private AtomicLong id = new AtomicLong();
	
	public EventGenerator() throws IOException{
		questions= new QuestionGenerator().gather();
		iterator = questions.iterator();
	}
	
	public void visit(Date randomDate) {
		if(iterator.hasNext()){
			Question<?> question = iterator.next().copy();
			if(currentCategory == Change.UPDATE){
				NumberFormat format = new DecimalFormat("000");
				String prefix = format.format(counter.getAndIncrement()) + "_";
				question.setQuestionText(prefix + question.getQuestionText());
			}
			events.add(new Event<Question<?>, Change>(id.getAndIncrement(), currentCategory, question, randomDate));
		}else if(currentCategory == Change.CREATE){
				currentCategory = Change.UPDATE;
				iterator = questions.iterator();
		}
	}
	
	public List<Event<Question<?>, Change>> getEvents() {
		return events;
	}
	
	public static void main(String[] args) throws IOException {
		DateGenerator generator = new DateGenerator();
		EventGenerator eventGenerator = new EventGenerator();
		generator.collect(eventGenerator);
		for (Event<Question<?>, Change> event : eventGenerator.events) {
			System.out.println(event.getCreatedDate() + " " + event.getEventType() + " " + event.getPayload().getQuestionText());
		}
		
	}
}
