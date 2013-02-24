package uk.co.optimisticpanda.dropwizard.util.batch;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public enum BatchGenerator{
	;
	
	public static List<Batch> getBatches(long min, long max, int batchSize) {
		List<Batch> result = Lists.newArrayList();
		long currentBottom = min;
		long currentTop = currentBottom + batchSize;
		long numberOfBatches = (max - min) / batchSize;
		Optional<Batch> previous = Optional.absent();
		
		for (long i = 0; i <= numberOfBatches; i++) {
			currentTop = Math.min(max + 1, (currentBottom + batchSize));
			
			Batch batch = new Batch(i, currentBottom, currentTop, previous);
			
			result.add(batch);
			previous = Optional.of(batch);
			
			currentBottom = currentBottom + batchSize;
		}
		
		return result;
	}
	
}
