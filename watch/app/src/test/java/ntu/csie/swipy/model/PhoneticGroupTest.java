package ntu.csie.swipy.model;


import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static junit.framework.Assert.assertEquals;

public class PhoneticGroupTest {


    @Test
    public void getFinalGroups() {
        for (Initial i : Initial.values()) {
            assertEquals(asList(i.getFinals()), asList(Final.getPhoneticGroups(i)));
        }
    }


    private <T> List<T> asList(T[] a) {
        return Stream.of(a).filter(Objects::nonNull).sorted().collect(toList());
    }

}
