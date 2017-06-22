package woogle.ds.reader;

import java.io.File;

import pengyifan.io.MyFileFactory;
import pengyifan.io.MyFileReader;
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
            }
            else if (line.startsWith("\\data\\")) {
                ; // continue;
            }
            else if (line.startsWith("\\end\\"))
                ; // continue;
            // \1-gram
            else if (line.startsWith("\\1")) {
                System.err.println("loading 1gram");
            }
            // \2-gram
            else if (line.startsWith("\\2")) {
                System.err.println("\nloading 2gram");
            }
            // \3-gram
            else if (line.startsWith("\\3")) {
                System.err.println("\nloading 3gram");
            }
            else {
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
            if (reader.getLineNumber() % 10000 == 0)
                System.err.print(".");
        }
        lm.setUNK("<unk>");
        // lm.setOOVProb(lm.getProb(new String[]{"<unk>"}));
        lm.setOOVProb(minUniScore - 0.1);
        reader.close();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        NGramLanguageModel lm = new NGramLanguageModel();
        File f = new File("data/2000-2007_wordsegment.rmrb.small.arpa");
        NGramLanguageModelReader.load(f, lm);

        String a = "1.1Íò";
        System.out.println(lm.getProb(new String[] { a }));

        System.out.println(System.currentTimeMillis() - l);
    }
}