package woogle.cky;

import java.io.File;
import java.util.List;

import woogle.spi.WoogleInputMethod;

public class Main {
	public static void main(String args[]) {
		Path path;
		ChartCell cell;
		
		Chart chart = new Chart();
		
		File file = new File("Æ´Òô×Öµä.txt");
		Dictory dic = new Dictory();
		dic.readHanziDic(file);
		file = new File("Æ´Òô´Êµä.txt");
		dic.readWordDic(file);
		
		File inputFile = new File("lm.arpa");
//		File inputFile = new File("try.arpa");
		LanguageModel lm = LanguageModelReader.readLanguageModel(inputFile);
		
		inputFile = new File("SogouR.mini.txt");
		DependencyModel dm = DependencyModelReader.readDependencyModel(inputFile);
		
//		String pinyin[] = {"ce", "shi", "pin", "yin", "shu", "ru"};
//		String pinyin[] = {"wo", "shi", "bei", "jing", "da", "xue", "xue", "sheng"};
//		String pinyin[] = {"shi", "beijingdaxue", "de", "xue"};
//		String pinyin[] = {"wo", "men", "neng", "gou"};
		String pinyin[] = {"xi", "an"};
		for(int i=0; i<pinyin.length; i++) {
			cell = new ChartCell();
			cell.pinyin.add(pinyin[i]);
			chart.setCell(i, i, cell);
			chart.CKYParse(lm, dm, dic, i+1, i);
//			System.out.println(chart);
//			path = chart.getOneBest();
//			System.out.println(path);
		}
//		chart.CKYParse(lm, dic, 1, 0);
		path = chart.getOneBest();
		System.out.println(path);
		System.out.println(chart);
		
//		chart.getCell(0, 1).selectedIndex = 1;
//		chart.CKYParse(lm, dic, 0, 1);
//		path = chart.getOneBest();
//		System.out.println(path);
//		System.out.println(chart);
		
//		chart.getCell(0, 7).selectedIndex = 2;
//		chart.CKYParse(lm, dic, 0, 7);
//		path = chart.getOneBest();
//		System.out.println(path);
//		System.out.println(chart);
		
//		for(int i=0; i<=0; i++) {
//			cell = chart.getCell(0);
//			System.out.println(cell);
//		}
		
		String pinyin2[] = {"xi", "an"};
		chart.CKYParse(pinyin2, lm, dm, dic);
		path = chart.getOneBest();
		System.out.println(path);
//		System.out.println(chart);
		
//		chart.getCell(0, 2).selectedIndex = 3;
//		chart.CKYParseWithSelect(lm, dic, 0, 2);
//		path = chart.getOneBest();
//		System.out.println(path);
//		System.out.println(chart);
		
//		String pinyin3[] = {"wo", "ai", "bei", "jing", "da", "x"};
//		updateChart(chart, pinyin3, lm, dic);
//		path = chart.getOneBest();
//		System.out.println(path);
//		
//		String pinyin4[] = {"wo", "ai", "bei", "jing", "da"};
//		updateChart(chart, pinyin4, lm, dic);
//		path = chart.getOneBest();
//		System.out.println(path);
		
//		System.out.println(chart);
//		System.out.println(chart.row + "," + chart.column);
//		
//		Path p = CKY.parse(chart, lm, dic, 0);
//		System.out.println(chart);
//		System.out.println(chart.row + "," + chart.column);
//		System.out.println(p);
	}
}