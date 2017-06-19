package ntu.csie.swipy.model;


import java.util.function.Function;

import static ntu.csie.swipy.model.Initial.*;

public enum Final {


    a(i -> "ㄚ"),
    o(i -> i == A ? "'ㄠ" : "ㄛ"),
    e(i -> i == Y ? "'ㄧㄝ" : "ㄜ"),
    r(i -> "ㄦ"),
    ai(i -> "ㄞ"),
    ei(i -> "ㄟ"),
    ao(i -> "ㄠ"),
    ou(i -> "ㄡ"),
    n(i -> i == A ? "'ㄢ" : "ㄋ"),
    ng(i -> i == A ? "'ㄤ" : "ㄫ"),
    an(i -> "ㄢ"),
    en(i -> "ㄣ"),
    ang(i -> "ㄤ"),
    eng(i -> "ㄥ"),
    ong(i -> i == Y ? "'ㄩㄥ" : "ㄨㄥ"),
    i(i -> i == A ? "'ㄞ" : i == R ? "'ㄖ" : i == S ? "'ㄙ" : i == Z ? "'ㄗ" : i == C ? "'ㄘ" : i == SH ? "'ㄕ" : i == ZH ? "'ㄓ" : i == CH ? "'ㄔ" : "ㄧ"),
    ia(i -> "ㄧㄚ"),
    iao(i -> "ㄧㄠ"),
    ie(i -> "ㄧㄝ"),
    iu(i -> "ㄧㄡ"),
    ian(i -> "ㄧㄢ"),
    in(i -> "ㄧㄣ"),
    iang(i -> "ㄧㄤ"),
    ing(i -> "ㄧㄥ"),
    iong(i -> "ㄩㄥ"),
    u(i -> i == Y ? "'ㄩ" : i == J ? "'ㄐㄩ" : i == X ? "'ㄒㄩ" : i == Q ? "'ㄑㄩ" : "ㄨ"),
    ua(i -> "ㄨㄚ"),
    uo(i -> "ㄨㄛ"),
    ue(i -> i == Y ? "'ㄩㄝ" : "ㄩㄝ"),
    uai(i -> "ㄨㄞ"),
    ui(i -> "ㄨㄟ"),
    un(i -> "ㄨㄣ"),      // yun  ... ㄩㄣ
    uan(i -> "ㄨㄢ"),     // yuan ... ㄩㄢ
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


}
