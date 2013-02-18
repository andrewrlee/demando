package uk.co.optimisticpanda.dropwizard.util;

import org.skife.jdbi.v2.DBI;

import uk.co.optimisticpanda.dropwizard.DemandoConfiguration;

import com.google.common.base.Throwables;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.jdbi.DBIFactory;

public class DaoFactory {

	private DBI jdbi;

	public DaoFactory(Environment environment, DemandoConfiguration configuration) {
		try {
			DBIFactory factory = new DBIFactory();
			jdbi = factory.build(environment, configuration.getDatabaseConfiguration(), "mysql");
		} catch (ClassNotFoundException e) {
			throw Throwables.propagate(e);
		}
	}

	public <D> D getDao(Class<D> clazz) {
		return jdbi.onDemand(clazz);
	}
}
