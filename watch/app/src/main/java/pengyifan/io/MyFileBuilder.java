package pengyifan.io;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public interface MyFileBuilder {
    public BufferedReader getReader(File file, Encoding encoding);

    public BufferedWriter getWriter(File file, Encoding encoding);
}
