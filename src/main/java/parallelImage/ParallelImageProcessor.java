package parallelImage;

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


    private int threadPoolSize;

    private File imageFile;

    private BufferedImage image;

    private int[][] imgRgbArray;

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
    public ProcessorResult processImage(BufferedImage image) throws InterruptedException, TimeoutException {
        readImageAsArray(image);
        // Make and run Tasks
        makeAndRunTask();
        return retrieveResultFromTask();
    }

    @Override
    public ProcessorResult processImage(File imageFile) throws IOException, InterruptedException, TimeoutException {
        if (this.imageFile != null) {
            resetProcessor();
        }
        checkImageFile(imageFile);
        this.imageFile = imageFile;
        // Read Image
        readImage();
        return processImage(image);
    }

    /**
     * Reset for a new Run
     */
    protected void resetProcessor() {
        this.imageFile = null;
        this.image = null;
        this.imgRgbArray = null;
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
     * @return the image as BufferedImage
     * @throws IOException I/O error
     */
    protected BufferedImage readImage() throws IOException {
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
     * @return {@link ExecutorService}
     */
    protected abstract ExecutorService makeTask();

    /**
     * Makes and runs tasks
     *
     * @throws InterruptedException if interrupted while waiting
     * @throws TimeoutException     if Threads did not finish in time
     */
    protected void makeAndRunTask() throws InterruptedException, TimeoutException {
        ExecutorService executor = makeTask();
        executor.shutdown();
        // TODO: add custom timeout
        boolean terminated = executor.awaitTermination(5, TimeUnit.MINUTES);
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
    public File getImageFile() {
        return imageFile;
    }

    protected void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

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

    protected void setImgRgbArray(int[][] imgRgbArray) {
        this.imgRgbArray = imgRgbArray;
    }
}
