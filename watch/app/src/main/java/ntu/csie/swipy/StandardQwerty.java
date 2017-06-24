package ntu.csie.swipy;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;


public class StandardQwerty extends AbstractPredictiveKeyboardLayout {

    private TreeMap<String, Button> keys;


    public StandardQwerty(Context context) {
        super(context, R.layout.keyboard_qwerty);

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


        // hook up keyboard listeners
        for (Button key : keys.values()) {
            key.setText(key.getText().toString().toLowerCase());
            key.setOnClickListener(v -> enterKey(key));
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
