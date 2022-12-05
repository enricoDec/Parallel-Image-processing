package parallelImage.histogram.task;

import parallelImage.histogram.Histogram;

import java.awt.*;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 19.11.22
 **/
public class HistogramBlockingTask extends HistogramTask implements Runnable {

    private final Histogram histogram;

    public HistogramBlockingTask(int[][] imgRgbArray, int rowIndex, Histogram histogram) {
        super(imgRgbArray, rowIndex);
        this.histogram = histogram;
    }

    @Override
    public void run() {
        for (int[] rows : imgRgbArray) {
            Color c = new Color(rows[rowIndex]);
            synchronized (histogram) {
                histogram.getRedBucket()[c.getRed()] += 1;
                histogram.getGreenBucket()[c.getGreen()] += 1;
                histogram.getBlueBucket()[c.getBlue()] += 1;
            }
        }
    }
}
