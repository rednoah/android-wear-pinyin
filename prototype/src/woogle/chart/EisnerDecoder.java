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

    EisnerChart       ec;

    EisnerParser      parser;

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
            }
            else {
                p.depScore = sentence.size() * DependencyLanguageModel.NOT_FOUND;
            }
        }
        rerankChartCellPathDep(cell);
    }

    protected void rerankChartCellPathDep(ChartCell cell) {
        Collections.sort(cell.sentences, new PathNodeComparator2());
    }

    public static void main(String args[]) {
        File file1 = new File("data/syllable.txt");
        File file2 = new File("data/syllable_chars.txt");
        File file3 = new File("data/syllable_words.txt");
        File file4 = new File("data/Hub4-2000-2007.0.551126.arpa");
        File file5 = new File("data/2000-2007.dgram_0_30_40.arpa");
        WoogleDatabase.load(file1, file2, file3, file4, file5);
        Score score = new Score(WoogleDatabase.nGramLanguageModel,
                WoogleDatabase.dependencyLanguageModel);

        EisnerDecoder decoder = new EisnerDecoder(score);

        Chart chart = new Chart();

        String line = "ran er zhong guo cheng gong di zu zhi le zhe lei ti an";
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

        // List<PathNode> nbest = decoder.getNBestPathNode(chart, 2);
        // System.out.println(nbest.get(0));
        // System.out.println(nbest.get(1));

        List<PathNode> nbest = decoder.getNBestPathNode(chart, 10);
        for (PathNode p : nbest) {
            System.out.println(Path.getDebugString(p));
        }
        System.out.println();

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
