import logger.Logger;

import java.awt.*;
import java.util.Map;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 01.11.22
 **/
public class RgbToGreyscaleTask implements Runnable {

    private static final Logger logger = Logger.getInstance();

    private final int[][] imgRgbArray;

    private final Map<Integer, int[]> results;

    private final int rowIndex;

    public RgbToGreyscaleTask(int[][] imgRgbArray, Map<Integer, int[]> results, int rowIndex) {
        this.imgRgbArray = imgRgbArray;
        this.results = results;
        this.rowIndex = rowIndex;
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        int[] row = new int[imgRgbArray.length];
        for (int i = 0; i < row.length; i++) {
            Color c = new Color(imgRgbArray[i][rowIndex]);
            int r = (int) (c.getRed() * 0.21);
            int g = (int) (c.getGreen() * 0.72);
            int b = (int) (c.getBlue() * 0.07);
            row[i] = new Color(r + g + b, r + g + b, r + g + b).getRGB();
        }
        synchronized (results) {
            results.put(this.rowIndex, row);
        }
        //logger.log(String.format("Thread %s Row[%d]", Thread.currentThread().getName(), rowID), Logger.TYPE.DEBUG);
    }
}
