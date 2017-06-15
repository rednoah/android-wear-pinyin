package ntu.csie.keydial;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;

import ntu.csie.swipy.R;

public class SwipeKey extends AbstractPredictiveKeyboardLayout {


    public SwipeKey(Context context) {
        super(context, R.layout.swipekey_qwerty);

        // use horizontal suggestion recycler
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
        return new int[]{
                R.id.swipekey1,
                R.id.swipekey2,
                R.id.swipekey3,
                R.id.swipekey4,
                R.id.swipekey5,
                R.id.swipekey6,
                R.id.swipekey7,
                R.id.swipekey1_2,
                R.id.swipekey2_2,
                R.id.swipekey3_2,
                R.id.swipekey4_2,
                R.id.swipekey5_2,
                R.id.swipekey6_2,
                R.id.swipekey7_2,
                R.id.controls,
                R.id.submit};
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
