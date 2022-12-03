package parallelImage.brightness;

import java.awt.*;
import java.util.Map;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 22.11.22
 **/
public class RgbBrightnessTask implements Runnable {

    private final int brightness;

    private final int[][] imgRgbArray;

    private final Map<Integer, int[]> results;

    private final int rowIndex;

    public RgbBrightnessTask(int brightness, int[][] imgRgbArray, Map<Integer, int[]> results, int rowIndex) {
        this.brightness = brightness;
        this.imgRgbArray = imgRgbArray;
        this.results = results;
        this.rowIndex = rowIndex;
    }

    public static int truncate(int value) {
        if (value < 0) {
            return 0;
        } else {
            return Math.min(value, 255);
        }
    }

    @Override
    public void run() {
        int[] row = new int[imgRgbArray.length];
        for (int i = 0; i < row.length; i++) {
            Color c = new Color(imgRgbArray[i][rowIndex]);
            row[i] = new Color(truncate(c.getRed() + brightness), truncate(c.getGreen() + brightness),
                    truncate(c.getBlue() + brightness)).getRGB();
            // TODO: Could try to set here the Pixel of image
        }
        synchronized (results) {
            results.put(this.rowIndex, row);
        }
    }
}
