package woogle.lattice;

import java.util.ArrayList;
import java.util.List;

import woogle.ds.Word;
import woogle.util.WoogleDatabase;

public class Lattice {
    List<LatticeColumn> columns;

    public Lattice() {
        columns = new ArrayList<LatticeColumn>();
    }

    private void addChineseWord(LatticeColumn column, String pinyin[],
            int beginIndex, int endIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = beginIndex; i < endIndex; i++) {
            sb.append(pinyin[i]);
        }
        List<Word> words = WoogleDatabase.findCandidates(sb.toString());
        for (Word word : words) {
            LatticeState state = new LatticeState(beginIndex, endIndex, word.first);
            column.states.add(state);
        }
    }

    public void buildLattice(String pinyin[], int beginIndex) {
        columns = columns.subList(0, beginIndex);
        for (int i = beginIndex; i < pinyin.length; i++) {
            LatticeColumn column = new LatticeColumn();
            // add char
            addChineseWord(column, pinyin, i, i + 1);
            // add word
            for (int j = i - 1; j >= 0 && j >= i - 4; j--) {
                addChineseWord(column, pinyin, j, i + 1);
            }
            columns.add(column);
        }
    }

    public void decode(Decoder decoder, int beginIndex) {
        if (beginIndex == 0) {
            decoder.init(this);
            beginIndex++;
        }
        decoder.decode(this, beginIndex);
        decoder.finish(this);
    }

    public LatticeColumn getColumn(int index) {
        return columns.get(index);
    }

    public LatticeColumn getLastColumn() {
        return getColumn(columns.size() - 1);
    }

    public int getColumnSize() {
        return columns.size();
    }
    
    public List<List<String>> getNBestPath(Decoder decoder, int n) {
        return decoder.getNBestPath(this, n);
    }
    
    public List<String> getOneBestPath(Decoder decoder) {
        return decoder.getOneBestPath(this);
    }
}
