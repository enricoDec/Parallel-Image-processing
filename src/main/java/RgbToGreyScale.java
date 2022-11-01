import logger.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 31.10.22
 **/
public class RgbToGreyScale {
    private static Logger logger = Logger.getInstance();

    public static void main(String[] args) throws IOException {
        logger.start(Logger.TYPE.DEBUG, null);
        logger.log("Reading Image", Logger.TYPE.INFO);
        File file = new File("src/main/resources/images/animal/1.kitten_small.jpg");
        BufferedImage image = ImageIO.read(file);
        int[][] array = ImageUtils.imageToRgbArray(image);
        int width = image.getWidth();
        int height = image.getHeight();


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = new Color(array[x][y]);
                int r = (int) (c.getRed() * 0.21);
                int g = (int) (c.getGreen() * 0.72);
                int b = (int) (c.getBlue() * 0.07);
                Color newColor = new Color(r + g + b, r + g + b, r + g + b);
                image.setRGB(x, y, newColor.getRGB());
            }
        }
        ImageIO.write(image, "png", new File("src/main/resources/images/animal/1.kitten_small_test2.jpg"));
        logger.cleanup();
    }
}
