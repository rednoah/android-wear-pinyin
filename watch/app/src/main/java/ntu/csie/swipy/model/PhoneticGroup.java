package ntu.csie.swipy.model;


import static ntu.csie.swipy.model.Final.*;
import static ntu.csie.swipy.model.Initial.*;


public class PhoneticGroup {


    public static Initial[][] getZhuyinGroups() {
        return new Initial[][]{
                {B, P, M, F},
                {D, T, N, L},
                {G, K, H},
                {J, Q, X},
                {A, Y, E, O},
                {Z, C, S, W},
                {ZH, CH, SH, R}
        };
    }


    public static Final[] getFinalGroups(Initial initial) {
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
                        u, ei, null, eng,
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
                        u, ei, en, eng,
                        ua, uai, null, uang,
                        i, null, null, ao,
                        e, null, ou, ong,
                        uo, ui, un, uan,
                };

            case CH:
                return new Final[]{
                        a, ai, an, ang,
                        u, null, en, eng,
                        ua, uai, null, uang,
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
                        u, ue, null, uan,
                        i, null, in, ing,
                        e, null, ou, ong,
                };

            case A:
                return new Final[]{
                        a, null, n, ng,
                        i, null, o, null
                };

            case E:
                return new Final[]{
                        e, r, n, ng,
                };

            case O:
                return new Final[]{
                        u, null, o, null,
                };

            case W:
                return new Final[]{
                        a, ai, an, ang,
                        u, ei, en, eng,
                        o, null, null, null,
                };
        }

        // can't happen
        throw new IllegalArgumentException("Initial: " + initial);
    }

}
