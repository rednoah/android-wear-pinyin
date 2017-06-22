package woogle.chart;

import java.util.Comparator;

import woogle.ds.PathNode;

public class PathNodeComparator implements Comparator<PathNode> {

    public static float LAMDA = 1.0f;

    @Override
    public int compare(PathNode o1, PathNode o2) {
        double score0 = o1.ngramScore;// Utils.linearInter(o1.ngramScore,
                                      // o1.depScore, LAMDA);
        double score1 = o2.ngramScore;// Utils.linearInter(o2.ngramScore,
                                      // o2.depScore, LAMDA);
        if (score0 > score1) {
            return -1;
        }
        else if (score0 < score1) {
            return 1;
        }
        else {
            int len0 = Path.getSentenceString(o1).length();
            int len1 = Path.getSentenceString(o2).length();
            return len0 - len1;
        }
    }
}
