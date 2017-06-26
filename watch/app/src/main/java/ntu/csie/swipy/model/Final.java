package ntu.csie.swipy.model;


import java.util.function.Function;

import static ntu.csie.swipy.model.Initial.*;


public enum Final {


    a(i -> newZhuyinSyllable(i, "ㄚ", A, "'ㄚ")),
    o(i -> newZhuyinSyllable(i, "ㄛ", O, "'ㄛ", A, "'ㄠ")),
    e(i -> newZhuyinSyllable(i, "ㄜ", E, "'ㄜ", Y, "'ㄧㄝ")),
    r(i -> newZhuyinSyllable(i, "ㄦ", E, "'ㄦ")),
    ai(i -> "ㄞ"),
    ei(i -> "ㄟ"),
    ao(i -> "ㄠ"),
    ou(i -> "ㄡ"),
    n(i -> newZhuyinSyllable(i, "ㄋ", A, "'ㄢ", E, "'ㄣ")),
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
    u(i -> newZhuyinSyllable(i, "ㄨ", O, "'ㄡ", Y, "'ㄩ", J, "ㄩ", X, "ㄩ", Q, "ㄩ")),
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


    public static Final[] getPhoneticGroups(Initial initial) {
        switch (initial) {
            case B:
                return new Final[]{
                        a, ai, an, ang,
                        u, ei, en, eng,
                        i, ie, in, ing,
                        o, ian, iao, ao
                };

            case P:
                return new Final[]{
                        a, ai, an, ang,
                        u, ei, en, eng,
                        i, ie, in, ing,
                        o, ian, iao, ao,
                        null, ou, null, null
                };

            case M:
                return new Final[]{
                        a, ai, an, ang,
                        u, ei, en, eng,
                        i, ie, in, ing,
                        o, ian, iao, ao,
                        e, ou, iu, null
                };

            case F:
                return new Final[]{
                        a, null, an, ang,
                        u, ei, en, eng,
                        o, ou, null, null
                };

            case D:
                return new Final[]{
                        a, ai, an, ang,
                        u, ei, en, eng,
                        i, ie, iu, ing,
                        null, ian, iao, ao,
                        e, null, ou, ong,
                        uo, ui, un, uan
                };

            case T:
                return new Final[]{
                        a, ai, an, ang,
                        u, null, null, eng,
                        i, ie, null, ing,
                        null, ian, iao, ao,
                        e, null, ou, ong,
                        uo, ui, un, uan
                };

            case N:
                return new Final[]{
                        a, ai, an, ang,
                        u, ei, en, eng,
                        i, ie, iu, ing,
                        in, ian, iao, ao,
                        e, iang, null, ong,
                        uo, ü, üe, uan
                };

            case L:
                return new Final[]{
                        a, ai, an, ang,
                        u, ei, un, eng,
                        i, ie, iu, ing,
                        in, ian, iao, ao,
                        e, iang, ou, ong,
                        uo, ü, üe, uan
                };

            case K:
                return new Final[]{
                        a, ai, an, ang,
                        u, null, en, eng,
                        ua, uai, null, uang,
                        null, null, null, ao,
                        e, null, ou, ong,
                        uo, ui, un, uan,
                };

            case G:
            case H:
                return new Final[]{
                        a, ai, an, ang,
                        u, ei, en, eng,
                        ua, uai, null, uang,
                        null, null, null, ao,
                        e, null, ou, ong,
                        uo, ui, un, uan,
                };

            case Z:
                return new Final[]{
                        a, ai, an, ang,
                        u, ei, en, eng,
                        i, null, null, null,
                        e, ao, ou, ong,
                        uo, ui, un, uan,
                };

            case C:
            case S:
                return new Final[]{
                        a, ai, an, ang,
                        u, null, en, eng,
                        i, null, null, null,
                        e, ao, ou, ong,
                        uo, ui, un, uan,
                };

            case J:
            case Q:
            case X:
                return new Final[]{
                        u, ue, un, uan,
                        i, ie, iu, ing,
                        in, ian, iao, ia,
                        null, iang, iong, null,
                };

            case ZH:
                return new Final[]{
                        a, ai, an, ang,
                        u, null, en, eng,
                        ua, uai, null, uang,
                        i, null, null, ao,
                        e, null, ou, ong,
                        uo, ui, un, uan,
                };

            case CH:
                return new Final[]{
                        a, ai, an, ang,
                        u, null, en, eng,
                        null, uai, null, uang,
                        i, null, null, ao,
                        e, null, ou, ong,
                        uo, ui, un, uan,
                };

            case SH:
                return new Final[]{
                        a, ai, an, ang,
                        u, ei, en, eng,
                        ua, uai, null, uang,
                        i, null, null, ao,
                        e, null, ou, null,
                        uo, ui, un, uan,
                };

            case R:
                return new Final[]{
                        null, null, an, ang,
                        u, null, en, eng,
                        i, null, null, ao,
                        e, null, ou, ong,
                        uo, ui, un, uan,
                };

            case Y:
                return new Final[]{
                        a, ao, an, ang,
                        u, ue, un, uan,
                        i, null, in, ing,
                        e, null, ou, ong,
                };

            case A:
                return new Final[]{
                        a, null, n, ng,
                        i, null, null, o
                };

            case E:
                return new Final[]{
                        e, r, n, null,
                };

            case O:
                return new Final[]{
                        u, null, null, o,
                };

            case W:
                return new Final[]{
                        a, ai, an, ang,
                        u, ei, en, eng,
                        null, null, null, o,
                };
        }

        // can't happen
        throw new IllegalArgumentException("Initial: " + initial);
    }


