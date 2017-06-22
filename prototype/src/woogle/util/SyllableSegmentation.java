package woogle.util;

import java.util.ArrayList;
import java.util.List;

import woogle.ds.SyllableDictory;
import woogle.ds.reader.SyllableDictoryReader;

public class SyllableSegmentation {

    private SyllableDictory    dic;

    private List<List<String>> results;

    private List<String>      curResult;

    private String             syllable;

    private int                resultsIndex;

    public SyllableSegmentation(SyllableDictory dic) {
        this.dic = dic;
        results = new ArrayList<List<String>>();
        resultsIndex = 0;
        curResult = new ArrayList<String>();
    }

    /**
     * 全切分，如果没有该音节，则切一个字符
     * 
     * @param syllable
     */
    public void segment(String syllable) {
        this.syllable = syllable;
        results.clear();
        resultsIndex = 0;
        segment(0);
    }

    private boolean isFilt(List<String> list) {
        // 如果有一个单独的字母，那其后，必须全为单独
        boolean hasOne = false;
        for (String s : list) {
            if (hasOne && s.length() == 1)
                return true;
            if (s.length() == 1 && !s.equals("\'"))
                hasOne = true;
        }
        return false;
    }

    private void segment(int start) {
        if (start == syllable.length()) {
            if (!isFilt(curResult))
                results.add(new ArrayList<String>(curResult));
            return;
        }
        boolean hasOne = false;
        for (int i = syllable.length(); i > start; i--) {
            String sy_can = syllable.substring(start, i);
            if (dic.contains(sy_can)) {
                hasOne = true;
                curResult.add(sy_can);
                segment(i);
                curResult.remove(curResult.size()-1);
            }
        }
        if (!hasOne) {
            String sy_can = syllable.substring(start, start + 1);
            if (dic.partialContains(sy_can)) {
            	curResult.add(sy_can);
                segment(start + 1);
                curResult.remove(curResult.size()-1);
            }
        }
    }

    public boolean hasNext() {
        return !(resultsIndex == results.size());
    }

    public List<String> next() {
        return results.get(resultsIndex++);
    }

    public List<List<String>> getResults() {
        return results;
    }
    
    public List<String> getBest() {
    	return results.get(0);
    }

    public static void main(String args[]) {
        SyllableDictory syllableDictory = new SyllableDictory();
        SyllableDictoryReader.load(syllableDictory);
        SyllableSegmentation ss = new SyllableSegmentation(syllableDictory);

        String pinyin[] = { "anhuidaxueshiwodemuxiao", "anhuidaxue",
                "woainizhonghuarenmingongheguo", "bengabengbeagabe'ga'",
                "xianshiminganxi'anshiming'an", };
        // String pinyin[] = { "anhuidaxueshiwodemuxiaobbbbbb" };
        for (String py : pinyin) {
            ss.segment(py);
            while (ss.hasNext()) {
                System.out.println(ss.next());
            }
            System.out.println();
        }
    }
}
