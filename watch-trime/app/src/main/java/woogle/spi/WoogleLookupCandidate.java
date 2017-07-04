package woogle.spi;

import woogle.chart.ChartCell;
import woogle.chart.Path;

public class WoogleLookupCandidate {

    public ChartCell c;

    public int       pathIndex; // index in c

    public WoogleLookupCandidate(int pathIndex, ChartCell c) {
        this.pathIndex = pathIndex;
        this.c = c;
    }

    public String getPathWords() {
        if (c != null) {
            return Path.getSentenceString(c.getPath(pathIndex));
        }
        else {
            return null;
        }
    }
}