    public static Final[][] getQwertyGroups(Initial initial) {
        switch (initial) {
            case B:
                return new Final[][]{
                        {ai, ao, ei, u, i, o},
                        {a, en, eng, ie, ian, iao},
                        {an, ang, in, ing}
                };


            case P:
                return new Final[][]{
                        {ai, ao, ei, u, i, o, ou},
                        {a, en, eng, ie, ian, iao},
                        {an, ang, in, ing}
                };


            case M:
                return new Final[][]{
                        {ai, ao, e, ei, u, i, iu, o},
                        {a, en, eng, ie, ian, iao, ou},
                        {an, ang, in, ing}
                };

            case F:
                return new Final[][]{
                        {u, o, ou},
                        {a, an, en, ei},
                        {ang, eng}
                };


            case D:
                return new Final[][]{
                        {ai, ao, e, ei, uo, u, ui, i, ie, ou},
                        {a, an, en, eng, un, uan, iu, ong},
                        {ang, ian, iao, ing}
                };

            case T:
                return new Final[][]{
                        {ai, e, un, uo, u, ui, i, ie, ou},
                        {a, eng, uan, ian, iao, ong},
                        {ao, an, ang, ing}
                };


            case N:
                return new Final[][]{
                        {ai, ao, e, ei, uo, u, i, iu, ong},
                        {a, an, en, uan, ian, iang, in},
                        {ang, eng, ü, üe, ie, iao, ing}
                };


            case L:
                return new Final[][]{
                        {ai, ao, e, ei, uo, u, i, iu, in, ou},
                        {a, an, un, uan, ian, iang, ong},
                        {ang, eng, ü, üe, ie, iao, ing}
                };


            case K:
                return new Final[][]{
                        {ai, ao, e, u, ui, uo, ou},
                        {a, an, en, un, ua, uai, ong},
                        {ang, eng, uan, uang}
                };


            case G:
            case H:
                return new Final[][]{
                        {ai, ao, e, ei, u, ui, uo, ou},
                        {a, an, en, ua, uai, un, ong},
                        {ang, eng, uan, uang}
                };


            case Z:
                return new Final[][]{
                        {ai, ao, e, ei, u, ui, i, ou},
                        {a, an, en, un, uo, ong},
                        {ang, eng, uan}
                };


            case C:
            case S:
                return new Final[][]{
                        {ai, ao, e, u, ui, i, ou},
                        {a, an, en, un, uo, ong},
                        {ang, eng, uan}
                };


            case J:
            case Q:
            case X:
                return new Final[][]{
                        {u, ue, i, ie, iu},
                        {un, uan, ia, in, ing},
                        {ian, iang, iao, iong}
                };


            case ZH:
                return new Final[][]{
                        {ai, ao, e, uo, u, ui, i, ou},
                        {a, en, eng, un, uan, ong},
                        {an, ang, ua, uai, uang}
                };

            case CH:
                return new Final[][]{
                        {ai, ao, e, u, ui, i, ou},
                        {a, an, en, un, uan, uo, ong},
                        {ang, eng, uai, uang}
                };


            case SH:
                return new Final[][]{
                        {ai, ao, e, ei, uo, u, ui, i, ou},
                        {a, en, eng, ua, uai},
                        {an, ang, un, uan, uang}
                };


            case R:
                return new Final[][]{
                        {ao, e, i, ui, u, uo, ou},
                        {an, en, un, ong},
                        {ang, eng, uan}
                };


            case Y:
                return new Final[][]{
                        {e, u, ue, i, ou},
                        {a, ao, un, uan, ong},
                        {an, ang, in, ing}
                };


            case A:
                return new Final[][]{
                        {i, o},
                        {a},
                        {n, ng}
                };

            case E:
                return new Final[][]{
                        {e, r},
                        {n}
                };

            case O:
                return new Final[][]{
                        {u, o}
                };

            case W:
                return new Final[][]{
                        {ai, ei, u, o},
                        {a, en, eng},
                        {an, ang}
                };
        }

        // can't happen
        throw new IllegalArgumentException("Initial: " + initial);
    }


    public int getColor() {
        switch (this) {
            case a:
            case ai:
            case an:
            case ang:
            case ao:
                return 1;
            case e:
            case ei:
            case en:
            case eng:
                return 2;
            case i:
            case ia:
            case ian:
            case iang:
            case iao:
            case ie:
            case in:
            case ing:
            case iong:
            case iu:
                return 3;
            case n:
            case ng:
                return 5;
            case o:
            case ong:
            case ou:
                return 5;
            case r:
                return 3;
            case u:
            case ua:
            case uai:
            case uan:
            case uang:
            case ue:
            case ui:
            case un:
            case uo:
                return 4;
            case ü:
            case üe:
                return 0;
        }

        // can't happen
        throw new IllegalArgumentException("Initial: " + this);
    }

}
