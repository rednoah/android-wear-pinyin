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


    public static Key[] getSurroundingKeys(Key key) {
        switch (key) {
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
                        Y, H, J, K, I
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
        }

        return new Key[0];
    }


    public static Key[] getSurroundingKeysForced(Key key) {
        switch (key) {
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
                        U, Y, H
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


    public static Key[] getSeparatorKeys() {
        return new Key[]{
                Z, X, C
        };
    }


}
