package com.osfans.trime;


import android.content.Context;
import android.util.Log;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;


public class RimeAutoComplete extends AutoComplete {


    public static boolean initialized = false;


    public RimeAutoComplete(Context context) {
        super(context);
    }


    public void prepare() {
        if (!initialized) {
            Log.d("RIME", "PREPARE");
            Rime.get(false);
            Rime.setOption("_horizontal", true); //水平模式
            Log.d("RIME", "INITIALIZED");
            initialized = true;
        }
    }


    public List<String> candidates() {
        if (Rime.isComposing()) {
            return stream(Rime.getCandidates()).map(c -> c.text).collect(toList());
        }
        return emptyList();
    }


    public void pressKeys(String keys) {
        Rime.onText(keys.replace('ü', 'v'));
    }

    public boolean selectCandidate(String candidate) {
        int i = candidates().indexOf(candidate);
        if (i >= 0) {
            Rime.selectCandidate(i);
            return true;
        }
        return false;
    }


    public void clear() {
        Rime.clearComposition();
    }


    /**
     * 從Rime獲得字符串並上屏
     *
     * @return 是否成功上屏
     */
    private String getCommitText() {
        if (Rime.getCommit()) {
            String value = Rime.getCommitText();
            if (!Rime.isComposing()) {
                Rime.commitComposition(); //自動上屏
            }
            return value;
        }
        return null;
    }


    public Result getSuggestions(String key, InputType type, String buffer) {
        prepare();

        printDebug();


        if (type == InputType.DELETE_LETTER) {
            clear();
            pressKeys(buffer);

            return new Result(Rime.getCompositionText(), false, candidates());
        }


        if (key.isEmpty() || buffer.isEmpty()) {
            Rime.clearComposition();
            return new Result(emptyList());
        }


        // update prediction engine
        switch (type) {
            case ENTER_LETTER:
                pressKeys(key);
                break;
            case ENTER_WORD:
                if (selectCandidate(key)) {
                    String s = getCommitText();
                    boolean commit = true;
                    if (s == null) {
                        s = Rime.getCompositionText();
                        commit = false;
                    }
                    return new Result(s, commit, candidates());
                }
                break;
        }


        printDebug();


        return new Result(candidates());
    }


    private void printDebug() {
        Log.d("RIME", "COMPOSING: " + Rime.isComposing());
        Log.d("RIME", "COMMIT: " + Rime.getCommitText());

        Log.d("RIME", "INLINE_PREVIEW: " + Rime.getComposingText());
        Log.d("RIME", "INLINE_COMPOSITION: " + Rime.getCompositionText());
        Log.d("RIME", "INLINE_INPUT: " + Rime.RimeGetInput());

        Log.d("RIME", "INDEX: " + Rime.getCandHighlightIndex());
        Log.d("RIME", "CANDIDATES: " + stream(Rime.getCandidates()).map(c -> c.text).collect(toList()));
    }


}
