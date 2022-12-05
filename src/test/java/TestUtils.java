import jakarta.annotation.Nullable;
import logger.Logger;
import parallelImage.Measurable;
import utils.CSVFileWriter;
import utils.CsvUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 20.11.22
 **/
public class TestUtils {

    public static void compareImages(BufferedImage originalImage, BufferedImage expected, BufferedImage actual) {
        assertEquals(originalImage.getHeight(), actual.getHeight());
        assertEquals(originalImage.getWidth(), actual.getWidth());

        for (int y = 0; y < expected.getHeight(); y++) {
            for (int x = 0; x < expected.getWidth(); x++) {
                Color expectedColor = new Color(expected.getRGB(x, y));
                Color actualColor = new Color(actual.getRGB(x, y));
                assertEquals("Color differs for Pixel at y: " + y + ", x: " + x, expectedColor, actualColor);
            }
        }
    }

    /**
     * Make the CSV Writer
     *
     * @param csvFile csv file
     * @return @{@link CSVFileWriter}
     */
    public static CSVFileWriter makeCsvWriter(File csvFile) {
        CSVFileWriter csvFileWriter = new CSVFileWriter(csvFile, Measurable.CsvHeader.CSV_HEADER);
        csvFileWriter.setSeparator(";");
        return csvFileWriter;
    }

    /**
     * Calculates the average of all results in the given csv file and writes it to a new csv
     *
     * @param csvFileWriter csvFileWriter
     * @param logger        logger may be null if no log is desired
     * @throws IOException if an I/O error occurs
     */
    public static void calculateAvgResults(CSVFileWriter csvFileWriter, @Nullable Logger logger) throws IOException {
        boolean doLog = logger != null;
        int rows = csvFileWriter.getNumberOfRows();
        File csvFile = csvFileWriter.getCsvFile();

        double avgRead = CsvUtils.getAverageOfCsvColumn(csvFile, Measurable.CsvHeader.READ_IMAGE.value, rows);
        if (doLog) {
            logger.log(String.format("Average time to read the image: %.3fms from %d data-points.", avgRead, rows),
                    Logger.TYPE.INFO);
        }

        double avgTask = CsvUtils.getAverageOfCsvColumn(csvFile, Measurable.CsvHeader.TASK_EXECUTION.value, rows);
        if (doLog) {
            logger.log(String.format("Average time to run the task: %.3fms from %d data-points.", avgTask, rows),
                    Logger.TYPE.INFO);
        }

        double avgMerge = CsvUtils.getAverageOfCsvColumn(csvFile,
                Measurable.CsvHeader.RETRIEVE_RESULT.value, rows);
        if (doLog) {
            logger.log(String.format("Average time to retrieve the result: %.3fms from %d data-points.", avgMerge,
                    rows), Logger.TYPE.INFO);
        }

        double avgTotal = CsvUtils.getAverageOfCsvColumn(csvFile, Measurable.CsvHeader.TOTAL_PROCESSING.value, rows);
        if (doLog) {
            logger.log(String.format("Average processing time: %.3fms from %d data-points.", avgTotal, rows),
                    Logger.TYPE.INFO);
        }

        String[] CSV_HEADER = new String[]{Measurable.CsvHeader.READ_IMAGE.value,
                Measurable.CsvHeader.TASK_EXECUTION.value,
                Measurable.CsvHeader.RETRIEVE_RESULT.value, Measurable.CsvHeader.TOTAL_PROCESSING.value};
        // CSV will be next to existing one
        String csvFileName = csvFileWriter.getCsvFile().getName().replaceFirst("[.][^.]+$", "");
        File avgCsvFile = new File(csvFileWriter.getCsvFile().getParentFile(), csvFileName + "_AVG_RESULT.csv");
        CSVFileWriter avgCsvFileWriter = new CSVFileWriter(avgCsvFile, CSV_HEADER);
        avgCsvFileWriter.addLine(new String[]{ //
                String.valueOf(avgRead), //
                String.valueOf(avgTask), //
                String.valueOf(avgMerge), //
                String.valueOf(avgTotal) //
        });
        avgCsvFileWriter.writeCSV(true); // flush
    }
}
