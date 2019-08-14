/**
 *
 */
package org.clinicalontology.fhir.tools.ig.common.util;

/**
 * utility to keep track of elapsed time and display values over time
 *
 */
public class ElapsedTime {

	private static final double NANO_SECONDS_PER_HUNDRETH = 1.0e7;
	private static final double NANO_SECONDS_PER_SECOND = (NANO_SECONDS_PER_HUNDRETH
			* 100);
	private static final double NANO_SECONDS_PER_MINUTE = (NANO_SECONDS_PER_SECOND * 60);
	private static final double NANO_SECONDS_PER_HOUR = (NANO_SECONDS_PER_MINUTE * 60);

	private long startTime;

	public ElapsedTime() {
		this.reset();
	}

	public void reset() {
		this.startTime = System.nanoTime();
	}

	public Long value() {
		return System.nanoTime() - this.startTime;
	}

	public Long seconds() {
		return (long) (this.value() / NANO_SECONDS_PER_SECOND);
	}

	public Long minutes() {
		return (long) (this.value() / NANO_SECONDS_PER_MINUTE);
	}

	@Override
	public String toString() {
		return elapsedTimeAsString(this.value());
	}

	private static String elapsedTimeAsString(long elapsedTime) {

		long hours = (long) (elapsedTime / NANO_SECONDS_PER_HOUR);
		long minutes = (long) ((elapsedTime % NANO_SECONDS_PER_HOUR)
				/ NANO_SECONDS_PER_MINUTE);
		long seconds = (long) ((elapsedTime % NANO_SECONDS_PER_MINUTE)
				/ NANO_SECONDS_PER_SECOND);
		long hundreths = (long) ((elapsedTime % NANO_SECONDS_PER_SECOND)
				/ NANO_SECONDS_PER_HUNDRETH);

		return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, hundreths);

	}
}
