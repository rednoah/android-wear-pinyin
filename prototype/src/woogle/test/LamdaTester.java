package woogle.test;

import java.io.File;

import pengyifan.io.Encoding;
import pengyifan.io.MyFileFactory;
import pengyifan.io.MyFileReader;
import pengyifan.io.MyFileWriter;

import woogle.chart.Chart;
import woogle.chart.EisnerDecoder;
import woogle.chart.Path;
import woogle.chart.PathNodeComparator2;
import woogle.ds.PathNode;
import woogle.util.Score;
import woogle.util.WoogleDatabase;

public class LamdaTester {
    public static void main(String args[]) {
        File file1 = new File("data/syllable.txt");
        File file2 = new File("data/syllable_chars.txt");
        File file3 = new File("data/syllable_words.txt");
        File file4 = new File("data/Hub4-2000-2007.0.551295.arpa");
        File file5 = new File("data/2000-2007.dgram_0_30_40.arpa");
        WoogleDatabase.load(file1, file2, file3, file4, file5);
        Score score = new Score(WoogleDatabase.nGramLanguageModel,
                WoogleDatabase.dependencyLanguageModel);
        EisnerDecoder decoder = new EisnerDecoder(score);

        Chart chart = new Chart();

        float lambda = 0.95f;
        while (lambda > 0.85f) {
            PathNodeComparator2.LAMDA = lambda;
            System.err.println("\nlamda = " + lambda);

            MyFileReader reader = MyFileFactory.getFileReader(new File(
                    "test/Hub4_eval_segment_clean5_20.pin"));
            String filename = String.format(
                    "test/Hub4_eval_5_20_Hub4-2000-2007_dgram_1bst2_%.2f.txt",
                    lambda);
            MyFileWriter writer = MyFileFactory
                    .getFileWriter(new File(filename));
            while (reader.hasNext()) {
                if (reader.getLineNumber() % 100 == 0)
                    System.err.print(".");

                String line = reader.nextLine().trim();
                String[] pinyin = line.split(" ");

                chart.clear();
                decoder.buildChart(chart, pinyin, 0);
                decoder.CKYParse(chart, 0);

                PathNode path = decoder.getOneBestPathNode(chart);
                line = Path.getSentenceString(path);
                writer.println(line);
            }
            reader.close();
            writer.close();
            lambda -= 0.01f;
        }
    }
}
