package uk.co.optimisticpanda.dropwizard.util.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static uk.co.optimisticpanda.dropwizard.util.batch.Batch.*;

import org.junit.Test;

public class BatchTest {

	@Test
	public void checkSmallBatch() {
		checkBatch(getBatch(0, 1, 6, 2).get(), 1, 2);
		checkBatch(getBatch(1, 1, 6, 2).get(), 3, 4);
		checkBatch(getBatch(2, 1, 6, 2).get(), 5, 6);

		checkBatch(getLastBatch(1, 6, 2), 5, 6);
	}

	@Test
	public void checkHugeBatch() {
		checkBatch(getBatch(0, 1, 1000, 20).get(), 1, 20);
		checkBatch(getBatch(49, 1, 1000, 20).get(), 981, 1000);
	
		checkBatch(getLastBatch(1, 1000, 20), 981, 1000);
	}

	@Test
	public void checkSingleBatch() {
		checkBatch(getBatch(0, 1, 1, 2).get(), 1, 1);
		checkBatch(getLastBatch(1, 1, 2), 1, 1);
	}

	@Test
	public void checkBatchSizeLargerThanEntrys() {
		checkBatch(getBatch(0, 1 ,3, 5).get(), 1, 3);
		checkBatch(getLastBatch(1, 3, 5), 1, 3);
	}

	@Test
	public void checkLastEntryHasNextButNoPrevious() {
		Batch batch = getBatch(2, 1, 6, 2).get();
		assertTrue(batch.getPreviousBatch().isPresent());
		assertFalse(batch.getNextBatch().isPresent());
		
		assertTrue(getLastBatch(1, 6, 2).getPreviousBatch().isPresent());
		assertFalse(getLastBatch(1, 6, 2).getNextBatch().isPresent());

		assertEquals(2, batch.getBatchNumber());
		assertEquals(Long.valueOf(1L), batch.getPreviousBatch().get());
	}

	@Test
	public void checkFirstEntryHasPreviousButNoNext() {
		Batch batch = getBatch(0, 1, 6, 2).get();

		assertFalse(batch.getPreviousBatch().isPresent());
		assertTrue(batch.getNextBatch().isPresent());

		assertEquals(0, batch.getBatchNumber());
		assertEquals(Long.valueOf(1L), batch.getNextBatch().get());
	}

	@Test
	public void checkMiddleEntryHasPreviousAndNext() {
		Batch batch = getBatch(1, 1, 6, 2).get();

		assertTrue(batch.getPreviousBatch().isPresent());
		assertTrue(batch.getNextBatch().isPresent());

		assertEquals(1, batch.getBatchNumber());
		assertEquals(Long.valueOf(0L), batch.getPreviousBatch().get());
		assertEquals(Long.valueOf(2L), batch.getNextBatch().get());
	}
	
	@Test
	public void checkOutsideIndex() {
		assertFalse(getBatch(-1, 1, 6, 2).isPresent());
		assertFalse(getBatch(3, 1, 6, 2).isPresent());
		assertFalse(getBatch(5000, 1, 6, 2).isPresent());
	}

	private void checkBatch(Batch batch, int from, int to) {
		assertEquals("from was expected to be " + from + ", but was " + batch.getFrom(), from, batch.getFrom());
		assertEquals("to was expected to be " + to + ", but was " + batch.getTo(), to, batch.getTo());
	}

}
