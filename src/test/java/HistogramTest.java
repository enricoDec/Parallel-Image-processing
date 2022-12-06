import logger.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import parallelImage.MeasurableParallelImageProcessor;
import parallelImage.ProcessorTaskType;
import parallelImage.histogram.Histogram;
import parallelImage.histogram.HistogramProcessor;
import utils.CSVFileWriter;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 02.11.22
 **/
public class HistogramTest {

    private static final String TEST_ID = UUID.randomUUID().toString();
    private static final Logger logger = Logger.getInstance();
    private final File csvFile = new File("src/main/resources/testResults/histogram");
    private final File image =
            new File(ClassLoader.getSystemResource("images/original/nature/4.nature_mega.jpeg").getFile());
    private final int repeat = 1000;
    private final int threadPoolSize = 8;

    @Before
    public void init() {
        //logger.start(Logger.TYPE.INFO, null);
    }

    @After
    public void clean() {
        //logger.close();
    }

    @Test
    public void histogramNonBlockingTest() throws IOException, InterruptedException, TimeoutException {
        String imageName = image.getName();
        boolean successful = false;
        CSVFileWriter csvWriter = TestUtils.makeCsvWriter(new File(csvFile, "histogramNonBlocking.csv"));
        for (int i = 0; i < repeat; i++) {
            MeasurableParallelImageProcessor processor =
                    new MeasurableParallelImageProcessor(new HistogramProcessor(threadPoolSize));
            try {
                Histogram histogram = processor.processImage(image, ProcessorTaskType.NON_BLOCKING).getHistogram();
                successful = true;
            } finally {
                processor.logResult(csvWriter, TEST_ID, threadPoolSize, imageName, successful);
            }
        }
        TestUtils.calculateAvgResults(csvWriter, null);
    }

    @Test
    public void histogramBlockingTest() throws IOException, InterruptedException, TimeoutException {
        String imageName = image.getName();
        boolean successful = false;
        CSVFileWriter csvWriter = TestUtils.makeCsvWriter(new File(csvFile, "histogramBlocking.csv"));
        for (int i = 0; i < repeat; i++) {
            MeasurableParallelImageProcessor processor =
                    new MeasurableParallelImageProcessor(new HistogramProcessor(threadPoolSize));
            try {
                Histogram histogram = processor.processImage(image, ProcessorTaskType.BLOCKING).getHistogram();
                successful = true;
            } finally {
                processor.logResult(csvWriter, TEST_ID, threadPoolSize, imageName, successful);
            }
        }
        TestUtils.calculateAvgResults(csvWriter, null);
    }
}
