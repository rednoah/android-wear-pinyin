package woogle.ds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class NGramLanguageModel {
    Node                       root;

    HashSet<String>            vocab;

    public static final double NOT_FOUND = -1000;

    private String             unk;

    private double             oovProb;

    public NGramLanguageModel() {
        root = new Node();
        vocab = new HashSet<String>();
        oovProb = -100;
        unk = "<unk>";
    }

    public void addWord(String word) {
        vocab.add(word);
    }

    public boolean hasWord(String word) {
        return vocab.contains(word);
    }

    public void add(String ngram[], double prob, double back) {
        Node n = root;
        for (String w : ngram) {
            if (n.child == null)
                n.child = new HashMap<String, Node>();
            Node c = n.child.get(w);
            if (c == null) {
                c = new Node();
                n.child.put(w, c);
            }
            n = c;
        }
        n.prob = prob;
        n.back = back;
    }

    public double getBackoff(String ngram[]) {
        Node n = root;
        for (String w : ngram) {
            if (n.child == null)
                return NOT_FOUND;
            n = n.child.get(w);
            if (n == null)
                return NOT_FOUND;
        }
        return n.back;
    }

    public double getProb(String ngram[]) {
        Node n = root;
        for (String w : ngram) {
            if (n.child == null)
                return NOT_FOUND;
            n = n.child.get(w);
            if (n == null)
                return NOT_FOUND;
        }
        return n.prob;
    }

    public void setOOVProb(double oov) {
        oovProb = oov;
    }

    public void setUNK(String unk) {
        this.unk = unk;
    }

    public double getOOVProb() {
        return oovProb;
    }

    public String getUNK() {
        return unk;
    }

    // public static double getMin() {
    // return -100;
    // }
}

class Node {
    double            prob;

    double            back;

    Map<String, Node> child;

    Node() {
        this(0, 0);
    }

    Node(double prob, double back) {
        this.prob = prob;
        this.back = back;
        this.child = null;
    }
}