package ntu.csie.keydial;


import android.content.Context;
import android.graphics.Typeface;
import android.support.wearable.view.BoxInsetLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ntu.csie.swipy.R;

import static java.util.stream.Collectors.toMap;

public abstract class AbstractKeyboardLayout extends BoxInsetLayout {


    protected View layout;
    protected TextView editor;

    protected TreeMap<String, Button> keys;

    protected String buffer;
    protected int highlightColor;


    protected LetterCase letterCase = LetterCase.UPPER;
    protected Mode mode = Mode.LETTERS;


    public AbstractKeyboardLayout(Context context, int layout) {
        super(context);

        this.layout = LayoutInflater.from(context).inflate(layout, this, true);
        this.editor = (TextView) this.layout.findViewById(getEditorLayout());

        this.keys = IntStream.of(getButtonGroups())
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
                .map(Button.class::cast)
                .collect(toMap(
                        k -> k.getText().toString(),
                        Function.identity(),
                        (a, b) -> a,
                        () -> new TreeMap(String.CASE_INSENSITIVE_ORDER)
                ));


        this.highlightColor = getResources().getColor(R.color.editor_highlight_fg, getContext().getTheme());

        // init text buffer
        updateTextBuffer("");

        // hook up editor touch input for SPACE and BACKSPACE
        editor.setOnTouchListener((v, evt) -> {
            // listen only for button press events
            if (evt.getAction() == MotionEvent.ACTION_DOWN) {
                onEditorClick(v, evt);
                return true;
            }
            return false;
        });

        // hook up keyboard listeners
        for (Button key : keys.values()) {
            key.setOnClickListener(v -> enterKey(key));
        }
    }


    protected abstract int[] getButtonGroups();

    protected abstract int getEditorLayout();

    protected abstract int getModeLayout(Mode mode);


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            // log hardware key events
            if (recorder != null) {
                recorder.record(KeyEvent.keyCodeToString(keyCode), buffer);
            }

