package woogle.cky;

import java.io.File;
import java.io.FileNotFoundException;

public class LanguageModelReader {
    
    public static LanguageModel readLanguageModel(File file) {
    	LanguageModel lm = new LanguageModel();
    	
    	MyFileReader reader = new MyFileReader(file);
    	
    	int n = 1;
		while(reader.hasNext()) {
			String line = reader.nextLine().trim();
			if (line.isEmpty())
				; // continue;
			// \data\
			else if (line.startsWith("\\data\\"))
				; //continue;
			else if (line.startsWith("\\end\\"))
				; // continue;
			// ngram 1=55312
			// ngram 2=467609
			else if (line.startsWith("ngram"))
				; //continue;
			// \1-gram
			else if (line.startsWith("\\1"))
				n = 1;
			// \2-gram
			else if (line.startsWith("\\2"))
				n = 2;
			// \3-gram
			else if (line.startsWith("\\3"))
				n = 3;
			else {
				XGram g = parse(line, n);
				if (g == null) {
					System.err.println(line);
					continue;
				}
				lm.add(g);
			}
			if (reader.lineno % 10000 == 0)
				System.out.println("读取行数:" + reader.lineno);
		}
		
		reader.close();
		return lm;
	}
	
	// -3.797958	‘	-0.7362673
	// -4.599457	</s>
	private static XGram parse(String line, int n) {
		String ss[] = line.split("\\s+");
		if (ss.length < n+1)
			return null;
		float prob = Float.parseFloat(ss[0]);
		XGram g = new XGram();
		g.prob = prob;
		if (n == 3) {
			g.w1 = ss[1];
			g.w2 = ss[2];
			g.w3 = ss[3];
		}
		else if (n == 2) {
			g.w2 = ss[1];
			g.w3 = ss[2];
		}
		else if (n == 1) {
			g.w3 = ss[1];
		}
		if (ss.length == n+2) {
			float backoff = Float.parseFloat(ss[n+1]);
			g.backoff = backoff;
		}
		return g;
	}
	
	public static void main(String args[]) throws FileNotFoundException {
		
		File inputFile = new File("lm.arpa");
//		File outputFile = new File("RMRB109801_n3_gram.arpa2");
//		
		LanguageModel lm = readLanguageModel(inputFile);
		float prob = 0;
//		MyFileWriter writer = new MyFileWriter(outputFile);
//		Iterator<XGram> itr = lm.iterator();
//		while (itr.hasNext()) {
//			writer.println(itr.next().toARPAString());
//		}
//		writer.close();
		
//		float prob = lm.queryProb("c", "d", "e");
//		System.out.println("c d e");
//		System.out.println(prob);
//		
//		prob = lm.queryProb("a", "b", "c");
//		System.out.println("a b c");
//		System.out.println(prob);
//		
//		prob = lm.queryProb(null, "a", "b");
//		System.out.println("a b");
//		System.out.println(prob);
//		
//		prob = lm.queryProb(null, null, "f");
//		System.out.println("f");
//		System.out.println(prob);
//		
		prob = lm.queryProb(null, null, "拼音");
		System.out.println("拼音");
		System.out.println(prob);
		
		Path p = new Path();
		p.word = "姻";
		Path p2 = new Path();
		p2.word = "品";
		p.history = p2;
		
//		p.words.add("e");
//		p.words.add("a");
//		p.words.add("b");
		lm.computeSentenceScore(p2);
		prob = lm.computeSentenceScore(p);
//		System.out.println("f d e a b");
		System.out.println(prob);
	}
}