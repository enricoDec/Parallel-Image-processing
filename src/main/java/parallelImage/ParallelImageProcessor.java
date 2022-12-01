package parallelImage;

import jakarta.annotation.Nonnull;
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
public abstract class ParallelImageProcessor {

    private File imageFile;

    private BufferedImage image;

    private int threadPoolSize;

    private int[][] imgRgbArray;

    public ParallelImageProcessor(@Nonnull File imageFile, int threadPoolSize) {
        String error = "";
        if (!imageFile.isFile()) {
            error = imageFile.getAbsolutePath() + " is not a File";
        } else if (!imageFile.exists() || !imageFile.canRead()) {
            error = imageFile.getAbsolutePath() + " does not exist or cannot be read.";
        }
        if (!error.isBlank())
            throw new IllegalArgumentException(error);
        this.imageFile = imageFile;
        this.threadPoolSize = threadPoolSize;
    }

    /**
     * Process the image.
     *
     * @return T extends {@link ProcessorResult}
     * @throws IOException          I/O error
     * @throws InterruptedException if interrupted while waiting
     * @throws TimeoutException     if Threads did not finish in time
     */
    public <T> T processImage() throws IOException, InterruptedException, TimeoutException {
        // Read Image
        readImage();
        readImageAsArray(image);
        // Make and run Tasks
        makeAndRunTask();
        return retrieveResultFromTask();
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
     * @return {@link ProcessorResult}
     */
    protected abstract <T> T retrieveResultFromTask();


    /* ----------------------------- Getters & Setters ----------------------------- */

    public File getImageFile() {
        return imageFile;
    }

    protected void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

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
