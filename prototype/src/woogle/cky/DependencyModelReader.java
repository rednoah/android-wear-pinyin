package woogle.cky;

import java.io.File;

import org.mapdb.DB;

public class DependencyModelReader {

	public static DependencyModel readDependencyModel(File file, DB db) {
		DependencyModel dm = new DependencyModel(db);

		MyFileReader reader = new MyFileReader(file);

		while (reader.hasNext()) {
			String line = reader.nextLine().trim();
			if (line.isEmpty())
				continue;
			// 工艺-供应 392
			int index = line.indexOf('\t');
			if (index != -1) {
				String w = line.substring(0, index);
				Integer i = Integer.parseInt(line.substring(index + 1));
				dm.put(w, i);
			}
			if (reader.lineno % 10000 == 0)
				System.out.println("读取行数:" + reader.lineno);
		}
		reader.close();
		return dm;
	}

	public static void main(String args[]) {
		File inputFile = new File("SogouR.mini.txt");
		DependencyModel dm = readDependencyModel(inputFile, null);

		System.out.println("风云-血战:" + dm.get("风云-血战"));

		String s[] = { "我", "是", "北京", "大学", "学生" };
		Path p = getPath(s);
		dm.computeSentenceScore(p);
		System.out.println(p.toStringWords() + ":" + p.depCount);

		String s2[] = { "卧室", "北京", "大学", "学生" };
		p = getPath(s2);
		dm.computeSentenceScore(p);
		System.out.println(p.toStringWords() + ":" + p.depCount);
	}

	private static Path getPath(String s[]) {
		Path curP = new Path();
		Path p = curP;
		for (int i = s.length - 1; i >= 0; i--) {
			p.word = s[i];
			p.history = new Path();
			p = p.history;
		}
		return curP;
	}
}
