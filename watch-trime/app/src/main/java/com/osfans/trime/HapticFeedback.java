package com.osfans.trime;


import android.content.Context;
import android.content.ContextWrapper;
import android.os.Vibrator;

public class HapticFeedback {


    Vibrator vibrator;
    boolean enabled;


    public HapticFeedback(ContextWrapper activity) {
        this.vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        this.enabled = vibrator != null && vibrator.hasVibrator();
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void feedback() {
        if (enabled) {
            vibrator.vibrate(30);
        }
    }


}

