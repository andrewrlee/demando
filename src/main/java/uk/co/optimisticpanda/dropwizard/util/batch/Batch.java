package uk.co.optimisticpanda.dropwizard.util.batch;

import com.google.common.base.Optional;

public class Batch {

    private final long batchNumber;
    private final long min;
    private final long max;
    private final Optional<Long> previous;
    private Optional<Long> next;

    public static Optional<Batch> getBatch(int index, long min, long max, int batchSize) {
        long numberOfBatches = (max - min) / batchSize;

        if (index > numberOfBatches || index < 0) {
            return Optional.absent();
        }

        long bottom = index * batchSize;
        long top = Math.min(max, (bottom + batchSize));

        Optional<Long> previous = index != 0 ? Optional.<Long> of(Long.valueOf(index - 1)) : Optional.<Long> absent();
        Optional<Long> next = index < numberOfBatches ? Optional.<Long> of(Long.valueOf(index + 1)) : Optional.<Long> absent();

        Batch batch = new Batch(index, bottom + 1, top, previous, next);
        return Optional.of(batch);
    }
        
    public static Batch getLastBatch(long min, long max, int batchSize) {
        int numberOfBatches = Long.valueOf((max - min) / batchSize).intValue();
        return getBatch(numberOfBatches, min, max, batchSize).get();
    }

    private Batch(long batchNumber, long min, long max, Optional<Long> previous, Optional<Long> next) {
        this.batchNumber = batchNumber;
        this.min = min;
        this.max = max;
        this.previous = previous;
        this.next = next;
    }

    public long getBatchNumber() {
        return batchNumber;
    }

    public long getTo() {
        return max;
    }

    public long getFrom() {
        return min;
    }

    public Optional<Long> getPreviousBatch() {
        return previous;
    }

    public Optional<Long> getNextBatch() {
        return next;
    }

    @Override
    public String toString() {
        return getBatchNumber() + " -> " + min + "-" + max + "\t[prev=" + previous.isPresent() + ",next=" + next.isPresent() + "]";
    }
}