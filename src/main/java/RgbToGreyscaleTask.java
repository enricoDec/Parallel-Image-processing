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

    private final int[] imgRow;

    private final Map<Integer, int[]> results;

    private final int rowID;

    public RgbToGreyscaleTask(int[] imgRow, Map<Integer, int[]> results, int rowID) {
        this.imgRow = imgRow;
        this.results = results;
        this.rowID = rowID;
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        int[] row = new int[imgRow.length];
        for (int i = 0; i < imgRow.length; i++) {
            Color c = new Color(this.imgRow[i]);
            int r = (int) (c.getRed() * 0.21);
            int g = (int) (c.getGreen() * 0.72);
            int b = (int) (c.getBlue() * 0.07);
            row[i] = new Color(r + g + b, r + g + b, r + g + b).getRGB();
        }
        synchronized (results) {
            results.put(this.rowID, row);
        }
        //logger.log(String.format("Thread %s Row[%d]", Thread.currentThread().getName(), rowID), Logger.TYPE.DEBUG);
    }
}
