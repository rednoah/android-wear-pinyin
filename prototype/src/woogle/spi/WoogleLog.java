package woogle.spi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;

public class WoogleLog {

	private static PrintWriter p;
	private static String last = "";
	private static int repeatCnt = 0;

	public static void init() {
		if (p != null)
			return;
		System.out.println("work dir=" + new File(".").getAbsolutePath());
		try {
			p = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
					new File(new File(".").getAbsolutePath(), "woogle.log")), "gbk"),
					true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void log(Object src, Object s) {
		String ss = s.toString();
		if (p != null) {
			if (last.equals(ss)) {
				repeatCnt++;
			} 
			else {
				if (repeatCnt > 0) {
					p.println("[repeat " + repeatCnt + "]");
					repeatCnt = 0;
				}
				String msg = WoogleLog.src(src) + ss;
				p.println(msg);
				System.out.println(msg);
				last = ss;
			}
		}
	}

	public static void log(Object src, String s, Exception e) {
		if (p != null) {
			p.println(src(src) + time() + s + " - ");
			e.printStackTrace(p);
		}
	}

	private static String src(Object src) {
		return "[" + src + "]";
	}

	private static String time() {
		return "[" + new Date() + "]";
	}
}
