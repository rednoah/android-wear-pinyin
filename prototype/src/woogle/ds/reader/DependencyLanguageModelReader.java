package woogle.ds.reader;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import pengyifan.io.MyFileFactory;
import pengyifan.io.MyFileReader;
import woogle.ds.DependencyLanguageModel;
import woogle.ds.NGramLanguageModel;

public class DependencyLanguageModelReader {

	public static void load(File file, DependencyLanguageModel lm) {
		HashMap<String, Integer> dm = new LinkedHashMap<>();
		MyFileReader reader = MyFileFactory.getFileReader(file);
		double minUniScore = 0;

		while (reader.hasNext()) {
			String line = reader.nextLine().trim();
			if (line.isEmpty())
				continue;

			// 工艺-供应 392
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
				System.out.println("读取行数:" + reader.lineno);
			}
		}
		reader.close();

		lm.setOOVProb(minUniScore - 0.1);
		reader.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long l = System.currentTimeMillis();
		DependencyLanguageModel lm = new DependencyLanguageModel();
		File f = new File("data/SogouR.mini.txt");
		DependencyLanguageModelReader.load(f, lm);

		String[] a = { "工艺", "供应" };
		System.out.println(lm.getProb(a));

		System.out.println(System.currentTimeMillis() - l);
	}
}
