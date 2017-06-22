package ntu.csie.swipy;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WearableRecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;
import java.util.function.Consumer;

import ntu.csie.keydial.AbstractPredictiveKeyboardLayout;
import ntu.csie.keydial.SwipeKeyGroup;
import ntu.csie.swipy.model.Final;
import ntu.csie.swipy.model.Initial;
import ntu.csie.swipy.model.PhoneticGroup;
import ntu.csie.swipy.model.Pinyin;
import woogle.spi.WoogleInputMethod;
import woogle.util.WoogleDatabase;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.partition;
import static java.util.Arrays.copyOf;
import static java.util.Collections.emptyList;
import static ntu.csie.swipy.model.Punctuation.APOSTROPHE;


public class MainActivity extends WearableActivity {

    boolean zhuyin = false;

    WoogleInputMethod woogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make sure that screen doesn't turn off during user study
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        setContentView(R.layout.keyboard_common);


        submit();
        getSuggestionView().setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));


        WoogleDatabase.load(getApplicationContext());
        woogle = new WoogleInputMethod();


        Button submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener(c -> submit());
    }


    public void submit() {
        buffer = "";
        getText().setText(buffer);
        setInitial(getTable());
        setSuggestions(emptyList());
    }


    public ViewGroup getTable() {
        return (ViewGroup) findViewById(R.id.keyboard_content_view);
    }


    public WearableRecyclerView getSuggestionView() {
        return (WearableRecyclerView) findViewById(R.id.suggestion_recycler);
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
                        keyButton.setText(i.getZhuyin());

                        if (!zhuyin) {
                            keyButton.setText(i.toString().toLowerCase());
                        }


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


                        if (label.startsWith("'")) {
                            Spannable span = new SpannableString(label.substring(1));
                            span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.editor_highlight_fg, getTheme())), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            keyButton.setText(span);
                        } else {
                            keyButton.setText(label);
                        }

                        if (!zhuyin) {
                            keyButton.setText(key.toString().toLowerCase());
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
            if (!zhuyin) {
                appendStart(t.toString().toLowerCase());
            } else {
                appendStart(((Initial) t).getZhuyin());
            }

            setFinal(getTable(), (Initial) t);
            return;
        }

        if (t instanceof Pinyin) {
            appendCommit(t.toString().toLowerCase());
            setInitial(getTable());


            woogle.clear();
            getText().getText().chars().forEach(c -> woogle.keyPressed((char) c));
            List<String> chars = woogle.getCandString();

            Log.d("WOOGLE", woogle.getCompString());
            Log.d("WOOGLE", "" + chars);
            setSuggestions(chars);


            return;
        }
    }


    protected int getSuggestionItemLayout() {
        return R.layout.item_suggestion_horizontal;
    }


    public void setSuggestions(List<String> suggestions) {
        // update suggestions
        getSuggestionView().setAdapter(new SuggestionViewAdapter(getSuggestionItemLayout(), suggestions, s -> getText().setText(s), "", Color.RED));


    }


    public static class SuggestionViewHolder extends RecyclerView.ViewHolder {

        public String value;
        public TextView view;

        public SuggestionViewHolder(View view, Consumer<String> handler) {
            super(view);
            this.view = (TextView) view.findViewById(R.id.text);

            // enter suggestion on click
            this.view.setOnClickListener(v -> handler.accept(this.value));
        }
    }


    public static class SuggestionViewAdapter extends RecyclerView.Adapter<AbstractPredictiveKeyboardLayout.SuggestionViewHolder> {

        private final List<String> suggestions;
        private final Consumer<String> handler;

        private final String lead;
        private final int leadColor;

        private final int layout;


        public SuggestionViewAdapter(int layout, List<String> suggestions, Consumer<String> handler, String lead, int leadColor) {
            this.layout = layout;
            this.suggestions = suggestions;
            this.handler = handler;
            this.lead = lead;
            this.leadColor = leadColor;
        }

        @Override
        public AbstractPredictiveKeyboardLayout.SuggestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            return new AbstractPredictiveKeyboardLayout.SuggestionViewHolder(view, handler);
        }

        @Override
        public void onBindViewHolder(AbstractPredictiveKeyboardLayout.SuggestionViewHolder holder, int position) {
            holder.value = suggestions.get(position);
            holder.view.setText(holder.value);
        }

        protected Spanned highlightLead(String word, int to) {
            SpannableString span = new SpannableString(word);
            span.setSpan(new ForegroundColorSpan(leadColor), 0, to, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            return span;
        }

        @Override
        public int getItemCount() {
            return suggestions.size();
        }

    }


}