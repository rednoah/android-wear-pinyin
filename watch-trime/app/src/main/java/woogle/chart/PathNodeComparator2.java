package woogle.chart;

import java.util.Comparator;
import woogle.ds.PathNode;
import woogle.util.Utils;

public class PathNodeComparator2 implements Comparator<PathNode> {

    public static float LAMDA = 0.93f;

    @Override
    public int compare(PathNode o1, PathNode o2) {
        double score0 = Utils.linearInter(o1.ngramScore, o1.depScore, LAMDA);
        double score1 = Utils.linearInter(o2.ngramScore, o2.depScore, LAMDA);
        if (score0 > score1) {
            return -1;
        }
        else if (score0 < score1) {
            return 1;
        }
        else {
            int len0 = Path.getLength(o1);
            int len1 = Path.getLength(o2);
            return len0 - len1;
        }
    }
}
