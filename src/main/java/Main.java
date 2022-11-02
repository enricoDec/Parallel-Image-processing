import logger.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 31.10.22
 **/
public class Main {
    private static final Logger logger = Logger.getInstance();
    private static final int threadPoolSize = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // Init Logger
        logger.start(Logger.TYPE.INFO, null);
        File file = new File(ClassLoader.getSystemResource("images/original/animal/2.kitten_medium.jpg").getFile());
        RgbToGreyScale rgbToGreyScale = new RgbToGreyScale();
        BufferedImage greyScale = rgbToGreyScale.rgbToGreyScaleSplittingRows(file, threadPoolSize);
        ImageIO.write(greyScale, "png", new File("src/main/resources/images/greyscale/animal/2.kitten_medium.jpg"));
        logger.close();
    }
}
