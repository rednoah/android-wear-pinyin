package ntu.csie.swipy.model;


import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static junit.framework.Assert.assertEquals;

public class GroupTest {


    @Test
    public void getPhoneticGroups() {
        for (Initial i : Initial.values()) {
            assertEquals(toString(i, i.getFinals()), toString(i, Final.getPhoneticGroups(i)));
        }
    }

    @Test
    public void getQwertyGroups() {
        for (Initial i : Initial.values()) {
            assertEquals(toString(i, i.getFinals()), toString(i, Final.getQwertyGroups(i)));
        }
    }

    private static String toString(Initial i, Final[] a) {
        return i + " -> " + Stream.of(a).filter(Objects::nonNull).sorted().collect(toList()).toString();
    }

    private static String toString(Initial i, Final[][] a) {
        return i + " -> " + Stream.of(a).flatMap(Stream::of).filter(Objects::nonNull).sorted().collect(toList()).toString();
    }

}
