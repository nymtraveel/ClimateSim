/**
 * Created with IntelliJ IDEA.
 * User: Florian
 * Date: 06.12.12
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 */
import java.math.*;
import java.util.Arrays;

public class ClimateSimulator_old {
    private float[][] heigthmap,climate;
    private int size;
    private float[][] blurLookup;
    private int blurRange;

    ClimateSimulator_old(float[][] hm, int blurRange){
        heigthmap = hm;
        this.blurRange = blurRange;
        size = heigthmap.length;
        blurLookup = new float[blurRange*2+1][blurRange*2+1];

        //Initiate the climate Map
        climate = new float[size][size];
        //At the poles, temperature is set to 0, at equator 1, otherwise 0,5
        for(int width = 0; width < size; width++){
            for(int heigth = 0; heigth < size; heigth++){
                if (heigth==0){
                    climate[heigth][width] = 0.5f;
                }
                else if (heigth == size/2){
                    climate[heigth][width] = 1;
                }
                else climate[heigth][width] = 0.5f;
            }
        }

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

    public float[][] getClimateMap(){
        return climate;
    }


    public void oneBlurStep(){
        float[][] tempClimate= climate.clone();
        //for (int i=0; i<size; i++) for (int j=0; j<size; j++) tempClimate[i][j] -=0.5f;

        for(int width = 0; width < size; width++){
            for(int heigth = 0; heigth < size; heigth++){


                float localTemp = climate[heigth][width];

                if (localTemp != 0){


                    for(int x=0; x<=blurRange*2; x++){
                        // xcoord to elaborate: (heigth+x-blurRange+size)%(size)
                        int currPointX= (heigth+x-blurRange+size)%(size);
                        float diff = localTemp - climate[currPointX][width];

                        if (diff<0){
                            tempClimate[currPointX][width] += diff * blurLookup[x][blurRange];
                        }
                        else if(diff>0){
                            tempClimate[currPointX][width] += diff * blurLookup[x][blurRange];
                        }

                        for (int y=0; y<=blurRange*2; y++){
                            // ycoord to elaborate: (width+y-blurRange+size-1)%(size)
                            int currPointY= (width+y-blurRange+size-1)%(size);
                            diff = localTemp - climate[currPointX][currPointY];

                            if (diff<0){
                                tempClimate[currPointX][currPointY] += diff * blurLookup[x][y];
                            }
                            else if(diff>0){
                                tempClimate[currPointX][currPointY] += diff * blurLookup[x][y];
                            }
                        }
                    }


                }
            }
        }

        //for (int i=0; i<size; i++) for (int j=0; j<size; j++) tempClimate[i][j] +=0.5f;

        for (int x=0; x<size; x++){
            tempClimate[0][x]=0;
            tempClimate[size/2][x]=1;
        }

        climate = tempClimate;
    }

    public void blurXTimes(int x){
        for (int i=0;i<x;i++) oneBlurStep();
    }


    public float[][] getBlurMap(){
        return blurLookup;
    }




}
