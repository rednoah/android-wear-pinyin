package woogle.spi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class WoogleLog {

    private static PrintWriter p;

    public static void init() {
        if (p != null)
            return;
        try {
            File logFile = new File(new File(".").getAbsolutePath(),
                    "woogle.log");
            p = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
                    logFile), "gbk"), true);
            log("log file: " + logFile.getAbsolutePath());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

     public static void log(Object s) {
        if (p != null) {
            p.println(s);
            System.out.println(s);
        }
    }

    public static void log(Object src, String msg) {
        if (p != null) {
            msg = WoogleLog.src(src) + msg;
            p.println(msg);
            System.out.println(msg);
        }
    }

    private static String src(Object src) {
        return "[" + src.getClass().getName() + "]";
    }
}
