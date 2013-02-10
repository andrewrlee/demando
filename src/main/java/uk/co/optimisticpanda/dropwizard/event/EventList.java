package uk.co.optimisticpanda.dropwizard.event;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class EventList<D extends Resource, E extends Enum<E>> {

	private List<Event<D,E>> events;
	private String eventRootResourceUrl;
	private String payloadRootResourceUrl;
	private final Class<E> categoryType;
	
	public EventList(List<Event<D,E>> events, Class<E> categoryType, String eventRootResourceUrl, String payloadRootResourceUrl) {
		this.events = events;
		this.categoryType = categoryType;
		this.eventRootResourceUrl = eventRootResourceUrl;
		this.payloadRootResourceUrl = payloadRootResourceUrl;
	}

	public List<Event<D,E>> getEvents() {
		return events;
	}

	public String getEventRootResourceUrl() {
		return eventRootResourceUrl;
	}

	public String getPayloadRootResourceUrl() {
		return payloadRootResourceUrl;
	}
	
	public String[] getCategories(){
		ArrayList<String> list = Lists.newArrayList();
		for (E type : categoryType.getEnumConstants()) {
			list.add(type.name());
		}
		return list.toArray(new String[0]);
	}
	
}
