/**
 * Created with IntelliJ IDEA.
 * User: Florian
 * Date: 06.12.12
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */

import java.io.File;
import java.util.Scanner;

public class ReadFile {

    public static float[][] readFile(String file, String delimiter, int size)
                    throws Exception {
        return(readValues(new java.io.FileInputStream(file), delimiter, size));
    }


    public static float[][] readValues(java.io.InputStream in, String delimiter, int size)
            throws  java.io.FileNotFoundException,
            java.io.IOException,
            java.lang.NumberFormatException {
        String thisLine;
        java.io.BufferedInputStream s = new java.io.BufferedInputStream(in);
        java.io.BufferedReader myInput = new java.io.BufferedReader
                (new java.io.InputStreamReader(s));

        int index = 0;
        float min=0;
        float max=0;
        float[][] theMap = new float[size][size];
        long start = System.currentTimeMillis();

        while ((thisLine = myInput.readLine()) != null) {

            // scan it line by line
            java.util.StringTokenizer st = new java.util.StringTokenizer(thisLine, delimiter);
            float a = Float.valueOf(st.nextToken());
            theMap[index/size][index%size] = a;
            //if (a>8) System.out.println(a + " at " +index%513 + " " + index/513);
            index++;
            min = a<min ? a:min;
            max = a>max ? a:max;
            if (index/size>size-1)break;
        }
        return(theMap);
    }
}