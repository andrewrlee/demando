package uk.co.optimisticpanda.dropwizard;

import java.util.List;
import java.util.Map;

import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.event.Event;
import uk.co.optimisticpanda.dropwizard.event.EventList;
import uk.co.optimisticpanda.dropwizard.util.batch.Batch;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class QuestionEventService {

	public enum ChangeType {
		CREATE, UPDATE, DELETE;
		public static String[] valuesAsStings() {
			List<String> strings = Lists.newArrayList();
			for (ChangeType type : values()) {
				strings.add(type.name());
			}
			return strings.toArray(new String[0]);
		}
	}

	private final QuestionEventDao dao;

	public QuestionEventService(QuestionEventDao dao) {
		this.dao = dao;
	}

	public EventList<Question<?>> getRecentEvents() {
		Map<String, Object> minAndMax = dao.getMinAndMax();
		Batch last = Batch.getLastBatch((Long) minAndMax.get("min"), (Long) minAndMax.get("max"), 20);
		List<Event<Question<?>>> results = dao.get(last.getFrom(), last.getTo());
		return wrap(results, last);
	}

	public EventList<Question<?>> getEvents(int id) {
		Map<String, Object> minAndMax = dao.getMinAndMax();
		Optional<Batch> batch = Batch.getBatch(id, (Long) minAndMax.get("min"), (Long) minAndMax.get("max"), 20);
		if (!batch.isPresent()) {
			return null;
		}
		return wrap(dao.get(batch.get().getFrom(), batch.get().getTo()), batch.get());
	}

	public EventList<Question<?>> wrap(List<Event<Question<?>>> events, Batch batch) {
		return new EventList<Question<?>>(events, ChangeType.valuesAsStings(), "Notifications", batch);
	}

}
