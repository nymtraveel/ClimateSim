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

    public static float[][] Read(String address) throws Exception {
        File input_file = new File (address);

        float[][] theMap = new float[513][513];
        int index = 0;

        long start = System.currentTimeMillis();
        float min=0;
        float max=0;
        Scanner scanner = new Scanner(input_file);

        while(true) {
                float a = scanner.nextFloat();
                theMap[index%513][index/513] = a;
                index++;
                min = a<min ? a:min;
                max = a>max ? a:max;
                //System.out.println(a);
            if(index<-1){break;}
        }
        System.out.println ("End of File");
        System.out.println("passed time:");
        System.out.println(System.currentTimeMillis() - start);

        System.out.println("Min Value:");
        System.out.println(min);
        System.out.println("Max Value:");
        System.out.println(max);
        return theMap;
    }

        public static float[][] readFile(String file, String delimiter)
                throws Exception {
            return(readValues(new java.io.FileInputStream(file), delimiter));
        }


    public static float[][] readValues(java.io.InputStream in, String delimiter)
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
        float[][] theMap = new float[512][512];
        long start = System.currentTimeMillis();

        while ((thisLine = myInput.readLine()) != null) {

            // scan it line by line
            java.util.StringTokenizer st = new java.util.StringTokenizer(thisLine, delimiter);
            float a = Float.valueOf(st.nextToken());
            theMap[index/512][index%512] = a;
            //if (a>8) System.out.println(a + " at " +index%513 + " " + index/513);
            index++;
            min = a<min ? a:min;
            max = a>max ? a:max;
            if (index/512>511)break;
        }
        /*
        System.out.println ("End of File");
        System.out.println("passed time:");
        System.out.println(System.currentTimeMillis() - start);

        System.out.println("Min Value:");
        System.out.println(min);
        System.out.println("Max Value:");
        System.out.println(max);
        */

        return(theMap);
    }
}