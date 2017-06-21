package woogle.cky;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

public class Utils {

    public static String join(String delim, List<? extends Object> list) {
        if (list.isEmpty())
            return new String();
        StringBuffer sb = new StringBuffer();
        Iterator<?> i = list.iterator();
        while (i.hasNext()) {
            sb.append(i.next());
            sb.append(delim);
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
        if (c1 < c2)
            return -1;
        else if (c1 == c2)
            return 0;
        else
            return 1;
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

    public static String getWorkDir() {
        try {
            return new File(".").getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void copy(Reader in, String filename) throws IOException {
        File f = new File(filename);
        File dir = f.getParentFile();
        dir.mkdirs();
        int numRead;
        char[] buffer = new char[1024];
        FileWriter out = new FileWriter(f);
        while ((numRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, numRead);
        }
        in.close();
        out.close();
    }

    private static Reader getJarReader(String filename) throws UnsupportedEncodingException {
        return new InputStreamReader(ClassLoader.getSystemResourceAsStream(filename));
    }

    // public static void main(String args[]) {
    // List<String> list = new ArrayList<String>();
    // list.add("aaa");
    // list.add("bbb");
    // System.out.println(join(" ", list));
    // }
}
