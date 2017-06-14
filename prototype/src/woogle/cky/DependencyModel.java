package woogle.cky;

import java.util.HashMap;

@SuppressWarnings("serial")
public class DependencyModel extends HashMap<String, Integer>{
	
	public DependencyModel() {
		super();
	}
	
	public int find(String w1, String w2) {
		String key = w1 + "-" + w2;
		Integer i = super.get(key);
//		if (i != null) {
//			System.out.println(key + ":" + i);
//		}
		return i==null ? 0 : i;
	}
	
	// 假设Path的历史已经有了得分。
	public int computeSentenceScore(Path path) {
		String curWord = path.word;
		String preWord = null;
		int count = 0;
		for(Path p=path.history; p!=null; p=p.history) {
			preWord = p.word;
			count += this.find(preWord, curWord);
		}
		path.depCount = count;
		return count;
	}
}
