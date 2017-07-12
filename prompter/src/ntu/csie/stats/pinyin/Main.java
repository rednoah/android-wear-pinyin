package ntu.csie.stats.pinyin;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

import ntu.csie.stats.DoubleStatistics;

public class Main {

	public static void main(String[] args) {
		System.out.println("Initials Total: " + Initial.values().length);
		System.out.println("Finals Total:   " + Final.values().length);

		DoubleStatistics finalStats = stats(Initial.values(), i -> (double) i.getFinals().length);
		System.out.println("Finals Bounds: " + finalStats.getMin() + " / " + finalStats.getMax());
		System.out.println("Finals Stats:  " + finalStats);

		List<String> finalLetters = stream(Initial.values()).flatMapToInt(i -> stream(i.getFinals()).map(Object::toString).flatMapToInt(String::chars)).distinct().mapToObj(i -> Character.toString((char) i)).collect(toList());
		System.out.println("Finals Letters: " + finalLetters.size() + " -> " + finalLetters);

		List<String> partialPinyinStates = Syllables.PINYIN_SET.stream().flatMap(pinyin -> {
			return IntStream.range(1, pinyin.length()).mapToObj(i -> pinyin.substring(0, i));
		}).filter(s -> s.length() > 0).distinct().collect(toList());

		System.out.println("Pinyin Syllables: " + Syllables.PINYIN_SET.size());
		System.out.println("Partial Pinyin: " + partialPinyinStates.size() + " -> " + partialPinyinStates);
		System.out.println("Partial Pinyin Distinct Next Letter Set: " + partialPinyinStates.stream().map(s -> getNextLetterOptions(s)).distinct().count());

		DoubleStatistics partialPinyinStats = stats(partialPinyinStates, s -> (double) getNextLetterOptions(s).size());
		System.out.println("Partial Pinyin Distinct Next Letter Set Bounds: " + partialPinyinStats.getMin() + " / " + partialPinyinStats.getMax());
		System.out.println("Partial Pinyin Distinct Next Letter Set Stats:  " + partialPinyinStats);
	}

	public static <T> DoubleStatistics stats(T[] values, Function<T, Double> metric) {
		return stream(values).mapToDouble(s -> metric.apply(s)).collect(DoubleStatistics::new, DoubleStatistics::accept, DoubleStatistics::combine);
	}

	public static <T> DoubleStatistics stats(Collection<T> values, Function<T, Double> metric) {
		return values.stream().mapToDouble(s -> metric.apply(s)).collect(DoubleStatistics::new, DoubleStatistics::accept, DoubleStatistics::combine);
	}

	private static final char OMEGA = 'z' + 1;

	public static Set<String> getNextLetterOptions(String head) {
		Set<String> keys = Syllables.PINYIN_SET.subSet(head, head + OMEGA).stream().filter(s -> s.length() > head.length()).map(s -> s.substring(head.length(), head.length() + 1)).distinct().collect(toSet());

		if (Syllables.PINYIN_SET.contains(head)) {
			keys.add("'");
		}

		return keys;
	}

}
