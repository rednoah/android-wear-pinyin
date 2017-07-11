package ntu.csie.stats;

import static java.nio.charset.StandardCharsets.*;
import static java.util.stream.Collectors.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import ntu.csie.prompter.KeyboardLayout;
import ntu.csie.prompter.Record;
import one.util.streamex.StreamEx;

public class Stats {

	enum Study {

		PILOT;

		public List<String> getPhraseSet() throws IOException {
			return Files.readAllLines(Paths.get(name().toLowerCase() + "-study-phrase-set.txt"), UTF_8);
		}

		public List<Record> getStudyRecord() throws IOException {
			return Files.lines(Paths.get(name().toLowerCase() + "-study-record.tsv"), UTF_8).map(Record::parse).collect(toList());
		}

		public int getPhraseLimit() {
			return 25;
		}

		public List<Sample> getSamples() throws Exception {
			List<String> phraseSet = getPhraseSet();
			int phraseLimit = getPhraseLimit();

			// ignore training phase and STX/ETX markers
			StreamEx<Record> stream = StreamEx.of(getStudyRecord()).filter(r -> r.phrase > 0 && r.phrase <= phraseLimit);

			return stream.groupRuns((r1, r2) -> r1.session.equals(r2.session)).flatMap(sessionRecords -> {
				Iterator<String> phrase = phraseSet.iterator();

				return StreamEx.of(sessionRecords).groupRuns((r1, r2) -> r1.phrase == r2.phrase).map(phraseRecords -> {
					Sample sample = new Sample(phrase.next(), phraseRecords);
					return sample;
				});
			}).toList();
		}
	}

	public void print(List<Sample> samples) throws Exception {
		for (Sample s : samples) {
			System.out.println(s);
		}
	}

	public void statsForEachSample(List<Sample> samples) {
		samples.stream().map(Sample::getKeyboard).distinct().sorted().forEach(k -> {
			List<Sample> ks = samples.stream().filter(Sample::isValidSample).filter(s -> k == s.getKeyboard()).collect(toList());

			for (Sample s : ks) {
				CharacterErrorStatistics err = s.getCharacterErrorStatistics();

				String line = StreamEx.of(s.getSession(), s.getKeyboard(), s.getHanziPerMinute(), s.getKSPC(), err.getTotalErrorRate(), err.getCorrectedErrorRate(), err.getNotCorrectedErrorRate(), s.getCommitString()).joining("\t");
				System.out.println(line);
			}
		});
	}

	public void statsForEachBlock(List<Sample> samples, int blocks, int blockSize) {
		for (KeyboardLayout k : getKeyboards(samples)) {
			List<List<Sample>> samplesByUser = StreamEx.of(samples).filter(Sample::isValidSample).filter(s -> k == s.getKeyboard()).groupRuns((s1, s2) -> s1.getSession().equals(s2.getSession())).toList();

			for (int i = 0; i < blocks; i++) {
				List<Sample> block = new ArrayList<Sample>();
				for (List<Sample> user : samplesByUser) {
					user.stream().skip(i * blockSize).limit(blockSize).forEach(block::add);
				}

				DoubleStatistics wpm = stats(block, Sample::getHanziPerMinute);
				DoubleStatistics kspc = stats(block, Sample::getKSPC);
				DoubleStatistics ter = stats(block, s -> s.getCharacterErrorStatistics().getTotalErrorRate() * 100);

				String line = StreamEx.of(k + " Block " + (i + 1), wpm, kspc, ter).joining("\t");
				System.out.println(line);
			}
		}
	}

	public void statsForEachKeyboard(List<Sample> samples) {
		for (KeyboardLayout k : getKeyboards(samples)) {
			List<Sample> samplesByKeyboard = StreamEx.of(samples).filter(Sample::isValidSample).filter(s -> k == s.getKeyboard()).toList();

			DoubleStatistics wpm = stats(samplesByKeyboard, Sample::getHanziPerMinute);
			DoubleStatistics kspc = stats(samplesByKeyboard, Sample::getKSPC);
			DoubleStatistics ter = stats(samplesByKeyboard, s -> s.getCharacterErrorStatistics().getTotalErrorRate() * 100);

			String line = StreamEx.of(k, wpm, kspc, ter).joining("\t");
			System.out.println(line);
		}
	}

	public void statsForEachUser(List<Sample> samples) {
		List<List<Sample>> samplesByUser = StreamEx.of(samples).filter(Sample::isValidSample).groupRuns((s1, s2) -> s1.getSession().equals(s2.getSession())).toList();

		int i = 1;
		for (List<Sample> userSamples : samplesByUser) {
			System.out.format("### User %d (%s)%n", i++, userSamples.get(0).getSession());
			statsForEachKeyboard(userSamples);
		}
	}

	public DoubleStatistics stats(List<Sample> samples, Function<Sample, Double> metric) {
		return samples.stream().mapToDouble(s -> metric.apply(s)).collect(DoubleStatistics::new, DoubleStatistics::accept, DoubleStatistics::combine);
	}

	public void wpmForEachUser(List<Sample> samples) {
		StreamEx.of(samples).groupRuns((s1, s2) -> s1.getSession().equals(s2.getSession())).forEach(userSamples -> {
			statsForEachSample(userSamples);
		});
	}

	public void statsTotal(List<Sample> samples) {
		List<Sample> total = StreamEx.of(samples).filter(Sample::isValidSample).toList();

		System.out.format("Total Count: %d samples%n", total.size());
		System.out.format("Total Time: %.01f hours%n", total.stream().map(Sample::getDuration).reduce(Duration.ZERO, Duration::plus).toMinutes() / 60d);
	}

	public KeyboardLayout[] getKeyboards(List<Sample> samples) {
		return samples.stream().map(Sample::getKeyboard).distinct().sorted().toArray(KeyboardLayout[]::new);
	}

	public static void main(String[] args) throws Exception {
		Stats stats = new Stats();

		for (Study s : Study.values()) {
			System.out.println("# " + s);
			System.out.println();

			List<Sample> samples = s.getSamples();

			// System.out.println("## Stats for each Sentence");
			// stats.print(samples);
			// System.out.println();

			// System.out.println("## Stats for each User");
			// stats.statsForEachUser(samples);
			// System.out.println();

			// System.out.println("## Stats for each Keyboard");
			// stats.statsForEachKeyboard(samples);
			// System.out.println();

			// System.out.println("## Stats for each Block");
			// stats.statsForEachBlock(samples, s.getPhraseLimit() / 5, 5);
			// System.out.println();

			System.out.println("## Total");
			stats.statsTotal(samples);
			System.out.println();
		}

	}

}
