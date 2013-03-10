package uk.co.optimisticpanda.dropwizard.domain;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class Questionnaire implements Iterable<Entry>{

	private List<Entry> entries = Lists.newArrayList();
	
	public void add(Entry entry){
		entries.add(entry);
	}

	public Iterator<Entry> iterator() {
		return entries.iterator();
	}
	
	
	
	
	
	
}
