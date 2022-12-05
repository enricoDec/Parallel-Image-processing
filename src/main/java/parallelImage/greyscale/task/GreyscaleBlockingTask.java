package parallelImage.greyscale.task;

import utils.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 01.11.22
 **/
public class GreyscaleBlockingTask extends GreyscaleTask implements Runnable {

    private final BufferedImage image;

    /**
     * Task that takes a rgb Array of an image and calculates for the specified row the greyscale values. The result is
     * put in the results Map which is synchronized
     *
     * @param imgRgbArray image array as integer pixels in the default RGB color model and default sRGB colorspace.
     * @param rowIndex    row index
     * @param image       {@link BufferedImage} to be modified
     */
    public GreyscaleBlockingTask(int[][] imgRgbArray, int rowIndex, BufferedImage image) {
        super(imgRgbArray, rowIndex);
        this.image = image;
    }

    @Override
    public void run() {
        int[] row = greyscaleTask();
        synchronized (image) {
            ImageUtils.setRgbRow(image, rowIndex, row);
        }
    }
}
