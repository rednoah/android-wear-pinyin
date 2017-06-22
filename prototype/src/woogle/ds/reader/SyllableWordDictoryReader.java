package woogle.ds.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import pengyifan.io.MyFileFactory;
import pengyifan.io.MyFileReader;
import woogle.ds.SyllableWordDictory;
import woogle.ds.Word;
import woogle.ds.WordType;

public class SyllableWordDictoryReader {
	public static void load(File file, SyllableWordDictory dic) {
		MyFileReader reader = MyFileFactory.getFileReader(file);
		while (reader.hasNext()) {
			String line = reader.nextLine().trim();
			StringTokenizer st = new StringTokenizer(line, "\t");
			String py = st.nextToken();
			List<Word> words = dic.get(py);
			if (words == null) {
				words = new ArrayList<Word>();
				dic.put(py, words);
			}
			while (st.hasMoreTokens()) {
			    String s = st.nextToken().intern();
			    if (s.length() == 1) {
			        words.add(new Word(s, WordType.WordChar));
			    }
			    else {
			        words.add(new Word(s, WordType.Word));
			    }
			}
		}
		reader.close();
	}
	
	public static void main(String args[]) {
        SyllableWordDictory dic = new SyllableWordDictory();
        File file = new File("data/syllable_words.txt");
        SyllableWordDictoryReader.load(file, dic);
        for(String s: dic.keySet()) {
        	System.out.println(dic.get(s));
        }
    }
}
