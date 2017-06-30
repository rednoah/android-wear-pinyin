package woogle.spi;


import java.util.List;


public class WoogleInputMethod {


    WooglePinyinHandler pinyinHandler;

    WoogleState state;


    public WoogleInputMethod() {
        this.state = new WoogleState();
        this.pinyinHandler = new WooglePinyinHandler(this);
    }


    public void keyPressed(char c) {
        if (c == 'ü') {
            c = 'v';
        }
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


    public List<String> getCandidates() {
        return state.getCandidates();
    }


    public String getCompString() {
        return state.result.toCompString();
    }


    public String getBuffer() {
        return state.inputString.toString();
    }

    public boolean select(String candidate) {
        return pinyinHandler.candidateSelected(candidate);
    }


}
