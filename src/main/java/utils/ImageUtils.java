package utils;

import jakarta.annotation.Nonnull;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 01.11.22
 **/
public class ImageUtils {

    public static int[][] imageToRgbArray(@Nonnull BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] array = new int[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                array[x][y] = image.getRGB(x, y);
            }
        }
        return array;
    }

    public static int[] imageToRgbArrayOptionTwo(@Nonnull BufferedImage image) {
        // TODO: This is way faster then other option
        int width = image.getWidth();
        int height = image.getHeight();
        return image.getRGB(0, 0, width, height, null, 0, width);
    }


    public static int[] getImageRow(int[][] imgArray, int rowNumber) {
        int[] row = new int[imgArray.length];
        for (int i = 0; i < row.length; i++) {
            row[i] = imgArray[i][rowNumber];
        }
        return row;
    }

    /**
     * Given a Map of RowId and Row RGB value set the RGB for each Pixel of the given Image
     *
     * @param image   {@link BufferedImage}
     * @param results Map of RowId and Row RGB value
     * @return Resulting Image
     */
    public static BufferedImage setRgbByRow(@Nonnull BufferedImage image, Map<Integer, int[]> results) {
        for (int y = 0; y < image.getHeight(); y++) {
            int[] row = results.get(y);
            for (int x = 0; x < image.getWidth(); x++) {
                image.setRGB(x, y, row[x]);
            }
        }
        return image;
    }

    /**
     * Given a row of rgb Pixels as int[] set the pixels in the image.
     *
     * @param image    {@link BufferedImage} to be changed
     * @param rowIndex index of the row (y) to be changed
     * @param row      int[] rgb array
     */
    public static void setRgbRow(@Nonnull BufferedImage image, int rowIndex, int[] row) {
        for (int x = 0; x < image.getWidth(); x++) {
            image.setRGB(x, rowIndex, row[x]);
        }
    }
}
