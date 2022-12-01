package parallelImage;

import logger.Logger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 30.11.22
 **/
public abstract class MeasurableParallelImageProcessor extends ParallelImageProcessor implements Measurable {

    private static final Logger logger = Logger.getInstance();

    private long imageReadTime;

    private long taskTime;

    private long totalExecutionTime;


    public MeasurableParallelImageProcessor(File imageFile, int threadPoolSize) {
        super(imageFile, threadPoolSize);
    }

    @Override
    public <T> T processImage() throws IOException, InterruptedException, TimeoutException {
        logger.log("Processing Result", Logger.TYPE.INFO);
        long startTime = System.nanoTime();
        T result = super.processImage();
        this.totalExecutionTime = System.nanoTime() - startTime;
        return result;
    }

    @Override
    protected BufferedImage readImage() throws IOException {
        logger.log("Reading Image.", Logger.TYPE.INFO);
        long startTime = System.nanoTime();
        BufferedImage image = super.readImage();
        this.imageReadTime = System.nanoTime() - startTime;
        return image;
    }

    @Override
    protected int[][] readImageAsArray(BufferedImage image) {
        logger.log("Converting Image to array.", Logger.TYPE.INFO);
        long startTime = System.nanoTime();
        int[][] rgbArray = super.readImageAsArray(image);
        this.imageReadTime += System.nanoTime() - startTime;
        return rgbArray;
    }

    @Override
    protected void makeAndRunTask() throws InterruptedException, TimeoutException {
        logger.log("Started " + getImage().getHeight() + " Tasks with " + getThreadPoolSize() + " Threads.", Logger.TYPE.INFO);
        long startTime = System.nanoTime();
        super.makeAndRunTask();
        this.taskTime = System.nanoTime() - startTime;
        logger.log("All Threads done.", Logger.TYPE.INFO);
    }

    @Override
    public Measurable.NanoTimeBuilder getImageReadTime() {
        return new Measurable.NanoTimeBuilder(imageReadTime);
    }

    @Override
    public NanoTimeBuilder getTaskTime() {
        return new Measurable.NanoTimeBuilder(taskTime);
    }

    @Override
    public NanoTimeBuilder getTotalExecutionTime() {
        return new Measurable.NanoTimeBuilder(totalExecutionTime);
    }
}
