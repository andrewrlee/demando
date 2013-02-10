package uk.co.optimisticpanda.dropwizard.atom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

@Provider
@Produces(MediaType.APPLICATION_ATOM_XML)
@Consumes(MediaType.APPLICATION_ATOM_XML)
public class AbderaSupport implements MessageBodyWriter<Object>, MessageBodyReader<Object> {

	private final static Abdera abdera = new Abdera();

	public static Abdera getAbdera() {
		return abdera;
	}

	public Object readFrom(Class<Object> feedOrEntry, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException {
		Document<Element> doc = getAbdera().getParser().parse(entityStream);
		Element el = doc.getRoot();
		if (feedOrEntry.isAssignableFrom(el.getClass())) {
			return el;
		} else {
			throw new IOException("Unexpected payload, expected " + feedOrEntry.getName() + ", received " + el.getClass().getName());
		}
	}

	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return (Feed.class.isAssignableFrom(type) || Entry.class.isAssignableFrom(type));
	}

	public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	public void writeTo(Object feedOrEntry, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException {
		if (feedOrEntry instanceof Feed) {
			Feed feed = (Feed) feedOrEntry;
			Document<Feed> doc = feed.getDocument();
			doc.writeTo(entityStream);
		} else {
			Entry entry = (Entry) feedOrEntry;
			Document<Entry> doc = entry.getDocument();
			doc.writeTo(entityStream);
		}

	}

	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return (Feed.class.isAssignableFrom(type) || Entry.class.isAssignableFrom(type));
	}
}