package woogle.cky;

import java.io.Serializable;

public class XGram implements Comparable<XGram>, Serializable {

	private static final long serialVersionUID = 1;

	public String w1;
	public String w2;
	public String w3;
	public float prob; // log10
	public float backoff; // log10

	public XGram() {
		this(null, null, null);
	}

	public XGram(String w1, String w2, String w3) {
		this.w1 = w1;
		this.w2 = w2;
		this.w3 = w3;
		this.prob = 0;
		this.backoff = 0;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(w1 + '\t');
		sb.append(w2 + '\t');
		sb.append(w3);
		return sb.toString();
	}

	public String toARPAString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.prob + '\t');
		sb.append(w1 + '\t');
		sb.append(w2 + '\t');
		sb.append(w3);
		sb.append(this.backoff);
		return sb.toString();
	}

	// p( </s> | ¡£ ...) = [2gram] 0.973809 [ -0.0115263 ]
	public String toProbFormString() {
		StringBuffer sb = new StringBuffer();
		sb.append("log10 p( ");
		sb.append(this.w3);
		if (this.w2 != null) {
			sb.append(" | ");
			sb.append(w2 + ',');
			sb.append(w1);
		}
		sb.append(" ) = ");
		sb.append(this.prob);
		sb.append(" backoff = " + this.backoff);
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof XGram))
			return false;
		if (obj == this)
			return true;
		XGram ng = (XGram) obj;
		boolean isEqual = Utils.equal(w3, ng.w3);
		if (isEqual) {
			isEqual = Utils.equal(w2, ng.w2);
			return isEqual ? Utils.equal(w1, ng.w1) : isEqual;
		} else
			return isEqual;
	}

	@Override
	public int compareTo(XGram ng) {
		int i = Utils.compare(w3, ng.w3);
		if (i == 0) {
			i = Utils.compare(w2, ng.w2);
			return (i == 0) ? Utils.compare(w1, ng.w1) : i;
		} else
			return i;
	}

}