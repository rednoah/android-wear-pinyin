package ntu.csie.swipy;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ntu.csie.swipy.model.Final;
import ntu.csie.swipy.model.Initial;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static ntu.csie.swipy.model.Punctuation.APOSTROPHE;

public class PinyinSyllablesQwerty extends AbstractPredictiveKeyboardLayout {


    public PinyinSyllablesQwerty(Context context) {
        super(context, R.layout.keyboard_common);

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(b -> submit());

        setInitial(getTable());
    }


    @Override
    public void onEditorClick(View view, MotionEvent event) {
        setInitial(getTable());
        markHighlightStart();
    }


    @Override
    public void clear() {
        super.clear();

        setInitial(getTable());
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


    public void setInitial(ViewGroup table) {
        table.removeAllViews();


        Initial[][] qwertyGroups = Initial.getQwertyGroups();
        int[] rowWidth = {R.dimen.qwerty_row_1_width, R.dimen.qwerty_row_2_width, R.dimen.qwerty_row_3_width};

        for (int n = 0; n < qwertyGroups.length; n++) {
            Initial[] r = qwertyGroups[n];
            int p = (getResources().getDimensionPixelSize(rowWidth[0]) - getResources().getDimensionPixelSize(rowWidth[n])) / 2;

            TableRow row = new TableRow(table.getContext());
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setGravity(Gravity.CENTER);
            row.setPadding(p, 0, p, 0);
            table.addView(row);

            stream(r).forEach(k -> {
                Button keyButton = (Button) LayoutInflater.from(row.getContext()).inflate(R.layout.button_qwerty, row, false);
                row.addView(keyButton);

                if (k != null) {
                    Initial i = (Initial) k;
                    keyButton.setText(i.toString().toLowerCase());
                    keyButton.setTag(i);
                    keyButton.setOnClickListener(this::keyPressed);
                }
            });
        }

    }


    public void setFinal(ViewGroup table, Initial initial) {
        table.removeAllViews();


        Final[][] qwertyGroups = Final.getQwertyGroups(initial);
        int[] rowWidth = {R.dimen.qwerty_row_1_width, R.dimen.qwerty_row_2_width, R.dimen.qwerty_row_3_width};

        for (int n = 0; n < qwertyGroups.length; n++) {
            Final[] r = qwertyGroups[n];
            int p = (getResources().getDimensionPixelSize(rowWidth[0]) - getResources().getDimensionPixelSize(rowWidth[n])) / 2;

            TableRow row = new TableRow(table.getContext());
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setGravity(Gravity.CENTER);
            row.setPadding(p, 0, p, 0);
            table.addView(row);

            stream(r).forEach(k -> {
                Button b = (Button) LayoutInflater.from(row.getContext()).inflate(R.layout.button_qwerty, row, false);
                row.addView(b);

                if (k != null) {
                    Final f = (Final) k;
                    b.setText(f.toString().toLowerCase());
                    b.setTag(f);
                    b.setOnClickListener(this::keyPressed);

                    switch (f.getColor()) {
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
                        case 5:
                            b.setBackgroundResource(R.drawable.rect_button_alt_6);
                            break;
                        default:
                            throw new IllegalStateException("Color Group: " + f);
                    }
                }
            });
        }
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
            appendStart(i.toString().toLowerCase());
            setFinal(getTable(), i);
            return;
        }

        if (t instanceof Final) {
            Final f = (Final) t;
            appendCommit(f.toString().toLowerCase());
            setInitial(getTable());
            return;
        }
    }

    @Override
    public void popHistory() {
        super.popHistory();

        String s = getHighlightBuffer();
        if (s.isEmpty()) {
            setInitial(getTable());
        } else {
            try {
                Initial i = Initial.valueOf(s.toUpperCase());
                setFinal(getTable(), i);
            } catch (Exception e) {
                Log.d("PinyinSyllables", "Bad buffer: " + s, e);
                setInitial(getTable());
            }
        }
    }
}
