package pengyifan.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MyTextFileBuilder implements MyFileBuilder {

    @Override
    public BufferedReader getReader(File file, Encoding encoding) {
        try {
            return new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), encoding.toString()));

        }
        catch (Exception e) {
            System.err.println("failed open:" + file.toString());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BufferedWriter getWriter(File file, Encoding encoding) {
        try {
            return new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), encoding.toString()));

        }
        catch (Exception e) {
            System.err.println("failed open:" + file.toString());
            e.printStackTrace();
        }
        return null;
    }

}
