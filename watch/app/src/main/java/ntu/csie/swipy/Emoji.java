package ntu.csie.swipy;


import java.util.Arrays;
import java.util.stream.Collector;

import static java.util.Arrays.binarySearch;

public class Emoji {


    private static final int[] MAPPING_CODE_POINTS = "!#$%&'()*+,-./0123456789:;=?@^".codePoints().sorted().toArray();
    private static final int[] EMOJI_CODE_POINTS = "😀😝🤔😴😌😡😭😲😳😷😈👻😱😎😻💋🍷🍻💰💖💔👍👎💪👌🙏🌟🎓💯🚀".codePoints().toArray();


    public static String mapPunctuation(String s) {
        return s.codePoints().map(c -> {
            int i = binarySearch(MAPPING_CODE_POINTS, c);
            if (i >= 0) {
                return EMOJI_CODE_POINTS[i];
            }
            return c;
        }).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }


    public static String deleteLastCodePoint(String s) {
        return s.codePoints().limit(s.codePoints().count() - 1).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }


}
