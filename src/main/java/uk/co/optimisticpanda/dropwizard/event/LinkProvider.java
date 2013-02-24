package uk.co.optimisticpanda.dropwizard.event;

import static uk.co.optimisticpanda.dropwizard.event.EventList.FeedType.RECENT_EVENTS;

import javax.ws.rs.core.UriBuilder;

import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

import uk.co.optimisticpanda.dropwizard.event.EventList.FeedType;
import uk.co.optimisticpanda.dropwizard.util.batch.Batch;

import com.google.common.base.Optional;

public class LinkProvider {

	private final Batch batch;
	private FeedType feedType;
	private UriBuilder payloadUriBuilder;
	private UriBuilder notificationUriBuilder;

	public LinkProvider(Batch batch) {
		this.batch = batch;
	}

	public void addFeedUrls(Feed feed) {
		feed.addLink(getThisUrl(), "this");
		if (getViaUrl().isPresent()) {
			feed.addLink(getViaUrl().get(), "via");
		}
		if (getNextFeedUrl().isPresent()) {
			feed.addLink(getNextFeedUrl().get(), "next-archive");
		}
		if (getPreviousFeedUrl().isPresent()) {
			feed.addLink(getPreviousFeedUrl().get(), "prev-archive");
		}
		//For debug purposes
		appendFeedEntriesToEntry(feed);
	}

	public void addEntryUrls(Entry entry, Resource<?> resource) {
		entry.addLink(payloadUriBuilder.build().toString() + resource.getId(), "rel");
	}

	public void prepare(UriBuilder notificationUriBuilder, UriBuilder payloadUriBuilder, FeedType feedType) {
		this.payloadUriBuilder = payloadUriBuilder;
		this.notificationUriBuilder = notificationUriBuilder;
		this.feedType = feedType;
	}

	private String getThisUrl() {
		return feedType != RECENT_EVENTS ? workingEventFeedUrl() + "/" + batch.getBatchNumber() : workingEventFeedUrl();
	}

	private Optional<String> getViaUrl() {
		return feedType != RECENT_EVENTS ? Optional.<String> absent() : Optional.of(workingEventFeedUrl() + "/" + batch.getBatchNumber());
	}

	private Optional<String> getPreviousFeedUrl() {
		if (batch.getPreviousBatch().isPresent()) {
			return Optional.of(workingEventFeedUrl() + "/" + batch.getPreviousBatch().get().getBatchNumber());
		}
		return Optional.<String> absent();
	}

	private Optional<String> getNextFeedUrl() {
		if (batch.getNextBatch().isPresent()) {
			return Optional.of(workingEventFeedUrl() + "/" + batch.getNextBatch().get().getBatchNumber());
		}
		return Optional.<String> absent();
	}

	private String workingEventFeedUrl() {
		return notificationUriBuilder.build().toString();
	}

	//TODO remove - adds another entry to allow debugging feed traversing
	private void appendFeedEntriesToEntry(Feed feed) {
		Entry entry = GenericAtomSupport.getAbdera().newEntry();

		StringBuilder builder = new StringBuilder();
		entry.setTitle("Links");

		for (Link link : feed.getLinks()) {
			builder.append("<a href=\"" + link.getHref() + "\">" + link.getRel() + "</a><br/>");
		}
		entry.addCategory("IGNORE");
		entry.setSummaryAsHtml(builder.toString());
		feed.addEntry(entry);
	}
}
