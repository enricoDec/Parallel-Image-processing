package parallelImage.histogram.task;

import java.awt.*;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 19.11.22
 **/
public class HistogramNonBlockingTask extends HistogramTask implements Runnable {

    private final int[] redBucket;

    private final int[] greenBucket;

    private final int[] blueBucket;

    public HistogramNonBlockingTask(int[][] imgRgbArray, int rowIndex, int[] redBucket, int[] greenBucket,
                                    int[] blueBucket) {
        super(imgRgbArray, rowIndex);
        this.redBucket = redBucket;
        this.greenBucket = greenBucket;
        this.blueBucket = blueBucket;
    }

    @Override
    public void run() {
        for (int[] rows : imgRgbArray) {
            Color c = new Color(rows[rowIndex]);
            this.redBucket[c.getRed()]++;
            this.greenBucket[c.getGreen()]++;
            this.blueBucket[c.getBlue()]++;
        }
    }
}
