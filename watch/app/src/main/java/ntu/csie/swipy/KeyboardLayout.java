package ntu.csie.swipy;


import android.content.Context;

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
