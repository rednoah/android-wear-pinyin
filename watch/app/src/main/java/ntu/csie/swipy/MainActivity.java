package ntu.csie.swipy;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.CurvedChildLayoutManager;
import android.support.wearable.view.WearableRecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.function.Consumer;


public class MainActivity extends WearableActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WearableRecyclerView keyboardRecycler = (WearableRecyclerView) findViewById(R.id.keyboardRecycler);
        keyboardRecycler.setHasFixedSize(true);
        keyboardRecycler.setCenterEdgeItems(true);

        keyboardRecycler.setLayoutManager(new CurvedChildLayoutManager(getApplicationContext()));
        keyboardRecycler.setAdapter(new KeyboardItemAdapter(KeyboardLayout.values(), this::openKeyboard));

        // make sure that screen doesn't turn off during user study
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    public static class KeyboardItem extends RecyclerView.ViewHolder {

        private KeyboardLayout keyboard;
        private TextView text;


        public KeyboardItem(ViewGroup parent, Consumer<KeyboardLayout> handler) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_keyboard, parent, false));

            this.text = (TextView) itemView.findViewById(R.id.text);
            this.text.setOnClickListener(v -> handler.accept(keyboard));
        }


        public void setValue(KeyboardLayout keyboard) {
            if (keyboard != null) {
                this.keyboard = keyboard;
                this.text.setText(keyboard.toString());
                this.text.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher, 0, 0, 0);
            } else {
                this.keyboard = null;
                this.text.setText("RESET");
                this.text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cc_clear, 0, 0, 0);
            }
        }
    }


    public static class KeyboardItemAdapter extends RecyclerView.Adapter<KeyboardItem> {

        private KeyboardLayout[] layouts;
        private Consumer<KeyboardLayout> handler;


        public KeyboardItemAdapter(KeyboardLayout[] layouts, Consumer<KeyboardLayout> handler) {
            this.layouts = layouts;
            this.handler = handler;
        }

        @Override
        public KeyboardItem onCreateViewHolder(ViewGroup parent, int viewType) {
            return new KeyboardItem(parent, handler);
        }

        @Override
        public void onBindViewHolder(KeyboardItem holder, int position) {
            try {
                holder.setValue(layouts[position]);
            } catch (ArrayIndexOutOfBoundsException e) {
                holder.setValue(null);
            }
        }

        @Override
        public int getItemCount() {
            return layouts.length + 1;
        }

    }


    public void openKeyboard(KeyboardLayout keyboard) {
        if (keyboard == null) {
            for (File f : getApplicationContext().getFilesDir().listFiles()) {
                try {
                    Log.d("RESET", "Delete " + f);
                    FileUtils.forceDelete(f);
                } catch (Exception e) {
                    Log.d("RESET", "RESET FAILED", e);
                }
            }
            finish();
            System.exit(0);
        }


        KeyboardFragment fragment = new KeyboardFragment();
        Bundle args = new Bundle();
        args.putInt(KeyboardFragment.EXTRA_KEYBOARD, keyboard.ordinal());
        fragment.setArguments(args);


        // open keyboard
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_view, fragment, fragment.toString())
                .addToBackStack(fragment.toString())
                .commit();


        // fix "click through" issues
        findViewById(R.id.keyboardRecycler).setVisibility(View.GONE);
    }


    public static class KeyboardFragment extends Fragment {

        public static final String EXTRA_KEYBOARD = "keyboard";


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            KeyboardLayout k = KeyboardLayout.values()[getArguments().getInt(EXTRA_KEYBOARD)];

            AbstractPredictiveKeyboardLayout keyboard = k.create(getContext());
            keyboard.setAutoComplete(new AutoComplete(getContext()));
            keyboard.addSubmitListener(this::submit);

            return keyboard;
        }


        public void submit(String s) {
            // close fragment
            if (s.isEmpty()) {
                getActivity().getFragmentManager().popBackStack();
                getActivity().findViewById(R.id.keyboardRecycler).setVisibility(View.VISIBLE);
            }
        }

    }


    public enum KeyboardLayout {

        GrowingFinals,
        PinyinSyllables,
        SwipePinyin,
        SwipeZhuyin,
        StandardQwerty;


        public AbstractPredictiveKeyboardLayout create(Context context) {
            switch (this) {
                case StandardQwerty:
                    return new StandardQwerty(context);
                case PinyinSyllables:
                    return new PinyinSyllablesQwerty(context);
                case GrowingFinals:
                    return new GrowingFinalsQwerty(context);
                case SwipePinyin:
                    return new PinZhuYinSwipeKey(context, PinZhuYinSwipeKey.Mode.PINYIN);
                case SwipeZhuyin:
                    return new PinZhuYinSwipeKey(context, PinZhuYinSwipeKey.Mode.ZHUYIN);
                default:
                    throw new IllegalStateException("Keyboard: " + this);
            }
        }

    }


}