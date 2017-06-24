package ntu.csie.swipy;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


public class Recorder {

    private static final String TAG = "Recorder";


    private RequestQueue queue;

    private String url;
    private File localStorageFile;

    private AtomicInteger recordNumber = new AtomicInteger(0);

    private Object[] tag;

    private boolean enabled = false;
    private Object[] progress = new Object[0];


    public Recorder(Context context, String url, Object... tag) {
        this.url = url;
        this.tag = tag;

        this.queue = Volley.newRequestQueue(context);
        this.localStorageFile = getLocalStorageFile(context);
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public void setProgress(Object... progress) {
        this.progress = progress;
    }


    public void record(String key, String buffer) {
        if (!enabled) {
            return;
        }


        // record remotely and on disk
        Log.d(TAG, String.format("Record: %s [%s]", key, buffer));


        String tsv = Stream.of(
                Stream.of(now(), recordNumber.incrementAndGet()),
                Stream.of(tag),
                Stream.of(progress),
                Stream.of(key, buffer))
                .flatMap(Function.identity())
                .map(Objects::toString)
                .collect(joining("\t"));


        // add the request to the request queue
        try {
            JSONObject json = new JSONObject();
            json.put("device", Build.SERIAL);
            json.put("line", tsv);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                    r -> Log.d(TAG, String.format("OK: %s", r)),
                    e -> Log.d(TAG, String.format("%s: %s", e, url), e));

            request.setShouldCache(false);
            request.setRetryPolicy(new DefaultRetryPolicy(10000, 10, 1));
            queue.add(request);
        } catch (Exception e) {
            Log.d(TAG, "Failed to post record", e);
        }


        // store to local disk as well
        try {
            FileUtils.writeLines(localStorageFile, singleton(tsv), true);
        } catch (Exception e) {
            Log.d(TAG, "Failed to write record", e);
        }
    }


    public static String now() {
        return String.valueOf(System.currentTimeMillis());
    }


    public static File getLocalStorageFile(Context context) {
        File file = new File(context.getDataDir(), "record.tsv");
        Log.d(TAG, "Using local storage: " + file);


        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            throw new IllegalStateException("Failed to create storage folder: " + file);
        }

        return file;
    }


}
