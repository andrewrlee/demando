package uk.co.optimisticpanda.dropwizard.event;

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


@Provider
@Produces(MediaType.APPLICATION_ATOM_XML)
public class GenericAtomSupport implements MessageBodyWriter<Object> {

	private final static Abdera abdera = new Abdera();

	public static Abdera getAbdera() {
		return abdera;
	}

	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type.isAssignableFrom(EventList.class);
	}

	public void writeTo(Object list, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException {
		EventList<?> eventList = (EventList<?>) list;
		Feed feed = getAbdera().getFactory().newFeed();
		feed.setTitle(eventList.getTitle());

		for (String category : eventList.getCategories()) {
			feed.addCategory(category);
		}
		for (Event<?> event : eventList) {
			Entry entry = createEntry(event);
			eventList.addEntryUrls(entry, event.getPayload());
			feed.addEntry(entry);
		}
		
		eventList.addFeedUrls(feed);
		feed.getDocument().writeTo(entityStream);
	}

	private Entry createEntry(Event<?> event) {
		Entry entry = getAbdera().newEntry();
		
		entry.addCategory(event.getCategory());
		entry.setContent(event.getContent(), MediaType.APPLICATION_JSON);
		
		entry.setId(event.getUuid());
		entry.setTitle(event.getCategory() + " : "+ event.getPayload().getClass() + " [" +  event.getId() + "]");
		
		entry.setSummary(event.getContent());
		
		entry.addAuthor("SYSTEM");
		
		entry.setUpdated(event.getCreatedDate());
		
		return entry;
	}

	public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}
}