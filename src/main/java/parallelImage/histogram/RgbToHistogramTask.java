package parallelImage.histogram;

import java.awt.*;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 19.11.22
 **/
public class RgbToHistogramTask implements Runnable {

    private final int[][] imgRgbArray;

    private final int[] redBucket;

    private final int[] greenBucket;

    private final int[] blueBucket;

    private final int rowIndex;

    public RgbToHistogramTask(int[][] imgRgbArray, int[] redBucket, int[] greenBucket, int[] blueBucket, int rowIndex) {
        this.imgRgbArray = imgRgbArray;
        this.redBucket = redBucket;
        this.greenBucket = greenBucket;
        this.blueBucket = blueBucket;
        this.rowIndex = rowIndex;
    }

    @Override
    public void run() {
        for (int[] ints : imgRgbArray) {
            Color c = new Color(ints[rowIndex]);
            this.redBucket[c.getRed()]++;
            this.greenBucket[c.getGreen()]++;
            this.blueBucket[c.getBlue()]++;
            // TODO: Alpha???
        }
    }
}
