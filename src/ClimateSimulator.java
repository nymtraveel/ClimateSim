/**
 * Created with IntelliJ IDEA.
 * User: Florian
 * Date: 07.12.12
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */

public class ClimateSimulator {
    private float[][] heightmap,climate,humidity, tpoles, tequator, mountainyness;
    private int size;

    ClimateSimulator(float[][] hm){
        heightmap = hm;
        size = heightmap.length;


        long start = System.currentTimeMillis();
        //Ready the Climate Map
        climate = new float[size][size];
        long startPoles = System.currentTimeMillis();
        tpoles = distanceFrom("poles",25);
        System.out.println("Poles: " + (System.currentTimeMillis() - startPoles) + " ms");

        long startEquator = System.currentTimeMillis();
        tequator = distanceFrom("equator",25);
        System.out.println("Equator: " + (System.currentTimeMillis() - startEquator) + " ms");

        long startCompose = System.currentTimeMillis();
        for(int width = 0; width < size; width++){
            for(int height = 0; height < size; height++){
                float distToEq =(0.5f-Math.abs(((height/(float)size)*2)-1))+0.25f;

                climate[width][height] = tpoles[width][height]*(1-distToEq) + distToEq*tequator[width][height]-1;
            }
        }
        normalize(climate);
        overlayHeight(25,0);
        System.out.println("Compose: " + (System.currentTimeMillis() - startCompose) + " ms");


        long startWater = System.currentTimeMillis();
        humidity = distanceFrom("water",20);
        System.out.println("Humidity: " + (System.currentTimeMillis() - startWater) + " ms");


        System.out.println("Climate: " + (System.currentTimeMillis() - start) + " ms");

        long start1 = System.currentTimeMillis();
        //ready steepness
        steepSides();
        System.out.println("Steep sides: " + (System.currentTimeMillis() - start1) + " ms");

        //normalizing

        long start2 = System.currentTimeMillis();
        normalize(climate);
        normalize(humidity);
        normalize(mountainyness);

        System.out.println("Normalizing: " + (System.currentTimeMillis() - start2) + " ms");

    }

    private void normalize(float[][] array){
        float min=0,max=0;
        for(int width = 0; width < size; width++){
            for(int height = 0; height < size; height++){
                float curr = array[width][height];
                min = min<curr? min:curr;
                max = max>curr? max:curr;
            }
        }
        float span = max-min;
        for(int width = 0; width < size; width++){
            for(int height = 0; height < size; height++){
                float curr = array[width][height];
                array[width][height] = (array[width][height]-min)/span;
            }
        }
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

                float slider = 0.8f; //value to be determined
                float temp = computeAbsMax(s)*2*slider+(1-slider)*currHeight*currHeight*0.1f;
                temp -=1;
                temp = temp<0?0:temp;
                mountainyness[width][height] = (float)Math.pow(temp,1.4);
            }
        }

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


    private void overlayHeight(int strength, int threshold){
        for(int width = 0; width < size; width++){
            for(int height = 0; height < size; height++){
                float distToEq = 1-Math.abs(((height/(float)size)*2)-1);
                float heightFactor = heightmap[height][width]-1;

                if (heightFactor<0){  // sea
                    climate[height][width] =  (distToEq+0.2f)*0.6f;
                }
                else {                // land
                    heightFactor -= 4;
                    if (heightFactor>=0) {
                        float candidate = climate[height][width] - (strength * (heightFactor*0.05f)*(heightmap[height][width]-4)*0.4f)*0.001f;
                        climate[height][width] = candidate<0?0:candidate;
                    }
                            //((distToEq*locationInfluence + (100-locationInfluence)*0.5f)*0.01f - heightFactor*0.05f))*0.01f;

                    //climate[height][width] = ((100-strength)*climate[height][width] + strength *((distToEq*locationInfluence + (100-locationInfluence)*0.5f)*0.01f - heightFactor*0.05f))*0.01f;
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


    public float[][] getTpoles(){
        return tpoles;
    }

    public float[][] getTequator(){
        return tequator;
    }

    public float[][] getMountainyness(){
        return mountainyness;
    }

    public float[][] getHumidity(){
        return humidity;
    }
}