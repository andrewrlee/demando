package uk.co.optimisticpanda.dropwizard;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.event.EventList;
import uk.co.optimisticpanda.dropwizard.event.EventList.FeedType;

import com.yammer.metrics.annotation.Timed;

@Path("/notifications")
@Produces({MediaType.APPLICATION_ATOM_XML})
public class QuestionEventsResource {

    @Context
    UriInfo uriInfo;
	private QuestionEventService service;

    public QuestionEventsResource (QuestionEventDao dao) {
        this.service = new QuestionEventService(dao);
    }

    @GET
    @Timed
    @Path("/")
    public EventList<Question<?>> getRecentEvents() {
    	EventList<Question<?>> recentEvents = service.getRecentEvents();
    	recentEvents.prepareLinks(getNotificationUriBuilder(), getPayloadUriBuilder(), FeedType.RECENT_EVENTS);
    	return recentEvents;
    }

    @GET
    @Timed
    @Path("/{id}")
    public EventList<Question<?>> getEvents(@PathParam("id") int id) {
    	EventList<Question<?>> events = service.getEvents(id);
    	events.prepareLinks(getNotificationUriBuilder(), getPayloadUriBuilder(), FeedType.ARCHIVE);
    	return events;
    }
    
    private UriBuilder getPayloadUriBuilder(){
    	return uriInfo.getBaseUriBuilder().path(QuestionResource.class);
    }
    
    private UriBuilder getNotificationUriBuilder(){
    	return uriInfo.getBaseUriBuilder().path(QuestionEventsResource.class);
    }
}
