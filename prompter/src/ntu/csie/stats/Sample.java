package ntu.csie.stats;

import static java.util.stream.Collectors.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Set;

import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.apache.commons.text.similarity.LevenshteinResults;

import ntu.csie.prompter.KeyboardLayout;
import ntu.csie.prompter.Record;

public class Sample {

	private String inputString;
	private List<Record> records;

	public Sample(String inputString, List<Record> records) {
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

	/**
	 * Chinese Characters Per Minute
	 */
	public double getCCPM() {
		double c = getCommitString().length();
		double t = getDuration().toMillis() / 1000d;

		return (c / t) * 60d;
	}

	/**
	 * Keystrokes per Chinese Character
	 */
	public double getKSPCC() {
		return (double) records.stream().filter(Record::isInputKey).count() / (double) getCommitString().length();
	}

	public CharacterErrorStatistics getCharacterErrorStatistics() {
		String s1 = getInputString();
		String s2 = getCommitString();

		LevenshteinResults editDistance = new LevenshteinDetailedDistance().apply(s1, s2);

		double incorrectNotFixed = editDistance.getDistance();
		double correct = getPinyinInputCount() + getHanziSelectionCount();

		// each DELETE may delete a Chinese character, but may also just delete the last Latin letter in the composition buffer
		double fixes = getDeleteCount();
		double incorrectFixed = fixes;

		return new CharacterErrorStatistics(correct, fixes, incorrectFixed, incorrectNotFixed);
	}

	public long getPinyinInputCount() {
		return records.stream().filter(Record::isPinyinPart).count();
	}

	public long getHanziSelectionCount() {
		return records.stream().filter(Record::isHanziSelection).count();
	}

	public long getDeleteCount() {
		return records.stream().filter(Record::isDelete).count();
	}

	public boolean isValidSample() {
		return getCommitString().length() > getInputString().length() / 2;
	}

	public double getCharacterSelectionDurationPerCharacter() {
		double d = 0;

		for (int i = 0; i < records.size(); i++) {
			if (i > 0 && records.get(i).isHanziSelection()) {
				d += records.get(i).getTimestamp() - records.get(i - 1).getTimestamp();
			}
		}

		return d / getInputString().length();
	}

	public double getCharacterSelectionCountPerCharacter() {
		double d = 0;

		for (int i = 0; i < records.size(); i++) {
			if (records.get(i).isHanziSelection()) {
				d += 1;
			}
		}

		return d / getInputString().length();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getInputString()).append('\n');
		sb.append(getCommitString()).append('\n');
		sb.append("----------------------------------------").append('\n');
		sb.append(records.stream().map(r -> r.key).collect(toList())).append('\n');
		sb.append(String.format("%s: %.01f CCPM %.01f KSPCC %s [T: %.01fs]", getKeyboard(), getCCPM(), getKSPCC(), getCharacterErrorStatistics(), getDuration().toMillis() / 1000d)).append('\n');
		return sb.toString();
	}

}
