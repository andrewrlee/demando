package uk.co.optimisticpanda.dropwizard;

import static com.sun.jersey.api.core.ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS;

import org.skife.jdbi.v2.DBI;

import uk.co.optimisticpanda.dropwizard.atom.GenericAtomSupport;
import uk.co.optimisticpanda.dropwizard.dao.QuestionDao;
import uk.co.optimisticpanda.dropwizard.odata.OdataContainerRequestFilter;

import com.google.common.base.Throwables;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.jdbi.DBIFactory;
import com.yammer.dropwizard.migrations.MigrationsBundle;
public class DemandoService extends Service<DemandoConfiguration> {
	public static void main(String[] args) throws Exception {
		new DemandoService().run(args);
	}

	@Override
	public void initialize(Bootstrap<DemandoConfiguration> bootstrap) {
		bootstrap.setName("demando");
		bootstrap.addCommand(new TestDataLoadCommand(this));
		bootstrap.addBundle(new AssetsBundle("/assets/app", "/"));
		bootstrap.addBundle(new AssetsBundle("/assets/test", "/test"));
		bootstrap.addBundle(new MigrationsBundle<DemandoConfiguration>(){
			public DatabaseConfiguration getDatabaseConfiguration(DemandoConfiguration configuration) {
				return configuration.getDatabaseConfiguration();
			}});
	}

	@Override
	public void run(DemandoConfiguration configuration, Environment env) {
		env.addResource(new QuestionResource(buildQuestionDao(env, configuration)));
//		env.addResource(new EventResource(eventStore));
		env.addProvider(GenericAtomSupport.class);
		env.setJerseyProperty(PROPERTY_CONTAINER_REQUEST_FILTERS, new OdataContainerRequestFilter());
	}
	
	private QuestionDao buildQuestionDao(Environment environment, DemandoConfiguration configuration) {
		try {
			DBIFactory factory = new DBIFactory();
			DBI jdbi = factory.build(environment, configuration.getDatabaseConfiguration(), "mysql");
			return jdbi.onDemand(QuestionDao.class);
		} catch (ClassNotFoundException e) {
			throw Throwables.propagate(e);
		}
	
	}
}
