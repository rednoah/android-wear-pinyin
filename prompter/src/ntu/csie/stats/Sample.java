package ntu.csie.stats;

import static java.util.stream.Collectors.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.apache.commons.text.similarity.LevenshteinResults;

import ntu.csie.prompter.KeyboardLayout;
import ntu.csie.prompter.Record;

public class Sample {

	public static final Function<String, String> CASE_INSENSITIVE = String::toLowerCase;
	public static final Function<String, String> EXACT = Function.identity();

	private Function<String, String> normalization;

	private String inputString;
	private List<Record> records;

	public Sample(Function<String, String> normalization, String inputString, List<Record> records) {
		this.normalization = normalization;
		this.inputString = inputString;
		this.records = records;
	}

	public KeyboardLayout getKeyboard() {
		Set<KeyboardLayout> keyboard = records.stream().map(Record::getKeyboard).distinct().collect(toSet());
		if (keyboard.size() != 1) {
			throw new IllegalStateException("Keyboard: " + keyboard);
		}
		return keyboard.iterator().next();
	}

	public List<Record> getRecords() {
		return records;
	}

	public String getSession() {
		return records.get(0).getSession();
	}

	public String getInputString() {
		return inputString.trim();
	}

	public String getCommitString() {
		return records.stream().filter(Record::isInputKey).reduce((first, second) -> second).map(Record::getBuffer).map(String::trim).orElse("");
	}

	public Duration getDuration() {
		LongSummaryStatistics stats = records.stream().filter(Record::isInputKey).mapToLong(r -> r.timestamp).summaryStatistics();

		if (stats.getCount() == 0) {
			return Duration.ZERO;
		}

		Instant t1 = Instant.ofEpochMilli(stats.getMin());
		Instant t2 = Instant.ofEpochMilli(stats.getMax());
		return Duration.between(t1, t2);
	}

	public double getWPM() {
		double w = getCommitString().length() - 1;
		double t = getDuration().toMillis() / 1000d;

		return (w / t) * 60 * (1d / 5d);
	}

	public double getKSPC() {
		return (double) records.stream().filter(Record::isInputKey).count() / (double) getCommitString().length();
	}

	public CharacterErrorStatistics getCharacterErrorStatistics() {
		String s1 = normalization.apply(getInputString());
		String s2 = normalization.apply(getCommitString());

		LevenshteinResults editDistance = new LevenshteinDetailedDistance().apply(s1, s2);

		double incorrectNotFixed = editDistance.getDistance();
		double correct = s2.length() - editDistance.getSubstituteCount() - editDistance.getDeleteCount();

		// each DELETE will always delete one character
		double fixes = records.stream().filter(Record::isDelete).count();
		double incorrectFixed = fixes;

		return new CharacterErrorStatistics(correct, fixes, incorrectFixed, incorrectNotFixed);
	}

	public boolean isValidSample() {
		return getCommitString().length() > getInputString().length() / 2;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getInputString()).append('\n');
		sb.append(getCommitString()).append('\n');
		sb.append("----------------------------------------").append('\n');
		sb.append(records.stream().map(r -> r.key).collect(toList())).append('\n');
		sb.append(String.format("%s: %.01f WPM %.01f KSPC %s [T: %.01fs]", getKeyboard(), getWPM(), getKSPC(), getCharacterErrorStatistics(), getDuration().toMillis() / 1000d)).append('\n');
		return sb.toString();
	}

}
