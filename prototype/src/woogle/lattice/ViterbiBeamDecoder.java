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

    public static void main(String args[]) {
        File file1 = new File("data/syllable.txt");
        File file2 = new File("data/syllable_chars.txt");
        File file3 = new File("data/syllable_words.txt");
        File file4 = new File("data/2000-2007_wordsegment.rmrb.small.arpa");
        File file5 = new File("data/2000-2007_wordsegment.xgram.small.arpa");

        WoogleDatabase.load(file1, file2, file3, file4, file5);
        Score score = new Score(WoogleDatabase.nGramLanguageModel,
                WoogleDatabase.dependencyLanguageModel);
        Lattice lattice = new Lattice();
        Decoder decoder = new ViterbiBeamDecoder(score, 10);

        String pinyin[] = { "wo", "sui", "ji", "ce", "shi", "pin", "yin",
                "shu", "ru", "fa" };
        lattice.buildLattice(pinyin, 0);
        lattice.decode(decoder, 0);
        List<List<String>> sentences = lattice.getNBestPath(decoder, 10);
        for (List<String> s : sentences) {
            System.out.println(s);
        }
    }
}
