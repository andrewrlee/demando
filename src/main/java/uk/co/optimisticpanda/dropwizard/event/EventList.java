package uk.co.optimisticpanda.dropwizard.event;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

import uk.co.optimisticpanda.dropwizard.util.batch.Batch;

public class EventList<D extends Resource<?>> implements Iterable<Event<D>> {

	private final List<Event<D>> events;
	private final String[] categories;
	private final String title;
	private LinkProvider linkProvider;
	
	public enum FeedType{
		RECENT_EVENTS, ARCHIVE
	}
	
	public EventList(List<Event<D>> events, String[] categories, String title, Batch batch) {
		this.events = events;
		this.categories = categories;
		this.title = title;
		this.linkProvider = new LinkProvider(batch);
	}

	public String getTitle() {
		return title;
	}
		
	public Iterator<Event<D>> iterator() {
		return events.iterator();
	}

	public void prepareLinks(UriBuilder notificationUriBuilder, UriBuilder payloadUriBuilder, FeedType feedType){
		linkProvider.prepare(notificationUriBuilder, payloadUriBuilder, feedType);
	}
	
	public void addFeedUrls(Feed feed) {
		linkProvider.addFeedUrls(feed);
	}
	
	public void addEntryUrls(Entry entry, Resource<?> resource) {
		linkProvider.addEntryUrls(entry, resource);
	}
	
	public String[] getCategories(){
		return categories;
	}

	
}
