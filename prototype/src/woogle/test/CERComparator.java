package woogle.test;

import java.io.File;

import pengyifan.io.Encoding;
import pengyifan.io.MyFileFactory;
import pengyifan.io.MyFileReader;


public class CERComparator {
    /**
     * @param args
     */
    public static void main(String args[]) {
        File f1;

        f1 = new File("Hub4_Test_5.gld");
        MyFileReader reader1 = MyFileFactory.getFileReader(f1, Encoding.GBK);
        f1 = new File("Hub4_Test_1986_2007_2000_1.txt");
        MyFileReader reader2 = MyFileFactory.getFileReader(f1, Encoding.GBK);
        f1 = new File("Hub4_Test_1986_2007_2000_0.85.txt");
        MyFileReader reader3 = MyFileFactory.getFileReader(f1, Encoding.GBK);

        while (reader1.hasNext() && reader2.hasNext() && reader3.hasNext()) {
            String line1 = reader1.nextLine();
            String line2 = reader2.nextLine();
            String line3 = reader3.nextLine();
            line1 = line1.trim();
            line2 = line2.trim();
            line3 = line3.trim();
            
            char[] cs1 = line1.toCharArray();
            char[] cs2 = line2.toCharArray();
            char[] cs3 = line3.toCharArray();
            for (int i = 0; i < cs1.length; i++) {
                if (i >= cs2.length) {
                    System.out.println("line no.: " + reader2.lineno);
                    break;
                }
                if (cs1[i] != cs2[i] && cs1[i] == cs3[i]){
                    System.out.println("line no.: " + reader3.lineno);
                    System.out.println(line1);
                    System.out.println(line2);
                    System.out.println(line3);
                    System.out.println();
                    break;
                }
            }
        }
    }
}
