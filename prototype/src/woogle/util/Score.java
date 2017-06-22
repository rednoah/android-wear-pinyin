package woogle.util;

import java.io.File;
import java.util.List;

import woogle.ds.DependencyLanguageModel;
import woogle.ds.NGramLanguageModel;
import woogle.ds.PathNode;
import woogle.ds.reader.DependencyLanguageModelReader;
import woogle.ds.reader.NGramLanguageModelReader;

public class Score {
    NGramLanguageModel      ngramLM;

    DependencyLanguageModel dependencyLM;

    public Score(NGramLanguageModel ngramLM,
            DependencyLanguageModel dependencyLM) {
        this.ngramLM = ngramLM;
        this.dependencyLM = dependencyLM;
    }

    public double computeNgram(String ngram[]) {
        // for (int i = 0; i < ngram.length; i++) {
        // if (!ngramLM.hasWord(ngram[i]))
        // ngram[i] = ngramLM.getUNK();
        // }

        double score = ngramLM.getProb(ngram);
        if (score != NGramLanguageModel.NOT_FOUND)
            return score;
        if (ngram.length == 1)
            return ngramLM.getOOVProb();
        String history[] = new String[ngram.length - 1];
        for (int i = 0; i < history.length; i++)
            history[i] = ngram[i];
        String backoff[] = new String[ngram.length - 1];
        for (int i = 0; i < backoff.length; i++)
            backoff[i] = ngram[i + 1];
        double historyBackoffScore = ngramLM.getBackoff(history);
        score = computeNgram(backoff);
        if (historyBackoffScore != NGramLanguageModel.NOT_FOUND)
            return score + historyBackoffScore;
        else
            return score;
    }

    public double computeNgram(List<String> ngram) {
        return computeNgram(ngram.toArray(new String[0]));
    }

    public double computeDependency(List<String> deplink) {
        return computeDependency(deplink.toArray(new String[0]));
    }

    public double computeDependency(String deplink[]) {
//        for (int i = 0; i < deplink.length; i++) {
//            if (!dependencyLM.hasWord(deplink[i]))
//                deplink[i] = dependencyLM.getUNK();
//        }

        double score = dependencyLM.getProb(deplink);
        if (score != DependencyLanguageModel.NOT_FOUND)
            return score;
        if (deplink.length == 1)
            return dependencyLM.getOOVProb();
        String history[] = new String[deplink.length - 1];
        for (int i = 0; i < history.length; i++)
            history[i] = deplink[i];
        String backoff[] = new String[deplink.length - 1];
        for (int i = 0; i < backoff.length; i++)
            backoff[i] = deplink[i + 1];
        double historyBackoffScore = dependencyLM.getBackoff(history);
        score = computeDependency(backoff);
        if (historyBackoffScore != DependencyLanguageModel.NOT_FOUND)
            return score + historyBackoffScore;
        else
            return score;
    }

    public static void main(String args[]) {

        File file = new File("data/Hub4-2000-2007.0.551126.arpa");
        NGramLanguageModel nlm = new NGramLanguageModel();
        NGramLanguageModelReader.load(file, nlm);

        file = new File("data/2000-2007.dgram_0_30_40.arpa");
        DependencyLanguageModel dlm = new DependencyLanguageModel();
        DependencyLanguageModelReader.load(file, dlm);

        Score score = new Score(nlm, dlm);

        String s[] = { "我", "是", "北京", "大学", "学生" };
        double d = score.computeNgram(s);
        System.out.println(d);
    }

}
