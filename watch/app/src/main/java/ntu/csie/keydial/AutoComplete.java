package ntu.csie.keydial;


import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class AutoComplete {


    private Context context;


    public AutoComplete(Context context) {
        this.context = context;
    }


    public List<String> getSuggestions(String key, InputType type, String buffer) {
        return emptyList();
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


    private File prepareAssets(Context context, String... assets) {
        try {
            File assetsFolder = new File(context.getFilesDir(), "assets");
            if (!assetsFolder.exists() && !assetsFolder.mkdirs()) {
                throw new IllegalStateException("Failed to create folder: " + assetsFolder);
            }

            for (String a : assets) {
                Log.d("AutoComplete", "Prepare asset: " + a);
                FileUtils.copyInputStreamToFile(context.getAssets().open(a, AssetManager.ACCESS_STREAMING), new File(assetsFolder, a));
            }

            return assetsFolder;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


}
