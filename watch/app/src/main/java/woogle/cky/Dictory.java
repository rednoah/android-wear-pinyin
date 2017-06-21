package woogle.cky;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import java.io.File;

public class Dictory {

	public HashMap<String, String> hanziMap;
	public HashMap<String, List<String>> wordMap;
	public HashSet<String> vocab;
	public RootSyllableNode syllableTree;
	
	public Dictory() {
		hanziMap = new HashMap<String, String>();
		wordMap = new HashMap<String, List<String>>();
		vocab = new HashSet<String>();
		syllableTree =  new RootSyllableNode();
	}
	
	public void readSyllable(File file) {
		MyFileReader reader = new MyFileReader(file);
		while(reader.hasNext()) {
			String line = reader.nextLine();
			if(line.isEmpty())
				continue;
			String[] tokens = line.split("\\s+");
			String initial = line.startsWith("@") ? "" : tokens[0];
			for(String t: tokens) {
				syllableTree.addSyllable(initial + t);
			}
		}
		reader.close();
	}
	
	public void readVocab(File file) {
		MyFileReader reader = new MyFileReader(file);
		while (reader.hasNext()) {
			String line = reader.nextLine();
			line = line.trim();
			if (line.isEmpty())
				continue;
			this.vocab.add(line);
		}
		reader.close();
	}

	public void readWordDic(File file) {
		MyFileReader reader = new MyFileReader(file);
		while (reader.hasNext()) {
			String line = reader.nextLine();
			line = line.trim();
			if (line.isEmpty())
				continue;
			String[] ws = line.split("\t");
			String py = ws[0];
			List<String> wds = wordMap.get(py);
			if (wds == null) {
				wds = new ArrayList<String>();
				this.wordMap.put(py, wds);
			}
			for (int i=1; i<ws.length; i++) {
				if (ws[i].length() > 1)
					wds.add(ws[i]);
			}
		}
		reader.close();
	}

	public void readHanziDic(File file) {
		MyFileReader reader = new MyFileReader(file);
		while (reader.hasNext()) {
			String line = reader.nextLine();
			line = line.trim();
			if (line.isEmpty())
				continue;
			int index = line.indexOf('\t');
			String py = line.substring(0, index);
			String hz = line.substring(index + 1);
			this.hanziMap.put(py, hz);
		}
		reader.close();
	}

	/**
	 * 根据pinyin找到对应的单词
	 * @param pinyin
	 * @return
	 */
	public List<String> find(String pinyin) {
		List<String> cand = new ArrayList<String>();
		List<String> wds = this.wordMap.get(pinyin);
		if (wds != null) {
			cand.addAll(wds);
		}
		String hz = this.hanziMap.get(pinyin);
		if (hz != null) {
			for (char c: hz.toCharArray()) {
				cand.add(Character.toString(c));
			}
		}
		if (cand.isEmpty()) {
			List<String> pys = this.syllableTree.find(pinyin);
			for(String py: pys) {
				hz = this.hanziMap.get(py);
				if (hz != null) {
					for (char c: hz.toCharArray()) {
						cand.add(Character.toString(c));
					}
				}
			}
		}
		return cand;
	}
	
	public static void main(String args[]) {
		File file = new File("拼音字典.txt");
		Dictory dic = new Dictory();
		dic.readHanziDic(file);
		file = new File("拼音词典.txt");
		dic.readWordDic(file);
		file = new File("syllable.txt");
		dic.readSyllable(file);
		
		List<String> cand = dic.find("wo");
		System.out.println("wo");
		for(String s: cand) {
			System.out.print(s + " ");
		}
		System.out.println();
		
		cand = dic.find("w");
		System.out.println("w");
		for(String s: cand) {
			System.out.print(s + " ");
		}
		System.out.println();
		
		cand = dic.find("suiji");
		System.out.println("suiji");
		for(String s: cand) {
			System.out.print(s + " ");
		}
		System.out.println();
		
		String pinyin[] = {"ceshi", "pinyin", "shuru"};
		for(String p: pinyin) {
			cand = dic.find(p);
			System.out.println(p);
			for(String s: cand) {
				System.out.print(s + " ");
			}
			System.out.println();
		}
	}
}