package woogle.util;

import java.util.List;

public class Utils {

    public static String join(String delim, List<? extends Object> list) {
        StringBuilder sb = new StringBuilder();
        for(Object o: list) {
            sb.append(o + delim);
        }
        return sb.substring(0, sb.length() - delim.length());
    }

    public static String join(String delim, Object[] list) {
        if (list.length == 0)
            return new String();
        StringBuffer sb = new StringBuffer();
        for (Object o : list) {
            sb.append(o);
            sb.append(delim);
        }
        return sb.substring(0, sb.length() - delim.length());
    }

    public static <T extends Object> int compare(Comparable<T> c1, T c2) {
        if (c1 == null && c2 == null)
            return 0;
        else if (c1 != null && c2 == null)
            return -1;
        else if (c1 == null && c2 != null)
            return 1;
        else
            return c1.compareTo(c2);
    }

    public static int compare(int c1, int c2) {
        return c1 - c2;
    }

    public static int compare(float c1, float c2) {
        if (c1 < c2)
            return -1;
        else if (c1 == c2)
            return 0;
        else
            return 1;
    }

    public static <T extends Object> boolean equal(T c1, T c2) {
        if (c1 == null && c2 == null)
            return true;
        else if (c1 != null && c2 == null)
            return false;
        else if (c1 == null && c2 != null)
            return false;
        else
            return c1.equals(c2);
    }

    /**
     * lamda*f1 + (1-lamda)*f2
     * 
     * @param f1
     * @param f2
     * @param lamda
     * @return
     */
    public static double linearInter(double f1, double f2, float lamda) {
        if (f1 == Float.NEGATIVE_INFINITY && f2 == Float.NEGATIVE_INFINITY)
            return Float.NEGATIVE_INFINITY;
        else if (f1 != Float.NEGATIVE_INFINITY && f2 == Float.NEGATIVE_INFINITY)
            return f1;
        else if (f1 == Float.NEGATIVE_INFINITY && f2 != Float.NEGATIVE_INFINITY)
            return f2;
        else
            return lamda * f1 + (1 - lamda) * f2;
    }

    // public static void main(String args[]) {
    // List<String> list = new ArrayList<String>();
    // list.add("aaa");
    // list.add("bbb");
    // System.out.println(join(" ", list));
    // }
}
