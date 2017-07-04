package com.osfans.trime;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class RimeTest extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int i = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.d("RIME", "requestPermissions: " + i);

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            Log.d("RIME", "requestPermissions");
        }


        Rime.get(false);

        Rime.onText("taiwandiyi");
        printState();

        Rime.selectCandidate(0);
        printState();

        Rime.getCommit();
        String hanzi = Rime.getCommitText();


        Log.d("RIME", "COMMIT: " + hanzi);


        TextView b = new TextView(this);
        b.setText(hanzi);
        b.setGravity(Gravity.CENTER);
        b.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        b.setTextColor(Color.WHITE);
        b.setBackgroundColor(Color.BLACK);
        b.setTextSize(24f);


        setContentView(b);


        Log.d("RIME", "READY");
    }


    private void printState() {
        Log.d("RIME", "COMPOSING: " + Rime.isComposing());
        Log.d("RIME", "COMMIT: " + Rime.getCommitText());
        Log.d("RIME", "INLINE_PREVIEW: " + Rime.getComposingText());
        Log.d("RIME", "INLINE_COMPOSITION: " + Rime.getCompositionText());
        Log.d("RIME", "INLINE_INPUT: " + Rime.RimeGetInput());

        Log.d("CandHighlightIndex", "" + Rime.getCandHighlightIndex());

        if (Rime.getCandHighlightIndex() >= 0) {
            for (Rime.RimeCandidate c : Rime.getCandidates()) {
                Log.d("CAND", c.text + " :: " + c.comment);
            }
        }
    }


}
