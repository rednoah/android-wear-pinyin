package woogle.cky;

public class Path {
	
	public String word;
	public Path history;
	public float score;
	public int depCount;
	
	public Path() {
		this(null, null, 0, 0);
	}
	
	public Path(String word, Path history, float score, int depCount) {
		this.word = word;
		this.history = history;
		this.score = score;
		this.depCount = depCount;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Path))
			return false;
		if (obj == this)
			return true;
		Path p = (Path)obj;
		boolean isEqual = Utils.equal(word, p.word);
		if (isEqual)
			return Utils.equal(history, p.history);
		else
			return isEqual;
	}
	
	public int getWordsLength() {
		int i = 1;
		Path p = this.history;
		while (p != null) {
			i ++;
			p = p.history;
		}
		return i;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.insert(0, word);
		for(Path p = this.history; p != null; p = p.history) {
			sb.insert(0, p.word + ".");
		}
		sb.append(":" + score);
		return sb.toString();
	}
	
	public String toStringWords() {
		StringBuffer sb = new StringBuffer();
		sb.insert(0, word);
		for(Path p = this.history; p != null; p = p.history) {
			sb.insert(0, p.word);
		}
		return sb.toString();
	}
}
