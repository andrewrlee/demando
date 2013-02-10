package uk.co.optimisticpanda.dropwizard.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.co.optimisticpanda.dropwizard.Environment;
import uk.co.optimisticpanda.dropwizard.EnvironmentStore;

import com.google.common.collect.Maps;
import com.yammer.metrics.core.HealthCheck;
import uk.co.optimisticpanda.dropwizard.event.EventResource.Categories;
public class EventStore {

	private final Map<String, Event<Environment,Categories>> map = Maps.newLinkedHashMap();

	public EventStore(EnvironmentStore store) {
			for (Environment environment : store.getEnvironments()) {
				addEvent(new Event<Environment,Categories>(Categories.CREATE, environment));
				addEvent(new Event<Environment,Categories>(Categories.UPDATE, environment));
				addEvent(new Event<Environment,Categories>(Categories.UPDATE, environment));
				addEvent(new Event<Environment,Categories>(Categories.UPDATE, environment));
				addEvent(new Event<Environment,Categories>(Categories.DELETE, environment));
		}
	}

	public void addEvent(Event<Environment,Categories> event) {
		map.put(event.getUuid(), event);
	}

	public void updateEvent(Event<Environment,Categories> event) {
		map.put(event.getUuid(), event);
	}

	public Event<Environment,Categories> getEvent(String uuid) {
		if (!map.containsKey(uuid)) {
			throw new IllegalStateException("Could not find environment with id:" + uuid);
		}
		return map.get(uuid);
	}

	public void deleteEvent(String id) {
		if (!map.containsKey(id)) {
			throw new IllegalStateException("Could not find environment with id to delete:" + id);
		}
		map.remove(id);
	}

	public List<Event<Environment,Categories>> getEvents() {
		return new ArrayList<Event<Environment,Categories>>(map.values());
	}

	public boolean isAvailable() {
		return true;
	}

	public HealthCheck getHealthCheck() {
		return new HealthCheck(this.getClass().getName()) {
			@Override
			protected Result check() throws Exception {
				if (isAvailable()) {
					return Result.healthy();
				} else {
					return Result.unhealthy("Cannot connect to " + this.getClass().getName());
				}
			}
		};
	}

}
