package woogle.chart;

import java.io.File;
import java.util.Collections;
import java.util.List;

import woogle.ds.DependencyLanguageModel;
import woogle.ds.PathNode;
import woogle.eisner.DependencyTree;
import woogle.eisner.EisnerChart;
import woogle.eisner.EisnerParser;
import woogle.util.Score;
import woogle.util.WoogleDatabase;

public class EisnerDecoder extends CKYDecoder {

    // double c[][][][];
    //
    // DependencyTree2 a[][][][];

    public static int NBEST = 5;

    EisnerChart ec;

    EisnerParser parser;

    public EisnerDecoder(Score score) {
        super(score);
        ec = new EisnerChart();
        parser = new EisnerParser(score);
    }

    @Override
    public void CKYParse(Chart chart, int beginIndex) {
        for (int j = beginIndex; j < chart.num; j++) {
            result[j] = new ChartCell(0, j);
            clone(result[j], chart.table[0][j]);
            if (result[j].sentences.size() > 10)
                result[j].sentences = result[j].sentences.subList(0, 10);
            for (int k = 0; k < j; k++) {
                union(result[j], result[k], chart.table[k + 1][j]);
            }
            super.rerankChartCellPath(result[j]);
            chartCellDep(result[j]);
        }
        // chartCellDep(result[chart.num - 1]);
        // Collections.sort(result[chart.num - 1].sentences,
        // new PathNodeComparator3());

        // for (int i = 0; i < result[chart.num - 1].sentences.size(); i++) {
        // PathNode p = result[chart.num - 1].sentences.get(i);
        // List<String> sentence = Path.getSentenceStrings(p);
        // if (i < NBEST) {
        // sentence.add(0, "<s>");
        // EisnerChart ec = new EisnerChart(sentence.size());
        // EisnerParser parser = new EisnerParser(ec, sentence
        // .toArray(new String[0]), score);
        // parser.init();
        // parser.parse();
        // DependencyTree2 tree = parser.getDependencyTree(0, sentence
        // .size() - 1, 1, 0);
        // System.out.println(tree.toString(sentence
        // .toArray(new String[0])));
        // }
        // }
    }

    protected void chartCellDep(ChartCell cell) {

        for (int i = 0; i < cell.sentences.size(); i++) {
            PathNode p = cell.sentences.get(i);
            List<String> sentence = Path.getSentenceStrings(p);
            if (i < NBEST) {
                sentence.add(0, "<s>");
                parser.init(ec, sentence.toArray(new String[0]));
                parser.parse(ec);
                double depScore = ec.getScore(0, sentence.size() - 1, 1, 0);
                p.depScore = depScore;
            } else {
                p.depScore = sentence.size() * DependencyLanguageModel.NOT_FOUND;
            }
        }
        rerankChartCellPathDep(cell);
    }

    protected void rerankChartCellPathDep(ChartCell cell) {
        Collections.sort(cell.sentences, new PathNodeComparator2());
    }


}
