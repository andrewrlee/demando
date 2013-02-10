package uk.co.optimisticpanda.dropwizard;

import java.util.Collection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.yammer.metrics.annotation.Timed;

@Produces({ MediaType.APPLICATION_ATOM_XML })
@Path("/environment-event/")
public class EventResource {

	private final EventStore store;

	public EventResource(EventStore store) {
		this.store = store;
	}

	@GET
	@Timed
	@Path("{id}")
	public Event<?> getEnvironment(@PathParam("id") String id) {
		return store.getEvent(id);
	}

	@GET
	@Timed
	@Path("")
	public Collection<Event<?>> getEnvironments() {
		return store.getEvents();
	}

	@POST
	@Timed
	@Path("")
	//Requires more thought
	public void addEvent(Event<?> event) {
		store.addEvent(event);
	}

	@DELETE
	@Timed
	@Path("{id}")
	public void deleteEvent(@PathParam("id") String id) {
		store.deleteEvent(id);
	}

}