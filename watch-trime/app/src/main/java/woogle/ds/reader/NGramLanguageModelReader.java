package woogle.ds.reader;

import android.util.Log;

import java.io.File;

import woogle.io.MyFileFactory;
import woogle.io.MyFileReader;
import woogle.ds.NGramLanguageModel;

public class NGramLanguageModelReader {
    public static void load(File file, NGramLanguageModel lm) {

        double minUniScore = 0;

        MyFileReader reader = MyFileFactory.getFileReader(file);
        while (reader.hasNext()) {
            String line = reader.nextLine().trim();
            if (line.isEmpty())
                ; // continue;
            else if (line.startsWith("ngram ")) {
                ; // continue;
            } else if (line.startsWith("\\data\\")) {
                ; // continue;
            } else if (line.startsWith("\\end\\"))
                ; // continue;
                // \1-gram
            else if (line.startsWith("\\1")) {

                Log.d("NGramLanguageModel", "loading 1gram");
            }
            // \2-gram
            else if (line.startsWith("\\2")) {
                Log.d("NGramLanguageModel", "loading 2gram");
            }
            // \3-gram
            else if (line.startsWith("\\3")) {
                Log.d("NGramLanguageModel", "loading 3gram");
            } else {
                String toks[] = line.split("\t");
                assert (toks.length == 2 || toks.length == 3);

                double p = Double.parseDouble(toks[0]);
                double b = NGramLanguageModel.NOT_FOUND;
                if (toks.length == 3)
                    b = Double.parseDouble(toks[2]);

                toks = toks[1].split(" ");
                lm.add(toks, p, b);
                if (toks.length == 1) {
                    lm.addWord(toks[0]);
                    if (p < minUniScore && !toks[0].equals("<s>"))
                        minUniScore = p;
                }
            }

            if (reader.lineno % 10000 == 0) {
                Log.d("LanguageModel", "LINE: " + reader.lineno);
            }
        }
        lm.setUNK("<unk>");
        lm.setOOVProb(minUniScore - 0.1);
        reader.close();
    }


}