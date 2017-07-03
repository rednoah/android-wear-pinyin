package ntu.csie.pinyin;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import ntu.csie.pinyin.model.Key;
import woogle.ds.reader.SyllableDictoryReader;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;


public class GrowingFinalsQwerty extends AbstractPredictiveKeyboardLayout {

    private Map<Key, Button> keys;


    public GrowingFinalsQwerty(Context context) {
        super(context, R.layout.keyboard_qwerty);


        keys = getButtons()
                .filter(b -> Key.forLetter(b.getText()) != null)
                .collect(toMap(
                        b -> Key.forLetter(b.getText()),
                        Function.identity(),
                        (a, b) -> a,
                        () -> new EnumMap<Key, Button>(Key.class)
                ));


        // tag character keys for fast access
        keys.forEach((k, b) -> {
            b.setTag(k);
        });


        setInitial();
    }


    @Override
    public void onEditorClick(View view, MotionEvent event) {
        setInitial();
    }


    @Override
    public void enterKey(Button button) {
        Key k = (Key) button.getTag();

        if (k != null) {
            keyPressed(k.getLetter(), InputType.ENTER_LETTER);
        } else {
            super.enterKey(button);
        }
    }


    @Override
    public void clear() {
        super.clear();

        setInitial();
    }


    public void setInitial() {
        // start composition here
        markHighlightStart();

        syllableKeys = EnumSet.allOf(Key.class);

        resetKeyboardKeys();
    }


    @Override
    public void enterSuggestion(String text) {
        super.enterSuggestion(text);

        setInitial();
    }

    private Set<Key> syllableKeys = EnumSet.noneOf(Key.class);


    @Override
    public void keyPressed(String key, InputType type) {
        Key letter = Key.forLetter(key);

        // control key)
        if (letter == null) {
            super.keyPressed(key, type);
            return;
        }


        // set composing text to after the entered apo
        if (letter == Key.APOSTROPHE) {
            super.keyPressed(key, type);
            setInitial();
            return;
        }


        // start new syllable
        if (!syllableKeys.contains(letter)) {
            Log.d("GrowingFinals", "ENTER: " + Key.APOSTROPHE.getLetter() + key);
            super.keyPressed(Key.APOSTROPHE.getLetter() + key, type);
            setInitial();
            return;
        }


        String head = getNextHighlightBuffer(key, type);
        updateSyllableKeys(head);

        // no more options, start next syllable
        if (syllableKeys.isEmpty() && head.length() > 1) {
            Log.d("GrowingFinals", "ENTER: " + key + Key.APOSTROPHE.getLetter());
            super.keyPressed(key + Key.APOSTROPHE.getLetter(), InputType.ENTER_LETTER);
            setInitial();
            return;
        }


        super.keyPressed(key, InputType.ENTER_LETTER);
        updateSyllableButtons(head);
    }


    private void updateSyllableKeys(String head) {
        syllableKeys.clear();
        stream(SyllableDictoryReader.SYLLABLES)
                .filter(s -> s.startsWith(head) && s.length() > head.length())
                .map(s -> s.substring(head.length(), head.length() + 1))
                .map(Key::forLetter)
                .forEach(syllableKeys::add);


        Log.d("KEYS", head + ": " + syllableKeys);
    }


    private void updateSyllableButtons(String head) {
        // grow and highlight possible next keys
        resetKeyboardKeys();


        if (syllableKeys.isEmpty()) {
            return;
        }


        keys.forEach((k, b) -> {
            setKeyEnabled(b, false);
        });

        syllableKeys.forEach(k -> {
            stream(k.getSurroundingKeys()).forEach(sk -> setSurroundingKey(sk, k));
        });

        syllableKeys.forEach(k -> {
            stream(k.getSurroundingKeysForced()).forEach(sk -> setSurroundingKey(sk, k));
        });

        // enable End of Syllable buttons
        if (stream(SyllableDictoryReader.SYLLABLES).anyMatch(s -> s.equals(head))) {
            stream(Key.APOSTROPHE.getSurroundingKeys()).forEach(sk -> setSurroundingKey(sk, Key.APOSTROPHE));
        }
    }


    private String getNextHighlightBuffer(String key, InputType type) {
        if (type == InputType.ENTER_LETTER) {
            return applyLetter(key, getHighlightBuffer()).toLowerCase();
        } else {
            return getHighlightBuffer().toLowerCase();
        }
    }


    @Override
    public void popHistory() {
        super.popHistory();

        String s = getHighlightBuffer();
        if (s.isEmpty()) {
            setInitial();
        } else {
            updateSyllableKeys(s);
            updateSyllableButtons(s);
        }
    }


    private void setKeyEnabled(Button key, boolean enabled) {
        if (enabled) {
            key.setAlpha(1.0f);
        } else {
            key.setAlpha(0.5f);
        }
    }


    private void resetKeyboardKeys() {
        keys.forEach((k, b) -> {
            b.setTag(k);
            setKeyEnabled(b, true);
            highlight(k.getLetter(), b, false);
        });
    }


    private void setSurroundingKey(Key buttonKey, Key displayKey) {
        Button b = keys.get(buttonKey);
        b.setTag(displayKey);
        setKeyEnabled(b, true);

        if (buttonKey == displayKey.getPhysicalKey(syllableKeys)) {
            highlight(displayKey.getLetter(), b, true);
        } else {
            highlight("", b, true);
        }


        switch (displayKey.getBackgroundGroup()) {
            case 0:
                b.setBackgroundResource(R.drawable.rect_button_alt_1);
                break;
            case 1:
                b.setBackgroundResource(R.drawable.rect_button_alt_2);
                break;
            case 2:
                b.setBackgroundResource(R.drawable.rect_button_alt_3);
                break;
            case 3:
                b.setBackgroundResource(R.drawable.rect_button_alt_4);
                break;
            case 4:
                b.setBackgroundResource(R.drawable.rect_button_alt_5);
                break;
            default:
                throw new IllegalStateException("Group: " + displayKey);
        }
    }


    @Override
    protected int getSuggestionRecyclerLayout() {
        return R.id.suggestion_recycler;
    }


    @Override
    protected int getSuggestionItemLayout() {
        return R.layout.item_suggestion_horizontal;
    }


    protected int[] getButtonGroups() {
        return new int[]{R.id.top_row, R.id.middle_row, R.id.bottom_row, R.id.number_row, R.id.symbol_row, R.id.punctuation_row, R.id.controls, R.id.submit};
    }

    @Override
    protected int getEditorLayout() {
        return R.id.text_editor;
    }


    @Override
    protected void highlight(String key, Button button, boolean enabled) {
        super.highlight(key, button, enabled);

        // use background highlighting as well as text highlighting
        if (enabled) {
            button.setBackgroundResource(R.drawable.qwerty_button_alt_bg);
        } else {
            button.setBackgroundResource(R.drawable.qwerty_button_bg);
        }
    }


    protected void backgroundHighlight(Button button, boolean enabled) {
        button.setBackgroundResource(enabled ? R.drawable.qwerty_button_alt_bg : R.drawable.qwerty_button_bg);
    }


}
