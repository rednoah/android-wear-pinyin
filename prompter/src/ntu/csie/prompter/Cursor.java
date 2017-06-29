package ntu.csie.prompter;

public class Cursor<T> {

	private int i;
	private T[] values;

	public Cursor(T[] values) {
		this.values = values;

		// position before first element
		reset();
	}

	public void reset() {
		i = -1;
	}

	public int position() {
		return i;
	}

	public int length() {
		return values.length;
	}

	public T current() {
		return values[i];
	}

	public T next() {
		return values[++i];
	}

	public T previous() {
		return values[--i];
	}

	public boolean hasNext() {
		return i < values.length - 1;
	}

	public boolean hasPrevious() {
		return i > 0;
	}

}
