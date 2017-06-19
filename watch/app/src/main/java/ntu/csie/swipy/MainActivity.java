package ntu.csie.swipy;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import ntu.csie.keydial.SwipeKeyGroup;
import ntu.csie.swipy.model.Final;
import ntu.csie.swipy.model.Initial;
import ntu.csie.swipy.model.PhoneticGroup;
import ntu.csie.swipy.model.Pinyin;

import static com.google.common.collect.Lists.*;
import static ntu.csie.swipy.model.Punctuation.*;


public class MainActivity extends WearableActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make sure that screen doesn't turn off during user study
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        setContentView(R.layout.keyboard_common);


        setInitial(getTable());
    }


    public ViewGroup getTable() {
        return (ViewGroup) findViewById(R.id.keyboard_content_view);
    }


    public TextView getText() {
        return (TextView) findViewById(R.id.text_editor);
    }


    public Object[] reorder(Object[] a) {
        if (a.length < 4) {
            a = copyOf(a, 4);
        }
        return new Object[]{a[0], a[2], a[3], a[1]};
    }


    public static final int MAX_COLUMNS = 4;
    public static final int MAX_KEYS = 4;


    public void setInitial(ViewGroup table) {
        table.removeAllViews();


        for (List<Initial[]> groups : partition(newArrayList(PhoneticGroup.getZhuyinGroups()), MAX_COLUMNS)) {
            TableRow row = new TableRow(table.getContext());
            row.setGravity(Gravity.CENTER);
            table.addView(row);

            groups.forEach(group -> {
                ViewGroup keyGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.group_swipekey, row, false);
                row.addView(keyGroup);

                for (Object key : reorder(group)) {
                    Button keyButton = (Button) getLayoutInflater().inflate(R.layout.button_swipekey, keyGroup, false);
                    if (key != null) {
                        Initial i = (Initial) key;
                        keyButton.setText(i.toString().toLowerCase());
                        keyButton.setText(i.getZhuyin());

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

        Final[] finals = PhoneticGroup.getFinalGroups(initial);
        int cols = finals.length <= 6 * 4 ? 3 : 4;

        partition(partition(newArrayList(finals), MAX_KEYS), cols).forEach(groups -> {
            TableRow row = new TableRow(table.getContext());
            row.setGravity(Gravity.CENTER);
            table.addView(row);

            groups.forEach(group -> {
                ViewGroup keyGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.group_swipekey, row, false);
                // keyGroup.setLayoutParams(new TableRow.LayoutParams((int) (keyGroup.getLayoutParams().width * 1.3), keyGroup.getLayoutParams().height));
                row.addView(keyGroup);


                for (Object key : reorder(group.toArray())) {
                    Button keyButton = (Button) getLayoutInflater().inflate(R.layout.button_swipekey, keyGroup, false);
                    keyButton.setLayoutParams(new SwipeKeyGroup.LayoutParams(keyButton.getLayoutParams().width * 3, keyButton.getLayoutParams().height));
                    keyButton.setBackground(null);
                    if (key != null) {
                        Final f = (Final) key;
                        String label = f.getZhuyin(initial);
                        // keyButton.setText(key.toString().toLowerCase());


                        if (label.startsWith("'")) {
                            Spannable span = new SpannableString(label.substring(1));
                            span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.editor_highlight_fg, getTheme())), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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


    private String buffer = "";


    private void appendStart(String s) {
        if (buffer.length() > 0) {
            s = APOSTROPHE + s;
        }

        getText().setText(buffer + s);
    }


    private void appendCommit(String s) {
        if (buffer.length() > 0) {
            s = APOSTROPHE + s;
        }

        buffer = buffer + s;

        getText().setText(buffer);
    }

    private void keyPressed(View view) {
        Object t = view.getTag();

        if (t instanceof Initial) {
            // appendStart(t.toString().toLowerCase());
            appendStart(((Initial) t).getZhuyin());

            setFinal(getTable(), (Initial) t);
            return;
        }

        if (t instanceof Pinyin) {
            appendCommit(t.toString().toLowerCase());
            setInitial(getTable());
            return;
        }
    }


}