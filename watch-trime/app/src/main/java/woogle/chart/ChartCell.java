package woogle.chart;

import java.util.ArrayList;
import java.util.List;
import woogle.ds.PathNode;

public class ChartCell {

    public int              row;

    public int              column;

    public List<PathNode>   sentences;

    public String           pinyin;

    public int              selectedIndex;

    public static final int NOT_SEL = -1;

    public ChartCell(int row, int column) {
        this.row = row;
        this.column = column;
        this.sentences = new ArrayList<PathNode>();
        this.pinyin = new String();
        this.selectedIndex = NOT_SEL;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(row);
        sb.append(',');
        sb.append(column);
        sb.append("]\n");
        for(PathNode node: sentences) {
            sb.append(node + "\n");
        }
        return sb.toString();
    }

    public boolean isSelected() {
        return selectedIndex != NOT_SEL;
    }
    
//    public ChartCell clone() {
//        ChartCell c = new ChartCell(row, column);
//        c.sentences.addAll(sentences);
//        c.selectedIndex = selectedIndex;
//        c.pinyin = pinyin;
//        return c;
//    }

    public PathNode getSelectedPath() {
        return getPath(selectedIndex);
    }

    public PathNode getPath(int index) {
        return sentences.get(index);
    }
}