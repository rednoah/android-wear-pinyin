package ntu.csie.swipy;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class UserStudyActivity extends MainActivity {


    public static final String RECORDER_NODE = "http://oasis1.csie.ntu.edu.tw:22148/record";
    public static final String RECORDER_SESSION = String.format("%08X", System.currentTimeMillis());

    public static final int PHRASE_COUNT = 20;


    @Override
    public MainActivity.KeyboardLayout[] getKeyboardLayoutItems() {
        return new MainActivity.KeyboardLayout[]{
                MainActivity.KeyboardLayout.GrowingFinals,
                MainActivity.KeyboardLayout.PinyinSyllables,
                MainActivity.KeyboardLayout.SwipePinyin,
                MainActivity.KeyboardLayout.StandardQwerty
        };
    }


    public static class UserStudyKeyboardFragment extends KeyboardFragment {


        AtomicInteger phraseIndex = new AtomicInteger(1);
        Recorder recorder;


        @Override
        public AbstractPredictiveKeyboardLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Recorder recorder = new Recorder(getContext(), RECORDER_NODE, RECORDER_SESSION, Build.MODEL);
            recorder.setProgress(phraseIndex);
            recorder.setEnabled(true);
            recorder.record(Symbols.START_OF_TEXT, Symbols.START_OF_TEXT);


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

            if (phraseIndex.incrementAndGet() >= PHRASE_COUNT) {
                recorder.record(Symbols.END_OF_TEXT, Symbols.END_OF_TEXT);
                recorder.setEnabled(false);

                // close fragment
                super.submit(s);
                return;
            }
        }

    }


}

