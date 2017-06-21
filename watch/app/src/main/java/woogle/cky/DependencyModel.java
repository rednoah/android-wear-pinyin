package woogle.cky;

import java.util.NavigableMap;

import org.mapdb.DB;

@SuppressWarnings("serial")
public class DependencyModel {

	private NavigableMap<String, Integer> set;

	public DependencyModel(DB db) {
		set = db.createTreeMap("DependencyModel").makeOrGet();
	}

	public Integer get(String key) {
		return set.get(key);
	}

	public void put(String w, Integer i) {
		set.put(w, i);
	}

	public int find(String w1, String w2) {
		String key = w1 + "-" + w2;
		Integer i = get(key);
		// if (i != null) {
		// System.out.println(key + ":" + i);
		// }
		return i == null ? 0 : i;
	}


	public int computeSentenceScore(Path path) {
		String curWord = path.word;
		String preWord = null;
		int count = 0;
		for (Path p = path.history; p != null; p = p.history) {
			preWord = p.word;
			count += this.find(preWord, curWord);
		}
		path.depCount = count;
		return count;
	}

}
