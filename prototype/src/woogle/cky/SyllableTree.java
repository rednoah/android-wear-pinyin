package woogle.cky;

import java.io.File;
import java.util.List;

public class SyllableTree {
	
	public static RootSyllableNode buildSyllableree(File file) {
		RootSyllableNode rootSyllableNode =  new RootSyllableNode();
		MyFileReader reader = new MyFileReader(file);
		while(reader.hasNext()) {
			String line = reader.nextLine();
			if(line.isEmpty())
				continue;
			String[] tokens = line.split("\\s+");
			String initial = line.startsWith("@") ? "" : tokens[0];
			for(String t: tokens) {
				rootSyllableNode.addSyllable(initial + t);
			}
		}
		reader.close();
		return rootSyllableNode;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RootSyllableNode syllableTree = SyllableTree.buildSyllableree(new File("syllable.txt"));
		
		//test the building of the syllable tree
		List<String> l = syllableTree.find("b");
		for(String s: l) {
			System.out.println(s);
		}
	}
}
