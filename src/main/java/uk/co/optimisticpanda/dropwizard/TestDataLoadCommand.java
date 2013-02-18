package uk.co.optimisticpanda.dropwizard;

import static uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao.Change.CREATE;
import static uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao.Change.UPDATE;
import net.sourceforge.argparse4j.inf.Namespace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.optimisticpanda.dropwizard.dao.QuestionDao;
import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao;
import uk.co.optimisticpanda.dropwizard.dao.QuestionEventDao.Change;
import uk.co.optimisticpanda.dropwizard.domain.Question;
import uk.co.optimisticpanda.dropwizard.domain.builder.EventGenerator;
import uk.co.optimisticpanda.dropwizard.event.Event;
import uk.co.optimisticpanda.dropwizard.util.DaoFactory;
import uk.co.optimisticpanda.dropwizard.util.RandomDateProvider;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.cli.EnvironmentCommand;
import com.yammer.dropwizard.config.Environment;

public class TestDataLoadCommand extends EnvironmentCommand<DemandoConfiguration> {

	private static final Logger log = LoggerFactory.getLogger(TestDataLoadCommand.class);

	protected TestDataLoadCommand(Service<DemandoConfiguration> service) {
		super(service, "loadData", "this is a command that generates test Questions and their associated events");
	}

	@Override
	protected void run(Environment environment, Namespace namespace, DemandoConfiguration configuration) throws Exception {
		DaoFactory factory= new DaoFactory(environment, configuration);
		QuestionDao questionDao = factory.getDao(QuestionDao.class);
		QuestionEventDao eventDao = factory.getDao(QuestionEventDao.class);

		EventGenerator eventGenerator = new EventGenerator();
		new RandomDateProvider().provide(eventGenerator);

		log.info("Processing {} events!", eventGenerator.getEvents().size());
		for (Event<Question<?>, Change> event : eventGenerator.getEvents()) {
			log.info("Inserting event: {}", event.getId());
			eventDao.insert(event.getEventType(), event);
			
			Question<?> question = event.getPayload();
			
			if (event.getEventType() == CREATE) {
				log.info("Creating Question: {}", question);
				questionDao.insert(question.getType(), question);
			
			} else if (event.getEventType() == UPDATE) {
				log.info("Updating Question: {}", question);
				questionDao.update(question.getType(), question);
			}
		}
	}

}
