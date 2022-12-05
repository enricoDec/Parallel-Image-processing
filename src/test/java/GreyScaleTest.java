import logger.Logger;
import org.junit.Test;
import parallelImage.MeasurableParallelImageProcessor;
import parallelImage.ProcessorTaskType;
import parallelImage.greyscale.GreyScaleProcessor;
import utils.CSVFileWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 02.11.22
 **/
public class GreyScaleTest {

    private static final String TEST_ID = UUID.randomUUID().toString();
    private static final Logger logger = Logger.getInstance();
    private final File csvFile = new File("src/main/resources/testResults/greyscale");

    @Test
    public void greyScaleNonBlockingTest() throws IOException, InterruptedException, TimeoutException {
        File image = new File(ClassLoader.getSystemResource("images/original/nature/4.nature_mega.jpeg").getFile());
        BufferedImage originalReference = ImageIO.read(image);
        BufferedImage resultReference = ImageIO.read(ClassLoader.getSystemResource("images/greyscale/nature/4" +
                ".nature_mega.jpeg"));
        String imageName = image.getName();
        int repeat = 50; // TODO: increase to 1000?
        int threadPoolSize = Runtime.getRuntime().availableProcessors();
        boolean successful = false;
        CSVFileWriter csvWriter = TestUtils.makeCsvWriter(new File(csvFile, "nonBlocking8ThreadBigImage.csv"));
        for (int i = 0; i < repeat; i++) {
            MeasurableParallelImageProcessor processor =
                    new MeasurableParallelImageProcessor(new GreyScaleProcessor(threadPoolSize));
            try {
                BufferedImage greyScale = processor.processImage(image, ProcessorTaskType.NON_BLOCKING).getImage();
                File file = Files.createTempFile(null, ".jpeg").toFile();
                ImageIO.write(greyScale, "jpeg", file);
                TestUtils.compareImages(originalReference, resultReference, ImageIO.read(file));
                successful = true;
            } finally {
                processor.logResult(csvWriter, TEST_ID, threadPoolSize, imageName, successful);
            }
        }
        logger.start(Logger.TYPE.INFO, null);
        TestUtils.calculateAvgResults(csvWriter, logger);
        logger.close();
    }
}
