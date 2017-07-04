package woogle.ds;

import java.io.Serializable;
import java.util.ArrayList;

import woogle.ds.reader.SyllableDictoryReader;

@SuppressWarnings("serial")
public class SyllableDictory extends ArrayList<String> implements Serializable {

    public SyllableDictory() {
        super();
    }


    public Pair<Integer, Integer> range(String prefix) {
        int start = begin(prefix, 0);
        int end = -1;
        if (start != -1)
            end = end(prefix, start);
        return new Pair<Integer, Integer>(start, end);
    }

    public int end(String prefix, int beginIndex) {
        for (beginIndex++; beginIndex < size(); beginIndex++) {
            if (!get(beginIndex).startsWith(prefix)) {
                break;
            }
        }
        return beginIndex;
    }

    public int begin(String prefix, int beginIndex) {
        for (int i = beginIndex; i < size(); i++) {
            if (get(i).startsWith(prefix)) {
                return i;
            }
        }
        return -1;
    }

    public boolean partialContains(String prefix) {
        return begin(prefix, 0) != -1;
    }


}
