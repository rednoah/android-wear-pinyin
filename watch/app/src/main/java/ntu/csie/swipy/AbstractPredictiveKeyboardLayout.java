package ntu.csie.swipy;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WearableRecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import ntu.csie.swipy.model.Punctuation;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;


public abstract class AbstractPredictiveKeyboardLayout extends AbstractKeyboardLayout {

    protected WearableRecyclerView suggestionView;
    protected int suggestionLeadHighlightColor;

    protected AutoComplete autoComplete;


    public AbstractPredictiveKeyboardLayout(Context context, int layout) {
        super(context, layout);

        this.suggestionView = (WearableRecyclerView) findViewById(getSuggestionRecyclerLayout());
        this.suggestionView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        this.suggestionView.setHasFixedSize(true);

        this.suggestionLeadHighlightColor = getResources().getColor(R.color.suggestion_lead_fg, getContext().getTheme());
    }


    protected abstract int getSuggestionRecyclerLayout();


    protected abstract int getSuggestionItemLayout();


    public void setAutoComplete(AutoComplete autoComplete) {
        this.autoComplete = autoComplete;
    }


    @Override
    public void keyPressed(String key, InputType type) {
        super.keyPressed(key, type);

        if (autoComplete != null && type != InputType.CONTROL_KEY) {
            autoComplete.getSuggestionsAsync(key, type, getComposingBuffer(), this::setSuggestions);
        }
    }


    public WearableRecyclerView getSuggestionView() {
        return suggestionView;
    }


    public void setSuggestions(AutoComplete.Result candidates) {
        if (candidates.buffer != null) {
            setComposingBuffer(candidates.buffer);

            if (candidates.commit) {
                markComposingStart();
            }
        }


        // update suggestions
        suggestionView.setAdapter(new SuggestionViewAdapter(getSuggestionItemLayout(), candidates.candidates, this::enterSuggestion, getComposingBuffer(), suggestionLeadHighlightColor));
    }


    @Override
    public void clear() {
        super.clear();
        setSuggestions(new AutoComplete.Result(emptyList()));

        if (autoComplete != null) {
            autoComplete.getSuggestionsAsync("", InputType.CONTROL_KEY, "", this::setSuggestions);
        }
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


    public static class SuggestionViewAdapter extends RecyclerView.Adapter<SuggestionViewHolder> {

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
        public SuggestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            return new SuggestionViewHolder(view, handler);
        }

        @Override
        public void onBindViewHolder(SuggestionViewHolder holder, int position) {
            String word = suggestions.get(position);

            holder.value = word;
            holder.view.setText(word);
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
