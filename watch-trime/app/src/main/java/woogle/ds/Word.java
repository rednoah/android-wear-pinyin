package woogle.ds;

import java.io.Serializable;

public class Word extends Pair<String, WordType> implements Serializable {

    public static final Word END = new Word("</s>", WordType.Word);

    public Word(String word, WordType type) {
        super(word, type);
    }

    public boolean isWord() {
        return second == WordType.Word;
    }

    public boolean isChar() {
        return second == WordType.Char;
    }

    public boolean isWordChar() {
        return second == WordType.WordChar;
    }

    public String getString() {
        return first;
    }
}
