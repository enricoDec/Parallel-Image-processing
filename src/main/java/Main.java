import logger.Logger;
import parallelImage.MeasurableParallelImageProcessor;
import parallelImage.ProcessorTaskType;
import parallelImage.histogram.HistogramProcessor;

import javax.imageio.ImageIO;
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
    private static final int threadPoolSize = 1;

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // Init Logger
        logger.start(Logger.TYPE.INFO, null);
        File file = new File(ClassLoader.getSystemResource("images/original/human/3.harold_large.jpg").getFile());
        File outputFile = new File("src/main/resources/images/brightness/human/3.harold_large.jpg");
        MeasurableParallelImageProcessor processor =
                new MeasurableParallelImageProcessor(new HistogramProcessor(threadPoolSize));
        BufferedImage image = processor.processImage(file, ProcessorTaskType.BLOCKING).getImage();
        processor.logExecutionTime();
        ImageIO.write(image, "jpg", outputFile);
        logger.close();
    }
}
