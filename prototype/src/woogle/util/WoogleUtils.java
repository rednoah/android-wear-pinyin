package woogle.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import woogle.spi.WoogleLog;

public class WoogleUtils {
    public static String getUserHomeDir() {
        return System.getProperty("user.home");
    }

    public static String getWorkDir() {
        String dir = getUserHomeDir() + "/.woogle";
        new File(dir).mkdirs();
        return dir;
    }

    public static File getInstalledFile(String filename)
            throws IOException {
        String installedFilename = getWorkDir() + "/" + filename;
        File f = new File(installedFilename);
        WoogleLog.log("getInstalledFile: " + f.getAbsolutePath());
        if (!f.exists()) {
            try {
                copy(getJarReader(filename), installedFilename);
            }
            catch (IOException e) {
                WoogleLog.log(null, e.getMessage());
                e.printStackTrace();
            }
        }
        return f;
    }

    private static Reader getJarReader(String filename)
            throws UnsupportedEncodingException {
        return new InputStreamReader(ClassLoader
                .getSystemResourceAsStream(filename));
    }

    private static void copy(Reader in, String filename)
            throws IOException {
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
}
