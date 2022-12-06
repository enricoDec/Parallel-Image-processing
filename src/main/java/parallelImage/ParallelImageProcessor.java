package parallelImage;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 30.11.22
 **/
public abstract class ParallelImageProcessor implements ImageProcessor {

    @Nonnull
    private int threadPoolSize;

    @Nonnull
    private BufferedImage image;

    @Nonnull
    private int[][] imgRgbArray;

    private int timeOutInMinutes = 5;

    /**
     * {@link ParallelImageProcessor} with a given image and threadPoolSize
     *
     * @param threadPoolSize max number of threads to be run parallel
     */
    public ParallelImageProcessor(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    /**
     * {@link ParallelImageProcessor} that uses as many processors as available
     */
    public ParallelImageProcessor() {
        this(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public ProcessorResult processImage(BufferedImage image, ProcessorTaskType type) throws InterruptedException,
            TimeoutException {
        this.image = image;
        readImageAsArray(image);
        // Make and run Tasks
        makeAndRunTask(type);
        return retrieveResultFromTask();
    }

    @Override
    public ProcessorResult processImage(BufferedImage image) throws InterruptedException, TimeoutException {
        return processImage(image, ProcessorTaskType.BLOCKING);
    }

    @Override
    public ProcessorResult processImage(File imageFile, ProcessorTaskType type) throws IOException,
            InterruptedException, TimeoutException {
        checkImageFile(imageFile);
        // Read Image
        this.image = readImage(imageFile);
        return processImage(image, type);
    }

    @Override
    public ProcessorResult processImage(File imageFile) throws IOException, InterruptedException, TimeoutException {
        return processImage(imageFile, ProcessorTaskType.BLOCKING);
    }

    /**
     * Checks if the given image file is valid
     *
     * @param imageFile Image File to process
     * @throws IllegalArgumentException if file does not exist or could not be read
     */
    protected void checkImageFile(File imageFile) throws IllegalArgumentException {
        String error = "";
        if (!imageFile.isFile()) {
            error = imageFile.getAbsolutePath() + " is not a File";
        } else if (!imageFile.exists() || !imageFile.canRead()) {
            error = imageFile.getAbsolutePath() + " does not exist or cannot be read.";
        }
        if (!error.isBlank())
            throw new IllegalArgumentException(error);
    }

    /**
     * Reads image as {@link BufferedImage}
     *
     * @param imageFile image file
     * @return the image as BufferedImage
     * @throws IOException I/O error
     */
    protected BufferedImage readImage(File imageFile) throws IOException {
        ImageIO.setUseCache(false); // TODO: Set to true, this is only done for consistent test results
        this.image = ImageIO.read(imageFile);
        return image;
    }

    /**
     * Reads a {@link BufferedImage} as a two-dimensional array were the first dimension is the x-axis of the image and
     * the second the y
     *
     * @param image {@link BufferedImage}
     * @return image as int[x][y]
     */
    protected int[][] readImageAsArray(BufferedImage image) {
        this.imgRgbArray = ImageUtils.imageToRgbArray(image);
        return imgRgbArray;
    }

    /**
     * Make {@link ExecutorService} Task to be run
     *
     * @param type {@link ProcessorTaskType}
     * @return {@link ExecutorService}
     */
    protected abstract ExecutorService makeTask(ProcessorTaskType type);

    /**
     * Makes and runs tasks
     *
     * @param type {@link ProcessorTaskType}
     * @throws InterruptedException if interrupted while waiting
     * @throws TimeoutException     if Threads did not finish in time
     */
    protected void makeAndRunTask(ProcessorTaskType type) throws InterruptedException, TimeoutException {
        ExecutorService executor = makeTask(type);
        executor.shutdown();
        boolean terminated = executor.awaitTermination(timeOutInMinutes, TimeUnit.MINUTES);
        if (!terminated) {
            throw new TimeoutException("Threads did not finish before timeout");
        }
    }

    /**
     * Retrieve Result from Task(s)
     *
     * @return ProcessorResult
     */
    protected abstract ProcessorResult retrieveResultFromTask();


    /* ----------------------------- Getters & Setters ----------------------------- */


    @Nullable
    public BufferedImage getImage() {
        return image;
    }

    protected void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    protected void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    protected int[][] getImgRgbArray() {
        return imgRgbArray;
    }

    public void setTimeOutInMinutes(int timeOutInMinutes) {
        this.timeOutInMinutes = timeOutInMinutes;
    }
}
