package ntu.csie.prompter;

import static java.util.stream.Collectors.*;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Record implements Comparable<Record> {

	public final long timestamp;
	public final long number;
	public final String session;
	public final String model;
	public final KeyboardLayout keyboard;
	public final int phrase;
	public final String key;
	public final String buffer;

	public Record(long timestamp, long number, String session, String model, KeyboardLayout keyboard, int phrase, String key, String buffer) {
		this.timestamp = timestamp;
		this.number = number;
		this.session = session;
		this.model = model;
		this.keyboard = keyboard;
		this.phrase = phrase;
		this.key = key;
		this.buffer = buffer;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public long getNumber() {
		return number;
	}

	public String getSession() {
		return session;
	}

	public String getModel() {
		return model;
	}

	public KeyboardLayout getKeyboard() {
		return keyboard;
	}

	public int getPhrase() {
		return phrase;
	}

	public String getKey() {
		return key;
	}

	public String getBuffer() {
		return buffer;
	}

	public boolean isInputKey() {
		return Stream.of(Symbols.ENTER, Symbols.START_OF_TEXT, Symbols.END_OF_TEXT).noneMatch(s -> s.equals(key));
	}

	public boolean isDelete() {
		return Symbols.BACKSPACE.equals(key);
	}

	@Override
	public int compareTo(Record o) {
		return Long.compare(this.number, o.number);
	}

	@Override
	public String toString() {
		return Stream.of(timestamp, number, session, model, keyboard, phrase, key, buffer).map(Objects::toString).collect(joining("\t"));
	}

	public static Record parse(String tsv) {
		String[] s = TAB.split(tsv, 8);
		return new Record(Long.parseLong(s[0]), Long.parseLong(s[1]), s[2], s[3], KeyboardLayout.valueOf(s[4]), Integer.parseInt(s[5]), s[6], s[7]);
	}

	public static final Pattern TAB = Pattern.compile("\\t");

}
