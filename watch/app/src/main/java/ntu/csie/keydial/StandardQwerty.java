package ntu.csie.keydial;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;

import ntu.csie.swipy.R;


public class StandardQwerty extends AbstractPredictiveKeyboardLayout {


    public StandardQwerty(Context context) {
        super(context, R.layout.keyboard_qwerty);

        this.suggestionView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }


    @Override
    protected int getSuggestionRecyclerLayout() {
        return R.id.suggestion_recycler;
    }


    @Override
    protected int getSuggestionItemLayout() {
        return R.layout.item_suggestion_horizontal;
    }


    @Override
    protected int[] getButtonGroups() {
        return new int[]{R.id.top_row, R.id.middle_row, R.id.bottom_row, R.id.number_row, R.id.symbol_row, R.id.punctuation_row, R.id.controls, R.id.submit};
    }

    @Override
    protected int getEditorLayout() {
        return R.id.text_editor;
    }


    @Override
    protected int getModeLayout(Mode mode) {
        switch (mode) {
            case LETTERS:
                return R.id.letters;
            case NUMBERS_AND_PUNCTUATION:
                return R.id.numbers;
        }
        return -1;
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

    @Override
    public void setLetterCase(LetterCase letterCase) {
        super.setLetterCase(letterCase);

        // toggle option button
        backgroundHighlight(keys.get(Symbols.OPTION), letterCase == LetterCase.UPPER);
    }


    @Override
    public void setMode(Mode mode) {
        super.setMode(mode);

        // toggle mode button
        backgroundHighlight(keys.get(Symbols.KEYBOARD), mode == Mode.LETTERS);
    }


    protected void backgroundHighlight(Button button, boolean enabled) {
        button.setBackgroundResource(enabled ? R.drawable.qwerty_button_alt_bg : R.drawable.qwerty_button_bg);
    }


}
