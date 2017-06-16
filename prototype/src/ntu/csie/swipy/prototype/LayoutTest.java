package ntu.csie.swipy.prototype;

import static java.util.stream.Collectors.*;
import static ntu.csie.swipy.model.Pinyin.Initial.*;

import java.util.EnumSet;
import java.util.stream.Stream;

import ntu.csie.swipy.model.Pinyin.Initial;

public class LayoutTest {

	public static void main(String[] args) {
		EnumSet<Initial> initials = EnumSet.allOf(Initial.class);

		Stream.of(
                Stream.of(
                        Stream.of(b, p, m, f),
                        Stream.of(d, t, n, l),
                        Stream.of(g, k, h),
						Stream.of(j, q, x)),
                Stream.of(
                        Stream.of(zh, ch, sh, r),
                        Stream.of(z, c, s),
                        Stream.of(a, y, w, e, o)
                )
		).flatMap(s -> s).flatMap(s -> s).forEach(initials::remove);

		// expected to be empty
		System.out.println(initials);
	}
	
}
