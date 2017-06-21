package woogle.spi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import woogle.cky.Chart;
import woogle.cky.ChartCell;
import woogle.cky.Path;

public class WooglePinyinHandler {
	
	boolean isFuntionDn;
	boolean isKeyPressedConsumed;
	
	WoogleInputMethod w;
	
	StringBuffer compStr;
	Display display;
//	String []result;
	List<Cand> cands;
	Chart chart;
//	int currentResultIndex;
	int currentCandPage;
	
	WooglePinyinHandler(WoogleInputMethod w) {
		this.w = w;
		
		this.isFuntionDn = false;
		this.isKeyPressedConsumed = false;

		this.compStr = new StringBuffer();
		this.display = new Display();
		this.chart = new Chart();
//		this.currentResultIndex = 0;
		this.currentCandPage = 0;
		this.cands = new ArrayList<Cand>();
	}

	public void clear() {
		this.isFuntionDn = false;
//		this.isKeyPressedConsumed = false;
		
		this.compStr = new StringBuffer();
//		this.result = null;
		this.cands.clear();
//		this.currentResultIndex = 0;
		this.currentCandPage = 0;
		this.cands.clear();
		this.display.clearAll();
	}
	
	public String getCompString() {
		return display.toString();
	}
	
	public String[] getCandString() {
		String []can = new String[5];
		int index = 5 * this.currentCandPage;
		int i = 0;
		for(i=0; i<5 && i+index<this.cands.size(); i++) {
			can[i] = (i+1) + "." + this.cands.get(i+index).getPathWords();
		}
		for(; i<5; i++)
			can[i] = (i+1) + ".  ";
		return can;
	}
	
	private void setCand() {
		this.cands.clear();
		this.currentCandPage = 0;
		HashSet<String> list = new HashSet<String>();
		ChartCell cell = this.chart.getOneBestCell();
		if (cell != null) {
			for(int i=0; list.size() <= 3 && i<cell.pathList.size(); i++) {
				Path p = cell.pathList.get(i);
				String s = p.toStringWords();
				if (!list.contains(s)) {
					this.cands.add(new Cand(i, cell));
					list.add(s);
				}
			}
		}
		// ��[currentResultIndex, currentResultIndex]����
		for(int j=this.chart.column;
			j>=this.display.currentResultIndex; 
			j--) 
		{
			cell = chart.getCell(this.display.currentResultIndex, j);
			if (cell != null) {
				for(int k=0; k<cell.pathList.size(); k++){
					Path p = cell.pathList.get(k);
					if (p.getWordsLength()==1) {
						String s = p.toStringWords();
						if (!list.contains(s)) {
							this.cands.add(new Cand(k, cell));
							list.add(s);
						}
					}
				}
			}
		}
	}
	
	class Cand {
		int pathIndex;
		ChartCell c;
		
		Cand(int pathIndex, ChartCell c) {
			this.pathIndex = pathIndex;
			this.c = c;
		}
		
		String getPathWords() {
			if (c != null) {
				return c.pathList.get(this.pathIndex).toStringWords();
			}
			else {
				return null;
			}
		}
	}
	
	class Display {
		List<String> pinyin;
		Stack<ChartCell> result;
		int currentResultIndex;
		
		Display() {
			pinyin = new ArrayList<String>();
			result = new Stack<ChartCell>();
			currentResultIndex = 0;
		}
		
		public void clearAll() {
			pinyin.clear();
			result.clear();
			currentResultIndex = 0;
		}
		
		public void clearResult() {
			result.clear();
			currentResultIndex = 0;
		}
		
		public void pushChartCell(ChartCell c) {
			result.push(c);
			currentResultIndex += c.pathList.get(c.selectedIndex)
				.toStringWords().length();
		}
		
		public boolean isFinished() {
			return (currentResultIndex == pinyin.size());
		}
		
		public boolean isEmpty() {
			return result.isEmpty();
		}
		
		public ChartCell popChartCell() {
			if (result.isEmpty()) {
				currentResultIndex = 0;
				return null;
			}
			else {
				ChartCell c = result.pop();
				currentResultIndex -= c.pathList.get(c.selectedIndex)
					.toStringWords().length();
				return c;
			}
		}
		
		public String[] getPinyin() {
			return pinyin.toArray(new String[0]);
		}
		
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			int pyIndex = 0;
			for(ChartCell c: result) {
				String word = c.pathList.get(c.selectedIndex).toStringWords();
				pyIndex += word.length();
				sb.append(word);
				sb.append('\'');
			}
			while (pyIndex < pinyin.size()) {
				sb.append(pinyin.get(pyIndex));
				if (pyIndex == pinyin.size()-1)
					sb.append('\'');
				else
					sb.append('\'');
				pyIndex ++;
			}
			return sb.toString();
		}
		
		public String toDisplayString() {
			StringBuffer sb = new StringBuffer();
			for(ChartCell c: result) {
				sb.append(c.pathList.get(c.selectedIndex).toStringWords());
			}
			for(int i=sb.length(); i<pinyin.size(); i++) {
				sb.append(pinyin.get(i));
			}
			return sb.toString();
		}
	}
}
