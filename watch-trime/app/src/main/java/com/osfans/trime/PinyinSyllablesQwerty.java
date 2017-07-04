package com.osfans.trime;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import com.osfans.trime.model.Final;
import com.osfans.trime.model.Initial;
import com.osfans.trime.model.Pinyin;
import com.osfans.trime.model.Punctuation;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;
import static com.osfans.trime.model.Punctuation.APOSTROPHE;

public class PinyinSyllablesQwerty extends AbstractPredictiveKeyboardLayout {

    private TableRow[] initialTable;
    private Map<Initial, TableRow[]> finalTable;


    public PinyinSyllablesQwerty(Context context) {
        super(context, R.layout.keyboard_common);

        initialTable = createInitialTable();
        finalTable = stream(Initial.values()).collect(toMap(Function.identity(), this::createFinalTable, (a, b) -> a, () -> new EnumMap<Initial, TableRow[]>(Initial.class)));

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(b -> submit());

        ViewGroup table = getTable();
        stream(initialTable).forEach(table::addView);
        finalTable.forEach((k, vs) -> {
            stream(vs).forEach(table::addView);
        });

        setInitial();
    }


    @Override
    public void onEditorClick(View view, MotionEvent event) {
        setInitial();
        markHighlightStart();
    }


    @Override
    public void clear() {
        super.clear();

        setInitial();
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


    private TableRow[] createInitialTable() {
        Initial[][] qwertyGroups = Initial.getQwertyGroups();
        TableRow[] rows = new TableRow[qwertyGroups.length];

        int[] rowWidth = {R.dimen.qwerty_row_1_width, R.dimen.qwerty_row_2_width, R.dimen.qwerty_row_3_width};

        for (int n = 0; n < qwertyGroups.length; n++) {
            Initial[] r = qwertyGroups[n];
            int p = (getResources().getDimensionPixelSize(rowWidth[0]) - getResources().getDimensionPixelSize(rowWidth[n])) / 2;

            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setGravity(Gravity.CENTER);
            row.setPadding(p, 0, p, 0);

            stream(r).forEach(k -> {
                Button keyButton = (Button) LayoutInflater.from(row.getContext()).inflate(R.layout.button_qwerty, row, false);
                row.addView(keyButton);

                if (k != null) {
                    Initial i = (Initial) k;
                    keyButton.setText(i.toString().toLowerCase());
                    keyButton.setTag(i);
                    keyButton.setOnClickListener(this::keyPressed);

                    if (i.toString().length() > 1) {
                        smallerKeyText(keyButton);
                    }
                }
            });

            rows[n] = row;
        }

        return rows;
    }

    private TableRow[] createFinalTable(Initial initial) {
        Final[][] qwertyGroups = Final.getQwertyGroups(initial);
        TableRow[] rows = new TableRow[qwertyGroups.length];

        int keyHeight = (int) ((float) getResources().getDimensionPixelOffset(R.dimen.qwerty_table_height) / (float) qwertyGroups.length);
        int[] rowWidth = {R.dimen.qwerty_row_1_width, R.dimen.qwerty_row_2_width, R.dimen.qwerty_row_3_width};

        for (int n = 0; n < qwertyGroups.length; n++) {
            Final[] r = qwertyGroups[n];

            int p = (getResources().getDimensionPixelSize(rowWidth[0]) - getResources().getDimensionPixelSize(rowWidth[n])) / 2;

            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setGravity(Gravity.CENTER);
            row.setPadding(p, 0, p, 0);

            stream(r).forEach(k -> {
                Button b = (Button) LayoutInflater.from(row.getContext()).inflate(R.layout.button_qwerty, row, false);
                b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, keyHeight, 1.0f));
                row.addView(b);

                if (k != null) {
                    Pinyin yin = new Pinyin(initial, (Final) k);
                    b.setOnClickListener(this::keyPressed);

                    if (yin.hasFinal()) {
                        b.setText(yin.getFinal().toString().toLowerCase());
                        b.setTag(yin);

                        if (b.getText().length() > 1) {
                            smallerKeyText(b);
                        }

                        switch (yin.getFinal().getColor()) {
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
                                throw new IllegalStateException("Color Group: " + yin.getFinal());
                        }
                    } else {
                        b.setText(APOSTROPHE.toString());
                        b.setTag(APOSTROPHE);
                        b.setBackgroundResource(R.drawable.rect_button_alt_1);
                    }

                }
            });

            rows[n] = row;
        }

        return rows;
    }


    private void smallerKeyText(Button b) {
        if (b.getText().length() >= 3) {
            b.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.button_font_size_smallest));
            b.setPadding(0, 12, 0, 0);
        } else if (b.getText().length() >= 2) {
            b.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.button_font_size_smaller));
            b.setPadding(0, 8, 0, 0);
        }
    }


    private void hideAllRows() {
        stream(initialTable).forEach(v -> v.setVisibility(GONE));

        finalTable.forEach((k, vs) -> {
            stream(vs).forEach(v -> v.setVisibility(GONE));
        });
    }


    public void setInitial() {
        hideAllRows();
        stream(initialTable).forEach(v -> v.setVisibility(VISIBLE));
    }


    public void setFinal(Initial initial) {
        hideAllRows();
        stream(finalTable.get(initial)).forEach(v -> v.setVisibility(VISIBLE));
    }


    private void keyPressed(View view) {
        Object t = view.getTag();

        if (t instanceof Initial) {
            Initial i = (Initial) t;

            markHighlightStart();
            keyPressed(i.toString().toLowerCase(), InputType.ENTER_LETTER);

            setFinal(i);
            return;
        } else if (t instanceof Pinyin) {
            Pinyin yin = (Pinyin) t;

            keyPressed(yin.getFinal().toString().toLowerCase() + APOSTROPHE, InputType.ENTER_LETTER);
            markHighlightStart();

            setInitial();
            return;
        } else if (t instanceof Punctuation) {
            Punctuation p = (Punctuation) t;

            keyPressed(p.toString(), InputType.ENTER_LETTER);
            markHighlightStart();

            setInitial();
            return;
        }
    }

    @Override
    public void popHistory() {
        super.popHistory();

        String s = getHighlightBuffer();
        if (s.isEmpty()) {
            setInitial();
        } else {
            try {
                Initial i = Initial.valueOf(s.toUpperCase());
                setFinal(i);
            } catch (Exception e) {
                Log.d("PinyinSyllables", "Bad buffer: " + s, e);
                setInitial();
            }
        }
    }
}
