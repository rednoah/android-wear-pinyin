package ntu.csie.swipy.model;

import static java.util.stream.Collectors.*;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class Pinyin {


    private final Initial head;
    private final Final tail;


    public Pinyin(Initial head, Final tail) {
        this.head = head;
        this.tail = head.name().equalsIgnoreCase(tail.name()) ? null : tail;    // not all sounds have a Final (e.g. 'a')
    }


    public boolean hasFinal() {
        return tail != null;
    }


    public Initial getInitial() {
        return head;
    }


    public Final getFinal() {
        return tail;
    }


    @Override
    public String toString() {
        return hasFinal() ? head.toString() + tail.toString() : head.toString();
    }

}