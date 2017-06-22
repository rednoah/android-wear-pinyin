package woogle.test;

import java.io.File;
import java.util.List;

import pengyifan.io.MyFileFactory;
import pengyifan.io.MyFileReader;
import pengyifan.io.MyFileWriter;

import woogle.chart.CKYDecoder;
import woogle.chart.Chart;
import woogle.chart.EisnerDecoder;
import woogle.chart.Path;
import woogle.ds.PathNode;
import woogle.util.Score;
import woogle.util.WoogleDatabase;

public class Pinyin2Hub4 {
    public static void main(String args[]) {
        File file1 = new File("data/syllable.txt");
        File file2 = new File("data/syllable_chars.txt");
        File file3 = new File("data/syllable_words.txt");
        File file4 = new File("data/Hub4-2000-2007.0.551126.arpa");
        File file5 = new File("data/2000-2007.dgram_0_30_40.arpa");
        WoogleDatabase.load(file1, file2, file3, file4, file5);
        Score score = new Score(WoogleDatabase.nGramLanguageModel,
                WoogleDatabase.dependencyLanguageModel);
        // CKYDecoder decoder = new CKYDecoder(score);
        EisnerDecoder decoder = new EisnerDecoder(score);

        Chart chart = new Chart();

        MyFileReader reader = MyFileFactory.getFileReader(new File(
                "test/Hub4_eval_segment_clean5_20.pin"));
        MyFileWriter writer = MyFileFactory.getFileWriter(new File(
                "test/Hub4_eval_5_20_Hub4-2000-2007_dgram_1bst3.txt"));
        while (reader.hasNext()) {
            if (reader.getLineNumber() % 100 == 0)
                System.err.print(".");
            // if (reader.lineno == 100)
            // break;

            String line = reader.nextLine().trim();
            String[] pinyin = line.split(" ");

            chart.clear();
            decoder.buildChart(chart, pinyin, 0);
            decoder.CKYParse(chart, 0);

            PathNode path = decoder.getOneBestPathNode(chart);
            line = Path.getSentenceString(path);
            writer.println(line);
            // writer.println(line + ":" + path.ngramScore + ":" +
            // path.depScore);

            // path = decoder.getTwoBestSetence(chart);
            // line = Path.getSentenceString(path);
            // writer.println(line + ":" + path.ngramScore + ":" +
            // path.depScore);

            // writer.println();

            // List<PathNode> nbest = decoder.getNBestPathNode(chart, 10);
            // for (PathNode p : nbest) {
            // writer.println(Path.getSentenceString(p));
            // }
            // writer.println();
        }
        reader.close();
        writer.close();
    }
}