            if (keyCode == KeyEvent.KEYCODE_STEM_1) {
                keyPressed(Symbols.KEYBOARD, InputType.CONTROL_KEY);
            } else if (keyCode == KeyEvent.KEYCODE_STEM_2) {
                keyPressed(Symbols.OPTION, InputType.CONTROL_KEY);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    protected String mapKey(String key) {
        switch (mode) {
            case LETTERS:
                switch (letterCase) {
                    case UPPER:
                        return key.toUpperCase();
                    case LOWER:
                        return key.toLowerCase();
                }
            case NUMBERS_AND_PUNCTUATION:
                switch (letterCase) {
                    case UPPER:
                        return key;
                    case LOWER:
                        return Emoji.mapPunctuation(key);
                }
        }
        return key;
    }

    public void onEditorClick(View view, MotionEvent event) {
        if (event.getX() < view.getWidth() / 2) {
            enterBackspace(); // click on the left for BACKSPACE / DELETE
        } else {
            enterSpace(); // click on the right to add SPACE
        }
    }

    public void enterKey(Button button) {
        String key = button.getText().toString();

        switch (key) {
            case Symbols.ENTER:
            case Symbols.KEYBOARD:
            case Symbols.OPTION:
                keyPressed(key, InputType.CONTROL_KEY);
                break;
            default:
                keyPressed(mapKey(key), InputType.ENTER_LETTER);
                break;
        }
    }


    public void enterSuggestion(String text) {
        keyPressed(text, InputType.ENTER_WORD);
    }


    public void enterBackspace() {
        if (buffer.isEmpty()) {
            return;
        }
        keyPressed(Symbols.BACKSPACE, InputType.DELETE_LETTER);
    }

    public void enterSpace() {
        if (buffer.isEmpty() || buffer.endsWith(WORD_SEPARATOR)) {
            return;
        }
        keyPressed(WORD_SEPARATOR, InputType.ENTER_LETTER);
    }


    public void keyPressed(String key, InputType type) {
        switch (type) {
            case DELETE_LETTER:
                updateTextBuffer(applyDelete(buffer));
                break;
            case ENTER_LETTER:
                updateTextBuffer(applyLetter(key, buffer));
                break;
            case ENTER_WORD:
                updateTextBuffer(applyWord(key, buffer));
                break;
            case CONTROL_KEY:
                switch (key) {
                    case Symbols.ENTER:
                        post(this::submit);
                        break;
                    case Symbols.KEYBOARD:
                        setMode(mode.shift());
                        break;
                    case Symbols.OPTION:
                        setLetterCase(letterCase.shift());
                        break;
                }

                break;
        }


        if (hapticFeedback != null) {
            hapticFeedback.feedback();
        }

        if (recorder != null) {
            recorder.record(key, buffer);
        }
    }


    protected String applyDelete(String buffer) {
        return buffer.isEmpty() ? buffer : Emoji.deleteLastCodePoint(buffer); // be aware of multi-byte emoji
    }


    protected String applyLetter(String key, String buffer) {
        return buffer + key;
    }


    public static final String WORD_SEPARATOR = " ";

    protected String applyWord(String word, String buffer) {
        int i = buffer.lastIndexOf(WORD_SEPARATOR);
        if (i > 0) {
            return buffer.substring(0, i) + WORD_SEPARATOR + word + WORD_SEPARATOR;
        }

        return word + WORD_SEPARATOR;
    }


    protected void updateTextBuffer(String buffer) {
        this.buffer = buffer;

        int i = buffer.trim().lastIndexOf(WORD_SEPARATOR);
        if (i < 0) {
            editor.setText(highlightTail(buffer, 0)); // highlight entire word
        } else {
            editor.setText(highlightTail(buffer, i + 1)); // highlight last word
        }
    }


    protected Spanned highlightTail(String text, int from) {
        // draw caret to visualize a trailing space
        if (text.endsWith(WORD_SEPARATOR)) {
            return highlightTail(text + Symbols.CARET, from);
        }

        SpannableString span = new SpannableString(text);
        span.setSpan(new ForegroundColorSpan(highlightColor), from, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    protected Spanned highlightKey(String key) {
        SpannableString span = new SpannableString(key);
        span.setSpan(new StyleSpan(Typeface.BOLD), 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(highlightColor), 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }


    protected Map<String, Button> getLetterKeys() {
        return keys.subMap("!", true, "Z", true);
    }


    protected void highlight(String key, Button button, boolean enabled) {
        if (enabled) {
            button.setText(highlightKey(key));
        } else {
            button.setText(key);
        }
    }


    protected String getLastWord() {
        int i = buffer.trim().lastIndexOf(WORD_SEPARATOR);
        if (i > 0) {
            return buffer.substring(i + 1);
        }
        return buffer;
    }


    public enum LetterCase {
        UPPER, LOWER;

        public LetterCase shift() {
            return this == UPPER ? LOWER : UPPER;
        }
    }


    public enum Mode {
        LETTERS, NUMBERS_AND_PUNCTUATION;

        public Mode shift() {
            return this == LETTERS ? NUMBERS_AND_PUNCTUATION : LETTERS;
        }
    }


    public void setLetterCase(LetterCase letterCase) {
        this.letterCase = letterCase;

        getLetterKeys().forEach((k, b) -> {
            highlight(mapKey(k), b, false);
        });
    }

    public void setMode(Mode mode) {
        this.mode = mode;

        for (Mode m : Mode.values()) {
            layout.findViewById(getModeLayout(m)).setVisibility(m == mode ? VISIBLE : GONE);
        }

        // update button text
        setLetterCase(LetterCase.UPPER);
    }


    protected HapticFeedback hapticFeedback;
    protected Recorder recorder;

    private final List<Consumer<String>> submitListener = new ArrayList<>();


    public void submit() {
        submitListener.forEach(c -> c.accept(buffer.trim()));
        clear();
    }


    public void clear() {
        setText("", Mode.LETTERS, LetterCase.UPPER);
    }


    public void setText(String s, Mode m, LetterCase c) {
        updateTextBuffer(s);
        setMode(m);
        setLetterCase(c);
    }


    public void setRecorder(Recorder recorder) {
        this.recorder = recorder;
    }

    public void setHapticFeedback(HapticFeedback hapticFeedback) {
        this.hapticFeedback = hapticFeedback;
    }

    public void addSubmitListener(Consumer<String> listener) {
        submitListener.add(listener);
    }


}
