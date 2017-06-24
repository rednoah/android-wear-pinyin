package woogle.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import woogle.io.MyFileReader;
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

    private static boolean isInit = false;

    public static SyllableWordDictory syllableCharDictory = new SyllableWordDictory();

    public static SyllableWordDictory syllableWordDictory = new SyllableWordDictory();
    ;

    public static SyllableDictory syllableDictory = new SyllableDictory();

    public static NGramLanguageModel nGramLanguageModel = new NGramLanguageModel();

    public static DependencyLanguageModel dependencyLanguageModel = new DependencyLanguageModel();


    public static File prepareAssets(Context context, String... assets) {
        try {
            File assetsFolder = new File(context.getFilesDir(), "assets");
            if (!assetsFolder.exists() && !assetsFolder.mkdirs()) {
                throw new IllegalStateException("Failed to create folder: " + assetsFolder);
            }

            for (String a : assets) {
                Log.d("WoogleInputMethod", "Prepare asset: " + a);
                FileUtils.copyInputStreamToFile(context.getAssets().open(a, AssetManager.ACCESS_STREAMING), new File(assetsFolder, a));
            }

            return assetsFolder;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


    public static void load(Context context) {
        if (isInit) {
            return;
        }


        File db = new File(context.getExternalCacheDir(), "woogle.db");


        if (db.exists()) {
            Log.d("WoogleDatabase", "Restore");

            try (ObjectInputStream os = new ObjectInputStream(new BufferedInputStream(new FileInputStream(db), MyFileReader.BUF_SIZ))) {
                Log.d("WoogleDatabase", "Load syllableDictory");
                syllableDictory = (SyllableDictory) os.readObject();
                Log.d("WoogleDatabase", "Load syllableCharDictory");
                syllableCharDictory = (SyllableWordDictory) os.readObject();
                Log.d("WoogleDatabase", "Load syllableWordDictory");
                syllableWordDictory = (SyllableWordDictory) os.readObject();
                Log.d("WoogleDatabase", "Load nGramLanguageModel");
                nGramLanguageModel = (NGramLanguageModel) os.readObject();
                Log.d("WoogleDatabase", "Load dependencyLanguageModel");
                dependencyLanguageModel = (DependencyLanguageModel) os.readObject();
            } catch (Exception e) {
                Log.d("WoogleDatabase", "Restore failed", e);
            }
        } else {
            Log.d("WoogleDatabase", "Generate");

            File assetsFolder = prepareAssets(context, "syllable.txt", "syllable_chars.txt", "syllable_words.txt", "lm.arpa", "SogouR.mini.txt");

            loadSyllableDictory(new File(assetsFolder, "syllable.txt"));
            loadSyllableCharDictory(new File(assetsFolder, "syllable_chars.txt"));
            loadSyllableWordDictory(new File(assetsFolder, "syllable_words.txt"));
            loadNGramLanguageModel(new File(assetsFolder, "lm.arpa"));
            loadDependencyLanguageModel(new File(assetsFolder, "SogouR.mini.txt"));


            try (ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(db), MyFileReader.BUF_SIZ))) {
                Log.d("WoogleDatabase", "Store syllableDictory");
                os.writeObject(syllableDictory);
                Log.d("WoogleDatabase", "Store syllableCharDictory");
                os.writeObject(syllableCharDictory);
                Log.d("WoogleDatabase", "Store syllableWordDictory");
                os.writeObject(syllableWordDictory);
                Log.d("WoogleDatabase", "Store nGramLanguageModel");
                os.writeObject(nGramLanguageModel);
                Log.d("WoogleDatabase", "Store dependencyLanguageModel");
                os.writeObject(dependencyLanguageModel);
            } catch (Exception e) {
                Log.d("WoogleDatabase", "Store failed", e);
            }


            Log.d("WoogleDatabase", "Store: " + db + " (" + FileUtils.byteCountToDisplaySize(db.length()) + ")");
        }


        isInit = true;
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


}
