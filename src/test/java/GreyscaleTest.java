import logger.Logger;
import org.junit.After;
import org.junit.Before;
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
public class GreyscaleTest {

    private static final String TEST_ID = UUID.randomUUID().toString();
    private static final Logger logger = Logger.getInstance();
    private final File csvFile = new File("src/main/resources/testResults/greyscale");
    private final File image =
            new File(ClassLoader.getSystemResource("images/original/human/3.harold_large.jpg").getFile());
    private final int repeat = 1000;
    private final int threadPoolSize = 1;

    @Before
    public void init() {
        //logger.start(Logger.TYPE.INFO, null);
    }

    @After
    public void clean() {
        //logger.close();
    }

    @Test
    public void greyscaleNonBlockingTest() throws IOException, InterruptedException, TimeoutException {
        BufferedImage originalReference = ImageIO.read(image);
        BufferedImage resultReference = ImageIO.read(ClassLoader.getSystemResource("images/greyscale/human/3.harold_large_non_blocking.jpg"));
        String imageName = image.getName();
        boolean successful = false;
        CSVFileWriter csvWriter = TestUtils.makeCsvWriter(new File(csvFile, "greyscaleNonBlocking_intel_mac_" + threadPoolSize + "_threads.csv"));
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
        TestUtils.calculateAvgResults(csvWriter, null);
    }

    @Test
    public void greyscaleBlockingTest() throws IOException, InterruptedException, TimeoutException {
        BufferedImage originalReference = ImageIO.read(image);
        BufferedImage resultReference = ImageIO.read(ClassLoader.getSystemResource("images/greyscale/human/3.harold_large_blocking.jpg"));
        String imageName = image.getName();
        boolean successful = false;
        CSVFileWriter csvWriter = TestUtils.makeCsvWriter(new File(csvFile, "greyscaleBlocking_intel_mac_" + threadPoolSize + "_threads.csv"));
        for (int i = 0; i < repeat; i++) {
            MeasurableParallelImageProcessor processor =
                    new MeasurableParallelImageProcessor(new GreyScaleProcessor(threadPoolSize));
            try {
                BufferedImage greyScale = processor.processImage(image, ProcessorTaskType.BLOCKING).getImage();
                File file = Files.createTempFile(null, ".jpeg").toFile();
                ImageIO.write(greyScale, "jpeg", file);
                TestUtils.compareImages(originalReference, resultReference, ImageIO.read(file));
                successful = true;
            } finally {
                processor.logResult(csvWriter, TEST_ID, threadPoolSize, imageName, successful);
            }
        }
        TestUtils.calculateAvgResults(csvWriter, null);
    }
}
