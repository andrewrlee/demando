package uk.co.optimisticpanda.dropwizard.domain;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Maps;

public class Questionnaire implements Iterable<Question<?>>{

	private AtomicInteger count = new AtomicInteger();
	private Map<Integer, Question<?>> questions = Maps.newLinkedHashMap();
	
	public void add(Question<?> question){
		questions.put(count.getAndIncrement(), question);
	}

	public Iterator<Question<?>> iterator() {
		return questions.values().iterator();
	}
	
	
	
	
	
	
}
