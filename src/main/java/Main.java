import brightness.RgbBrightness;
import greyscale.RgbToGreyScale;
import histogram.Histogram;
import histogram.RgbToHistogram;
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
        rgbToHistogram("images/original/human/3.harold_large.jpg",
                new File("src/main/resources/images/histogram/test"));
        rgbToGreyScale("images/original/human/3.harold_large.jpg",
                new File("src/main/resources/images/greyscale/output.png"));
        rgbBrightness("images/original/human/3.harold_large.jpg",
                new File("src/main/resources/images/brightness/output.png"), 80);
        logger.close();
    }

    private static void rgbToHistogram(String resource, File outputImage) throws IOException, InterruptedException, TimeoutException {
        // TODO: We could try to test if more efficient to have one result list and synchronize it or each thread with
        //  own and merge it after. Also would be interesting to analyse what part really takes more time and if
        //  overhead of creating all those threads is really worth and if so at what point (image size) it makes a difference
        File file = new File(ClassLoader.getSystemResource(resource).getFile());
        RgbToHistogram rgbToHistogram = new RgbToHistogram();
        Histogram histogram = rgbToHistogram.rgbToHistogramSplittingRows(file, threadPoolSize);
        histogram.showHistorgram();
        histogram.saveHistogram(outputImage);
    }

    private static void rgbToGreyScale(String resource, File outputImage) throws IOException, InterruptedException, TimeoutException {
        File file = new File(ClassLoader.getSystemResource(resource).getFile());
        RgbToGreyScale rgbToGreyScale = new RgbToGreyScale();
        BufferedImage greyScale = rgbToGreyScale.rgbToGreyScaleSplittingRows(file, threadPoolSize);
        ImageIO.write(greyScale, "png", outputImage);
    }

    private static void rgbBrightness(String resource, File outputImage, int brightness) throws IOException, InterruptedException, TimeoutException {
        File file = new File(ClassLoader.getSystemResource(resource).getFile());
        RgbBrightness rgbBrightness = new RgbBrightness();
        BufferedImage result = rgbBrightness.rgbBrightnessSplittingRows(file, brightness, threadPoolSize);
        ImageIO.write(result, "png", outputImage);
    }
}
