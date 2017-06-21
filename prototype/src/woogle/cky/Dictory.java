package woogle.cky;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.mapdb.Atomic.Var;
import org.mapdb.DB;
import org.mapdb.Serializer;

public class Dictory {

	public HashMap<String, String> hanziMap;
	public HashMap<String, List<String>> wordMap;
	public HashSet<String> vocab;
	public RootSyllableNode syllableTree;

	public Dictory() {
		hanziMap = new HashMap<String, String>();
		wordMap = new HashMap<String, List<String>>();
		vocab = new HashSet<String>();
		syllableTree = new RootSyllableNode();
	}

	public void readSyllable(File file, DB db) {
		MyFileReader reader = new MyFileReader(file);
		while (reader.hasNext()) {
			String line = reader.nextLine();
			if (line.isEmpty())
				continue;
			String[] tokens = line.split("\\s+");
			String initial = line.startsWith("@") ? "" : tokens[0];
			for (String t : tokens) {
				syllableTree.addSyllable(initial + t);
			}
		}
		reader.close();

		Var<Object> diskStore = db.createAtomicVar("SyllableTree", syllableTree, Serializer.JAVA);
		diskStore.set(syllableTree);
	}

	public void readSyllable(DB db) {
		Var<Object> diskStore = db.getAtomicVar("SyllableTree");
		this.syllableTree = (RootSyllableNode) diskStore.get();
	}

	public void readWordDic(File file, DB db) {
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
			for (int i = 1; i < ws.length; i++) {
				if (ws[i].length() > 1)
					wds.add(ws[i]);
			}
		}
		reader.close();

		NavigableMap<String, List<String>> diskStore = db.createTreeMap("WordDic").makeOrGet();
		diskStore.putAll(wordMap);
	}

	public void readWordDic(DB db) {
		NavigableMap<String, List<String>> diskStore = db.createTreeMap("WordDic").makeOrGet();
		this.wordMap.putAll(diskStore);
	}

	public void readHanziDic(File file, DB db) {
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

		NavigableMap<String, String> diskStore = db.createTreeMap("HanziDic").makeOrGet();
		diskStore.putAll(hanziMap);
	}

	public void readHanziDic(DB db) {
		NavigableMap<String, String> diskStore = db.createTreeMap("HanziDic").makeOrGet();
		this.hanziMap.putAll(diskStore);
	}

	public List<String> find(String pinyin) {
		List<String> cand = new ArrayList<String>();
		List<String> wds = this.wordMap.get(pinyin);
		if (wds != null) {
			cand.addAll(wds);
		}
		String hz = this.hanziMap.get(pinyin);
		if (hz != null) {
			for (char c : hz.toCharArray()) {
				cand.add(Character.toString(c));
			}
		}
		if (cand.isEmpty()) {
			List<String> pys = this.syllableTree.find(pinyin);
			for (String py : pys) {
				hz = this.hanziMap.get(py);
				if (hz != null) {
					for (char c : hz.toCharArray()) {
						cand.add(Character.toString(c));
					}
				}
			}
		}
		return cand;
	}

}