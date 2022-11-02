import logger.Logger;
import utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 02.11.22
 **/
public class RgbToGreyScale {

    private static final Logger logger = Logger.getInstance();

    private long conversionTime = 0;

    private long totalTime = 0;

    public BufferedImage rgbToGreyScaleSplittingRows(File file, int threadPoolSize) throws IOException, InterruptedException, TimeoutException {
        // Read Image
        logger.log("Reading Image.", Logger.TYPE.INFO);
        long startTime = System.nanoTime();
        BufferedImage image = ImageIO.read(file);
        int[][] imgRgbArray = ImageUtils.imageToRgbArray(image);
        int height = image.getHeight();
        // Split Image in rows
        // TODO: 01.11.22 Conversion of Array actually useless (but helps understanding)
        ArrayList<int[]> rows = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            rows.add(ImageUtils.getImageRow(imgRgbArray, i));
        }
        // Make Tasks
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        Map<Integer, int[]> results =
                Collections.synchronizedMap(new TreeMap<>()); // Key is rowIndex, Value is row
        long startConversionTime = System.nanoTime();
        for (int row = 0; row < rows.size(); row++) {
            executor.execute(new RgbToGreyscaleTask(rows.get(row), results, row));
        }
        logger.log("Started " + rows.size() + " Tasks with " + threadPoolSize + " Threads.", Logger.TYPE.INFO);
        executor.shutdown();
        boolean terminated = executor.awaitTermination(5, TimeUnit.MINUTES);
        this.conversionTime = System.nanoTime() - startConversionTime;
        if (!terminated) {
            throw new TimeoutException("Threads did not finish before timeout");
        }
        logger.log("All Threads done.", Logger.TYPE.INFO);
        ImageUtils.setRgbByRow(image, results);
        this.totalTime = System.nanoTime() - startTime;
        return image;
    }

    public long getConversionTime() {
        return conversionTime;
    }

    public long getTotalTime() {
        return totalTime;
    }
}
