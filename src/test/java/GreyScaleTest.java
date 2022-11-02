import org.junit.Test;
import utils.CSVFileWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 02.11.22
 **/
public class GreyScaleTest {
    // | TestId | Number of Threads | Successful | Elapsed Time for Conversion in Nano | Total Elapsed Time in Nano | Image |
    private static final String[] CSV_HEADER = new String[]{"TestId", "Number of Threads", "Successful", "Elapsed Time for Conversion in Nano", "Elapsed Total Time in Nano", "Image"};

    private static final String TEST_ID = "0";

    @Test
    public void greyScaleSingleThreadedTest() throws IOException, InterruptedException, TimeoutException {
        File image = new File(ClassLoader.getSystemResource("images/original/animal/2.kitten_medium.jpg").getFile());
        BufferedImage resultReference = ImageIO.read(ClassLoader.getSystemResource("images/greyscale/animal/2.kitten_medium.jpg"));
        String imageName = "2.kitten_medium.jpg";
        int repeat = 100;
        int threadPoolSize = Runtime.getRuntime().availableProcessors();
        boolean successful = false;
        File csvFile = new File("src/main/resources/testMulticore2.csv");
        CSVFileWriter csvFileWriter = new CSVFileWriter(csvFile, CSV_HEADER);
        csvFileWriter.setSeparator(";");

        for (int i = 0; i < repeat; i++) {
            RgbToGreyScale rgbToGreyScale = new RgbToGreyScale();
            try {
                BufferedImage greyScale = rgbToGreyScale.rgbToGreyScaleSplittingRows(image, threadPoolSize);
                compareResultWithReference(ImageIO.read(image), resultReference, greyScale);
                successful = true;
            } finally {
                csvFileWriter.addLine(new String[]{TEST_ID, String.valueOf(threadPoolSize),
                        String.valueOf(successful), String.valueOf(rgbToGreyScale.getConversionTime()),
                        String.valueOf(rgbToGreyScale.getTotalTime()), imageName});
                csvFileWriter.writeCSV(true); // flush each time
            }
        }
    }

    private void compareResultWithReference(BufferedImage originalImage, BufferedImage expected, BufferedImage actual) {
        assertEquals(originalImage.getHeight(), actual.getHeight());
        assertEquals(originalImage.getWidth(), actual.getWidth());
        for (int y = 0; y < expected.getHeight(); y++) {
            for (int x = 0; x < expected.getWidth(); x++) {
                int expectedRgbValue = expected.getRGB(x, y);
                int actualRgbValue = actual.getRGB(x, y);
                assertEquals("RGB differs for Pixel at y: " + y + "x: " + x, expectedRgbValue, actualRgbValue);
            }
        }
    }
}
