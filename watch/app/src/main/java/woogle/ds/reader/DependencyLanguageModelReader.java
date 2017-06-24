package woogle.ds.reader;

import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import woogle.io.MyFileFactory;
import woogle.io.MyFileReader;
import woogle.ds.DependencyLanguageModel;
import woogle.ds.NGramLanguageModel;

public class DependencyLanguageModelReader {

    public static void load(File file, DependencyLanguageModel lm) {
        HashMap<String, Integer> dm = new LinkedHashMap<>();
        MyFileReader reader = MyFileFactory.getFileReader(file);

        while (reader.hasNext()) {
            String line = reader.nextLine().trim();
            if (line.isEmpty())
                continue;

            int index = line.indexOf('\t');
            if (index != -1) {
                double p = Double.parseDouble(line.substring(index + 1));
                double b = NGramLanguageModel.NOT_FOUND;

                String[] toks = Pattern.compile("-").splitAsStream(line.substring(0, index)).map(String::trim).filter(it -> !it.replaceAll("[a-z]", "").isEmpty()).toArray(String[]::new);
                if (toks.length != 2) {
                    continue;
                }

                lm.add(toks, p, b);
            }

            if (reader.lineno % 10000 == 0) {
                Log.d("DependencyModel", "LINE: " + reader.lineno);
            }
        }
        reader.close();

        lm.setOOVProb(-100);
        reader.close();
    }


}
