import logger.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import parallelImage.MeasurableParallelImageProcessor;
import parallelImage.ProcessorTaskType;
import parallelImage.brightness.BrightnessProcessor;
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
public class BrightnessTest {

    private static final String TEST_ID = UUID.randomUUID().toString();
    private static final Logger logger = Logger.getInstance();
    private final File csvFile = new File("src/main/resources/testResults/brightness");
    private final File image =
            new File(ClassLoader.getSystemResource("images/original/human/3.harold_large.jpg").getFile());
    private final int repeat = 1000;
    private final int brightness = 60;

    @Before
    public void init() {
        //logger.start(Logger.TYPE.INFO, null);
    }

    @After
    public void clean() {
        //logger.close();
    }

    @Test
    public void brightnessNonBlockingAllThreadsTest() throws IOException, InterruptedException, TimeoutException {
        BufferedImage originalReference = ImageIO.read(image);
        BufferedImage resultReference = ImageIO.read(ClassLoader.getSystemResource("images/brightness/human/3" +
                ".harold_large.jpg"));
        String imageName = image.getName();
        int threadPoolSize = Runtime.getRuntime().availableProcessors();
        boolean successful = false;
        CSVFileWriter csvWriter = TestUtils.makeCsvWriter(new File(csvFile, "nonBlocking8Thread.csv"));
        for (int i = 0; i < repeat; i++) {
            MeasurableParallelImageProcessor processor =
                    new MeasurableParallelImageProcessor(new BrightnessProcessor(threadPoolSize, brightness));
            try {
                BufferedImage brightness = processor.processImage(image, ProcessorTaskType.NON_BLOCKING).getImage();
                File file = Files.createTempFile(null, ".jpeg").toFile();
                ImageIO.write(brightness, "jpeg", file);
                TestUtils.compareImages(originalReference, resultReference, ImageIO.read(file));
                successful = true;
            } finally {
                processor.logResult(csvWriter, TEST_ID, threadPoolSize, imageName, successful);
            }
        }
        TestUtils.calculateAvgResults(csvWriter, null);
    }

    @Test
    public void greyScaleNonBlockingSingleThreadTest() throws IOException, InterruptedException, TimeoutException {
        BufferedImage originalReference = ImageIO.read(image);
        BufferedImage resultReference = ImageIO.read(ClassLoader.getSystemResource("images/brightness/human/3" +
                ".harold_large.jpg"));
        String imageName = image.getName();
        int threadPoolSize = 1;
        boolean successful = false;
        CSVFileWriter csvWriter = TestUtils.makeCsvWriter(new File(csvFile, "nonBlocking1Thread.csv"));
        for (int i = 0; i < repeat; i++) {
            MeasurableParallelImageProcessor processor =
                    new MeasurableParallelImageProcessor(new BrightnessProcessor(threadPoolSize, brightness));
            try {
                BufferedImage brightness = processor.processImage(image, ProcessorTaskType.NON_BLOCKING).getImage();
                File file = Files.createTempFile(null, ".jpeg").toFile();
                ImageIO.write(brightness, "jpeg", file);
                TestUtils.compareImages(originalReference, resultReference, ImageIO.read(file));
                successful = true;
            } finally {
                processor.logResult(csvWriter, TEST_ID, threadPoolSize, imageName, successful);
            }
        }
        TestUtils.calculateAvgResults(csvWriter, null);
    }

    @Test
    public void greyScaleBlockingAllThreadsTest() throws IOException, InterruptedException, TimeoutException {
        BufferedImage originalReference = ImageIO.read(image);
        BufferedImage resultReference = ImageIO.read(ClassLoader.getSystemResource("images/brightness/human/3" +
                ".harold_large.jpg"));
        String imageName = image.getName();
        int threadPoolSize = Runtime.getRuntime().availableProcessors();
        boolean successful = false;
        CSVFileWriter csvWriter = TestUtils.makeCsvWriter(new File(csvFile, "blocking1Thread.csv"));
        for (int i = 0; i < repeat; i++) {
            MeasurableParallelImageProcessor processor =
                    new MeasurableParallelImageProcessor(new BrightnessProcessor(threadPoolSize, brightness));
            try {
                BufferedImage brightness = processor.processImage(image, ProcessorTaskType.BLOCKING).getImage();
                File file = Files.createTempFile(null, ".jpeg").toFile();
                ImageIO.write(brightness, "jpeg", file);
                TestUtils.compareImages(originalReference, resultReference, ImageIO.read(file));
                successful = true;
            } finally {
                processor.logResult(csvWriter, TEST_ID, threadPoolSize, imageName, successful);
            }
        }
        TestUtils.calculateAvgResults(csvWriter, null);
    }

    @Test
    public void greyScaleBlockingSingleThreadTest() throws IOException, InterruptedException, TimeoutException {
        BufferedImage originalReference = ImageIO.read(image);
        BufferedImage resultReference = ImageIO.read(ClassLoader.getSystemResource("images/brightness/human/3" +
                ".harold_large.jpg"));
        String imageName = image.getName();
        int threadPoolSize = 1;
        boolean successful = false;
        CSVFileWriter csvWriter = TestUtils.makeCsvWriter(new File(csvFile, "blocking8Thread.csv"));
        for (int i = 0; i < repeat; i++) {
            MeasurableParallelImageProcessor processor =
                    new MeasurableParallelImageProcessor(new BrightnessProcessor(threadPoolSize, brightness));
            try {
                BufferedImage brightness = processor.processImage(image, ProcessorTaskType.BLOCKING).getImage();
                File file = Files.createTempFile(null, ".jpeg").toFile();
                ImageIO.write(brightness, "jpeg", file);
                TestUtils.compareImages(originalReference, resultReference, ImageIO.read(file));
                successful = true;
            } finally {
                processor.logResult(csvWriter, TEST_ID, threadPoolSize, imageName, successful);
            }
        }
        TestUtils.calculateAvgResults(csvWriter, null);
    }
}
