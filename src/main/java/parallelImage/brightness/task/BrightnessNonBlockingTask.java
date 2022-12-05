package parallelImage.brightness.task;

import java.util.Map;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 22.11.22
 **/
public class BrightnessNonBlockingTask extends BrightnessTask implements Runnable {

    private final Map<Integer, int[]> results;

    public BrightnessNonBlockingTask(int[][] imgRgbArray, int rowIndex, int brightness, Map<Integer, int[]> results) {
        super(imgRgbArray, rowIndex, brightness);
        this.results = results;
    }

    @Override
    public void run() {
        int[] row = brightnessTask(imgRgbArray, rowIndex, brightness);
        synchronized (results) {
            results.put(this.rowIndex, row);
        }
    }
}
