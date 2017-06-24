package ntu.csie.swipy;


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

import org.apache.commons.lang3.text.StrBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ntu.csie.swipy.model.Punctuation;

import static java.util.stream.Collectors.toMap;
import static ntu.csie.swipy.model.Punctuation.APOSTROPHE;

public abstract class AbstractKeyboardLayout extends BoxInsetLayout {


    protected View layout;
    protected TextView editor;


    protected String buffer;
    protected int highlightColor;

    protected int highlightStart = -1;


    public AbstractKeyboardLayout(Context context, int layout) {
        super(context);

        this.layout = LayoutInflater.from(context).inflate(layout, this, true);
        this.editor = (TextView) this.layout.findViewById(getEditorLayout());


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
    }


    protected abstract int getEditorLayout();


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
            case Symbols.BACKSPACE:
                keyPressed(key, InputType.DELETE_LETTER);
                break;
            default:
                keyPressed(key, InputType.ENTER_LETTER);
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
        if (buffer.isEmpty()) {
            return;
        }

        keyPressed(Punctuation.DOT.toString(), InputType.ENTER_LETTER);
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


    protected String applyWord(String word, String buffer) {
        if (buffer.length() > 0) {
            for (int i = 0; i < buffer.length(); i++) {
                if (Character.isIdeographic(buffer.charAt(i))) {
                    continue;
                }
                return buffer.substring(0, i) + word;
            }
        }
        return buffer + word;
    }


    protected void updateTextBuffer(String buffer) {
        this.buffer = buffer;

        editor.setText(highlightTail(buffer, highlightStart > 0 && highlightStart < buffer.length() ? highlightStart : 0));
    }


    protected Spanned highlightTail(String text, int from) {
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


    protected void highlight(String key, Button button, boolean enabled) {
        if (enabled) {
            button.setText(highlightKey(key));
        } else {
            button.setText(key);
        }
    }


    protected String getLastWord() {
        return buffer;
    }


    protected HapticFeedback hapticFeedback;
    protected Recorder recorder;

    private final List<Consumer<String>> submitListener = new ArrayList<>();


    public void submit() {
        submitListener.forEach(c -> c.accept(buffer.trim()));
        clear();
    }


    public void clear() {
        setText("");
        highlightStart = -1;
    }


    public void setText(String s) {
        updateTextBuffer(s);
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
