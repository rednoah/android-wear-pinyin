package ntu.csie.swipy;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Collections;
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


    public synchronized List<String> getSuggestions(String key, InputType type, String buffer) {
        if (key.isEmpty() || buffer.isEmpty()) {
            woogle.clear();
            return emptyList();
        }


        // initialize
        if (woogle == null) {
            WoogleDatabase.load(context);
            woogle = new WoogleInputMethod();
        }


        // update prediction engine
        key.chars().forEach(c -> woogle.keyPressed((char) c));


        List<String> suggestions = woogle.getCandString();

        Log.d("AutoComplete", "PINYIN: " + woogle.getCompString());
        Log.d("AutoComplete", "HANZI: " + suggestions);

        return suggestions;
    }


    public void getSuggestionsAsync(String key, InputType type, String buffer, Consumer<List<String>> handler) {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                return getSuggestions(key, type, buffer);
            }

            @Override
            protected void onPostExecute(List<String> suggestions) {
                handler.accept(suggestions);
            }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }


}
