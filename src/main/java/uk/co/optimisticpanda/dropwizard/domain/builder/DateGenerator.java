package uk.co.optimisticpanda.dropwizard.domain.builder;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DateGenerator {

	private static Random random = new Random();

	// Generates events for the last 30 days - roughly 50/day
	public void collect(DateVisitor visitor) {
		long now = new Date().getTime();
		long inThePast = TimeUnit.DAYS.toMillis(30);
		Date startDate = new Date(now - inThePast);

		long currentDate = startDate.getTime();
		while (currentDate < now) {
			long newTime = currentDate + getRandomDuration();
			if (now < newTime) {
				break;
			}
			visitor.visit(new Date(newTime));
			currentDate = newTime;
		}
	}

	// Up to 2 hour
	private static long getRandomDuration() {
		int _2hour = 60000 * 120;
		return Long.valueOf(random.nextInt(_2hour)).longValue();
	}

	public interface DateVisitor {
		public void visit(Date randomDate);
	}

}
