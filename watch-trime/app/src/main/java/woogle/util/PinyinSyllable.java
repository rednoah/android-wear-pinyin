package woogle.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import woogle.ds.SyllableDictory;
import woogle.ds.reader.SyllableDictoryReader;

public class PinyinSyllable {

	private final static String[] INITIAL_ARRAY = { "b", "c", "ch", "f", "g",
			"h", "j", "k", "l", "m", "p", "q", "r", "s", "sh", "t", "w", "x",
			"y", "z", "zh" };

	private final static String[] FINAL_ARRAY = { "a", "o", "e", "ai", "ei",
			"ao", "ou", "an", "en", "ang", "eng", "ong", "ia", "ie", "iao",
			"iou", "iu", "ian", "in", "iang", "ing", "iong", "ua", "uo", "uai",
			"uei", "ui", "uan", "uen", "un", "uang", "ueng", "ung", "ve",
			"van", "vn" };

	public Set<String> initial;
	public Set<String> finalll;
	public SyllableDictory syllable;

	public PinyinSyllable() {
		initial = new HashSet<String>();
		finalll = new HashSet<String>();
		syllable = new SyllableDictory();

		arrays2set(INITIAL_ARRAY, initial);
		arrays2set(FINAL_ARRAY, finalll);
		SyllableDictoryReader.load(syllable);
	}

	private <T> void arrays2set(T s[], Set<T> set) {
		for (T i : s) {
			set.add(i);
		}
	}

	public boolean isInitial(String initial) {
		return initial.contains(initial);
	}

	public boolean isPartialSyllable(String prefix) {
		return syllable.partialContains(prefix);
	}

	public boolean isSyllbale(String s) {
		return syllable.contains(s);
	}

	private void error(List<String> list, char py[], int start, int end) {
		while (start != end) {
			list.add(Character.toString(py[start]));
			start++;
		}
	}

	public List<String> split(String pinyin) {
		String ps[] = pinyin.split("\'");
		List<String> syllables = new ArrayList<String>();
		for (String p : ps) {
			syllables.addAll(split2(p));
		}
		return syllables;
	}

	public List<String> split2(String pinyin) {
		List<String> list = new ArrayList<String>();
		char py[] = pinyin.toCharArray();
		int start = 0;
		for (int i = 0; i < py.length; i++) {
			char c = py[i];
			// 2
			if ((c == 'i' || c == 'u' || c == 'v') && (i - start) == 0) {
				list.add(pinyin.substring(start, i + 1));
				start = i + 1;
			}
			// 3
			else if (isPartialSyllable(pinyin.substring(start, i + 1))) {
			}
			// 4
			else if (isInitial(Character.toString(c))) {
				// 5
				if (isSyllbale(pinyin.substring(start, i))) {
					list.add(pinyin.substring(start, i));
				} else { // 12
					error(list, py, start, i);
				}
				if (c == '\'')
					i++;
				start = i;
			}
			// 6
			else if (i >= 1
					&& (py[i - 1] == 'g' || py[i - 1] == 'n' || py[i - 1] == 'r')) {
				// 7
				if (isSyllbale(pinyin.substring(start, i - 1))) {
					// 8
					if (isPartialSyllable(pinyin.substring(i - 1, i + 1))) {
						list.add(pinyin.substring(start, i - 1));
						start = i - 1;
					} else if (isSyllbale(pinyin.substring(start, i))) {
						list.add(pinyin.substring(start, i));
						start = i;
					} else { // 12
						error(list, py, start, i);
						start = i;
					}
				}
				// 10
				else if (isSyllbale(pinyin.substring(start, i))) {
					list.add(pinyin.substring(start, i));
					start = i;
				} else { // 12
					error(list, py, start, i);
					start = i;
				}
			}
			// 11
			else if (isSyllbale(pinyin.substring(start, i))) {
				list.add(pinyin.substring(start, i));
				start = i;
			} else {
				// 12
				error(list, py, start, i);
				start = i;
			}
		}
		list.add(pinyin.substring(start));
		return list;
	}

	public static void main(String args[]) {
		PinyinSyllable pinyinSyllable = new PinyinSyllable();

		String pinyin = "chih";
		List<String> pinyins = pinyinSyllable.split(pinyin);
		System.out.println(pinyin);
		print(pinyins);

//		pinyin = "anhuidaxueshiwodemuxiao";
//		pinyins = pinyinSyllable.split(pinyin);
//		System.out.println(pinyin);
//		print(pinyins);
//
//		pinyin = "anhuidaxue";
//		pinyins = pinyinSyllable.split(pinyin);
//		System.out.println(pinyin);
//		print(pinyins);
//
//		pinyin = "woainizhonghuarenmingongheguo";
//		pinyins = pinyinSyllable.split(pinyin);
//		System.out.println(pinyin);
//		print(pinyins);
//
//		pinyin = "bengabengbeagabe'ga'";
//		pinyins = pinyinSyllable.split(pinyin);
//		System.out.println(pinyin);
//		print(pinyins);
//
//		pinyin = "xianshiminganxi'anshiming'an";
//		pinyins = pinyinSyllable.split(pinyin);
//		System.out.println(pinyin);
//		print(pinyins);
	}

	private static void print(List<String> ss) {
		for (String s : ss) {
			System.out.print(s + " ");
		}
		System.out.println('\n');
	}
}
