/**
 * Created with IntelliJ IDEA.
 * User: Florian
 * Date: 06.12.12
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
class DrawPanel extends JPanel
{
    private float[][] array;
    String type;

    DrawPanel(float[][] map, String input){
        this.array= map;
        this.type = input;
    }


    @Override
    protected void paintComponent( Graphics g )
    {
        float max=0;
        super.paintComponent( g );
        float tempColor;
        for (int height= 0;height < array.length;height++){
            for (int width= 0;width < array[height].length;width++){
                tempColor = (2+array[height][width])/4;
                if (max < tempColor){max=tempColor;}
                g.setColor( computeColor(array[height][width], type));
                g.drawRect(width, height, 1, 1);
            }
        }
    }

    private Color computeColor (float value, String type){
        if (type.equals("climate")) {
            value -= 0.666;
            value *= 3;

            if (value>1){
                //System.out.println("Ouch, Hot! " + value);
                value = 1;
            }
            if (value< -2) value = -2;

            float r,g,b;

            if (value<-1) {
                b=1;
                r=g=-value-1;
            }
            else if (value<0){
                r= value+1;
                g= value+1;
                b= Math.abs(value);
            }
            else{
                r= 1;
                g= Math.abs(value-1);
                b= 0;
            }
            return new Color(r,g,b);
        }
        else if (type.equals("heightmap")){ //copied from Laurie's plate tectonic simulation
            float COLOR_STEP = 2.0f;
            if (value < 0.5)
                return new Color(0.0f, 0.0f, 0.25f + 1.5f * value);
            else if (value < 1.0)
                return new Color(0.0f, 2 * (value - 0.5f), 1.0f);
            else
            {
                value -= 1.0;
                if (value < 1.0 * COLOR_STEP)
                    return new Color(0.0f, 0.5f + 0.5f * value / COLOR_STEP, 0.0f);
                else if (value < 1.5 * COLOR_STEP)
                    return new Color(2 * (value - 1.0f * COLOR_STEP) / COLOR_STEP, 1.0f, 0.0f);
                else if (value < 2.0 * COLOR_STEP)
                    return new Color(1.0f, 1.0f - (value - 1.5f * COLOR_STEP) /  COLOR_STEP, 0);
                else if (value < 3.0 * COLOR_STEP)
                    return new Color(1.0f - 0.5f * (value - 2.0f * COLOR_STEP) / COLOR_STEP,  0.5f - 0.25f * (value - 2.0f * COLOR_STEP) / COLOR_STEP, 0);
                else if (value < 5.0 * COLOR_STEP)
                    return new Color(0.5f - 0.125f * (value - 3.0f * COLOR_STEP) / (2*COLOR_STEP), 0.25f + 0.125f * (value - 3.0f * COLOR_STEP) / (2*COLOR_STEP), 0.375f * (value - 3.0f *COLOR_STEP) / (2*COLOR_STEP));
                else if (value < 8.0 * COLOR_STEP)
                    return new Color(0.375f + 0.625f * (value - 5.0f *COLOR_STEP) / (3*COLOR_STEP), 0.375f + 0.625f * (value - 5.0f * COLOR_STEP) / (3*COLOR_STEP), 0.375f + 0.625f * (value - 5.0f *COLOR_STEP) / (3*COLOR_STEP));
                else
                    return new Color(255, 255, 255);
            }
        }
        else if(type.equals("humidity")){
            value-=0.5f;
            value*=2;
            if (value>1){
                value = 1;
                // System.out.println("Very Dry here :O"); //debugging line
            }

            float r,g,b;

            if (value<0){
                r= value+1;
                g= value+1;
                b= Math.abs(value);
            }
            else{
                r= 1;
                g= Math.abs(value-1);
                b= 0;
            }
            return new Color(r,g,b);
        }
        else if(type.equals("testClimate")){
            value -=0.5f;
            value *=2;
            if (value>1){
                value = 1;
                System.out.println("Ouch, Hot!");
            }
            if (value< -2) value = -2;

            float r,g,b;

            if (value<-1) {
                b=1;
                r=g=-value-1;
            }
            else if (value<0){
                r= value+1;
                g= value+1;
                b= Math.abs(value);
            }
            else{
                r= 1;
                g= Math.abs(value-1);
                b= 0;
            }
            return new Color(r,g,b);
        }

        else if(type.equals("Steepness")){
            //value /= 40;
            if(value>1){
                //System.out.println(value);
                value=1;

            }
            return new Color(value,value,value);
        }

        else{
            System.out.println("Invalid Print-Type");
            return  new Color(0,0,0);
        }

    }


}

