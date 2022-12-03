package parallelImage.greyscale;

import java.awt.*;
import java.util.Map;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 01.11.22
 **/
public class RgbToGreyscaleTask implements Runnable {

    private final int[][] imgRgbArray;

    private final Map<Integer, int[]> results;

    private final int rowIndex;

    /**
     * Task that takes a rgb Array of an image and calculates for the specified row the greyscale values. The result is
     * put in the results Map which is synchronized
     *
     * @param imgRgbArray image array as integer pixels in the default RGB color model and default sRGB colorspace.
     * @param results     reference of map where to put results
     * @param rowIndex    row index
     */
    public RgbToGreyscaleTask(int[][] imgRgbArray, Map<Integer, int[]> results, int rowIndex) {
        this.imgRgbArray = imgRgbArray;
        this.results = results;
        this.rowIndex = rowIndex;
    }

    @Override
    public void run() {
        int[] row = new int[imgRgbArray.length];
        for (int i = 0; i < row.length; i++) {
            // TODO: Could try variant with bit shift instead of getRed, getGreen...
            Color c = new Color(imgRgbArray[i][rowIndex]);
            int r = (int) (c.getRed() * 0.21);
            int g = (int) (c.getGreen() * 0.72);
            int b = (int) (c.getBlue() * 0.07);
            row[i] = new Color(r + g + b, r + g + b, r + g + b).getRGB();
        }
        // TODO: More performant way without blocking?
        synchronized (results) {
            results.put(this.rowIndex, row);
        }
    }
}
