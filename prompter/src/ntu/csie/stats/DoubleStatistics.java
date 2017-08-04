package ntu.csie.stats;

import java.util.DoubleSummaryStatistics;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class DoubleStatistics extends DoubleSummaryStatistics {

	public final SummaryStatistics stats = new SummaryStatistics();

	@Override
	public void accept(double value) {
		super.accept(value);
		stats.addValue(value);
	}

	public DoubleStatistics combine(DoubleStatistics other) {
		throw new UnsupportedOperationException();
	}

	public double getMean() {
		return stats.getMean();
	}

	public double getStandardDeviation() {
		return stats.getStandardDeviation();
	}

	@Override
	public String toString() {
		return String.format("%.02f (%.02f)", getMean(), getStandardDeviation());
	}

}
