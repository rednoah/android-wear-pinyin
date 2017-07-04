package woogle.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import woogle.chart.ChartCell;
import woogle.chart.Path;

public class WoogleCompResult {
    public List<String> pinyin;

    Stack<ChartCell>    selectedCell;

    private int         len;

    WoogleCompResult() {
        pinyin = new ArrayList<String>();
        selectedCell = new Stack<ChartCell>();
        len = 0;
    }

    public void clearAll() {
        pinyin.clear();
        clearResult();
    }

    public void clearResult() {
        selectedCell.clear();
        len = 0;
    }

    public void pushChartCell(ChartCell c) {
        selectedCell.push(c);
        len += Path.getSentenceString(c.getSelectedPath()).length();
    }

    public boolean isFinished() {
        return selectedCell.peek().column + 1 == pinyin.size();
        //return selectedCell.size() == pinyin.size();
    }

    public boolean isEmpty() {
        return selectedCell.isEmpty();
    }

    public ChartCell popChartCell() {
        if (selectedCell.isEmpty()) {
            return null;
        }
        else {
            ChartCell c = selectedCell.pop();
            len -= Path.getSentenceString(c.getSelectedPath()).length();
            return c;
        }
    }

    public String[] getPinyin() {
        return pinyin.toArray(new String[0]);
    }

    public String toCompString() {
        StringBuffer sb = new StringBuffer();
        for (ChartCell c : selectedCell) {
            sb.append(Path.getSentenceString(c.getSelectedPath()));
            sb.append('\'');
        }
        for (int i = len; i < pinyin.size(); i++) {
            sb.append(pinyin.get(i));
            if (i != pinyin.size() - 1)
                sb.append('\'');
        }
        return sb.toString();
    }

    public String toSendText() {
        StringBuffer sb = new StringBuffer();
        for (ChartCell c : selectedCell) {
            sb.append(Path.getSentenceString(c.getSelectedPath()));
        }
        for (int i = len; i < pinyin.size(); i++) {
            sb.append(pinyin.get(i));
        }
        return sb.toString();
    }
}