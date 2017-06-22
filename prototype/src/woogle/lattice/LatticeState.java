package woogle.lattice;

public class LatticeState {
	int beginIndex;
	int endIndex;
	String word;
	double ngramScore;
	double depScore;
	LatticeState ngramHistory;
	LatticeState depHistory;
	
	public LatticeState(int beginIndex, int endIndex, String word) {
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
		this.word = word;
		ngramScore = Double.NEGATIVE_INFINITY;
		depScore = Double.NEGATIVE_INFINITY;
		ngramHistory = null;
		depHistory = null;
	}
}
