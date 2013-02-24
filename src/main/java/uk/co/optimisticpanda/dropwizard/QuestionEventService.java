package uk.co.optimisticpanda.dropwizard;

import java.util.List;
import java.util.Map;

import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.event.Event;
import uk.co.optimisticpanda.dropwizard.event.EventList;
import uk.co.optimisticpanda.dropwizard.util.batch.Batch;
import uk.co.optimisticpanda.dropwizard.util.batch.BatchGenerator;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class QuestionEventService {

	public enum ChangeType{
		CREATE, UPDATE, DELETE;
		public static String[] valuesAsStings(){
			List<String> strings = Lists.newArrayList();
			for (ChangeType type: values()) {
				strings.add(type.name());
			}
			return strings.toArray(new String[0]);
		}
	}
	
	private final QuestionEventDao dao;
	
	public QuestionEventService(QuestionEventDao dao){
		this.dao = dao;
	}
	
	public EventList<Question<?>> getRecentEvents(){
		Map<String, Object> minAndMax = dao.getMinAndMax();
		List<Batch> batches = BatchGenerator.getBatches((Long)minAndMax.get("min"), (Long)minAndMax.get("max"), 20);
		Batch last = Iterables.getLast(batches);
		List<Event<Question<?>>> results = dao.get(last.getFrom(), last.getTo());
		return wrap(results, last);
	}
	
	public EventList<Question<?>> getEvents(int id) {
		Map<String, Object> minAndMax = dao.getMinAndMax();
		List<Batch> batches = BatchGenerator.getBatches((Long)minAndMax.get("min"), (Long)minAndMax.get("max"), 20);
		Batch batch = batches.get(id);
		List<Event<Question<?>>> results = dao.get(batch.getFrom(), batch.getTo());
		return wrap(results, batch);
	}

	public EventList<Question<?>> wrap(List<Event<Question<?>>> events, Batch batch) {
    	return new EventList<Question<?>>(events, ChangeType.valuesAsStings(), "Notifications", batch);
	}


	
}
