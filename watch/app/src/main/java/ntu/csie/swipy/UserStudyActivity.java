package ntu.csie.swipy;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.concurrent.atomic.AtomicInteger;

import woogle.util.WoogleDatabase;

public class UserStudyActivity extends MainActivity {


    public static final String RECORDER_NODE = "http://oasis1.csie.ntu.edu.tw:22148/record";
    public static final String RECORDER_SESSION = String.format("%08X", System.currentTimeMillis());

    public static final int PHRASE_COUNT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make sure that Woogle is initialized and loaded into memory
        WoogleDatabase.load(getApplicationContext());
    }


    @Override
    public MainActivity.KeyboardLayout[] getKeyboardLayoutItems() {
        return new MainActivity.KeyboardLayout[]{
                MainActivity.KeyboardLayout.GrowingFinals,
                MainActivity.KeyboardLayout.PinyinSyllables,
                MainActivity.KeyboardLayout.SwipePinyin,
                MainActivity.KeyboardLayout.StandardQwerty
        };
    }

    @Override
    public KeyboardFragment getKeyboardFragment() {
        return new UserStudyKeyboardFragment();
    }


    public static class UserStudyKeyboardFragment extends KeyboardFragment {


        AtomicInteger phraseIndex = new AtomicInteger(0);
        Recorder recorder;


        @Override
        public AbstractPredictiveKeyboardLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            recorder = new Recorder(getContext(), RECORDER_NODE, RECORDER_SESSION, Build.MODEL, getKeyboardLayout());
            recorder.setProgress(phraseIndex);
            recorder.setEnabled(true);
            recorder.record(Symbols.START_OF_TEXT, Symbols.START_OF_TEXT);

            // start with Phrase 1
            phraseIndex.incrementAndGet();

            AbstractPredictiveKeyboardLayout keyboard = super.onCreateView(inflater, container, savedInstanceState);
            keyboard.setRecorder(recorder);

            return keyboard;
        }


        @Override
        public void submit(String s) {
            if (s.isEmpty()) {
                // ignore accidental button presses
                return;
            }


            // make sure to record last input before turning of key logging
            recorder.record(Symbols.ENTER, s);

            if (phraseIndex.incrementAndGet() >= PHRASE_COUNT) {
                recorder.record(Symbols.END_OF_TEXT, Symbols.END_OF_TEXT);
                recorder.setEnabled(false);

                // close fragment
                back();
                return;
            }
        }

    }


}

