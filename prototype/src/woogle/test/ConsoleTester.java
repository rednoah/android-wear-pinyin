package woogle.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import woogle.chart.CKYDecoder;
import woogle.chart.Chart;
import woogle.chart.Path;
import woogle.ds.PathNode;

// import woogle.lattice.Lattice;
// import woogle.lattice.ViterbiDecoder;
import woogle.util.PinyinSyllable;
import woogle.util.Score;
import woogle.util.WoogleDatabase;

public class ConsoleTester {

	public static void main(String args[]) throws IOException {
		File file1 = new File("data/syllable.txt");
		File file2 = new File("data/syllable_chars.txt");
		File file3 = new File("data/syllable_words.txt");
		File file4 = new File("data/2000-2007_wordsegment.rmrb.small.arpa");
		File file5 = new File("data/SogouR.mini.txt");

		WoogleDatabase.load(file1, file2, file3, file4, file5);
		Score score = new Score(WoogleDatabase.nGramLanguageModel, WoogleDatabase.dependencyLanguageModel);
		Chart chart = new Chart();
		CKYDecoder decoder = new CKYDecoder(score);
		// Lattice lattice = new Lattice();
		// ViterbiDecoder decoder = new ViterbiDecoder(score);
		PinyinSyllable pinyinSyllable = new PinyinSyllable();

		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print(" ‰»Î∆¥“Ù: ");
			String line = bf.readLine();
			if (line.equals("end"))
				break;
			List<String> pinyin = pinyinSyllable.split(line);
			System.out.println(pinyin);
			decoder.buildChart(chart, pinyin.toArray(new String[0]), 0);
			decoder.CKYParse(chart, 0);
			// lattice.buildLattice(pinyin.toArray(new String[0]), 0);
			// lattice.decode(decoder, 0);
			List<PathNode> sentences = decoder.getNBestPathNode(chart, 10);
			for (PathNode p : sentences) {
				System.out.println(Path.getSentenceString(p));
			}
		}
	}
}
