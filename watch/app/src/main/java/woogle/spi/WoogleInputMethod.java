package woogle.spi;


import android.util.Log;

import java.util.List;


public class WoogleInputMethod {


    WooglePinyinHandler pinyinHandler;

    WoogleState state;


    public WoogleInputMethod() {
        this.state = new WoogleState();
        this.pinyinHandler = new WooglePinyinHandler(this);
    }


    public void keyPressed(char c) {
        this.pinyinHandler.keyPressed(c);
    }


    public void backspace() {
        this.pinyinHandler.backspaceAction();
    }


    String commit;


    public void sendText(String s) {
        commit = s;
    }


    public String result() {
        String s = commit;
        commit = null;
        return s;
    }


    public void clear() {
        pinyinHandler.clear();
    }


    public List<String> getCandString() {
        return state.getCandString(5);
    }


    public String getCompString() {
        return state.result.toCompString();
    }


    public String getBuffer() {
        return state.inputString.toString();
    }

    public boolean select(String candidate) {
        List<String> cands = getCandString();
        int i = cands.indexOf(candidate);
        if (i >= 0) {
            pinyinHandler.candidatePressed(i + 1);
            return true;
        }

        Log.d("WoogleInputMethod", "Illegal candidate: " + candidate + " -> " + cands);
        return false;
    }


}
