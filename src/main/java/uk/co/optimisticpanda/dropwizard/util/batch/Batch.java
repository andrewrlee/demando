package uk.co.optimisticpanda.dropwizard.util.batch;

import com.google.common.base.Optional;

public class Batch {

		private final long batchNumber;
		private final long min;
		private final long max;
		private final Optional<Batch> previous;
		private Optional<Batch> next;

		public Batch(long batchNumber, long min, long max, Optional<Batch> previous) {
			this.batchNumber = batchNumber;
			this.min = min;
			this.max = max;
			this.previous = previous;
			if(previous.isPresent()){
				previous.get().next = Optional.of(this);
			}
			this.next = Optional.absent();
		}

		public void setNext(Optional<Batch> next){
			this.next = next;
		}
		
		public long getBatchNumber(){
			return batchNumber;
		}

		public long getTo(){
			return max;
		}

		public long getFrom(){
			return min;
		}

		public Optional<Batch> getPreviousBatch(){
			return previous;
		}

		public Optional<Batch>  getNextBatch(){
			return next;
		}
		
		@Override
		public String toString() {
			return getBatchNumber() + " -> " + min + "-" + max + "\t[prev="+ previous.isPresent() + ",next=" + next.isPresent() +"]" ;
		}
	}