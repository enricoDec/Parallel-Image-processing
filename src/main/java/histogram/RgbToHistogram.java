package histogram;

import logger.Logger;
import utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 19.11.22
 **/
public class RgbToHistogram {


    private static final Logger logger = Logger.getInstance();
    private final int[] resultRedBucket = new int[256];
    private final int[] resultGreenBucket = new int[256];
    private final int[] resultBlueBucket = new int[256];
    private long conversionTime = 0;
    private long totalTime = 0;
    private BufferedImage image;

    private int[][] imgRgbArray;

    private int threadPoolSize;

    public Histogram rgbToHistogramSplittingRows(File imageFile, int threadPoolSize) throws IOException, InterruptedException, TimeoutException {
        // Read Image
        logger.log("Reading Image.", Logger.TYPE.INFO);
        long startTime = System.nanoTime();
        this.image = ImageIO.read(imageFile);
        this.imgRgbArray = ImageUtils.imageToRgbArray(image);
        this.threadPoolSize = threadPoolSize;
        // Make Tasks
        long startConversionTime = System.nanoTime();
        LinkedList<int[]> redBuckets = new LinkedList<>();
        LinkedList<int[]> greenBuckets = new LinkedList<>();
        LinkedList<int[]> blueBuckets = new LinkedList<>();
        ExecutorService executor = makeTaskBySplittingRows(redBuckets, greenBuckets, blueBuckets);
        logger.log("Started " + image.getHeight() + " Tasks with " + threadPoolSize + " Threads.", Logger.TYPE.INFO);
        executor.shutdown();
        boolean terminated = executor.awaitTermination(5, TimeUnit.MINUTES);
        this.conversionTime = System.nanoTime() - startConversionTime;
        if (!terminated) {
            throw new TimeoutException("Threads did not finish before timeout");
        }
        logger.log("All Threads done.", Logger.TYPE.INFO);
        mergeResults(redBuckets, greenBuckets, blueBuckets);
        this.totalTime = System.nanoTime() - startTime;
        logger.log("Elapsed time: " + totalTime / 1000000 + "ms", Logger.TYPE.INFO);
        return new Histogram(resultRedBucket, resultGreenBucket, resultBlueBucket);
    }

    private ExecutorService makeTaskBySplittingRows(LinkedList<int[]> redBuckets, LinkedList<int[]> greenBuckets, LinkedList<int[]> blueBuckets) {
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        // Each Thread gets 3 buckets (r, g, b)
        for (int row = 0; row < image.getHeight(); row++) {
            int[] redBucket = new int[256];
            redBuckets.add(redBucket);
            int[] greenBucket = new int[256];
            greenBuckets.add(greenBucket);
            int[] blueBucket = new int[256];
            blueBuckets.add(blueBucket);
            executor.execute(new RgbToHistogramTask(imgRgbArray, redBucket, greenBucket, blueBucket, row));
        }
        return executor;
    }

    private void mergeResults(LinkedList<int[]> redBuckets, LinkedList<int[]> greenBuckets, LinkedList<int[]> blueBuckets) {
        for (int i = 0; i < 255; i++) {
            for (int[] bucket : redBuckets) {
                resultRedBucket[i] += bucket[i];
            }
            for (int[] bucket : greenBuckets) {
                resultGreenBucket[i] += bucket[i];
            }
            for (int[] bucket : blueBuckets) {
                resultBlueBucket[i] += bucket[i];
            }
        }
    }

    public long getConversionTime() {
        return conversionTime;
    }

    public long getTotalTime() {
        return totalTime;
    }
}
