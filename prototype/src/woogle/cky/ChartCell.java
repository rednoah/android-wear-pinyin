package woogle.cky;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChartCell {

	public int row;
	public int column;
	public List<Path> pathList;
	public List<String> pinyin;
	public int selectedIndex;

	public ChartCell() {
		this(-1, -1, new ArrayList<Path>(), new ArrayList<String>(), -1);
	}
	
	public ChartCell(int row, int column) {
		this(row, column, new ArrayList<Path>(), new ArrayList<String>(), -1);
	}
	
	public ChartCell(int row, int column, List<Path> pathList, List<String> pinyin, int selectedIndex) {
		this.row = row;
		this.column = column;
		this.pathList = pathList;
		this.pinyin = pinyin;
		this.selectedIndex = selectedIndex;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		sb.append(row);
		sb.append(',');
		sb.append(column);
		sb.append("]\n");
		sb.append(Utils.join(";", pathList));
		return sb.toString();
	}
	
	// 增加只含有一个word的路径
	public void addPath(String word, LanguageModel lm) {
		Path path = new Path();
		pathList.add(path);
		path.word = word;
		path.score = lm.computeSentenceScore(path);
	}
	
	public void rerankPath() {
		Collections.sort(this.pathList, new Comparator<Path>() {
			@Override
			public int compare(Path arg0, Path arg1) {
				int i = Utils.compare(arg0.score, arg1.score);
				if (i == 0) {
					int len0 = arg0.getWordsLength();
					int len1 = arg1.getWordsLength();
					return Utils.compare(len0, len1);
				}
				else {
					return -i;
				}
			}
		});
		// prune
		if (this.pathList.size() > 60)
			this.pathList = this.pathList.subList(0, 60);
		Collections.sort(this.pathList, new Comparator<Path>() {
			@Override
			public int compare(Path arg0, Path arg1) {
				int i = Utils.compare(arg0.depCount, arg1.depCount);
				return -i;
			}
		});
		if (this.pathList.size() > 30)
			this.pathList = this.pathList.subList(0, 30);
	}
}