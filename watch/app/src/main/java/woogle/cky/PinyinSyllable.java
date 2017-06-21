package woogle.cky;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PinyinSyllable {
	
	private final static String[] INITIAL_ARRAY = {
		"b", 
		"c",
		"ch",
		"f",
		"g",
		"h",
		"j",
		"k",
		"l",
		"m",
		"p",
		"q", 
		"r",
		"s",
		"sh",
		"t",
		"w",
		"x",
		"y",
		"z",
		"zh"
	};
	
	private final static String[] FINAL_ARRAY = {
		"a",
		"o",
		"e",
		"ai",
		"ei",
		"ao",
		"ou",
		"an",
		"en",
		"ang",
		"eng",
		"ong",
		"ia",
		"ie",
		"iao",
		"iou",
		"iu",
		"ian",
		"in",
		"iang",
		"ing",
		"iong",
		"ua",
		"uo",
		"uai",
		"uei",
		"ui",
		"uan",
		"uen",
		"un",
		"uang",
		"ueng",
		"ung",
		"ve",
		"van",
		"vn"
	};

	private final static String[] SYLLABLE_ARRAY = {
		"a", 
		"ai", 
		"an", 
		"ang", 
		"ao", 
		"ba", 
		"bai", 
		"ban", 
		"bang", 
		"bao", 
		"bei", 
		"ben", 
		"beng", 
		"bi", 
		"bian", 
		"biao", 
		"bie", 
		"bin", 
		"bing", 
		"bo", 
		"bu",
		"ca", 
		"cai", 
		"can", 
		"cang", 
		"cao", 
		"ce", 
		"cen", 
		"ceng", 
		"cha", 
		"chai", 
		"chan", 
		"chang", 
		"chao", 
		"che", 
		"chen", 
		"cheng", 
		"chii", 
		"chong", 
		"chou", 
		"chu", 
		"chuai", 
		"chuan", 
		"chuang", 
		"chui", 
		"chun", 
		"chuo", 
		"ci", 
		"cong", 
		"cou", 
		"cu", 
		"cuan", 
		"cui", 
		"cun", 
		"cuo", 
		"da", 
		"dai", 
		"dan", 
		"dang", 
		"dao", 
		"de", 
		"dei", 
		"deng", 
		"di", 
		"dian", 
		"diao", 
		"die", 
		"ding", 
		"diu", 
		"dong", 
		"dou", 
		"du", 
		"duan", 
		"dui", 
		"dun", 
		"duo", 
		"e", 
		"en", 
		"er", 
		"fa", 
		"fan", 
		"fang", 
		"fei", 
		"fen", 
		"feng", 
		"fo", 
		"fou", 
		"fu", 
		"ga", 
		"gai", 
		"gan", 
		"gang", 
		"gao", 
		"ge", 
		"gei", 
		"gen", 
		"geng", 
		"gerer", 
		"gong", 
		"gou", 
		"gu", 
		"gua", 
		"guai", 
		"guan", 
		"guang", 
		"gui", 
		"gun", 
		"guo", 
		"ha", 
		"hai", 
		"han", 
		"hang", 
		"hao", 
		"he", 
		"hei", 
		"hen", 
		"heng", 
		"hong", 
		"hou", 
		"hu", 
		"hua", 
		"huai", 
		"huan", 
		"huang", 
		"hui", 
		"hun", 
		"huo", 
		"i", 
		"ji", 
		"jia", 
		"jian", 
		"jiang", 
		"jiao", 
		"jie", 
		"jin", 
		"jing", 
		"jiong", 
		"jiu", 
		"ju", 
		"juan", 
		"jue", 
		"jun", 
		"ka", 
		"kai", 
		"kan", 
		"kang", 
		"kao", 
		"ke", 
		"ken", 
		"keng", 
		"kong", 
		"kou", 
		"ku", 
		"kua", 
		"kuai", 
		"kuan", 
		"kuang", 
		"kui", 
		"kun", 
		"kuo", 
		"la", 
		"lai", 
		"lan", 
		"lang", 
		"lao", 
		"le", 
		"lei", 
		"leng", 
		"li", 
		"lia", 
		"lian", 
		"liang", 
		"liao", 
		"lie", 
		"lin", 
		"ling", 
		"liu", 
		"lo", 
		"long", 
		"lou", 
		"lu", 
		"luan", 
		"lun", 
		"luo", 
		"lv", 
		"lve", 
		"ma", 
		"mai", 
		"man", 
		"mang", 
		"mao", 
		"me", 
		"mei", 
		"men", 
		"meng", 
		"mi", 
		"mian", 
		"miao", 
		"mie", 
		"min", 
		"ming", 
		"miu", 
		"mo", 
		"mou", 
		"mu", 
		"na", 
		"nai", 
		"nan", 
		"nang", 
		"nao", 
		"ne", 
		"nei", 
		"nen", 
		"neng", 
		"ni", 
		"nian", 
		"niang", 
		"niao", 
		"nie", 
		"nin", 
		"ning", 
		"niu", 
		"nong", 
		"nu", 
		"nuan", 
		"nuo", 
		"nv", 
		"nve", 
		"o", 
		"ou", 
		"pa", 
		"pai", 
		"pan", 
		"pang", 
		"pao", 
		"pei", 
		"pen", 
		"peng", 
		"pi", 
		"pian", 
		"piao", 
		"pie", 
		"pin", 
		"ping", 
		"po", 
		"pou", 
		"pu", 
		"qi", 
		"qia", 
		"qian", 
		"qiang", 
		"qiao", 
		"qie", 
		"qin", 
		"qing", 
		"qiong", 
		"qiu",  
		"qu", 
		"quan", 
		"que", 
		"qun", 
		"ran", 
		"rang", 
		"rao", 
		"re", 
		"ren", 
		"reng", 
		"ri", 
		"rong", 
		"rou", 
		"ru", 
		"ruan", 
		"rui", 
		"run", 
		"ruo", 
		"sa", 
		"sai", 
		"san", 
		"sang", 
		"sao", 
		"se", 
		"sen", 
		"seng", 
		"sha", 
		"shai", 
		"shan", 
		"shang", 
		"shao", 
		"she", 
		"shen", 
		"sheng", 
		"shi", 
		"shou", 
		"shu", 
		"shua", 
		"shuai", 
		"shuan", 
		"shuang", 
		"shui", 
		"shun", 
		"shuo", 
		"si", 
		"song", 
		"sou", 
		"su", 
		"suan", 
		"sui", 
		"sun", 
		"suo", 
		"ta", 
		"tai", 
		"tan", 
		"tang", 
		"tao", 
		"te", 
		"teng", 
		"ti", 
		"tian", 
		"tiao", 
		"tie", 
		"ting", 
		"tong", 
		"tou", 
		"tu", 
		"tuan", 
		"tui", 
		"tun", 
		"tuo", 
		"wa", 
		"wai", 
		"wan", 
		"wang", 
		"wei", 
		"wen", 
		"weng", 
		"wo", 
		"wu", 
		"xi", 
		"xia", 
		"xian", 
		"xiang", 
		"xiao", 
		"xie", 
		"xin", 
		"xing", 
		"xiong", 
		"xiu", 
		"xu", 
		"xuan", 
		"xue", 
		"xun", 
		"ya", 
		"yan", 
		"yang", 
		"yao", 
		"ye", 
		"yi", 
		"yin", 
		"ying", 
		"yo", 
		"yong", 
		"you",  
		"yu", 
		"yuan", 
		"yue", 
		"yun", 
		"za", 
		"zai", 
		"zan", 
		"zang", 
		"zao", 
		"ze", 
		"zei", 
		"zen", 
		"zeng", 
		"zha", 
		"zhai", 
		"zhan", 
		"zhang", 
		"zhao", 
		"zhe", 
		"zhei", 
		"zhen", 
		"zheng", 
		"zhi", 
		"zhong", 
		"zhou", 
		"zhu", 
		"zhua", 
		"zhuai", 
		"zhuan", 
		"zhuang", 
		"zhui", 
		"zhun", 
		"zhuo", 
		"zi", 
		"zong", 
		"zou", 
		"zu", 
		"zuan", 
		"zui", 
		"zun", 
		"zuo", 
	};
	
	public final static HashSet<String> INITIAL = toHashSet(INITIAL_ARRAY);
	public final static HashSet<String> FINAL = toHashSet(FINAL_ARRAY);
	public final static HashSet<String> SYLLABLE = toHashSet(SYLLABLE_ARRAY);
	
	public static HashSet<String> toHashSet(String s[]) {
		HashSet<String> hashSet = new HashSet<String>(s.length);
		for(String i: s) {
			hashSet.add(i);
		}
		return hashSet;
	}
	
	public static boolean isInitial(String initial) {
		return INITIAL.contains(initial);
	}
	
	public static boolean isPartialSyllbale(String syllable) {
		for(String s: SYLLABLE){
			if (s.startsWith(syllable))
				return true;
		}
		return false;
	}
	
	public static boolean isSyllbale(String syllable) {
		return SYLLABLE.contains(syllable);
	}
	
	private static void error(List<String> list, char py[], int start, int end){
		while(start != end) {
			list.add(Character.toString(py[start]));
			start ++;
		}
	}
	
	public static List<String> split(String pinyin) {
		List<String> list = new ArrayList<String>();
		char py[] = pinyin.toCharArray();
		int start = 0;
		for(int i=0; i<py.length; i++) {
			char c = py[i];
			// 2
			if ((c=='i' || c=='u' || c=='v') && (i-start)==0){
				list.add(pinyin.substring(start, i+1));
				start = i+1;
			}
			// 3
			else if (isPartialSyllbale(pinyin.substring(start, i+1))){
			}
			// 4
			else if (isInitial(Character.toString(c)) || c=='\''){
				// 5
				if (isSyllbale(pinyin.substring(start, i))){
					list.add(pinyin.substring(start, i));
				}
				else { // 12
					error(list, py, start, i);
				}
				if (c == '\'')
					i++;
				start = i;
			}
			// 6
			else if (i>=1 && (py[i-1]=='g' || py[i-1]=='n' || py[i-1]=='r')) {
				// 7
				if (isSyllbale(pinyin.substring(start, i-1))){
					// 8
					if (isPartialSyllbale(pinyin.substring(i-1, i+1))) {
						list.add(pinyin.substring(start, i-1));
						start = i-1;
					}
					else if (isSyllbale(pinyin.substring(start, i))){
						list.add(pinyin.substring(start, i));
						start = i;						
					}
					else { // 12
						error(list, py, start, i);
						start = i;
					}
				}
				// 10
				else if (isSyllbale(pinyin.substring(start, i))){
					list.add(pinyin.substring(start, i));
					start = i;						
				}
				else { // 12
					error(list, py, start, i);
					start = i;
				}
			}
			// 11
			else if (isSyllbale(pinyin.substring(start, i))){
				list.add(pinyin.substring(start, i));
				start = i;						
			}
			else {
				// 12
				error(list, py, start, i);
				start = i;
			}
		}
		list.add(pinyin.substring(start));
		return list;
	}
	
	public static void main(String args[]) {
		String pinyin = "";
		List<String> pinyins = PinyinSyllable.split(pinyin);
		System.out.println(pinyin);
		print(pinyins);
		
		pinyin = "anhuidaxueshiwodemuxiao";
		pinyins = PinyinSyllable.split(pinyin);
		System.out.println(pinyin);
		print(pinyins);
		
		pinyin = "anhuidaxue";
		pinyins = PinyinSyllable.split(pinyin);
		System.out.println(pinyin);
		print(pinyins);
		
		pinyin = "woainizhonghuarenmingongheguo";
		pinyins = PinyinSyllable.split(pinyin);
		System.out.println(pinyin);
		print(pinyins);
		
		pinyin = "bengabengbeagabe'ga'";
		pinyins = PinyinSyllable.split(pinyin);
		System.out.println(pinyin);
		print(pinyins);
		
		pinyin = "xianshiminganxi'anshiming'an";
		pinyins = PinyinSyllable.split(pinyin);
		System.out.println(pinyin);
		print(pinyins);
	}
	
	private static void print(List<String> ss) {
		for(String s: ss) {
			System.out.print(s + " ");
		}
		System.out.println('\n');
	}
}
