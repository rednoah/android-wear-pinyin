package woogle.eisner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import woogle.ds.DependencyLanguageModel;
import woogle.util.Score;
import woogle.util.WoogleDatabase;

public class EisnerParser {

    Score  score;

    String sentence[];

    public EisnerParser(Score score) {
        this.score = score;
    }

    public void init(EisnerChart chart, String sentence[]) {
        this.sentence = sentence;
        chart.init(sentence.length);
    }

    public void parse(EisnerChart chart) {
        int n = sentence.length;
        for (int t = 0; t < n; t++) {
            for (int s = t - 1; s >= 0; s--) {
                max1(chart, s, t);
                max2(chart, s, t);
                max3(chart, s, t);
                max4(chart, s, t);
            }
        }
    }

    // public DependencyTree getDependencyTree(int row, int col, int dir, int
    // com) {
    // return chart.a[row][col][dir][com];
    // }

    private void max1(EisnerChart chart, int s, int t) {
        double maxScore = Double.NEGATIVE_INFINITY;
        int maxIndex = -1;
        for (int q = s; q < t; q++) {
            double tmp = chart.c[s][q][1][0] + chart.c[q + 1][t][0][0]
                    + getParameter(t, s, chart.a[s][q][1][0]);
            if (tmp > maxScore) {
                maxScore = tmp;
                maxIndex = q;
            }
        }
        chart.c[s][t][0][1] = maxScore;
        chart.a[s][t][0][1].add(chart.a[s][maxIndex][1][0]);
        chart.a[s][t][0][1].add(chart.a[maxIndex + 1][t][0][0]);
        chart.a[s][t][0][1].add(t, s);
        // System.out.println(String.format("[%d,%d,%d,%d]: %f\n", s, t, 0, 1,
        // maxScore));
        // System.out.println("q: " + maxIndex);
        // System.out.println(chart.a[s][t][0][1]);
        // System.out.println(chart.a[s][maxIndex][1][0]);
        // System.out.println(chart.a[maxIndex + 1][t][0][0]);
    }

    private void max2(EisnerChart chart, int s, int t) {
        double maxScore = Double.NEGATIVE_INFINITY;
        int maxIndex = -1;
        for (int q = s; q < t; q++) {
            double tmp = chart.c[s][q][1][0] + chart.c[q + 1][t][0][0]
                    + getParameter(s, t, chart.a[q + 1][t][0][0]);
            if (tmp > maxScore) {
                maxScore = tmp;
                maxIndex = q;
            }
        }
        chart.c[s][t][1][1] = maxScore;
        chart.a[s][t][1][1].add(chart.a[s][maxIndex][1][0]);
        chart.a[s][t][1][1].add(chart.a[maxIndex + 1][t][0][0]);
        chart.a[s][t][1][1].add(s, t);
        // System.out.println(String.format("[%d,%d,%d,%d]: %f\n", s, t, 1, 1,
        // maxScore));
        // System.out.println("q: " + maxIndex);
        // System.out.println(chart.a[s][t][1][1]);
        // System.out.println(chart.a[s][maxIndex][1][0]);
        // System.out.println(chart.a[maxIndex + 1][t][0][0]);
    }

    private void max3(EisnerChart chart, int s, int t) {
        double maxScore = Double.NEGATIVE_INFINITY;
        int maxIndex = -1;
        for (int q = s; q < t; q++) {
            double tmp = chart.c[s][q][0][0] + chart.c[q][t][0][1];
            if (tmp > maxScore) {
                maxScore = tmp;
                maxIndex = q;
            }
        }
        chart.c[s][t][0][0] = maxScore;
        chart.a[s][t][0][0].add(chart.a[s][maxIndex][0][0]);
        chart.a[s][t][0][0].add(chart.a[maxIndex][t][0][1]);
        // System.out.println(String.format("[%d,%d,%d,%d]: %f\n", s, t, 0, 0,
        // maxScore));
        // System.out.println("q: " + maxIndex);
        // System.out.println(chart.a[s][t][0][0]);
        // System.out.println(chart.a[s][maxIndex][0][0]);
        // System.out.println(chart.a[maxIndex][t][0][1]);
    }

