package woogle.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import woogle.ds.DependencyLanguageModel;
import woogle.ds.NGramLanguageModel;
import woogle.ds.Pair;
import woogle.ds.SyllableDictory;
import woogle.ds.SyllableWordDictory;
import woogle.ds.Word;
import woogle.ds.reader.DependencyLanguageModelReader;
import woogle.ds.reader.NGramLanguageModelReader;
import woogle.ds.reader.SyllableCharDictoryReader;
import woogle.ds.reader.SyllableDictoryReader;
import woogle.ds.reader.SyllableWordDictoryReader;

public class WoogleDatabase {

    private static boolean                isInit                  = false;

    public static SyllableWordDictory     syllableCharDictory     = new SyllableWordDictory();

    public static SyllableWordDictory     syllableWordDictory     = new SyllableWordDictory();     ;

    public static SyllableDictory         syllableDictory         = new SyllableDictory();

    public static NGramLanguageModel      nGramLanguageModel      = new NGramLanguageModel();

    public static DependencyLanguageModel dependencyLanguageModel = new DependencyLanguageModel();

    public static void load(File syllableFile, File syllableCharFile,
            File syllableWordFile, File ngramLanguageModelFile,
            File dependencyLanguageModelFile) {
        if (!isInit) {
            loadSyllableDictory(syllableFile);
            loadSyllableCharDictory(syllableCharFile);
            loadSyllableWordDictory(syllableWordFile);
            loadNGramLanguageModel(ngramLanguageModelFile);
            loadDependencyLanguageModel(dependencyLanguageModelFile);
            isInit = true;
        }
    }

    public static boolean isInit() {
        return isInit;
    }

    private static void loadNGramLanguageModel(File file) {
        NGramLanguageModelReader.load(file, nGramLanguageModel);
    }

    private static void loadDependencyLanguageModel(File file) {
        DependencyLanguageModelReader.load(file, dependencyLanguageModel);
    }

    private static void loadSyllableDictory(File file) {
        SyllableDictoryReader.load(file, syllableDictory);
    }

    private static void loadSyllableCharDictory(File file) {
        SyllableCharDictoryReader.load(file, syllableCharDictory);
    }

    private static void loadSyllableWordDictory(File file) {
        SyllableWordDictoryReader.load(file, syllableWordDictory);
    }

    /**
     * 根据syllable找到对应的单词、汉字
     * 
     * @param pinyin
     * @return
     */
    public static List<Word> findCandidates(String syllable) {
        List<Word> cands = new ArrayList<Word>();
        List<Word> words = syllableWordDictory.get(syllable);
        if (words != null) {
            for (Word w : words) {
                cands.add(w);
            }
        }
        List<Word> chars = syllableCharDictory.get(syllable);
        if (chars != null) {
            for (Word w : chars) {
                cands.add(w);
            }
        }
        if (cands.isEmpty()) {
            Pair<Integer, Integer> p = syllableDictory.range(syllable);
            for (int i = p.first; i < p.second; i++) {
                syllable = syllableDictory.get(i);
                chars = syllableCharDictory.get(syllable);
                if (chars != null)
                    for (Word w : chars) {
                        cands.add(w);
                    }
            }
        }
        return cands;
    }

    public static void main(String args[]) {
        File file1 = new File("data/syllable.txt");
        File file2 = new File("data/syllable_chars.txt");
        File file3 = new File("data/syllable_words.txt");
        File file4 = new File("data/2000-2007_wordsegment.rmrb.small.arpa");
        File file5 = new File("data/2000-2007_wordsegment.xgram.small.arpa");

        WoogleDatabase.load(file1, file2, file3, file4, file5);

        String pinyin[] = { "wo", "w", "suiji", "ceshi", "pinyin", "shuru" };
        for (String p : pinyin) {
            List<Word> cand = WoogleDatabase.findCandidates(p);
            System.out.println(p + ":" + cand);
        }
    }
}
