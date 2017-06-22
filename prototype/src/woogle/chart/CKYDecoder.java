package woogle.chart;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import woogle.chart.Chart;
import woogle.chart.ChartCell;
import woogle.ds.PathNode;
import woogle.ds.Word;
import woogle.ds.WordType;
import woogle.util.Score;
import woogle.util.WoogleDatabase;

public class CKYDecoder {

    Score     score;

    ChartCell result[];

    public CKYDecoder(Score score) {
        this.score = score;
    }

    public void clear(Chart chart) {
        for (int i = 0; i < result.length; i++) {
            result[i] = null;
        }
        chart.clear();
        // for (int i = 0; i < chart.num; i++) {
        // for (int j = i; j >= 0; j--) {
        // chart.table[i][j] = null;
        // }
        // }
    }

    public void buildChart(Chart chart, String[] pinyin, int beginIndex) {
        for (int j = beginIndex; j < pinyin.length; j++) {
            if (chart.table[j][j] == null) {
                chart.table[j][j] = new ChartCell(j, j);
            }
            else if (chart.table[j][j].pinyin.equals(pinyin[j])) {
                continue;
            }
            chart.table[j][j].pinyin = pinyin[j];
            buildChartCellPath(chart.table[j][j]);
            // add new word
            for (int i = j - 1; i >= 0; i--) {
                chart.table[i][j] = new ChartCell(i, j);
                chart.table[i][j].pinyin = getPinyin(chart, i, j);
                buildChartCellPath(chart.table[i][j]);
            }
        }
        // chart.table[pinyin.length][pinyin.length] = new ChartCell(
        // pinyin.length, pinyin.length);
        // chart.table[pinyin.length][pinyin.length].pinyin = "";
        // PathNode pathNode = new PathNode(Word.END);
        // pathNode.ngramScore = computeNgram(pathNode, 1);
        // chart.table[pinyin.length][pinyin.length].sentences.add(pathNode);
        // for (int i = pinyin.length - 1; i >= 0; i--) {
        // chart.table[i][pinyin.length] = new ChartCell(i, pinyin.length);
        // chart.table[i][pinyin.length].pinyin = getPinyin(chart, i,
        // pinyin.length);
        // }

        chart.num = pinyin.length;
        result = new ChartCell[chart.num + 1];
    }

    public void CKYParse(Chart chart, int beginIndex) {
        for (int j = beginIndex; j < chart.num; j++) {
            result[j] = new ChartCell(0, j);
            clone(result[j], chart.table[0][j]);
            // if (result[j].sentences.size() > 10)
            // result[j].sentences = result[j].sentences.subList(0, 10);
            // if (j == 8)
            // System.out.println();
            for (int k = 0; k < j; k++) {
                union(result[j], result[k], chart.table[k + 1][j]);
            }
            rerankChartCellPath(result[j]);
            if (result[j].sentences.size() > 10)
                result[j].sentences = result[j].sentences.subList(0, 10);
        }
    }

    protected void clone(ChartCell dst, ChartCell src) {
        dst.selectedIndex = src.selectedIndex;
        dst.pinyin = src.pinyin;
        for (PathNode p : src.sentences) {
            if (p.word.isWord() || p.word.isWordChar())
                dst.sentences.add(p);
        }
        if (dst.sentences.isEmpty())
            dst.sentences.addAll(src.sentences);
        if (dst.sentences.size() > 10)
            dst.sentences = dst.sentences.subList(0, 10);
    }

    public void setChartCellSelected(Chart chart, ChartCell cell) {
        for (int j = cell.column + 1; j < chart.num; j++) {
            if (cell.isSelected()) {
                chart.table[cell.row][j].sentences.clear();
                chart.table[cell.row][j].selectedIndex = ChartCell.NOT_SEL;
            }
            else {
                buildChartCellPath(chart.table[cell.row][j]);
            }
        }
    }

