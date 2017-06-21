package woogle.spi;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import woogle.cky.Chart;
import woogle.cky.ChartCell;
import woogle.cky.Path;
import woogle.cky.PinyinSyllable;

import static java.util.Arrays.asList;

public class Main {

    public static void test(Context context) {
        Main p = new Main(context);
        p.clear();

        System.out.println(Runtime.getRuntime().totalMemory());

        p.lowerLetterAction('b');
        p.lowerLetterAction('e');
        p.lowerLetterAction('i');
        p.lowerLetterAction('j');
        p.lowerLetterAction('i');
        p.lowerLetterAction('n');
        p.lowerLetterAction('g');

        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        p.clear();
        "womingtianyaoxiezuoyele".chars().forEach(c -> p.lowerLetterAction((char) c));
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        p.clear();
        "wo'ming'tian'yao'xie'zuo'ye'le".chars().forEach(c -> p.lowerLetterAction((char) c));
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        p.clear();
        "wo'ming't".chars().forEach(c -> p.lowerLetterAction((char) c));
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        p.clear();
        "wo'ming".chars().forEach(c -> p.lowerLetterAction((char) c));
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        p.clear();
        "wo'min".chars().forEach(c -> p.lowerLetterAction((char) c));
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        p.clear();
        "wo'mi".chars().forEach(c -> p.lowerLetterAction((char) c));
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        p.clear();
        "wo'm".chars().forEach(c -> p.lowerLetterAction((char) c));
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        System.out.println("\n\n--------------- INTERACTIVE --------------------\n\n");


        p.clear();
        "wo'ming'tian'yao'xie'zuo'ye'le".chars().forEach(c -> p.lowerLetterAction((char) c));

        p.digitAction(5);
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        p.digitAction(5);
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        p.upAction();
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        p.digitAction(2);
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        p.upAction();
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        p.digitAction(2);
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

        p.digitAction(5);
        System.out.println(p.getCompString());
        System.out.println(asList(p.getCandString()));

    }

    boolean isFuntionDn;
    boolean isKeyPressedConsumed;


    WoogleInputMethod w;

    StringBuffer compStr;
    Display display;
    // String []result;
    List<Cand> cands;
    Chart chart;
    // int currentResultIndex;
    int currentCandPage;

    public Main(Context context) {
        w = new WoogleInputMethod(context);
        w.setLocale(Locale.SIMPLIFIED_CHINESE);

        this.isFuntionDn = false;
        this.isKeyPressedConsumed = false;

        this.compStr = new StringBuffer();
        this.display = new Display();
        this.chart = new Chart();
        // this.currentResultIndex = 0;
        this.currentCandPage = 0;
        this.cands = new ArrayList<Cand>();
    }


    public void upAction() {
        if (w.getLocale().equals(Locale.SIMPLIFIED_CHINESE) && this.compStr.length() != 0) {
            if ((this.currentCandPage + 1) * 5 <= this.cands.size())
                this.currentCandPage++;
            this.isKeyPressedConsumed = true;
        }
    }


    public void downAction() {
        if (w.getLocale().equals(Locale.SIMPLIFIED_CHINESE) && this.compStr.length() != 0) {
            if (this.currentCandPage > 0)
                this.currentCandPage--;
            this.isKeyPressedConsumed = true;
        }
    }


    public void digitAction(int digit) {
        if (w.getLocale().equals(Locale.SIMPLIFIED_CHINESE) && this.compStr.length() != 0) {
            if (1 <= digit && digit <= 5) {
                Cand c = this.cands.get(this.currentCandPage * 5 + digit - 1);
                c.c.selectedIndex = c.pathIndex;
                if (this.currentCandPage == 0 && c.c.row == 0 && c.c.column == this.chart.column) {
                    this.display.clearResult();
                }
                this.display.pushChartCell(c.c);
                if (this.display.isFinished()) {
                    w.sendText(this.display.toDisplayString());
                    this.clear();
                } else {
                    this.chart.CKYParseWithSelect(w.Lm, w.Dm, w.Dic, c.c.row, c.c.column);
                    this.setCand();
                }
            }
            this.isKeyPressedConsumed = true;
        }
    }


    public void lowerLetterAction(char c) {
        this.compStr.append(c);
        this.display.pinyin = PinyinSyllable.split(this.compStr.toString());
        this.chart.CKYParse(this.display.getPinyin(), w.Lm, w.Dm, w.Dic);
        this.setCand();
        this.isKeyPressedConsumed = true;
    }


    public void clear() {
        System.out.println("------------------------------");
        this.isFuntionDn = false;
        // this.isKeyPressedConsumed = false;

        this.compStr = new StringBuffer();
        // this.result = null;
        this.cands.clear();
        // this.currentResultIndex = 0;
        this.currentCandPage = 0;
        this.cands.clear();
        this.display.clearAll();
    }

    public String getCompString() {
        return display.toString();
    }

    public String[] getCandString() {
        String[] can = new String[5];
        int index = 5 * this.currentCandPage;
        int i = 0;
        for (i = 0; i < 5 && i + index < this.cands.size(); i++) {
            can[i] = (i + 1) + "." + this.cands.get(i + index).getPathWords();
        }
        for (; i < 5; i++)
            can[i] = (i + 1) + ".  ";
        return can;
    }

    private void setCand() {
        this.cands.clear();
        this.currentCandPage = 0;
        HashSet<String> list = new HashSet<String>();
        ChartCell cell = this.chart.getOneBestCell();
        if (cell != null) {
            for (int i = 0; list.size() <= 3 && i < cell.pathList.size(); i++) {
                Path p = cell.pathList.get(i);
                String s = p.toStringWords();
                if (!list.contains(s)) {
                    this.cands.add(new Cand(i, cell));
                    list.add(s);
                }
            }
        }

        for (int j = this.chart.column; j >= this.display.currentResultIndex; j--) {
            cell = chart.getCell(this.display.currentResultIndex, j);
            if (cell != null) {
                for (int k = 0; k < cell.pathList.size(); k++) {
                    Path p = cell.pathList.get(k);
                    if (p.getWordsLength() == 1) {
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
            } else {
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
            currentResultIndex += c.pathList.get(c.selectedIndex).toStringWords().length();
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
            } else {
                ChartCell c = result.pop();
                currentResultIndex -= c.pathList.get(c.selectedIndex).toStringWords().length();
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
            for (ChartCell c : result) {
                String word = c.pathList.get(c.selectedIndex).toStringWords();
                pyIndex += word.length();
                sb.append(word);
                sb.append('\'');
            }
            while (pyIndex < pinyin.size()) {
                sb.append(pinyin.get(pyIndex));
                if (pyIndex == pinyin.size() - 1)
                    sb.append('\'');
                else
                    sb.append('\'');
                pyIndex++;
            }
            return sb.toString();
        }

        public String toDisplayString() {
            StringBuffer sb = new StringBuffer();
            for (ChartCell c : result) {
                sb.append(c.pathList.get(c.selectedIndex).toStringWords());
            }
            for (int i = sb.length(); i < pinyin.size(); i++) {
                sb.append(pinyin.get(i));
            }
            return sb.toString();
        }
    }
}
