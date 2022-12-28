import logger.Logger;
import parallelImage.MeasurableParallelImageProcessor;
import parallelImage.ParallelImageProcessor;
import parallelImage.ProcessorTaskType;
import parallelImage.brightness.BrightnessProcessor;
import parallelImage.greyscale.GreyScaleProcessor;
import parallelImage.greyscale.MultithreadingGreyscaleConverter;
import parallelImage.histogram.Histogram;
import parallelImage.histogram.HistogramProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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

        /*
        ######################### Greyscale #########################
         */
        long startTime = System.nanoTime();
        BufferedImage image = MultithreadingGreyscaleConverter.convertToGreyscale(file);
        long endTime = System.nanoTime();
        System.out.println("Runtime for algo1: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + "ms");
        try {
            File outputfile = new File("src/main/resources/images/greyscale/human/3.harold_large.jpg");
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {}

        startTime = System.nanoTime();
        ParallelImageProcessor processor = new GreyScaleProcessor(threadPoolSize);
        image = processor.processImage(file, ProcessorTaskType.NON_BLOCKING).getImage();
        endTime = System.nanoTime();
        System.out.println("Runtime for algo2: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + "ms");
        try {
            File outputfile = new File("src/main/resources/images/greyscale/human/3.harold_large_non_blocking.jpg");
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {}

        startTime = System.nanoTime();
        image = processor.processImage(file, ProcessorTaskType.BLOCKING).getImage();
        endTime = System.nanoTime();
        System.out.println("Runtime for algo3: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + "ms");
        try {
            File outputfile = new File("src/main/resources/images/greyscale/human/3.harold_large_blocking.jpg");
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {}

        /*
        ######################### Brightness #########################
        */
        startTime = System.nanoTime();
        int newThreadPoolSize = 8;
        ParallelImageProcessor newProcessor = new BrightnessProcessor(newThreadPoolSize, 60);
        image = newProcessor.processImage(file, ProcessorTaskType.NON_BLOCKING).getImage();
        endTime = System.nanoTime();
        System.out.println("Runtime for algo4: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + "ms");
        try {
            File outputfile = new File("src/main/resources/images/brightness/human/3.harold_large_non_blocking.jpg");
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {}

        startTime = System.nanoTime();
        image = newProcessor.processImage(file, ProcessorTaskType.BLOCKING).getImage();
        endTime = System.nanoTime();
        System.out.println("Runtime for algo5: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + "ms");
        try {
            File outputfile = new File("src/main/resources/images/brightness/human/3.harold_large_blocking.jpg");
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {}

        /*
        ######################### Histogram #########################
        */
        startTime = System.nanoTime();
        ParallelImageProcessor histogramProcessor = new HistogramProcessor(newThreadPoolSize);
        Histogram histogram = histogramProcessor.processImage(file, ProcessorTaskType.NON_BLOCKING).getHistogram();
        endTime = System.nanoTime();
        System.out.println("Runtime for algo6: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + "ms");
        File nonblockingHistogram = new File("src/main/resources/images/histogram/human/3.harold_large_non_blocking.png");
        histogram.saveHistogram(nonblockingHistogram);

        startTime = System.nanoTime();
        histogram = histogramProcessor.processImage(file, ProcessorTaskType.BLOCKING).getHistogram();
        endTime = System.nanoTime();
        System.out.println("Runtime for algo7: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + "ms");
        File blockingHistogram = new File("src/main/resources/images/histogram/human/3.harold_large_blocking.png");
        histogram.saveHistogram(blockingHistogram);
    }
}
