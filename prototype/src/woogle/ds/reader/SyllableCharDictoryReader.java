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

public class SyllableCharDictoryReader {
	public static void load(File file, SyllableWordDictory dic) {
		MyFileReader reader = MyFileFactory.getFileReader(file);
		while (reader.hasNext()) {
			String line = reader.nextLine().trim();

			if (line.isEmpty())
				continue;

			StringTokenizer st = new StringTokenizer(line, "\t");
			String py = st.nextToken();
			List<Word> chars = dic.get(py);
			if (chars == null) {
				chars = new ArrayList<Word>();
				dic.put(py, chars);
			}
			String hz = st.nextToken();
			for (char c : hz.toCharArray())
				chars.add(new Word(Character.toString(c).intern(), WordType.Char));
		}
		reader.close();
	}

	public static void main(String args[]) {
		SyllableWordDictory dic = new SyllableWordDictory();
		File file = new File("data/syllable_chars.txt");
		SyllableCharDictoryReader.load(file, dic);
		for (String s : dic.keySet()) {
			System.out.println(dic.get(s));
		}
	}
}
