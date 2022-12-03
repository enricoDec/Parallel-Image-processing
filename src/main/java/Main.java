import logger.Logger;
import parallelImage.ParallelImageProcessor;
import parallelImage.histogram.RgbToHistogram;

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
        logger.start(Logger.TYPE.INFO, null);
        File file = new File(ClassLoader.getSystemResource("images/original/human/3.harold_large.jpg").getFile());
        File outputFile = new File("src/main/resources/images/histogram/test.jpg");
        ParallelImageProcessor processor = new RgbToHistogram(threadPoolSize);
        logger.close();
    }
}
