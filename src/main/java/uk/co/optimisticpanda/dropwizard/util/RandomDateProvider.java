package uk.co.optimisticpanda.dropwizard.util;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RandomDateProvider {

	private static Random random = new Random();

	// Generates events for the last 30 days - roughly 50/day
	public void provide(DateVisitor visitor) {
		long now = new Date().getTime();
		long inThePast = TimeUnit.DAYS.toMillis(30);
		Date startDate = new Date(now - inThePast);

		long currentDate = startDate.getTime();
		while (currentDate < now) {
			long newTime = currentDate + getRandomDuration();
			if (now < newTime) {
				break;
			}
			if(!visitor.visit(new Date(newTime))){
				return;
			};
			currentDate = newTime;
		}
	}

	// Up to 2 hour
	private static long getRandomDuration() {
		int _2hour = 60000 * 120;
		return Long.valueOf(random.nextInt(_2hour)).longValue();
	}

	public interface DateVisitor {
		public boolean visit(Date randomDate);
	}

}
