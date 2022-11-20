package greyscale;

import logger.Logger;
import utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
        ImageIO.setUseCache(true);
        BufferedImage image = ImageIO.read(file);
        int[][] imgRgbArray = ImageUtils.imageToRgbArray(image);
        // Make Tasks
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        Map<Integer, int[]> results =
                Collections.synchronizedMap(new TreeMap<>()); // Key is rowIndex, Value is row
        long startConversionTime = System.nanoTime();
        for (int row = 0; row < image.getHeight(); row++) {
            executor.execute(new RgbToGreyscaleTask(imgRgbArray, results, row));
        }
        logger.log("Started " + image.getHeight() + " Tasks with " + threadPoolSize + " Threads.", Logger.TYPE.INFO);
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
