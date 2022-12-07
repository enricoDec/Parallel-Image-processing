import logger.Logger;
import parallelImage.ParallelImageProcessor;
import parallelImage.ProcessorTaskType;
import parallelImage.greyscale.GreyScaleProcessor;
import parallelImage.greyscale.MultithreadingGreyscaleConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 31.10.22
 **/
public class Main {
    private static final Logger logger = Logger.getInstance();
    private static final int threadPoolSize = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // Init Logger
        ImageIO.setUseCache(false);
        File file = new File(ClassLoader.getSystemResource("images/original/human/3.harold_large.jpg").getFile());

        long startTime = System.nanoTime();
        BufferedImage image = MultithreadingGreyscaleConverter.convertToGreyscale(file);
        long endTime = System.nanoTime();
        System.out.println("Runtime for algo1: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + "ms");

        startTime = System.nanoTime();
        ParallelImageProcessor processor = new GreyScaleProcessor(threadPoolSize);
        image = processor.processImage(file, ProcessorTaskType.NON_BLOCKING).getImage();
        endTime = System.nanoTime();
        System.out.println("Runtime for algo2: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + "ms");

    }
}
