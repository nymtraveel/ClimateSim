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


        float[][] heightMap = ReadFile.readFile("Heightmap.txt", "\n");
        heightMap = shiftArray(rotateArray(heightMap),-50,-100);
        //try also other combinations with shift and rotate
        //heightMap = rotateArray(heightMap);

        long start = System.currentTimeMillis();
        ClimateSimulator climate = new ClimateSimulator(heightMap);
        System.out.println("Total time: " + (System.currentTimeMillis() - start) + " ms");

        JFrame f = new JFrame();
        JFrame f2 = new JFrame();
        JFrame f3 = new JFrame();
        JPanel panel = new DrawPanel(climate.getClimate(), "climate");
        JPanel panel2 = new DrawPanel(climate.gett2(), "climate");
        JPanel panel3 = new DrawPanel(heightMap,  "heightmap");


        f.setSize( 600 , 600 );
        f2.setSize( 600 , 600 );
        f3.setSize( 600 , 600 );
        f.setDefaultCloseOperation( EXIT_ON_CLOSE );
        f.setResizable(true);
        f.add( panel );
        f.setVisible( true );
        f.setTitle("Climate Map");

        f2.setDefaultCloseOperation( EXIT_ON_CLOSE );
        f2.setResizable(true);
        f2.add( panel2 );
        f2.setVisible( true );
        f2.setTitle("t2");

        f3.setDefaultCloseOperation( EXIT_ON_CLOSE );
        f3.setResizable(true);
        f3.add( panel3 );
        f3.setVisible( true );
        f3.setTitle("Heightmap");
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
