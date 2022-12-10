import logger.Logger;
import parallelImage.MeasurableParallelImageProcessor;
import parallelImage.ParallelImageProcessor;
import parallelImage.ProcessorResult;
import parallelImage.greyscale.GreyScaleProcessor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 31.10.22
 **/
public class Main {
    private static final Logger logger = Logger.getInstance();
    private static final int threadPoolSize = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // Init Logger
        ParallelImageProcessor processor = new GreyScaleProcessor(threadPoolSize);
        ProcessorResult result = processor.processImage(new File(""));
        BufferedImage image = result.getImage();


        MeasurableParallelImageProcessor measurableProcessor =
                new MeasurableParallelImageProcessor(new GreyScaleProcessor(threadPoolSize));

    }
}
