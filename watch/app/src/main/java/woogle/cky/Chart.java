package woogle.cky;

import java.util.ArrayList;
import java.util.List;

public class Chart {

	public ChartCell table[][];
	public int row;
	public int column;

	public Chart() {
		table = new ChartCell[10][10];
		row = 0;
		column = 0;
	}
	
	public void clear() {
		for(int j=0; j<=this.column; j++) {
			for(int i=j; i>=0; i--) {
				this.table[i][j] = null;
			}
		}
		this.row = 0;
		this.column = 0;
	}
	
	private void enlarge() {
		int size = table.length * 2;
		ChartCell tmp[][] = new ChartCell[size][size];
		for(int i=0; i<=row; i++) {
			for(int j=0; j<=column; j++) {
				tmp[i][j] = table[i][j];
			}
		}
		table = tmp;
	}
	
	public void setCell(int row, int column, ChartCell cell) {
		cell.row = row;
		cell.column = column;
		if (row >= table.length || column >= table.length)
			enlarge();
		table[row][column] = cell;
		if (row > this.row)
			this.row = row;
		if (column > this.column)
			this.column = column;
	}

	public void removeCell(int row, int column) {
		if (row <= this.row && column <= this.column)
			table[row][column] = null;
	}
	
	public ChartCell getCell(int row, int column) {
		return (row <= this.row && column <= this.column) ? 
				table[row][column] : null;
	}
	
	public Path getOneBest() {
		ChartCell c = this.getOneBestCell();
		if (c != null && !c.pathList.isEmpty()) {
			return (c.selectedIndex != -1) ?
				c.pathList.get(c.selectedIndex) :
				c.pathList.get(0);
		}
		else {
			return null;
		}
	}
	
	public ChartCell getOneBestCell() {
		int j = this.column;
		ChartCell c = table[0][j];
		while ((c == null  || c.pathList.isEmpty()) && j>0) {
			j--;
			c = table[0][j];
		}
		return c;
	}
	
	private void createCellPath(ChartCell cell, LanguageModel lm, Dictory dic) {
		if (cell == null)
			return;
		if (!cell.pathList.isEmpty())
			return;
		String pinyin = Utils.join("", cell.pinyin);
		List<String> cand = dic.find(pinyin);
		for(String s: cand) {
			cell.addPath(s, lm);
		}
		cell.rerankPath();
	}
	
	public void CKYParse(LanguageModel lm, DependencyModel dm, Dictory dic) {
		this.CKYParse(lm, dm, dic, 1, 0);
	}
	

	public void CKYParse(LanguageModel lm, DependencyModel dm, Dictory dic, 
			int startRow, int startColumn) {
		// clear since table[startRow-1, startColumn]
		for(int j=startColumn; j<=this.column; j++) {
			int i = j==startColumn ? startRow-1 : j;
			for(; i>=0; i--) {
				if (i == j && table[i][j] != null) {
					table[i][i].pathList.clear();
					table[i][i].selectedIndex = -1;
					this.createCellPath(table[i][i], lm, dic);
				}
				else {
					table[i][j] = new ChartCell(i, j);
					// add new word
					for(int k=i; k<=j; k++) {
						table[i][j].pinyin.addAll(table[k][k].pinyin);
					}
					this.createCellPath(table[i][j], lm, dic);
					if (i == 0){
						for(int k=i; k<j; k++) {
							union(table[i][j], table[i][k], table[k+1][j], lm, dm, dic);
						}
					}
					else if (table[i][j].pathList.isEmpty()){
						table[i][j] = null;
					}
				}
			}
		}
	}
	
	// �޸�table[i, i]����ƴ��
	public void CKYParse(String[] pinyin, 
			LanguageModel lm, DependencyModel dm, Dictory dic) {
		int r = this.row+1;
		int c = this.column+1;
		if (table.length <= pinyin.length) {
			this.enlarge();
		}
		for(int i=0; i<pinyin.length; i++) {
			// �����ڡ�������
			if (table[i][i] == null 
					|| !table[i][i].pinyin.get(0).equals(pinyin[i])) {
				table[i][i] = new ChartCell(i, i);
				table[i][i].pinyin.add(pinyin[i]);
				if (i <= r) {
					r = c = i;
				}
			}
			else if (i > r) {
				table[i][i].selectedIndex = -1;
			}
		}
		this.column = this.row = pinyin.length - 1;
		if (r != this.row+1) {
			// ����һ������Ϊ��ҪCKYParse����(r,c)
			this.CKYParse(lm, dm, dic, r+1, c);
		}
	}
	
	// ��[startRow, startColumn]����Լ��
	public void CKYParseWithSelect(LanguageModel lm, DependencyModel dm, Dictory dic, 
			int startRow, int startColumn) {
		// �������[0, startColumn]�����´���[0, startColumn]
		if (startRow != 0) {
			table[0][startColumn] = new ChartCell(0, startColumn);
			for(int k=0; k<=startColumn; k++) {
				table[0][startColumn].pinyin.addAll(table[k][k].pinyin);
			}
			for(int k=0; k<startColumn; k++) {
				union(table[0][startColumn], table[0][k], table[k+1][startColumn], lm, dm, dic);
			}
		}
		// �ӵ�startColumn+1�п�ʼ��ÿ�д��´�����startColumn��
		// clear since table[startRow-1, startColumn]
		for(int j=startColumn+1; j<=this.column; j++) {
			for(int i=j; i>=startColumn; i--) {
				if (i == j && table[i][j] != null) {
					table[i][i].pathList.clear();
					table[i][i].selectedIndex = -1;
					this.createCellPath(table[i][i], lm, dic);
				}
				else {
					table[i][j] = new ChartCell(i, j);
					// add new word
					for(int k=i; k<=j; k++) {
						table[i][j].pinyin.addAll(table[k][k].pinyin);
					}
					this.createCellPath(table[i][j], lm, dic);
					if (table[i][j].pathList.isEmpty()){
						table[i][j] = null;
					}
				}
			}
			table[0][j] = new ChartCell(0, j);
			for(int k=0; k<=j; k++) {
				table[0][j].pinyin.addAll(table[k][k].pinyin);
			}
			for(int k=startColumn; k<j; k++) {
				union(table[0][j], table[0][k], table[k+1][j], lm, dm, dic);
			}
		}
	}
	
	private void union(ChartCell cell, 
			ChartCell cell1, ChartCell cell2,
			LanguageModel lm, DependencyModel dm, Dictory dic) {
		if (cell1 == null || cell2 == null || cell == null)
			return;
		if (cell.row != 0)
			return;
		
		// �����Լ������ֻѡ��Լ��
		List<Path> pathList1 = new ArrayList<Path>();
		if (cell1.selectedIndex != -1)
			pathList1.add(cell1.pathList.get(cell1.selectedIndex));
		else
			pathList1 = cell1.pathList;
		
		List<Path> pathList2 = new ArrayList<Path>();
		if (cell2.selectedIndex != -1)
			pathList2.add(cell2.pathList.get(cell2.selectedIndex));
		else
			pathList2 = cell2.pathList;
		
		for(Path p1: pathList1) {
			// p2 has only word
			for(Path p2: pathList2) {
				Path p = new Path();
				p.word = p2.word;
				p.history = p1;
				if (!cell.pathList.contains(p)) {
					p.score = lm.computeSentenceScore(p);
					p.depCount = dm.computeSentenceScore(p);
					cell.pathList.add(p);
				}
			}
		}
		cell.rerankPath();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<=this.column; i++) {
			sb.append(table[0][i]);
			sb.append('\n');
		}
		return sb.toString();
	}
}