/**
 * Created with IntelliJ IDEA.
 * User: Florian
 * Date: 07.12.12
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */

public class ClimateSimulator {
    private float[][] heightmap,climate,humidity, t1, t2, mountainyness;
    //private float[][] blurLookup;
    private int size;
    //private int blurRange = 4;

    ClimateSimulator(float[][] hm){
        heightmap = hm;
        size = heightmap.length;

        //Ready the Climate Map
        climate = new float[size][size];
        t1 = distanceFrom("poles",20);
        t2 = distanceFrom("equator",20);
        for(int width = 0; width < size; width++){
            for(int height = 0; height < size; height++){
                climate[width][height] = t1[width][height] + t2[width][height] - 1;
            }
        }
        overlayHeight(5,0);

        //ready mountainyness
        steepSides();

    }

    private void steepSides(){
        mountainyness = new float[size][size];
        System.out.println("starting mountainyness");
        for(int width = 0; width < size; width++){
            for(int height = 0; height < size; height++){
                float currHeight = heightmap[width][height];

                float[] s = new float[8];
                s[0] = heightmap[(width+1)%size][height]-currHeight;
                s[1] = heightmap[width][(height+1)%size]-currHeight;
                s[2] = heightmap[(width-1+size)%size][height]-currHeight;
                s[3] = heightmap[width][(height-1+size)%size]-currHeight;
                s[4] = heightmap[(width+1)%size][(height+1)%size]-currHeight;
                s[5] = heightmap[(width-1+size)%size][(height+1)%size]-currHeight;
                s[6] = heightmap[(width+1)%size][(height-1+size)%size]-currHeight;
                s[7] = heightmap[(width-1+size)%size][(height-1+size)%size]-currHeight;

                float temp = computeAbsMax(s)+heightmap[width][height]*0.2f;//multiplicator to be determined
                temp -=0.5;
                temp = temp<0?0:temp;
                mountainyness[width][height] = (float)Math.pow(temp,1.4);
            }
        }
        System.out.println("end mountainyness");

    }


    private float[][] initDist(String fromWhat){

        float[][] distArr = new float[size][size];
        if (fromWhat.equals("water"))
        {
            for(int width = 0; width < size; width++){
                for(int height = 0; height < size; height++){
                    float heightFactor = heightmap[height][width]-1;

                    if (heightFactor<0){  // sea
                        distArr[height][width] = 0;
                    }
                    else{  // land
                        distArr[height][width] = size;
                    }
                }
            }
        }

        else if(fromWhat.equals("poles")){
            for(int width = 0; width < size; width++){
                for(int height = 0; height < size; height++){

                    if (height==0){  // topOfTheMap
                        distArr[height][width] = 0;
                    }
                    else{
                        distArr[height][width] = size;
                    }
                }
            }
        }
        else if(fromWhat.equals("equator")){
            for(int width = 0; width < size; width++){
                for(int height = 0; height < size; height++){

                    if (height==size/2){  // equator level
                        distArr[height][width] = 0;
                    }
                    else{
                        distArr[height][width] = size;
                    }
                }
            }
        }
        return distArr;
    }

    private float[][] distanceFrom(String fromWhat, float heightInfluence){

        float[][] distArr=initDist(fromWhat);
        float currentDistance = 0;

        System.out.println("Starting distance calculation: " + fromWhat);
        while (currentDistance<size){
            for(int width = 0; width < size; width++){
                for(int height = 0; height < size; height++){
                    float currHeight = heightmap[width][height];
                    if(distArr[width][height]==size){ //Block could update
                        if (distArr[(width+1)%size][height]+(heightmap[(width+1)%size][height]-currHeight)*heightInfluence-currentDistance <= 0 ||
                            distArr[width][(height+1)%size]+(heightmap[width][(height+1)%size]-currHeight)*heightInfluence-currentDistance <= 0 ||
                            distArr[(width-1+size)%size][height]+(heightmap[(width-1+size)%size][height]-currHeight)*heightInfluence-currentDistance <= 0||
                            distArr[width][(height-1+size)%size]+(heightmap[width][(height-1+size)%size]-currHeight)*heightInfluence-currentDistance <= 0){

                            //System.out.println("In case 1");
                            distArr[width][height]= currentDistance+1;
                        }
                        else if (
                                distArr[(width+1)%size][(height+1)%size]+(heightmap[(width+1)%size][(height+1)%size]-currHeight)*heightInfluence-(currentDistance+0.41421) <= 0||
                                distArr[(width-1+size)%size][(height+1)%size]+(heightmap[(width-1+size)%size][(height+1)%size]-currHeight)*heightInfluence-(currentDistance+0.41421) <= 0||
                                distArr[(width+1)%size][(height-1+size)%size]+(heightmap[(width+1)%size][(height-1+size)%size]-currHeight)*heightInfluence-(currentDistance+0.41421) <= 0||
                                distArr[(width-1+size)%size][(height-1+size)%size]+(heightmap[(width-1+size)%size][(height-1+size)%size]-currHeight)*heightInfluence-(currentDistance+0.41421 ) <= 0)
                        {
                            distArr[width][height] = currentDistance+1.41421f;
                            //System.out.println("In case 2");
                        }
                    }
                }
            }
            currentDistance++;
            //System.out.println("I was here " + currentDistance + " times");
        }

        //normalize Array
        float max=0;
        for(int width = 0; width < size; width++){
            for(int height = 0; height < size; height++){
                max = distArr[width][height]>max ? distArr[width][height]:max;
            }
        }
        for(int width = 0; width < size; width++){
            for(int height = 0; height < size; height++){
                distArr[width][height] /= max;
            }
        }

        //invert if necessary
        if (fromWhat.equals("equator")) {
            for(int width = 0; width < size; width++){
                for(int height = 0; height < size; height++){
                    distArr[width][height]= 1- distArr[width][height];
                }
            }
        }

        return distArr;
    }


    private void overlayHeight(int strength, int locationInfluence){
        for(int width = 0; width < size; width++){
            for(int height = 0; height < size; height++){
                float distToEq =(float)(0.5f-Math.abs(((height/512.)*2)-1))*2;
                float heightFactor = heightmap[height][width]-1;

                if (heightFactor<0){  // sea
                    climate[height][width] =  distToEq*0.4f;
                }
                else {                // land
                    heightFactor *= heightFactor;
                    climate[height][width] = ((100-strength)*climate[height][width] + strength *((distToEq*locationInfluence + (100-locationInfluence)*0.5f)*0.01f - heightFactor*0.05f))*0.01f;
                }
            }
        }
    }

    private float computeAbsMax(float[] arr){
        float max=0;
        for (float a: arr){
            max = a>max? a : max;
            max = (-a)>max? (-a) : max;
        }
        return max;
    }

    public float[][] getClimate(){
        return climate;
    }


    public float[][] gett1(){
        return t1;
    }

    public float[][] gett2(){
        return t2;
    }

    public float[][] getMountainyness(){
        return mountainyness;
    }

    public float[][] getHumidity(){
        return humidity;
    }
}