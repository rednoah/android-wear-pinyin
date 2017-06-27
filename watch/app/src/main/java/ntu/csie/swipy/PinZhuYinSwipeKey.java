package ntu.csie.swipy;


import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import ntu.csie.swipy.model.Final;
import ntu.csie.swipy.model.Initial;
import ntu.csie.swipy.model.Pinyin;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.partition;
import static java.util.Arrays.copyOf;
import static java.util.Collections.emptyList;
import static ntu.csie.swipy.model.Punctuation.APOSTROPHE;

public class PinZhuYinSwipeKey extends AbstractPredictiveKeyboardLayout {


    public enum Mode {
        PINYIN,
        ZHUYIN;
    }


    private Mode mode;


    public PinZhuYinSwipeKey(Context context, Mode mode) {
        super(context, R.layout.keyboard_common);

        this.mode = mode;

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(b -> submit());

        setInitial(getTable());
    }

    @Override
    public void clear() {
        super.clear();

        setInitial(getTable());
    }


    private String getLabel(Initial i) {
        switch (mode) {
            case ZHUYIN:
                return i.getZhuyin();
            default:
                return i.toString().toLowerCase();
        }
    }


    private String getLabel(Initial i, Final f) {
        switch (mode) {
            case ZHUYIN:
                return f.getZhuyin(i);
            default:
                return f.toString().toLowerCase();
        }
    }


    @Override
    protected int[] getButtonGroups() {
        return new int[]{R.id.controls, R.id.submit};
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
    protected int getEditorLayout() {
        return R.id.text_editor;
    }


    public ViewGroup getTable() {
        return (ViewGroup) findViewById(R.id.keyboard_content_view);
    }


    public TextView getText() {
        return (TextView) findViewById(R.id.text_editor);
    }


    private static final int MAX_COLUMNS = 4;
    private static final int MAX_KEYS = 4;

    private static Object[] reorder(Object[] a) {
        if (a.length < 4) {
            a = copyOf(a, 4);
        }
        return new Object[]{a[0], a[2], a[3], a[1]};
    }


    public void setInitial(ViewGroup table) {
        table.removeAllViews();


        for (List<Initial[]> groups : partition(newArrayList(Initial.getPhoneticGroups()), MAX_COLUMNS)) {
            TableRow row = new TableRow(table.getContext());
            row.setGravity(Gravity.CENTER);
            table.addView(row);

            groups.forEach(group -> {
                ViewGroup keyGroup = (ViewGroup) LayoutInflater.from(row.getContext()).inflate(R.layout.group_swipekey, row, false);
                row.addView(keyGroup);

                for (Object key : reorder(group)) {
                    Button keyButton = (Button) LayoutInflater.from(keyGroup.getContext()).inflate(R.layout.button_swipekey, keyGroup, false);
                    if (key != null) {
                        Initial i = (Initial) key;
                        keyButton.setText(getLabel(i));
                        keyButton.setTag(i);
                        keyButton.setOnClickListener(this::keyPressed);
                    }
                    keyGroup.addView(keyButton);
                }
            });
        }
    }


    public void setFinal(ViewGroup table, Initial initial) {


        table.removeAllViews();

        Final[] finals = Final.getPhoneticGroups(initial);
        int cols = finals.length <= 6 * 4 ? 3 : 4;

        partition(partition(newArrayList(finals), MAX_KEYS), cols).forEach(groups -> {
            TableRow row = new TableRow(table.getContext());
            row.setGravity(Gravity.CENTER);
            table.addView(row);

            groups.forEach(group -> {
                ViewGroup keyGroup = (ViewGroup) LayoutInflater.from(row.getContext()).inflate(R.layout.group_swipekey, row, false);
                row.addView(keyGroup);

                for (Object key : reorder(group.toArray())) {
                    Button keyButton = (Button) LayoutInflater.from(keyGroup.getContext()).inflate(R.layout.button_swipekey, keyGroup, false);

                    keyButton.setLayoutParams(new SwipeKeyGroup.LayoutParams(keyButton.getLayoutParams().width * 3, keyButton.getLayoutParams().height));
                    keyButton.setBackground(null);

                    if (key != null) {
                        Final f = (Final) key;
                        String label = getLabel(initial, f);

                        if (label.startsWith(APOSTROPHE.toString())) {
                            Spannable span = new SpannableString(label.substring(1));
                            span.setSpan(new ForegroundColorSpan(highlightColor), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            keyButton.setText(span);
                        } else {
                            keyButton.setText(label);
                        }

                        keyButton.setTag(new Pinyin(initial, f));
                        keyButton.setOnClickListener(this::keyPressed);
                    }
                    keyGroup.addView(keyButton);
                }
            });
        });


    }


    private void appendStart(String s) {
        markHighlightStart();
        keyPressed(s, InputType.ENTER_LETTER);
    }


    private void appendCommit(String s) {
        keyPressed(s + APOSTROPHE, InputType.ENTER_LETTER);
        markHighlightStart();
    }


    private void keyPressed(View view) {
        Object t = view.getTag();

        if (t instanceof Initial) {
            Initial i = (Initial) t;

            appendStart(i.getPinyin());
            setFinal(getTable(), i);
            return;
        }

        if (t instanceof Pinyin) {
            Pinyin p = (Pinyin) t;
            appendCommit(p.hasFinal() ? p.getFinal().getPinyin() : ""); // FIXME: ZHUYIN SUPPORT
            setInitial(getTable());
            return;
        }
    }


}
