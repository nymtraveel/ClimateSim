/**
 * Created with IntelliJ IDEA.
 * User: Florian
 * Date: 06.12.12
 * Time: 11:08
 * To change this template use File | Settings | File Templates.
 */
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Main
{
    public static void main( String[] args ) throws Exception {

        long start = System.currentTimeMillis();

        long start1 = System.currentTimeMillis();
        float[][] heightMap = ReadFile.readFile("heightmap5.txt", "\n");
        int hmSize = heightMap.length;
        //heightMap = shiftArray(rotateArray(heightMap),-50,-100);
        //try also other combinations with shift and rotate
        //heightMap = rotateArray(heightMap);

        System.out.println("Reading file: " + (System.currentTimeMillis() - start1) + " ms");

        ClimateSimulator climate = new ClimateSimulator(heightMap);

        JFrame c1 = new JFrame();
        JFrame c2 = new JFrame();
        JFrame c3 = new JFrame();
        JFrame hum = new JFrame();
        JFrame steep = new JFrame();
        JFrame map = new JFrame();
        /*
        JPanel panel = new DrawPanel(climate.getTpoles(), "climate");
        c1.add(panel);
        c1.setTitle("Distance to Poles");
        c1.setSize(hmSize + 17, hmSize + 39);
        c1.setDefaultCloseOperation(EXIT_ON_CLOSE);
        c1.setResizable(true);
        c1.setVisible(true);

        JPanel panel2 = new DrawPanel(climate.getTequator(), "climate");
        c2.add(panel2);
        c2.setTitle("Distance to equator");
        c2.setSize(hmSize + 17, hmSize + 39);
        c2.setDefaultCloseOperation(EXIT_ON_CLOSE);
        c2.setResizable(true);
        c2.setVisible(true);
          */
        JPanel panel3 = new DrawPanel(climate.getClimate(),  "climate");
        c3.add(panel3);
        c3.setTitle("Climate");
        c3.setSize(hmSize + 17, hmSize + 39);
        c3.setDefaultCloseOperation(EXIT_ON_CLOSE);
        c3.setResizable(true);
        c3.setVisible(true);

        JPanel panel4 = new DrawPanel(climate.getHumidity(),  "humidity");
        hum.add(panel4);
        hum.setTitle("Humidity");
        hum.setSize(hmSize + 17, hmSize + 39);
        hum.setDefaultCloseOperation(EXIT_ON_CLOSE);
        hum.setResizable(true);
        hum.setVisible(true);

        JPanel panel5 = new DrawPanel(climate.getMountainyness(),  "Steepness");
        steep.add(panel5);
        steep.setTitle("Steepness");
        steep.setSize(hmSize + 17, hmSize + 39);
        steep.setDefaultCloseOperation(EXIT_ON_CLOSE);
        steep.setResizable(true);
        steep.setVisible(true);

        JPanel panel6 = new DrawPanel(heightMap,  "heightmap");
        map.add(panel6);
        map.setTitle("Heightmap");
        map.setSize(hmSize + 17, hmSize + 39);
        map.setDefaultCloseOperation(EXIT_ON_CLOSE);
        map.setResizable(true);
        map.setVisible(true);

        System.out.println("Total time: " + (System.currentTimeMillis() - start) + " ms");
    }

    public static float[][] rotateArray(float[][] array) {
        float[][] newArray = new float[array[0].length][array.length];
        for (int i=0; i<newArray.length; i++) {
            for (int j=0; j<newArray[0].length; j++) {
                newArray[i][j] = array[j][array[j].length-i-1];
            }
        }
        return newArray;
    }

    public static float[][] shiftArray(float[][] array,int x,int y){
        int size = array.length;
        float[][] newArray = new float[size][size];
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                newArray[i][j] = array[(i+x+size)%size][(j+y+size)%size];
            }
        }
        return newArray;
    }


}
