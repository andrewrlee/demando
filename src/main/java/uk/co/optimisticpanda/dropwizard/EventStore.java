package uk.co.optimisticpanda.dropwizard;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;
import com.yammer.metrics.core.HealthCheck;

public class EventStore {

	private final Map<String, Event<?>> map = Maps.newLinkedHashMap();

	public EventStore(EnvironmentStore store) {
			for (Environment environment : store.getEnvironments()) {
				addEvent(new Event<Environment>(Event.Type.CREATE, environment));
				addEvent(new Event<Environment>(Event.Type.UPDATE, environment));
				addEvent(new Event<Environment>(Event.Type.UPDATE, environment));
				addEvent(new Event<Environment>(Event.Type.UPDATE, environment));
				addEvent(new Event<Environment>(Event.Type.DELETE, environment));
		}
	}

	public void addEvent(Event<?> event) {
		map.put(event.getUuid(), event);
	}

	public void updateEvent(Event<?> event) {
		map.put(event.getUuid(), event);
	}

	public Event<?> getEvent(String uuid) {
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

	public Collection<Event<?>> getEvents() {
		return map.values();
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
