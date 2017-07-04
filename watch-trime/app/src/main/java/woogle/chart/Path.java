package woogle.chart;

import java.util.ArrayList;
import java.util.List;

import woogle.ds.PathNode;

public class Path {
    // public static int getSentenceLength(PathNode path) {
    // int len = 0;
    // for (PathNode p = path; p != null; p = p.ngramHistory) {
    // len++;
    // }
    // return len;
    // }

    /**
     * word1 word2 word3, no space
     */
    public static String getSentenceString(PathNode path) {
        StringBuilder sb = new StringBuilder();
        for (PathNode p = path; p != null; p = p.ngramHistory) {
            sb.insert(0, p.word.getString());
        }
        return sb.toString();
    }

    public static String getDebugString(PathNode path) {
        StringBuilder sb = new StringBuilder();
        for (PathNode p = path; p != null; p = p.ngramHistory) {
            sb.insert(0, p.word + ".");
        }
        sb.append(":" + path.ngramScore + ":" + path.depScore);
        return sb.toString();
    }

    public static List<String> getNGram(PathNode path, int order) {
        List<String> list = new ArrayList<String>(order);
        PathNode p = path;
        for (int i = order; i > 0; i--) {
            if (p == null)
                break;
            list.add(0, p.word.getString());
            p = p.ngramHistory;
        }
        return list;
    }

    public static List<String> getSentenceStrings(PathNode path) {
        List<String> s = new ArrayList<String>();
        for (PathNode p = path; p != null; p = p.ngramHistory) {
            s.add(0, p.word.getString());
        }
        return s;
    }

    public static int getLength(PathNode path) {
        int len = 0;
        for (PathNode p = path; p != null; p = p.ngramHistory) {
            len++;
        }
        return len;
    }

    // public static PathNode getNgramPath(String s[]) {
    // PathNode p = new PathNode(null);
    // PathNode c = p;
    // for (int i = s.length - 1; i >= 0; i--) {
    // c.word = s[i];
    // c.ngramHistory = new PathNode(null);
    // c = c.ngramHistory;
    // }
    // return p;
    // }

    // public List<String> getDepGram(int order) {
    // List<String> list = new ArrayList<String>(5);
    // Path p = this;
    // if (order == -1)
    // order = Integer.MAX_VALUE;
    // for (int i = order; i > 0; i--) {
    // if (p == null || p.word == null)
    // break;
    // list.add(0, p.word);
    // p = p.depHistory;
    // }
    // return list;
    // }
}