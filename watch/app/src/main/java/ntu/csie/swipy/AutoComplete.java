package ntu.csie.swipy;


import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.function.Consumer;


public abstract class AutoComplete {


    protected final Context context;


    public AutoComplete(Context context) {
        this.context = context;
    }


    public abstract Result getSuggestions(String key, InputType type, String buffer);


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
