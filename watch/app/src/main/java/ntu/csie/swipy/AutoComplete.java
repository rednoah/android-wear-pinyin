package ntu.csie.swipy;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.function.Consumer;

import woogle.spi.WoogleInputMethod;
import woogle.util.WoogleDatabase;

import static java.util.Collections.emptyList;


public class AutoComplete {


    private Context context;


    private WoogleInputMethod woogle;


    public AutoComplete(Context context) {
        this.context = context;
    }


    public synchronized Result getSuggestions(String key, InputType type, String buffer) {
        // Log.d("AutoComplete", key + " -> " + buffer);

        if (type == InputType.DELETE_LETTER) {
            if (woogle != null) {
                woogle.clear();
                try {
                    buffer.chars().forEach(c -> woogle.keyPressed((char) c));
                } catch (Exception e) {
                    // Log.d("AutoComplete", type + " -> " + buffer);
                    woogle.clear();
                }
                return new Result(woogle.getBuffer(), false, woogle.getCandidates());
            }
            return new Result(emptyList());
        }


        if (key.isEmpty() || buffer.isEmpty()) {
            if (woogle != null) {
                woogle.clear();
            }
            return new Result(emptyList());
        }


        // initialize
        if (woogle == null) {
            WoogleDatabase.load(context);
            woogle = new WoogleInputMethod();
        }


        // update prediction engine
        switch (type) {
            case ENTER_LETTER:
                key.chars().forEach(c -> woogle.keyPressed((char) c));
                break;
            case ENTER_WORD:
                if (woogle.select(key)) {
                    String s = woogle.result();
                    boolean commit = true;
                    if (s == null) {
                        s = woogle.getCompString();
                        commit = false;
                    }
                    return new Result(s, commit, woogle.getCandidates());
                }
                break;
        }


        List<String> suggestions = woogle.getCandidates();

        // Log.d("AutoComplete", "PINYIN: " + woogle.getCompString());
        // Log.d("AutoComplete", "HANZI: " + suggestions);


        return new Result(suggestions);
    }


    public void getSuggestionsAsync(String key, InputType type, String buffer, Consumer<Result> handler) {
        new AsyncTask<Void, Void, Result>() {
            @Override
            protected Result doInBackground(Void... params) {
                return getSuggestions(key, type, buffer);
            }

            @Override
            protected void onPostExecute(Result suggestions) {
                handler.accept(suggestions);
            }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }


    public static class Result {

        public final String buffer;
        public final boolean commit;


        public final List<String> candidates;


        public Result(String buffer, boolean commit, List<String> candidates) {
            this.buffer = buffer;
            this.commit = commit;
            this.candidates = candidates;
        }

        public Result(List<String> candidates) {
            this.buffer = null;
            this.commit = false;
            this.candidates = candidates;
        }


    }


}
