package ntu.csie.swipy.model;


import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

import static java.util.Arrays.sort;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static ntu.csie.swipy.model.Initial.*;
import static ntu.csie.swipy.model.Final.*;


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
                        e, iang, ou, ong,
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

            case G:
            case K:
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
        }

        Final[] finals = initial.getFinals();

        // TODO: sort by zhuyin components???

        Comparator<Final> order = comparingInt(f -> f.name().length());
        Comparator<Final> alpha = comparing(f -> f.name());

        sort(finals, alpha);

        return finals;
    }

}
