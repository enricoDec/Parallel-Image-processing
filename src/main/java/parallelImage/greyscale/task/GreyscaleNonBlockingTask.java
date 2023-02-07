package parallelImage.greyscale.task;

import java.util.Map;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 01.11.22
 **/
public class GreyscaleNonBlockingTask extends GreyscaleTask implements Runnable {

    private final Map<Integer, int[]> results;

    /**
     * Task that takes a rgb Array of an image and calculates for the specified row the greyscale values. The result is
     * put in the results Map which is synchronized
     *
     * @param imgRgbArray image array as integer pixels in the default RGB color model and default sRGB colorspace.
     * @param rowIndex    row index
     * @param results     reference of map where to put results
     */
    public GreyscaleNonBlockingTask(int[][] imgRgbArray, int rowIndex, Map<Integer, int[]> results) {
        super(imgRgbArray, rowIndex);
        this.results = results;
    }

    @Override
    public void run() {
        int[] row = greyscaleTask();
        results.put(this.rowIndex, row);
    }
}
