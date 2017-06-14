package woogle.cky;

import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("serial")
public class LanguageModel extends TreeSet<XGram>{
	
	public LanguageModel() {
		super();
	}

	public XGram find(String w1, String w2, String w3) {
//		System.out.println("find: " + w1 + " " + w2 + " " + w3);
		XGram gram = new XGram(w1, w2, w3);
		SortedSet<XGram> s = super.tailSet(gram);
		if (!s.isEmpty()) {
			XGram g = s.first();
			if (g.equals(gram))
				return g;
		}
		return null;
	}
	
	public float queryBackoff(String w1, String w2, String w3){
		XGram gram = this.find(w1, w2, w3);
		return (gram != null) ? gram.backoff : 0;		
	}
	
	// p(w1, w2, w3) = 
	// 1. p(w1, w2, w3)
	// 2. b(w1, w2) + p(w2, w3)
	//
	// p(w2, w3) = 
	// 1. p(w2, w3)
	// 2. b(w2) + p(w3)
	public float queryProb(String w1, String w2, String w3){
		if (w1 == null && w2 == null && w3 == null)
			return Float.NEGATIVE_INFINITY;
		XGram gram = this.find(w1, w2, w3);
		if (gram != null) {
			return gram.prob;
		}
		else {
			float backoff = this.queryBackoff(null, w1, w2);
			if (w2 == null)
				w3 = null;
			if (w1 == null)
				w2 = null;
			float prob = this.queryProb(null, w2, w3);
			return prob + backoff;
		}
	}

	/**
	 * 只计算三元
	 * @param path
	 * @return
	 */
	public float computeSentenceScore(Path path) {

		String w1 = null;
		String w2 = null;
		String w3 = null;
		
		w3 = path.word;
		if (path.history != null) {
			w2 = path.history.word;
			if (path.history.history != null)
				w1 = path.history.history.word;
		}
		float score = this.queryProb(w1, w2, w3);
		if (path.history != null)
			path.score = score + path.history.score;
		else
			path.score = score;
		return path.score;
	}
}