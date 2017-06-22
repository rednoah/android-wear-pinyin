package woogle.lattice;

import java.io.File;
import java.util.Collections;
import java.util.List;

import woogle.util.Score;
import woogle.util.WoogleDatabase;

public class ViterbiBeamDecoder extends ViterbiDecoder {

    int beam;

    public ViterbiBeamDecoder(Score score, int beam) {
        super(score);
        this.beam = beam;
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
                for (int j = 0; j < lastColumn.getStateSize() && j < beam; j++) {
                    LatticeState lastState = lastColumn.getState(j);
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


}
