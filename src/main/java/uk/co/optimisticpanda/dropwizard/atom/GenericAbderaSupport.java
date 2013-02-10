package uk.co.optimisticpanda.dropwizard.atom;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

import uk.co.optimisticpanda.dropwizard.Environment;
import uk.co.optimisticpanda.dropwizard.Event;

@Provider
@Produces(MediaType.APPLICATION_ATOM_XML)
public class GenericAbderaSupport implements MessageBodyWriter<Object> {

	private final static Abdera abdera = new Abdera();
	
	public static Abdera getAbdera() {
		return abdera;
	}

	//VERY NOT SAFE!
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
//		return type == Event.class || type.isAssignableFrom(Collection.class);
	}


	//VERY NOT SAFE!
	public void writeTo(Object elementsOrSingleEvent, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException {
		if (elementsOrSingleEvent instanceof Collection<?>) {
			Collection<Event<?>> entries = (Collection<Event<?>>)elementsOrSingleEvent;
			Feed feed = getAbdera().getFactory().newFeed();
			
			for (uk.co.optimisticpanda.dropwizard.Event.Type eventType: Event.Type.values()) {
				feed.addCategory(eventType.toString());
			}
			for (Event<?> event : entries) {
				feed.addEntry(createEntry(event));
			}
			feed.addLink("http://localhost:8080/services/environment-event/","self");
			feed.getDocument().writeTo(entityStream);
		}else if (elementsOrSingleEvent instanceof Event) {
			Entry entry = createEntry((Event<?>)elementsOrSingleEvent);
			entry.getDocument().writeTo(entityStream);
		} 
	}

	private Entry createEntry(Event<?> event) {
		Entry entry = getAbdera().newEntry();
		entry.addCategory(event.getType().name());
		entry.setContent(event.getContent(), MediaType.APPLICATION_JSON);
		entry.setId(event.getUuid());
		entry.setUpdated(event.getUpdateDate());
		entry.addLink("http://localhost:8080/service/environment-event/" + event.getUuid(), "self");
		entry.addLink("http://localhost:8080/service/environment/" + ((Environment)event.getPayload()).getId(), "related");
		return entry;
	}

	public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}
}