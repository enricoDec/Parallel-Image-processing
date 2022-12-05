package parallelImage.brightness.task;

import utils.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 22.11.22
 **/
public class BrightnessBlockingTask extends BrightnessTask implements Runnable {

    private final BufferedImage image;

    public BrightnessBlockingTask(int[][] imgRgbArray, int rowIndex, int brightness, BufferedImage image) {
        super(imgRgbArray, rowIndex, brightness);
        this.image = image;
    }

    @Override
    public void run() {
        int[] row = brightnessTask(imgRgbArray, rowIndex, brightness);
        synchronized (image) {
            ImageUtils.setRgbRow(image, rowIndex, row);
        }
    }
}
