package parallelImage.brightness.task;

import java.awt.*;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 04.12.22
 **/
public abstract class BrightnessTask {

    protected final int[][] imgRgbArray;

    protected final int rowIndex;

    protected final int brightness;

    public BrightnessTask(int[][] imgRgbArray, int rowIndex, int brightness) {
        this.imgRgbArray = imgRgbArray;
        this.rowIndex = rowIndex;
        this.brightness = brightness;
    }

    protected static int truncate(int value) {
        if (value < 0) {
            return 0;
        } else {
            return Math.min(value, 255);
        }
    }

    protected int[] brightnessTask(int[][] imgRgbArray, int rowIndex, int brightness) {
        int[] row = new int[imgRgbArray.length];
        for (int i = 0; i < row.length; i++) {
            Color c = new Color(imgRgbArray[i][rowIndex]);
            row[i] = new Color(truncate(c.getRed() + brightness), truncate(c.getGreen() + brightness),
                    truncate(c.getBlue() + brightness)).getRGB();
        }
        return row;
    }
}
