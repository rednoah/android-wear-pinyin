/**
 * Copyright (c) 2009 Yifan Peng. All rights reserved.
 */
package woogle.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class MyFileWriter {

    BufferedWriter writer;

    int            lineno;

    File           file;

    public MyFileWriter(File file, Encoding encoding, MyFileBuilder builder) {
        System.err.println("open file:" + file.toString());
        this.file = file;
        this.writer = builder.getWriter(file, encoding);
        this.lineno = 0;
    }

    public void print(Object o) {
        try {
            this.writer.write(o.toString());
            this.lineno++;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void println(Object o) {
        this.print(o.toString());
        this.println();
    }

    public void println() {
        try {
            this.writer.newLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        System.err.println("close file:" + this.file.toString());
        System.err.println(lineno + " lines written.");
        try {
            this.writer.flush();
            this.writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
