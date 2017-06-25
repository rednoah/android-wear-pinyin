package ntu.csie.swipy.model;


import static java.util.Arrays.stream;

public enum Key {

    A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, APOSTROPHE;


    public static Key forLetter(CharSequence cs) {
        String s = cs.toString().toUpperCase();

        if (APOSTROPHE.getLetter().equals(s)) {
            return APOSTROPHE;
        }

        return stream(values()).filter(k -> k.toString().equals(s)).findFirst().orElse(null);
    }


    public String getLetter() {
        switch (this) {
            case APOSTROPHE:
                return "'";
            default:
                return toString().toLowerCase();
        }
    }


    public Key[] getSurroundingKeys() {
        switch (this) {
            case H:
                return new Key[]{
                        Y, U, J, N, B, V, G
                };

            case A:
                return new Key[]{
                        Q, W, S, X, Z
                };

            case E:
                return new Key[]{
                        W, S, D, F, R
                };

            case U:
                return new Key[]{
                        Y, H, J, K, I, T
                };

            case V:
                return new Key[]{
                        C, F, G, H, N
                };

            case I:
                return new Key[]{
                        U, J, K, O
                };

            case O:
                return new Key[]{
                        I, K, L, P
                };

            case N:
                return new Key[]{
                        B, H, J, M
                };

            case G:
                return new Key[]{
                        H, B, V, C, F
                };

            case APOSTROPHE:
                return new Key[]{
                        Z, X, C
                };
        }

        return new Key[0];
    }


    public Key[] getSurroundingKeysForced() {
        switch (this) {
            case H:
                return new Key[]{
                        H, G
                };

            case A:
                return new Key[]{
                        A, Q, W, S
                };

            case E:
                return new Key[]{
                        E, R, D, F
                };

            case U:
                return new Key[]{
                        U, Y
                };

            case V:
                return new Key[]{
                        V, B
                };

            case I:
                return new Key[]{
                        I, J, K
                };

            case O:
                return new Key[]{
                        O, P, L
                };

            case N:
                return new Key[]{
                        N, M
                };

            case G:
                return new Key[]{
                        F, G
                };
        }

        return new Key[0];
    }


    public static Key getPhysicalApostropheKey() {
        return X;
    }


    public int getBackgroundGroup() {
        switch (this) {
            case A:
                return 1;
            case E:
                return 2;
            case U:
                return 3;
            case V:
                return 4;
            case I:
                return 2;
            case O:
                return 1;
            case N:
                return 4;
            case H:
                return 4;
            case G:
                return 2;
            default:
                return 0;

        }
    }


}
