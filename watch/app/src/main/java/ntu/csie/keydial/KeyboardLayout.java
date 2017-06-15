package ntu.csie.keydial;


import android.content.Context;

import ntu.csie.swipy.R;

public enum KeyboardLayout {


    SwipePinyin {
        @Override
        public AbstractPredictiveKeyboardLayout createView(Context context) {
            return null;
        }
    };


    public abstract AbstractPredictiveKeyboardLayout createView(Context context);


    public int getIcon() {
        return R.mipmap.ic_launcher;
    }


}
