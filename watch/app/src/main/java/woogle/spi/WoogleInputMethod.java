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


    public void sendText(String s) {
        Log.d("WoogleInputMethod", "COMMIT: " + s);
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


}
