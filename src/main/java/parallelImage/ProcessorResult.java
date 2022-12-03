package parallelImage;

import jakarta.annotation.Nonnull;
import parallelImage.histogram.Histogram;
import parallelImage.histogram.RgbToHistogram;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 03.12.22
 **/
public class ProcessorResult {

    @Nonnull
    private final BufferedImage image;

    @Nonnull
    private Histogram histogram;

    public ProcessorResult(BufferedImage image) {
        this.image = image;
    }

    public ProcessorResult(BufferedImage image, Histogram histogram) {
        this(image);
        this.histogram = histogram;
    }


    public BufferedImage getImage() {
        return image;
    }

    public Histogram getHistogram() {
        // Lazy init as making histogram image costs
        if (histogram == null) {
            ParallelImageProcessor processor = new RgbToHistogram(1);
            try {
                ProcessorResult result = processor.processImage(image);
                this.histogram = result.getHistogram();
            } catch (InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
        }
        return histogram;
    }
}
