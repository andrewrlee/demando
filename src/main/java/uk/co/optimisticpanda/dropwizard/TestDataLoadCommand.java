package uk.co.optimisticpanda.dropwizard;

import net.sourceforge.argparse4j.inf.Namespace;

import org.skife.jdbi.v2.DBI;

import uk.co.optimisticpanda.dropwizard.dao.QuestionDao;
import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao;
import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao.Change;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.domain.builder.DateGenerator;
import uk.co.optimisticpanda.dropwizard.domain.builder.EventGenerator;
import uk.co.optimisticpanda.dropwizard.event.Event;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.cli.EnvironmentCommand;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.jdbi.DBIFactory;

public class TestDataLoadCommand extends EnvironmentCommand<DemandoConfiguration> {

	protected TestDataLoadCommand(Service<DemandoConfiguration> service) {
		super(service, "loadData", "this is a command");
	}

	@Override
	protected void run(Environment environment, Namespace namespace, DemandoConfiguration configuration) throws Exception {
		DBI jdbi = new DBIFactory().build(environment, configuration.getDatabaseConfiguration(), "mysql");
		QuestionDao questionDao = jdbi.onDemand(QuestionDao.class);
		QuestionEventDao eventDao = jdbi.onDemand(QuestionEventDao.class);
		
		DateGenerator generator = new DateGenerator();
		EventGenerator eventGenerator = new EventGenerator();
		generator.collect(eventGenerator);
		
		System.out.println("Processing " + eventGenerator.getEvents().size() + " events!");
		for (Event<Question<?>, Change> event: eventGenerator.getEvents()) {
			System.out.println("Inserting event: " + event.getId());
			eventDao.insert(event.getEventType(), event);
			if(event.getEventType().equals(Change.CREATE)){
				System.out.println("Creating Question: " + event.getPayload().getId());
				questionDao.insert(event.getPayload().getType(), event.getPayload());
			}else if(event.getEventType().equals(Change.UPDATE)){
				System.out.println("Updating Question: " + event.getPayload().getId());
				questionDao.update(event.getPayload().getType(), event.getPayload());
			}
		}
	}

}
