import logger.Logger;

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
 * @since : 31.10.22
 **/
public class RgbToGreyScale {
    private static final Logger logger = Logger.getInstance();
    private static final int threadPoolSize = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // Init Logger
        logger.start(Logger.TYPE.INFO, null);
        logger.log("Reading Image", Logger.TYPE.INFO);
        // Read Image
        File file = new File("src/main/resources/images/animal/1.kitten_small.jpg");
        BufferedImage image = ImageIO.read(file);
        int[][] imgRgbArray = ImageUtils.imageToRgbArray(image);
        int width = image.getWidth();
        int height = image.getHeight();
        // Split Image in rows
        // TODO: 01.11.22 Conversion of Array actually useless (but helps understanding)
        ArrayList<int[]> rows = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            rows.add(ImageUtils.getImageRow(imgRgbArray, i));
        }
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        // ExecutorService executor = Executors.newSingleThreadExecutor();
        Map<Integer, int[]> results =
                Collections.synchronizedMap(new TreeMap<>()); // Key is row, Value is row
        long startTime = System.nanoTime();
        for (int row = 0; row < rows.size(); row++) {
            executor.execute(new RgbToGreyscaleTask(rows.get(row), results, row));
        }
        logger.log("Started " + rows.size() + " Tasks with " + threadPoolSize + " Threads.", Logger.TYPE.INFO);
        executor.shutdown();
        boolean terminated = executor.awaitTermination(5, TimeUnit.MINUTES);
        long endTime = System.nanoTime();
        if (!terminated) {
            throw new TimeoutException("Threads did not finish before timeout");
        }
        logger.log("All Threads done", Logger.TYPE.INFO);
        logger.log("Execution Time: " + (endTime - startTime) / 1000000 + "ms", Logger.TYPE.INFO);
        // Write new Image
        for (int y = 0; y < height; y++) {
            int[] row = results.get(y);
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, row[x]);
            }
        }
        ImageIO.write(image, "png", new File("src/main/resources/images/animal/1.kitten_small_greyscale.jpg"));
        logger.cleanup();
    }
}
