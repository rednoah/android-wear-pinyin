package woogle.lattice;

import java.util.ArrayList;
import java.util.List;


public class LatticePath {
	public static List<String> getSentence(LatticeState state) {
		List<String> sentence = new ArrayList<String>();
		for (LatticeState s = state; s != null; s = s.ngramHistory)
			sentence.add(0, s.word);
		return sentence;
	}

	public static List<String> getNgram(LatticeState state, int n) {
		List<String> sentence = new ArrayList<String>();
		int i = 0;
		for (LatticeState s = state; s != null && i < n; s = s.ngramHistory) {
			sentence.add(0, s.word);
			i++;
		}
		return sentence;
	}
}
