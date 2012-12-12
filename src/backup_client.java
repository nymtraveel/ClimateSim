/**
 * Created with IntelliJ IDEA.
 * User: Florian
 * Date: 06.12.12
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;

public class backup_client {

    //Read config file
    public static String ReadConfig(String file) throws IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        String data = in.readLine();
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void ReadFile(String file) throws IOException
    {
        File fileHandle = new File(file);
        long length = fileHandle.length();

        DataInputStream in = new DataInputStream(new BufferedInputStream(
                new FileInputStream(file)));
        while (in.available() != 0)
        {
            System.out.print((char) in.readByte());
        }
    }
}