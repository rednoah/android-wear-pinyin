package ntu.csie.swipy.model;

import static ntu.csie.swipy.model.Final.*;

public enum Initial {


    A(a, i, o, n, ng),
    O(o, u),
    E(e, r, n, ng),
    Y(i, a, ao, e, ou, an, in, ang, ing, ong, u, ue, uan),
    W(u, a, o, ai, ei, an, en, ang, eng),
    B(a, o, ai, ei, ao, an, en, ang, eng, i, iao, ie, ian, in, ing, u),
    P(a, o,    ai, ei, ao, ou, an, en, ang, eng, i, iao, ie,     ian, in, ing, u),
    M(a, o, e, ai, ei, ao, ou, an, en, ang, eng, i, iao, ie, iu, ian, in, ing, u),
    F(a, o, ei, ou, an, en, ang, eng, u),
    D(a, e, ai, ei, ao, ou, an, en, ang, eng, ong, i, iao, ie, iu, ian, ing, u, uo, ui, uan, un),
    T(a, e, ai, ei, ao, ou, an, ang, eng, ong, i, iao, ie, ian, ing, u, uo, ui, uan, un),
    N(a, e, ai, ei, ao, ou, an, en, ang, eng, ong, i, iao, ie, iu, ian, in, iang, ing, u, uo, uan),
    L(a, e, ai, ei, ao, ou, an, ang, eng, ong, i, ia, iao, ie, iu, ian, in, iang, ing, u, uo, uan, un, ü), // ignore 'üe' for, now, to, keep, finals, below, 6 * 4
    G(a, e, ai, ei, ao, ou, an, en, ang, eng, ong, u, ua, uo, uai, ui, uan, un, uang),
    K(a, e, ai, ei, ao, ou, an, en, ang, eng, ong, u, ua, uo, uai, ui, uan, un, uang),
    H(a, e, ai, ei, ao, ou, an, en, ang, eng, ong, u, ua, uo, uai, ui, uan, un, uang),
    Z(a, e, i, ai, ei, ao, ou, an, en, ang, eng, ong, u, uo, ui, uan, un),
    C(a, e, i, ai, ei, ao, ou, an, en, ang, eng, ong, u, uo, ui, uan, un),
    S(a, e, i, ai, ei, ao, ou, an, en, ang, eng, ong, u, uo, ui, uan, un),
    ZH(a, e, i, ai, ei, ao, ou, an, en, ang, eng, ong, u, ua, uo, uai, ui, uan, un, uang),
    CH(a, e, i, ai, ao, ou, an, en, ang, eng, ong, u, ua, uo, uai, ui, uan, un, uang),
    SH(a, e, i, ai, ei, ao, ou, an, en, ang, eng, u, ua, uo, uai, ui, uan, un, uang),
    R(e, i, ao, ou, an, en, ang, eng, ong, u, ua, uo, ui, uan, un),
    J(i, ia, iao, ie, iu, ian, in, iang, ing, iong, u, ue, uan, un),
    Q(i, ia, iao, ie, iu, ian, in, iang, ing, iong, u, ue, uan, un),
    X(i, ia, iao, ie, iu, ian, in, iang, ing, iong, u, ue, uan, un);


    private final Final[] finals;


    private Initial(Final... finals) {
        this.finals = finals;
    }


    public Final[] getFinals() {
        return finals.clone();
    }


}