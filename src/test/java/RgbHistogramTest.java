import org.junit.Test;
import parallelImage.MeasurableParallelImageProcessor;
import parallelImage.ProcessorResult;
import parallelImage.histogram.Histogram;
import parallelImage.histogram.HistogramProcessor;
import utils.CSVFileWriter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 20.11.22
 **/
public class RgbHistogramTest {
    private static final String[] CSV_HEADER = new String[]{"TestId", "Number of Threads", "Successful",
            "Elapsed Time for Calculation in Nano", "Elapsed Total Time in Nano", "Image"};

    private static final String TEST_ID = "1";

    @Test
    public void greyScaleSingleThreadedTest() throws IOException, InterruptedException, TimeoutException {
        File image = new File(ClassLoader.getSystemResource("images/original/animal/2.kitten_medium.jpg").getFile());
        String imageName = image.getName();
        int repeat = 100;
        int threadPoolSize = 1;
        boolean successful = false;
        File csvFile = new File(String.format("src/main/resources/testMulticoreHistogram_%s.csv", TEST_ID));
        CSVFileWriter csvFileWriter = new CSVFileWriter(csvFile, CSV_HEADER);
        csvFileWriter.setSeparator(";");

        for (int i = 0; i < repeat; i++) {
            MeasurableParallelImageProcessor rgbToHistogram =
                    new MeasurableParallelImageProcessor(new HistogramProcessor(threadPoolSize));
            try {
                ProcessorResult result = rgbToHistogram.processImage(image);
                Histogram histogram = result.getHistogram();
                if (histogram.getRedBucket() != null && histogram.getGreenBucket() != null && histogram.getBlueBucket() != null) {
                    successful = true;
                }
            } finally {
                csvFileWriter.addLine(new String[]{ //
                        TEST_ID, //
                        String.valueOf(threadPoolSize), //
                        String.valueOf(successful),
                        String.valueOf(rgbToHistogram.getTasksTime()), //
                        String.valueOf(rgbToHistogram.getTotalProcessingTime()), //
                        imageName}); //
                csvFileWriter.writeCSV(true); // flush each time
            }
        }
//        long datapoints = TestUtils.getNumberOrRows(csvFile);
//        double avgCalc = TestUtils.getAverageOfCsvColumn(csvFile, "Elapsed Time for Calculation in Nano", repeat);
//        System.out.printf("Average Calc time: %d ms from %d data-points.", TimeUnit.NANOSECONDS.toMillis((long)
//        avgCalc), datapoints);
//        System.out.println(System.lineSeparator());
//        double avgTot = TestUtils.getAverageOfCsvColumn(csvFile, "Elapsed Total Time in Nano", repeat);
//        System.out.printf("Average Total time: %d ms from %d data-points.", TimeUnit.NANOSECONDS.toMillis((long)
//        avgTot), datapoints);
    }
}