    private void max4(EisnerChart chart, int s, int t) {
        double maxScore = Double.NEGATIVE_INFINITY;
        int maxIndex = -1;
        for (int q = s + 1; q <= t; q++) {
            double tmp = chart.c[s][q][1][1] + chart.c[q][t][1][0];
            if (tmp > maxScore) {
                maxScore = tmp;
                maxIndex = q;
            }
        }
        chart.c[s][t][1][0] = maxScore;
        chart.a[s][t][1][0].add(chart.a[s][maxIndex][1][1]);
        chart.a[s][t][1][0].add(chart.a[maxIndex][t][1][0]);
        // System.out.println(String.format("[%d,%d,%d,%d]: %f\n", s, t, 1, 0,
        // maxScore));
        // System.out.println("q: " + maxIndex);
        // System.out.println(chart.a[s][t][1][0]);
        // System.out.println(chart.a[s][maxIndex][1][1]);
        // System.out.println(chart.a[maxIndex][t][1][0]);
    }

    public void finish() {

    }

    // private static int lambda = 1;
    //
    // private static int lambdas[][] = { { 0, 9, 10, 9 }, { 0, 0, 20, 3 },
    // { 0, 30, 0, 30 }, { 0, 11, 0, 0 } };

    /**
     * head --> dependent
     * 
     * @param indexOfHead
     * @param indexOfDependent
     * @param dependentTree
     * @return
     */
    public double getParameter(int indexOfHead, int indexOfDependent,
            DependencyTree dependentTree) {
        double d = 0;
        List<String> deplink = new ArrayList<String>(3);
        deplink.add(sentence[indexOfHead]);
        deplink.add(sentence[indexOfDependent]);
        // boolean isFoundTrigram = false;
        // for (int i = 0; i < dependentTree.getSize(); i++) {
        // if (dependentTree.dependents[indexOfDependent][i]) {
        // deplink.add(sentence[i]);
        // d += score.computeDependency(deplink);
        // deplink.remove(2);
        // isFoundTrigram = true;
        // }
        // }
        // if (!isFoundTrigram) {
        // d += score.computeDependency(deplink);
        // }
        d += score.computeDependency(deplink);
        return d;

        // return lambdas[indexOfHead][indexOfDependent];
        // return lambda ++;
        // return Math.abs(Math.random());
    }

    // /**
    // * head --> dependent
    // *
    // * @param indexOfHead
    // * @param indexOfDependent
    // * @return
    // */
    // public double getParameter(int indexOfHead, int indexOfDependent) {
    // return lambdas[indexOfHead][indexOfDependent];
    // // return lambda ++;
    // // return Math.abs(Math.random());
    // }

    public static void main(String args[]) {
        File file1 = new File("data/syllable.txt");
        File file2 = new File("data/syllable_chars.txt");
        File file3 = new File("data/syllable_words.txt");
        File file4 = new File("data/2000-2007.default_0_30_40.arpa");
        File file5 = new File("data/2000-2007.dgram_0_30_40.arpa");
        WoogleDatabase.load(file1, file2, file3, file4, file5);
        Score score = new Score(WoogleDatabase.nGramLanguageModel,
                WoogleDatabase.dependencyLanguageModel);

        String sentence[] = "<s> 与 政权 交接 有关 的 一些 新 议题".split("\\s+");
        EisnerChart chart = new EisnerChart();
        EisnerParser parser = new EisnerParser(score);
        parser.init(chart, sentence);
        parser.parse(chart);
        DependencyTree tree = chart.a[0][sentence.length - 1][1][0];
        System.out.println(tree.toString(sentence));
        System.out.println(chart.c[0][sentence.length - 1][1][0]);
    }
}
