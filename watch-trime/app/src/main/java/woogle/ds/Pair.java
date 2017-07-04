package woogle.ds;

import java.io.Serializable;

public class Pair<T, U> implements Serializable {
    public T first;

    public U second;

    public Pair() {
    }

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public String toString() {
        return "<" + first + "," + second + ">";
    }
}
