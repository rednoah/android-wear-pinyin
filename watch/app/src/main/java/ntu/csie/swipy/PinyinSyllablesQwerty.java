package ntu.csie.swipy;

import static java.util.Arrays.*;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.common.collect.Table;

import java.util.LinkedList;
import java.util.stream.Stream;

import ntu.csie.swipy.model.Final;
import ntu.csie.swipy.model.Initial;
import ntu.csie.swipy.model.Pinyin;


import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.partition;
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
    public void clear() {
        super.clear();

        setInitial(getTable());
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
                Button keyButton = (Button) LayoutInflater.from(row.getContext()).inflate(R.layout.button_qwerty, row, false);
                row.addView(keyButton);

                if (k != null) {
                    Final f = (Final) k;
                    keyButton.setText(f.toString().toLowerCase());
                    keyButton.setTag(f);
                    keyButton.setOnClickListener(this::keyPressed);
                }
            });
        }
    }


    private void appendStart(String s) {
        highlightStart = buffer.length();
        keyPressed(s, InputType.ENTER_LETTER);
    }


    private void appendCommit(String s) {
        keyPressed(s + APOSTROPHE, InputType.ENTER_LETTER);
        highlightStart = buffer.length();
    }


    @Override
    public void setText(String s) {
        super.setText(s);

        setInitial(getTable());
        setSuggestions(emptyList());
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


}
