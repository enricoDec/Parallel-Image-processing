import java.awt.image.BufferedImage;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 01.11.22
 **/
public class ImageUtils {

    public static int[][] imageToRgbArray(BufferedImage image) {
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

    public static int[] getImageRow(int[][] imgArray, int rowNumber) {
        int[] row = new int[imgArray.length];
        for (int i = 0; i < row.length; i++) {
            row[i] = imgArray[i][rowNumber];
        }
        return row;
    }
}
