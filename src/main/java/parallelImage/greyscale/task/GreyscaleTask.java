package parallelImage.greyscale.task;

import java.awt.*;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 04.12.22
 **/
public abstract class GreyscaleTask {

    protected final int[][] imgRgbArray;

    protected final int rowIndex;

    /**
     * Task that takes a rgb Array of an image and calculates for the specified row the greyscale values.
     *
     * @param imgRgbArray image array as integer pixels in the default RGB color model and default sRGB colorspace.
     * @param rowIndex    row index
     */
    public GreyscaleTask(int[][] imgRgbArray, int rowIndex) {
        this.imgRgbArray = imgRgbArray;
        this.rowIndex = rowIndex;
    }

    protected int[] greyscaleTask() {
        int[] row = new int[imgRgbArray.length];
        for (int i = 0; i < row.length; i++) {
            int pixel = imgRgbArray[i][rowIndex];
            int red = (pixel >> 16) & 0xFF;
            int green = (pixel >> 8) & 0xFF;
            int blue = (pixel) & 0xFF;
            int rgbRes = (int) (red * 0.21 + green * 0.72 + blue * 0.07);
            row[i] = ((0xFF) << 24) | // alpha not needed
                    ((rgbRes & 0xFF) << 16) |
                    ((rgbRes & 0xFF) << 8) |
                    ((rgbRes & 0xFF));
        }
        return row;
    }

    /**
     * Kept for reference, the current bitshift solution seems to be more performant (-2.45797% runtime)
     *
     * @return
     */
    private int[] oldGreyscaleTask() {
        int[] row = new int[imgRgbArray.length];
        for (int i = 0; i < row.length; i++) {
            Color c = new Color(imgRgbArray[i][rowIndex]);
            double r = c.getRed() * 0.21;
            double g = c.getGreen() * 0.72;
            double b = c.getBlue() * 0.07;
            int greyscale = (int) (r + g + b);
            row[i] = new Color(greyscale, greyscale, greyscale).getRGB();
        }
        return row;
    }
}
