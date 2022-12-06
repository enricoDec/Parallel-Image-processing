package parallelImage;

import logger.Logger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 30.11.22
 **/
public class MeasurableParallelImageProcessor extends ParallelImageProcessor implements Measurable, ImageProcessor {

    private static final Logger logger = Logger.getInstance();
    private final ParallelImageProcessor processor;
    private long imageReadTime;
    private long tasksTime;
    private long retrieveResultTime;
    private long totalProcessingTime;


    public MeasurableParallelImageProcessor(ParallelImageProcessor processor) {
        super(processor.getThreadPoolSize());
        this.processor = processor;
    }

    @Override
    public ProcessorResult processImage(BufferedImage image, ProcessorTaskType type) throws InterruptedException,
            TimeoutException {
        logger.log("Processing image", Logger.TYPE.INFO);
        resetProcessor(); // Failsafe to make sure execution times are reset
        long startTime = System.nanoTime();
        ProcessorResult result = super.processImage(image, type);
        this.totalProcessingTime = System.nanoTime() - startTime;
        return result;
    }

    private void resetProcessor() {
        this.imageReadTime = 0;
        this.tasksTime = 0;
        this.retrieveResultTime = 0;
        this.totalProcessingTime = 0;
    }

    @Override
    protected BufferedImage readImage(File imageFile) throws IOException {
        logger.log("Reading Image.", Logger.TYPE.INFO);
        long startTime = System.nanoTime();
        BufferedImage image = processor.readImage(imageFile);
        this.imageReadTime = System.nanoTime() - startTime;
        return image;
    }

    @Override
    protected int[][] readImageAsArray(BufferedImage image) {
        logger.log("Converting Image to array.", Logger.TYPE.INFO);
        long startTime = System.nanoTime();
        int[][] rgbArray = processor.readImageAsArray(processor.getImage());
        this.imageReadTime += System.nanoTime() - startTime;
        return rgbArray;
    }

    @Override
    protected ExecutorService makeTask(ProcessorTaskType type) {
        return processor.makeTask(type);
    }

    @Override
    protected void makeAndRunTask(ProcessorTaskType type) throws InterruptedException, TimeoutException {
        logger.log("Started " + processor.getImage().getHeight() + " Tasks with " + processor.getThreadPoolSize() +
                        " Threads.",
                Logger.TYPE.INFO);
        long startTime = System.nanoTime();
        processor.makeAndRunTask(type);
        this.tasksTime = System.nanoTime() - startTime;
        logger.log("All Threads done.", Logger.TYPE.INFO);
    }

    @Override
    protected ProcessorResult retrieveResultFromTask() {
        logger.log("Retrieving result from Tasks.", Logger.TYPE.INFO);
        long startTime = System.nanoTime();
        ProcessorResult result = processor.retrieveResultFromTask();
        this.retrieveResultTime = System.nanoTime() - startTime;
        return result;
    }

    @Override
    public void logExecutionTime() {
        logger.log("Execution time:", Logger.TYPE.INFO);
        logger.log("It took " + getImageReadTime().asMillis() + "ms to read the image file", Logger.TYPE.INFO);
        logger.log("It took " + getTasksTime().asMillis() + "ms to execute all tasks", Logger.TYPE.INFO);
        logger.log("It took " + getRetrieveResultTime().asMillis() + "ms to retrieve the result from all tasks",
                Logger.TYPE.INFO);
        logger.log("It took a total of " + getTotalProcessingTime().asMillis() + "ms to process the image",
                Logger.TYPE.INFO);
    }

    /* ----------------------------- Getters & Setters ----------------------------- */

    @Override
    public Measurable.NanoTimeBuilder getImageReadTime() {
        if (imageReadTime == 0) {
            throw new IllegalStateException("No image read time recorded!");
        }
        return new Measurable.NanoTimeBuilder(imageReadTime);
    }

    @Override
    public NanoTimeBuilder getTasksTime() {
        if (tasksTime == 0) {
            throw new IllegalStateException("No task time recorded!");
        }
        return new Measurable.NanoTimeBuilder(tasksTime);
    }

    @Override
    public NanoTimeBuilder getTotalProcessingTime() {
        if (totalProcessingTime == 0) {
            throw new IllegalStateException("No total execution time recorded!");
        }
        return new Measurable.NanoTimeBuilder(totalProcessingTime);
    }

    @Override
    public NanoTimeBuilder getRetrieveResultTime() {
        if (retrieveResultTime == 0) {
            throw new IllegalStateException("No retrieve result time recorded!");
        }
        return new Measurable.NanoTimeBuilder(retrieveResultTime);
    }

    @Override
    public BufferedImage getImage() {
        return processor.getImage();
    }

    @Override
    public int getThreadPoolSize() {
        return processor.getThreadPoolSize();
    }
}
