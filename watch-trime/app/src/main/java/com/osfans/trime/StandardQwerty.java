package com.osfans.trime;

import android.content.Context;
import android.widget.Button;

import static java.util.stream.Collectors.toMap;


public class StandardQwerty extends AbstractPredictiveKeyboardLayout {


    public StandardQwerty(Context context) {
        super(context, R.layout.keyboard_qwerty);

        // prefer lower-case button labels
        getButtons().forEach(b -> b.setText(b.getText().toString().toLowerCase()));
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
