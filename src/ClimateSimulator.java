import sun.font.TrueTypeFont;

/**
 * Created with IntelliJ IDEA.
 * User: Florian
 * Date: 07.12.12
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */

public class ClimateSimulator {
    private float[][] heightmap,climate,humidity, t1, t2;
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

        //Ready the HumidityMap
        //humidity = distanceFrom("water",5);

        /*-----Old attempt-----
        //At first, climate is set accordingly to distance from poles/equator

        for(int width = 0; width < size; width++){
            for(int height = 0; height < size; height++){
                float distToEq =(float)(0.5f-Math.abs(((height/512.)*2)-1))*2;
                float heightFactor = heightmap[height][width]-1;

                if (heightFactor<0){  // sea
                    climate[height][width] =  distToEq*0.4f; //dividor to be determined exactly
                }
                else {                // land
                    climate[height][width] = distToEq - heightFactor*0.05f;   //dividor to be determined exactly
                }
            }
        }

        //one simulation Step
        initBlurTable();
        smearUpDown(-1, size / 2);
        smearUpDown(1, 0);
        smearUpDown(-1,size);
        smearUpDown(1, size / 2);
        overlayHeight(0,0);
        oneBlurStep();
        oneBlurStep();
        oneBlurStep();
        overlayHeight(20, 30);
        */
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

    public float[][] getClimate(){
        return climate;
    }


    public float[][] gett1(){
        return t1;
    }

    public float[][] gett2(){
        return t2;
    }

    public float[][] getHumidity(){
        return humidity;
    }

/* Old methods I hesitate to delete completely

    private void smearUpDown(int upOrDown, int start){
        for(int width = 0; width < size; width++){
            int height = start;
            float baseValue=0;
            if (start == 0 || start == size){
                baseValue = -1;
            }
            else if (start == size/2){
                baseValue=1;
            }
            else {
                System.out.println("invalid start");
            }

            float energy = 1;
            float oneHeight= 1.f/256;
            int counter = 0;

            //main loop
            while(energy>0) {
                //smear Down
                float heightDifference;
                if (heightmap[(height+size)%size][width]>1 || heightmap[(height+upOrDown+size)%size][width]>0){
                    heightDifference = heightmap[(height+upOrDown+size)%size][width] - heightmap[(height+size)%size][width];
                }
                else{
                    heightDifference = 0;
                }
                float heightFactor = heightDifference<0?0:heightDifference*0.15f;
                energy -= Math.pow(heightFactor,5)*10 + oneHeight*1.5f;
                float indicator = energy>0?(float)Math.pow(energy,1):0;
                climate[(height+upOrDown+size)%size][width] = indicator*baseValue + (1-indicator)*climate[(height+upOrDown+size)%size][width];
                height += upOrDown;

                //debug code
                counter++;
                //System.out.println("I was here " + counter + " times and energy was " + energy);
            }
        }
    }

     private void initBlurTable() {
        blurLookup = new float[2*blurRange+1][2*blurRange+1];
        //initiate Blur lookup
        blurLookup[blurRange][blurRange]=1.f;
        for (int i=1; i <= blurRange; i++){
            blurLookup[blurRange+i][blurRange] = blurLookup[blurRange-i][blurRange] = ((float) Math.cos(  (float)i/(blurRange) * Math.PI  )/2+0.5f);
            blurLookup[blurRange][blurRange+i]=blurLookup[blurRange][blurRange-i]=blurLookup[blurRange+i][blurRange];
            for (int j=1; j <= blurRange;j++){
                blurLookup[blurRange+i][blurRange+j] = blurLookup[blurRange+i][blurRange-j] = blurLookup[blurRange-i][blurRange+j] = blurLookup[blurRange-i][blurRange-j]= blurLookup[blurRange+i][blurRange]* ( (float) Math.cos((float)j/(blurRange) * Math.PI)/2+0.5f);
            }
        }

        //normalize Blur Lookup
        float total=0;
        for(int x=0; x<=blurRange*2; x++){
            for (int y=0; y<=blurRange*2; y++){
                total+=blurLookup[x][y];
            }
        }
        for(int x=0; x<=blurRange*2; x++){
            for (int y=0; y<=blurRange*2; y++){
                blurLookup[x][y]/=total;
            }
        }
    }

    private void oneBlurStep(){
        float[][] tempClimate= climate.clone();
        //for (int i=0; i<size; i++) for (int j=0; j<size; j++) tempClimate[i][j] -=0.5f;

        float min=1;
        float max=0;
        for(int width = 0; width < size; width++){
            for(int heigth = 0; heigth < size; heigth++){

                float localTemp = climate[heigth][width];
                float localHeight =  heightmap [heigth][width];

                //determine heigth offset
                float[][] heightOffsets = new float[2*blurRange+1][2*blurRange+1];
                float total=0;
                for(int i=-blurRange;i<=blurRange;i++)    {
                    for(int j=-blurRange;j<=blurRange;j++) {
                        float diff = heightmap[(heigth+i+size)%size][(width+j+size)%size]-localHeight;
                        //diff=(float)Math.pow(diff*100,5);
                        heightOffsets[i+blurRange][j+blurRange] = diff;
                        total +=  Math.abs(diff);
                    }
                }

                for(int x=0; x<=blurRange*2; x++){
                    for (int y=0; y<=blurRange*2; y++){
                        if (total != 0){
                            heightOffsets[x][y] /= total;
                            heightOffsets[x][y] = 1 - heightOffsets[x][y];
                        }
                        else heightOffsets[x][y]=1;
                        min = heightOffsets[x][y]<min ? heightOffsets[x][y]:min;
                        max = heightOffsets[x][y]>max ? heightOffsets[x][y]:max;
                    }
                }
                //iterate through blurtable
                for(int x=0; x<=blurRange*2; x++){
                    // xcoord to elaborate: (heigth+x-blurRange+size)%(size)
                    int currPointX= (heigth+x-blurRange+size)%(size);
                    float diff = localTemp - climate[currPointX][width];
                    tempClimate[currPointX][width] += diff * blurLookup[x][blurRange]*heightOffsets[x][blurRange];


                    for (int y=0; y<=blurRange*2; y++){
                        // ycoord to elaborate: (width+y-blurRange+size-1)%(size)
                        int currPointY= (width+y-blurRange+size-1)%(size);
                        diff = localTemp - climate[currPointX][currPointY];
                        tempClimate[currPointX][currPointY] += diff * blurLookup[x][y]*heightOffsets[x][y];
                    }
                }
            }
        }
        System.out.println("Min: " + min + " Max: " + max);
        climate = tempClimate;
    }
*/
}