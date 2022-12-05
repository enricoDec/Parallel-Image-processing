package parallelImage;

import jakarta.annotation.Nonnull;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 03.12.22
 **/
public interface ImageProcessor {

    /**
     * Process the image.
     *
     * @param imageFile Image File to process
     * @return {@link ProcessorResult}
     * @throws IOException          I/O error
     * @throws InterruptedException if interrupted while waiting
     * @throws TimeoutException     if Threads did not finish in time
     */
    ProcessorResult processImage(@Nonnull File imageFile) throws IOException, InterruptedException, TimeoutException;

    /**
     * Process the image.
     *
     * @param imageFile Image File to process
     * @param type      the Type of Task that should be run check {@link ProcessorTaskType} for further information
     * @return {@link ProcessorResult}
     * @throws IOException          I/O error
     * @throws InterruptedException if interrupted while waiting
     * @throws TimeoutException     if Threads did not finish in time
     */
    ProcessorResult processImage(@Nonnull File imageFile, ProcessorTaskType type) throws IOException,
            InterruptedException, TimeoutException;

    /**
     * Process the image.
     *
     * @param image {@link BufferedImage} to process
     * @return {@link  ProcessorResult}
     * @throws InterruptedException if interrupted while waiting
     * @throws TimeoutException     if Threads did not finish in time
     */
    ProcessorResult processImage(BufferedImage image) throws InterruptedException, TimeoutException;

    /**
     * Process the image with at specific {@link ProcessorTaskType}
     *
     * @param image {@link BufferedImage} to process
     * @param type  the Type of Task that should be run check {@link ProcessorTaskType} for further information
     * @return {@link  ProcessorResult}
     * @throws InterruptedException if interrupted while waiting
     * @throws TimeoutException     if Threads did not finish in time
     */
    ProcessorResult processImage(BufferedImage image, ProcessorTaskType type) throws InterruptedException,
            TimeoutException;
}
