package uk.co.optimisticpanda.dropwizard;

import static com.sun.jersey.api.core.ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS;
import uk.co.optimisticpanda.dropwizard.atom.GenericAbderaSupport;
import uk.co.optimisticpanda.dropwizard.odata.OdataContainerRequestFilter;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
public class SignpostService extends Service<Config> {
	public static void main(String[] args) throws Exception {
		new SignpostService().run(args);
	}

	@Override
	public void initialize(Bootstrap<Config> bootstrap) {
		bootstrap.setName("signpost");
		bootstrap.addBundle(new AssetsBundle("/assets/app", "/"));
		bootstrap.addBundle(new AssetsBundle("/assets/test", "/test"));
	}

	@Override
	public void run(Config configuration, Environment env) {
		EnvironmentStore environmentStore = new EnvironmentStore();
		EventStore eventStore = new EventStore(environmentStore);
		
		env.addResource(new EnvironmentResource(environmentStore));
		env.addResource(new EventResource(eventStore));
		env.addHealthCheck(environmentStore.getHealthCheck());
		env.addProvider(GenericAbderaSupport.class);
		env.setJerseyProperty(PROPERTY_CONTAINER_REQUEST_FILTERS, new OdataContainerRequestFilter());
	}
}
