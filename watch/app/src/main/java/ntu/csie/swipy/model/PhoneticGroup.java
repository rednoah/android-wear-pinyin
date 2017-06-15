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
                return new Final[]{a, ai, an, ang, u, ei, en, eng, i, ie, in, ing, o, ian, iao, ao};
            case P:
                return new Final[]{a, ai, an, ang, u, ei, en, eng, i, ie, in, ing, o, ian, iao, ao, null, ou};
            case M:
                return new Final[]{a, ai, an, ang, u, ei, en, eng, i, ie, in, ing, o, ian, iao, ao, e, ou, iu};
            case F:
                return new Final[]{a, null, an, ang, u, ei, en, eng, o, ou};
        }

        Final[] finals = initial.getFinals();

        // TODO: sort by zhuyin components???

        Comparator<Final> order = comparingInt(f -> f.name().length());
        Comparator<Final> alpha = comparing(f -> f.name());

        sort(finals, alpha);

        return finals;
    }

}
