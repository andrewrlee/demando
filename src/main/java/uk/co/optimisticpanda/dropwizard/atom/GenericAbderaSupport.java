package uk.co.optimisticpanda.dropwizard.atom;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

import uk.co.optimisticpanda.dropwizard.event.Event;
import uk.co.optimisticpanda.dropwizard.event.EventList;

@Provider
@Produces(MediaType.APPLICATION_ATOM_XML)
public class GenericAbderaSupport implements MessageBodyWriter<Object> {

	private final static Abdera abdera = new Abdera();

	public static Abdera getAbdera() {
		return abdera;
	}

	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type.isAssignableFrom(EventList.class);
	}

	public void writeTo(Object list, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException {
		EventList<?, ?> eventList = (EventList<?, ?>) list;
		Feed feed = getAbdera().getFactory().newFeed();

		for (String category : eventList.getCategories()) {
			feed.addCategory(category);
		}
		for (Event<?, ?> event : eventList.getEvents()) {
			feed.addEntry(createEntry(eventList, event));
		}
		feed.addLink(eventList.getEventRootResourceUrl(), "self");
		feed.getDocument().writeTo(entityStream);
	}

	private Entry createEntry(EventList<?,?> list, Event<?, ?> event) {
		Entry entry = getAbdera().newEntry();
		entry.addCategory(event.getType().name());
		entry.setContent(event.getContent(), MediaType.APPLICATION_JSON);
		entry.setId(event.getUuid());
		entry.setUpdated(event.getUpdateDate());
		entry.addLink(list.getEventRootResourceUrl() + event.getUuid(), "self");
		entry.addLink(list.getPayloadRootResourceUrl() + event.getUuid(), "related");
		return entry;
	}

	public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}
}