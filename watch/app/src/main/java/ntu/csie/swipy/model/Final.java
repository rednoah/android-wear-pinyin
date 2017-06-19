package ntu.csie.swipy.model;


import java.util.function.Function;

import static ntu.csie.swipy.model.Initial.*;


public enum Final {


    a(i -> newZhuyinSyllable(i, "ㄚ", A, "'ㄚ")),
    o(i -> newZhuyinSyllable(i, "ㄛ", O, "'ㄛ", A, "'ㄠ")),
    e(i -> newZhuyinSyllable(i, "ㄜ", E, "'ㄜ", Y, "'ㄧㄝ")),
    r(i -> "ㄦ"),
    ai(i -> "ㄞ"),
    ei(i -> "ㄟ"),
    ao(i -> "ㄠ"),
    ou(i -> "ㄡ"),
    n(i -> newZhuyinSyllable(i, "ㄋ", A, "'ㄢ")),
    ng(i -> newZhuyinSyllable(i, "ㄫ", A, "'ㄤ")),
    an(i -> "ㄢ"),
    en(i -> "ㄣ"),
    ang(i -> "ㄤ"),
    eng(i -> "ㄥ"),
    ong(i -> newZhuyinSyllable(i, "ㄨㄥ", Y, "'ㄩㄥ")),
    i(i -> newZhuyinSyllable(i, "ㄧ", Y, "'ㄧ", A, "'ㄞ", R, "'ㄖ", S, "'ㄙ", Z, "'ㄗ", C, "'ㄘ", SH, "'ㄕ", ZH, "'ㄓ", CH, "'ㄔ")),
    ia(i -> "ㄧㄚ"),
    iao(i -> "ㄧㄠ"),
    ie(i -> "ㄧㄝ"),
    iu(i -> "ㄧㄡ"),
    ian(i -> "ㄧㄢ"),
    in(i -> newZhuyinSyllable(i, "ㄧㄣ", Y, "'ㄧㄣ")),
    iang(i -> "ㄧㄤ"),
    ing(i -> newZhuyinSyllable(i, "ㄧㄥ", Y, "'ㄧㄥ")),
    iong(i -> "ㄩㄥ"),
    u(i -> newZhuyinSyllable(i, "ㄨ", Y, "'ㄩ", J, "ㄩ", X, "ㄩ", Q, "ㄩ")),
    ua(i -> "ㄨㄚ"),
    uo(i -> "ㄨㄛ"),
    ue(i -> newZhuyinSyllable(i, "ㄩㄝ", Y, "'ㄩㄝ")),
    uai(i -> "ㄨㄞ"),
    ui(i -> "ㄨㄟ"),
    un(i -> newZhuyinSyllable(i, "ㄨㄣ", Y, "'ㄩㄣ", J, "ㄩㄣ", X, "ㄩㄣ", Q, "ㄩㄣ")),
    uan(i -> newZhuyinSyllable(i, "ㄨㄢ", Y, "'ㄩㄢ", J, "ㄩㄢ", X, "ㄩㄢ", Q, "ㄩㄢ")),
    uang(i -> "ㄨㄤ"),
    ü(i -> "ㄩ"),
    üe(i -> "ㄩㄝ");


    private Function<Initial, String> zhuyin;


    Final(Function<Initial, String> zhuyin) {
        this.zhuyin = zhuyin;
    }


    public String getZhuyin(Initial i) {
        return zhuyin.apply(i);
    }


    private static String newZhuyinSyllable(Initial i, String yin, Object... syllables) {
        for (int n = 0; n < syllables.length; n += 2) {
            if (i == syllables[n]) {
                return syllables[n + 1].toString();
            }
        }
        return yin;
    }


}
