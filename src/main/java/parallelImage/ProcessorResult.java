package parallelImage;

import jakarta.annotation.Nonnull;
import parallelImage.histogram.Histogram;
import parallelImage.histogram.HistogramProcessor;

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

    /**
     * Gets the processed Image
     *
     * @return processed Image as {@link BufferedImage}
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Get the {@link Histogram} of the processed image
     *
     * @return {@link Histogram}
     */
    public Histogram getHistogram() {
        // Lazy init as making histogram image costs
        if (histogram == null) {
            ParallelImageProcessor processor = new HistogramProcessor(1);
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