    protected String getPinyin(Chart chart, int beginIndex, int endIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = beginIndex; i <= endIndex; i++) {
            sb.append(chart.table[i][i].pinyin);
        }
        return sb.toString();
    }

    protected void union(ChartCell cell, ChartCell cell1, ChartCell cell2) {
        if (cell1 == null || cell2 == null || cell == null || cell.row != 0)
            return;
        List<PathNode> pathList1 = new ArrayList<PathNode>();
        if (cell1.isSelected())
            pathList1.add(cell1.getSelectedPath());
        else {
            // if (cell1.column == cell1.row) {
            for (PathNode p : cell1.sentences)
                if (p.word.isWord() || p.word.isWordChar())
                    pathList1.add(p);
            // }
            // else {
            // for (PathNode p : cell1.sentences)
            // // 修改！！！！不考虑拼音合并后的单字
            // if (p.word.isWord())
            // pathList1.add(p);
            // }
            if (pathList1.isEmpty())
                pathList1 = cell1.sentences;
            if (pathList1.size() > 10)
                pathList1 = pathList1.subList(0, 10);
        }
        // else if (cell1.sentences.size() > 10)
        // pathList1 = cell1.sentences.subList(0, 10);
        // else
        // pathList1 = cell1.sentences;

        List<PathNode> pathList2 = new ArrayList<PathNode>();
        if (cell2.isSelected())
            pathList2.add(cell2.getSelectedPath());
        else {
            if (cell2.column == cell2.row) {
                for (PathNode p : cell2.sentences)
                    if (p.word.isWord() || p.word.isWordChar())
                        pathList2.add(p);
            }
            else {
                for (PathNode p : cell2.sentences)
                    // 修改！！！！不考虑拼音合并后的单字
                    if (p.word.isWord())
                        pathList2.add(p);
            }
            if (pathList2.isEmpty())
                pathList2 = cell2.sentences;
            if (pathList2.size() > 10)
                pathList2 = pathList2.subList(0, 10);
        }

        for (PathNode p1 : pathList1) {
            // p2 has only word
            for (PathNode p2 : pathList2) {
                // p = p1 p2
                PathNode p = new PathNode(p2.word);
                p.ngramHistory = p1;
                if (!cell.sentences.contains(p)) {
                    p.ngramScore = p1.ngramScore + computeNgram(p, 3);
                    cell.sentences.add(p);
                }
            }
        }
        // rerankChartCellPath(cell);
        // if (cell.sentences.size() > 10)
        // cell.sentences = cell.sentences.subList(0, 10);
    }

    protected void buildChartCellPath(ChartCell cell) {
        cell.sentences.clear();
        cell.selectedIndex = ChartCell.NOT_SEL;
        String pinyin = cell.pinyin;
        List<Word> cand = WoogleDatabase.findCandidates(pinyin);
        for (Word w : cand) {
            PathNode pathNode = new PathNode(w);
            addChartCellPath(cell, pathNode);
        }
        rerankChartCellPath(cell);
    }

    protected void addChartCellPath(ChartCell cell, PathNode pathNode) {
        double ngramScore = computeNgram(pathNode, 3);
        if (pathNode.ngramHistory != null) {
            ngramScore += pathNode.ngramHistory.ngramScore;
        }
        pathNode.ngramScore = ngramScore;
        cell.sentences.add(pathNode);
    }

    protected double computeNgram(PathNode node, int n) {
        List<String> ngram = new ArrayList<String>(n);
        for (PathNode cur = node; cur != null && ngram.size() <= n; cur = cur.ngramHistory) {
            ngram.add(0, cur.word.getString());
        }
        return score.computeNgram(ngram);
    }

    protected void rerankChartCellPath(ChartCell cell) {
        Collections.sort(cell.sentences, new PathNodeComparator());
    }

    public PathNode getOneBestPathNode(Chart chart) {
        ChartCell c = result[chart.num - 1];
        if (c != null && !c.sentences.isEmpty()) {
            return c.getPath(0);
        }
        else {
            return null;
        }
    }

    public ChartCell getOneBestCell(Chart chart) {
        return result[chart.num - 1];
    }

    /**
     * -1 for all
     * 
     * @param chart
     * @param n
     * @return
     */
    public List<PathNode> getNBestPathNode(Chart chart, int n) {
        ChartCell cell = result[chart.num - 1];
        if (cell != null) {
            if (n > cell.sentences.size() || n == -1)
                return cell.sentences.subList(0, cell.sentences.size());
            else
                return cell.sentences.subList(0, n);
        }
        return new ArrayList<PathNode>(n);
    }

    public static void main(String args[]) {
        File file1 = new File("data/syllable.txt");
        File file2 = new File("data/syllable_chars.txt");
        File file3 = new File("data/syllable_words.txt");
        File file4 = new File("data/2000-2007.default_0_30_40.arpa");
        File file5 = new File("data/2000-2007.dgram_0_30_40.arpa");
        WoogleDatabase.load(file1, file2, file3, file4, file5);
        Score score = new Score(WoogleDatabase.nGramLanguageModel,
                WoogleDatabase.dependencyLanguageModel);

        CKYDecoder decoder = new CKYDecoder(score);

        Chart chart = new Chart();

        String line = "hao zi yi qian ba bai wan yuan de mei guo kong jun bo wu guan";
        // String line = "wo men bu nan fa xian";
        String pinyin[] = line.split(" ");

        // for (int i = 0; i < pinyin.length; i++) {
        // cell = new ChartCell(i, i);
        // cell.pinyin = pinyin[i];
        // chart.setCell(cell, i, i);
        // decoder.CKYParse(chart, i + 1, i);
        // System.out.println(chart);
        // System.out.println(decoder.getOneBestSetence(chart));
        // }

        decoder.buildChart(chart, pinyin, 0);
        decoder.CKYParse(chart, 0);
        System.out.println(decoder.getNBestPathNode(chart, 3));

        // chart.table[0][0].selectedIndex = 1;
        // decoder.setChartCellSelected(chart, chart.table[0][0]);
        // chart.table[1][1].selectedIndex = 1;
        // decoder.setChartCellSelected(chart, chart.table[1][1]);
        // decoder.CKYParse(chart, 1);
        // System.out.println(decoder.getOneBestSetence(chart));

        // chart.table[6][6].selectedIndex = 5;
        // decoder.setChartCellSelected(chart, chart.table[6][6]);
        // decoder.CKYParse(chart, 0);
        // System.out.println(decoder.getOneBestSetence(chart));
        //
        // chart.table[0][0].selectedIndex = ChartCell.NOT_SEL;
        // decoder.setChartCellSelected(chart, chart.table[0][0]);
        // decoder.CKYParse(chart, 0);
        // System.out.println(decoder.getOneBestSetence(chart));
    }
}
