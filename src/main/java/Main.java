import logger.Logger;
import parallelImage.MeasurableParallelImageProcessor;
import parallelImage.ProcessorResult;
import parallelImage.ProcessorTaskType;
import parallelImage.brightness.BrightnessProcessor;

import javax.imageio.ImageIO;
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
        logger.start(null);
        MeasurableParallelImageProcessor processor =
                new MeasurableParallelImageProcessor(new BrightnessProcessor(threadPoolSize, 60));
        ProcessorResult result = processor.processImage(new File("src/main/resources/images/original/nature/4" +
                ".nature_mega.jpeg"), ProcessorTaskType.NON_BLOCKING);
        ImageIO.write(result.getImage(), "jpeg", new File("src/main/resources/testResults/brightness/nonBlocking/4" +
                ".nature_mega_new.jpeg"));
        processor.logExecutionTime();
        logger.close();
    }
}
