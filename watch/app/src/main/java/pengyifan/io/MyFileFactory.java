/**
 * Copyright (c) 2009 Yifan Peng. All rights reserved.
 */
package pengyifan.io;

import java.io.File;

public class MyFileFactory {
    
    public static Encoding encoding = Encoding.GBK;

    public static MyFileReader getFileReader(File file) {
        return getFileReader(file, encoding);
    }

    public static MyFileReader getFileReader(File file, Encoding encoding) {
        String filename = file.getName();
        if (filename.endsWith(".zip"))
            return new MyFileReader(file, encoding, new MyGZFileBuilder());
        else if (filename.endsWith(".bz2"))
            return new MyFileReader(file, encoding, new MyBZ2FileBuilder());
        else
            return new MyFileReader(file, encoding, new MyTextFileBuilder());
    }

    public static MyFileWriter getFileWriter(File file) {
        return getFileWriter(file, Encoding.GBK);
    }

    public static MyFileWriter getFileWriter(File file, Encoding encoding) {
        String filename = file.getName();
        if (filename.endsWith(".zip"))
            return new MyFileWriter(file, encoding, new MyGZFileBuilder());
        else if (filename.endsWith(".bz2"))
            return new MyFileWriter(file, encoding, new MyBZ2FileBuilder());
        else
            return new MyFileWriter(file, encoding, new MyTextFileBuilder());
    }

//    public static void main(String args[]) {
//        File file = new File("e:\\x.bz2");
//        MyFileWriter writer = MyFileFactory.getFileWriter(file);
//        for (int i = 0; i < 1000000; i++)
//            writer.println(Integer.toString(i));
//        writer.close();
//
//        file = new File("e:\\x.txt");
//        writer = MyFileFactory.getFileWriter(file);
//        for (int i = 0; i < 1000000; i++)
//            writer.println(Integer.toString(i));
//        writer.close();
//
//        file = new File("e:\\x.zip");
//        writer = MyFileFactory.getFileWriter(file);
//        for (int i = 0; i < 1000000; i++)
//            writer.println(Integer.toString(i));
//        writer.close();
//
//        // MyFileReader reader = MyFileBuilder.getFileReader(file);
//        // System.out.println(reader.nextLine());
//        // reader.close();
//    }
}
