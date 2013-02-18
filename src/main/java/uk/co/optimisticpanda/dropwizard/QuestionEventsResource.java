package uk.co.optimisticpanda.dropwizard;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao;
import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao.Change;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.event.Event;
import uk.co.optimisticpanda.dropwizard.event.EventList;

import com.google.common.base.Preconditions;
import com.yammer.metrics.annotation.Timed;

@Path("/question_event")
@Produces({MediaType.APPLICATION_ATOM_XML})
public class QuestionEventsResource {

	@Context
	UriInfo uriInfo;
	private final QuestionEventDao dao;

	public QuestionEventsResource (QuestionEventDao dao) {
        this.dao = dao;
    }

    @GET
    @Timed
    @Path("/{id}")
    public EventList<Question<?>, Change> get(@PathParam("id") String id) {
        return wrap(Collections.singletonList(dao.get(id)));
    }
    
    @GET
    @Timed
    @Path("/")
    public EventList<Question<?>, Change> getAll() {
    	return wrap(dao.getAll());
    }

    @POST
    @Timed
    @Path("/{id}")
    public void update(@PathParam("uuid") String uuid, Event<Question<?>, Change> event) {
    	Preconditions.checkArgument(uuid == event.getUuid(), "Trying to update wrong environment.");
//    	dao.update(question.getType(), question);
    }
    
    @POST
    @Timed
    @Path("/")
    public void insert(Event<Question<?>, Change> event) {
    	dao.insert(event.getEventType(), event);
    }
    
    @DELETE
    @Timed
    @Path("/{uuid}")
    public void delete(@PathParam("uuid") String id) {
    	dao.delete(id);
    }
    
    public EventList<Question<?>, Change> wrap(List<Event<Question<?>,Change>> events) {
    	return new EventList<Question<?>,Change>(
    			events,                               //
    			Change.class,                         //
    			uriInfo.getAbsolutePath().toString(), //
    			uriInfo.getBaseUri().toString() + "question/");
	}
}