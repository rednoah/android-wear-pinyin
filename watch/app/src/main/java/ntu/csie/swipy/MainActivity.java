package ntu.csie.swipy;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.WindowManager;


public class MainActivity extends WearableActivity {


    private AbstractPredictiveKeyboardLayout keyboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make sure that screen doesn't turn off during user study
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // keyboard = new PinZhuYinKeyboardLayout(getApplicationContext(), PinZhuYinKeyboardLayout.Mode.PINYIN);
//        keyboard = new StandardQwerty(getApplicationContext());
        keyboard = new GrowingFinalsQwerty(getApplicationContext());
//        keyboard.setAutoComplete(new AutoComplete(getApplicationContext()));
        keyboard.addSubmitListener(this::submit);

        setContentView(keyboard);
    }


    public void submit(String s) {
        keyboard.clear();
    }


}