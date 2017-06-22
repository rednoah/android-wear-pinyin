package woogle.lattice;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import woogle.util.Score;
import woogle.util.WoogleDatabase;

public class ViterbiDecoder implements Decoder {

    Score score;

    protected static LatticeStateComparator comprator = new LatticeStateComparator();

    public ViterbiDecoder(Score score) {
        this.score = score;
    }

    @Override
    public void decode(Lattice lattice, int beginIndex) {
        assert (beginIndex >= 1);
        for (int i = beginIndex; i < lattice.getColumnSize(); i++) {
            LatticeColumn column = lattice.getColumn(i);
            for (LatticeState state : column.states) {
                int lastIndex = state.beginIndex - 1;
                if (lastIndex == -1)
                    continue;
                LatticeColumn lastColumn = lattice.getColumn(lastIndex);
                double bestScore = Double.NEGATIVE_INFINITY;
                LatticeState bestState = null;
                for (LatticeState lastState : lastColumn.states) {
                    state.ngramHistory = lastState;
                    List<String> ngram = LatticePath.getNgram(state, 3);
                    double s = score.computeNgram(ngram);
                    s += lastState.ngramScore;
                    if (s > bestScore) {
                        bestScore = s;
                        bestState = lastState;
                    }
                }
                state.ngramScore = bestScore;
                state.ngramHistory = bestState;
            }
            Collections.sort(column.states, comprator);
        }
    }

    @Override
    public void finish(Lattice lattice) {
    }

    @Override
    public List<List<String>> getNBestPath(Lattice lattice, int n) {
        LatticeColumn lastColumn = lattice.getLastColumn();
        List<List<String>> sentences = new ArrayList<List<String>>(n);
        for (int i = 0; i < n && i < lastColumn.getStateSize(); i++) {
            sentences.add(LatticePath.getSentence(lastColumn.getState(i)));
        }
        return sentences;
    }

    @Override
    public List<String> getOneBestPath(Lattice lattice) {
        LatticeColumn lastColumn = lattice.getLastColumn();
        return LatticePath.getSentence(lastColumn.getState(0));
    }

    @Override
    public void init(Lattice lattice) {
        for (LatticeColumn column : lattice.columns) {
            for (LatticeState state : column.states) {
                if (state.beginIndex == 0) {
                    String ngram[] = new String[]{state.word};
                    state.ngramScore = score.computeNgram(ngram);
                }
            }
            Collections.sort(column.states, comprator);
        }
    }


}
