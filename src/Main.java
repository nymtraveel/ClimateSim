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


        ReadFile t = new ReadFile();
        float[][] heightMap = ReadFile.readFile("Heightmap.txt", "\n");
        heightMap = dreheArray(heightMap);

        long start = System.currentTimeMillis();
        ClimateSimulator climate = new ClimateSimulator(heightMap);

        JFrame f = new JFrame();
        JFrame f2 = new JFrame();
        JFrame f3 = new JFrame();

        JPanel panel = new DrawPanel(climate.getClimate(), "climate");
        JPanel panel2 = new DrawPanel(climate.getHumidity(), "humidity");
        //JPanel panel2 = new DrawPanel(climate.distanceFrom("water",5), "distanceMap");
        JPanel panel3 = new DrawPanel(heightMap,  "heightmap");
        System.out.println("Total time: " + (System.currentTimeMillis() - start) + " ms");
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
        f2.setTitle("Humidity");

        f3.setDefaultCloseOperation( EXIT_ON_CLOSE );
        f3.setResizable(true);
        f3.add( panel3 );
        f3.setVisible( true );
        f3.setTitle("Heightmap");
    }

    public static float[][] dreheArray(float[][] array) {
        float[][] neuesArray = new float[array[0].length][array.length];

        for (int i=0; i<neuesArray.length; i++) {
            for (int j=0; j<neuesArray[0].length; j++) {
                neuesArray[i][j] = array[j][array[j].length-i-1];
            }
        }

        return neuesArray;
    }


}
