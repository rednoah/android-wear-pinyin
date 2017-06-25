package ntu.csie.swipy;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ntu.csie.swipy.model.Key;
import woogle.ds.reader.SyllableDictoryReader;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;


public class GrowingFinalsQwerty extends AbstractPredictiveKeyboardLayout {

    private Map<Key, Button> keys;


    public GrowingFinalsQwerty(Context context) {
        super(context, R.layout.keyboard_qwerty);


        List<Button> buttons = IntStream.of(getButtonGroups())
                .mapToObj(this.layout::findViewById)
                .flatMap(v -> {
                    if (v instanceof ViewGroup) {
                        ViewGroup g = (ViewGroup) v;
                        return IntStream.range(0, g.getChildCount()).mapToObj(g::getChildAt);
                    }
                    if (v instanceof Button) {
                        return Stream.of(v);
                    }
                    return Stream.empty();
                })
                .map(Button.class::cast).collect(toList());


        keys = buttons.stream()
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


        // hook up keyboard listeners
        for (Button key : buttons) {
            key.setOnClickListener(v -> enterKey(key));
        }


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
        highlightStart = buffer.length();

        syllableKeys = EnumSet.allOf(Key.class);

        resetKeyboardKeys();

        // update composition highlight
        updateTextBuffer(buffer);
    }


    private Set<Key> syllableKeys = EnumSet.noneOf(Key.class);


    @Override
    public void keyPressed(String key, InputType type) {
        Key typedKey = Key.forLetter(key);

        // control key
        if (typedKey == null) {
            super.keyPressed(key, type);
            return;
        }

        // start new syllable
        if (!syllableKeys.contains(typedKey)) {
            setInitial();
        }

        // drill down pinyin syllable
        super.keyPressed(key, type);


        // set composing text to after the entered apo
        if (typedKey == Key.APOSTROPHE) {
            setInitial();
            return;
        }


        String head = buffer.substring(highlightStart).toLowerCase();

        syllableKeys.clear();
        stream(SyllableDictoryReader.SYLLABLES)
                .filter(s -> s.startsWith(head) && s.length() > head.length())
                .map(s -> s.substring(head.length(), head.length() + 1))
                .map(Key::forLetter)
                .forEach(syllableKeys::add);


        Log.d("KEYS", head + ": " + syllableKeys);


        // no more options, start next syllable
        if (syllableKeys.isEmpty()) {
            updateTextBuffer(applyLetter(Key.APOSTROPHE.getLetter(), buffer));
            setInitial();
            return;
        }


        // grow and highlight possible next keys
        resetKeyboardKeys();

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


    private void resetKeyboardKeys() {
        keys.forEach((k, b) -> {
            b.setTag(k);
            highlight(k.getLetter(), b, false);
        });
    }


    private void setSurroundingKey(Key buttonKey, Key displayKey) {
        Button b = keys.get(buttonKey);
        b.setTag(displayKey);

        if (buttonKey == displayKey || (displayKey == Key.APOSTROPHE && buttonKey == Key.getPhysicalApostropheKey())) {
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
