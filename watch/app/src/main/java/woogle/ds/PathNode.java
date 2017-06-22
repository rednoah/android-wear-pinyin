package woogle.ds;

import woogle.chart.Path;
import woogle.util.Utils;

public class PathNode {

    public Word     word;

    public PathNode ngramHistory;

    public PathNode depHistory;

    public double   ngramScore;

    public double   depScore;

    public PathNode(Word word) {
        this.word = word;
        this.ngramHistory = null;
        this.depHistory = null;
        this.ngramScore = 0;
        this.depScore = 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof PathNode))
            return false;
        if (obj == this)
            return true;
        PathNode p = (PathNode) obj;
        // String pStr = Path.getSentenceString(p);
        // String tStr = Path.getSentenceString(this);
        // return tStr.equals(pStr);

        boolean isEqual = Utils.equal(word.getString(), p.word.getString());
        if (isEqual) {
            isEqual = Utils.equal(ngramHistory, p.ngramHistory);
            // if (!isEqual) {
            // String pStr = Path.getSentenceString(p);
            // String tStr = Path.getSentenceString(this);
            // return tStr.equals(pStr);
            // }
            return isEqual;
        }
        else {
            return isEqual;
        }
    }

    @Override
    public String toString() {
        return Path.getDebugString(this);
    }
}
