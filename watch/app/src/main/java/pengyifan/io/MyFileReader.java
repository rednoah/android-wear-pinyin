/**
 * Copyright (c) 2009 Yifan Peng. All rights reserved.
 */
package pengyifan.io;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class MyFileReader {

    BufferedReader reader;

    public int lineno;

    String next;

    File file;

    public static int BUF_SIZ = 65535;

    char buf[];

    LinkedList<String> lines;

    public static String newline = System.getProperty("line.separator");

    public MyFileReader(File file, Encoding encoding, MyFileBuilder builder) {
        Log.d("MyFileReader", "OPEN: " + file);

        this.file = file;
        this.reader = builder.getReader(file, encoding);
        this.lineno = 0;
        this.buf = new char[BUF_SIZ];
        this.lines = new LinkedList<String>();
        readMore();
        this.next = lines.removeFirst();
    }


    public String nextLine() {
        String current = next;
        try {
            if (lines.isEmpty()) {
                readMore();
            }
            this.next = lines.removeFirst();
            lineno++;
        } catch (NoSuchElementException e) {
            this.next = null;
        }
        return current;
    }

    public boolean hasNext() {
        return next != null;
    }

    public void close() {
        Log.d("MyFileReader", "CLOSE: " + file + "(" + lineno + ")");
        try {
            reader.close();
        } catch (IOException e) {
            Log.d("MyFileReader", "CLOSE", e);
        }
    }

    public int getLineNumber() {
        return lineno;
    }

    private void readMore() {
        try {
            int size = reader.read(buf, 0, BUF_SIZ);
            if (size == -1)
                return;
            String bufStr = new String(buf, 0, size);
            if (size == BUF_SIZ)
                bufStr += reader.readLine();
            StringTokenizer st = new StringTokenizer(bufStr, newline);
            while (st.hasMoreTokens())
                lines.add(st.nextToken());
        } catch (IOException e) {
            Log.d("MyFileReader", "READ FAILED", e);

        }
    }
}
