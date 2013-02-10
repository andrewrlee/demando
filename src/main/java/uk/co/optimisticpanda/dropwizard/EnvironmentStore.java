package uk.co.optimisticpanda.dropwizard;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.Maps;
import com.yammer.metrics.core.HealthCheck;

public class EnvironmentStore {

	private static final AtomicLong idProvider = new AtomicLong();
	private final Map<Long,Environment> map = Maps.newLinkedHashMap();

	public EnvironmentStore(){
    	addEnvironment(new Environment("test", "www.test.com"));
    	addEnvironment(new Environment("development", "www.dev.com"));
    	addEnvironment(new Environment("production", "www.prod.com" ));
	}

	public void addEnvironment(Environment environment){
		long id = idProvider.getAndIncrement();
		environment.setId(id);
		map.put(id, environment);
	}
	
	public void updateEnvironment(Environment environment) {
		map.put(environment.getId(), environment);
	}

	
	public Environment getEnvironment(long id){
		if(!map.containsKey(id)){
			throw new IllegalStateException("Could not find environment with id:" + id);
		}
		return map.get(id);
	}
	
	public void deleteEnvironment(long id){
		if(!map.containsKey(id)){
			throw new IllegalStateException("Could not find environment with id to delete:" + id);
		}
		map.remove(id);
	}

	public Collection<Environment> getEnvironments() {
		return map.values();
	}

	public boolean isAvailable() {
		return true;
	}
	
	public HealthCheck getHealthCheck(){
		return new HealthCheck(this.getClass().getName()){
			@Override
			protected Result check() throws Exception {
				if (isAvailable()) {
					return Result.healthy();
				} else {
					return Result.unhealthy("Cannot connect to " + this.getClass().getName());
				}
			}
		};
	}

}
