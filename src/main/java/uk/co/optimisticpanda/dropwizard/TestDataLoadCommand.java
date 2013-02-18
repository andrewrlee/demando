package uk.co.optimisticpanda.dropwizard;

import net.sourceforge.argparse4j.inf.Namespace;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.optimisticpanda.dropwizard.dao.QuestionDao;
import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao;
import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao.Change;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.domain.builder.EventGenerator;
import uk.co.optimisticpanda.dropwizard.event.Event;
import uk.co.optimisticpanda.dropwizard.util.RandomDateProvider;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.cli.EnvironmentCommand;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.jdbi.DBIFactory;

public class TestDataLoadCommand extends EnvironmentCommand<DemandoConfiguration> {

	private static final Logger log = LoggerFactory.getLogger(TestDataLoadCommand.class);

	protected TestDataLoadCommand(Service<DemandoConfiguration> service) {
		super(service, "loadData", "this is a command that generates test Questions and their associated events");
	}

	@Override
	protected void run(Environment environment, Namespace namespace, DemandoConfiguration configuration) throws Exception {
		DBI jdbi = new DBIFactory().build(environment, configuration.getDatabaseConfiguration(), "mysql");
		QuestionDao questionDao = jdbi.onDemand(QuestionDao.class);
		QuestionEventDao eventDao = jdbi.onDemand(QuestionEventDao.class);

		EventGenerator eventGenerator = new EventGenerator();
		new RandomDateProvider().provide(eventGenerator);

		System.out.println();
		log.info("Processing {} events!", eventGenerator.getEvents().size());
		for (Event<Question<?>, Change> event : eventGenerator.getEvents()) {
			log.info("Inserting event: {}", event.getId());
			eventDao.insert(event.getEventType(), event);
			
			if (event.getEventType().equals(Change.CREATE)) {
				log.info("Creating Question: {}", event.getPayload().getId());
				questionDao.insert(event.getPayload().getType(), event.getPayload());
			
			} else if (event.getEventType().equals(Change.UPDATE)) {
				log.info("Updating Question: {}", event.getPayload().getId());
				questionDao.update(event.getPayload().getType(), event.getPayload());
			}
		}
	}

}
