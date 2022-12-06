package parallelImage;

import utils.CSVFileWriter;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 22.11.22
 **/
public interface Measurable {

    /**
     * Gets the execution time needed to read the image (includes reading image as int[][] if applicable)
     *
     * @return the execution time needed to read the image
     */
    NanoTimeBuilder getImageReadTime();

    /**
     * Gets the total execution time needed to process the image
     *
     * @return the total execution time for processing
     */
    NanoTimeBuilder getTotalProcessingTime();

    /**
     * Gets the execution time for the task
     *
     * @return the execution time for the task
     */
    NanoTimeBuilder getTasksTime();

    /**
     * Gets the execution time needed to retrieve the result from the task
     *
     * @return the execution time needed to retrieve the result
     */
    NanoTimeBuilder getRetrieveResultTime();

    /**
     * Log all the recorded execution times
     */
    void logExecutionTime();

    /**
     * Log the result in the csv. It will be flushed each time.
     *
     * @param csvFileWriter  csvFileWriter
     * @param id             id
     * @param threadPoolSize threadPoolSize
     * @param imageName      imageName
     * @param successful     successful
     * @throws FileNotFoundException if csv file not found
     */
    default void logResult(CSVFileWriter csvFileWriter, String id, int threadPoolSize,
                           String imageName, boolean successful) throws FileNotFoundException {
        csvFileWriter.addLine(new String[]{ //
                id, // ID
                String.valueOf(threadPoolSize), // number of threads
                String.valueOf(getImageReadTime().asMillis()), // read image time
                String.valueOf(getTasksTime().asMillis()), // task time
                String.valueOf(getRetrieveResultTime().asMillis()), // retrieve result time
                String.valueOf(getTotalProcessingTime().asMillis()), // total execution time
                imageName, // image name
                String.valueOf(successful)}); // check if reference is equal
        csvFileWriter.writeCSV(true); // flush each time
    }

    enum CsvHeader {
        ID("ID"),
        THREAD_NUMBER("Number of Threads"),
        READ_IMAGE("Execution time for reading image (ms)"),
        TASK_EXECUTION("Task Execution time (ms)"),
        RETRIEVE_RESULT("Execution time for retrieving result from tasks (ms)"),
        TOTAL_PROCESSING("Total processor execution time (ms)"),
        IMAGE_NAME("Image name"),
        IS_SUCCESSFUL("Successful");

        public static final String[] CSV_HEADER = new String[]{ID.value, THREAD_NUMBER.value, READ_IMAGE.value,
                TASK_EXECUTION.value, RETRIEVE_RESULT.value, TOTAL_PROCESSING.value, IMAGE_NAME.value,
                IS_SUCCESSFUL.value};
        public final String value;

        CsvHeader(String value) {
            this.value = value;
        }
    }

    class NanoTimeBuilder {

        private final long timeInNano;

        public NanoTimeBuilder(long timeInNano) {
            this.timeInNano = timeInNano;
        }

        public long asNano() {
            return timeInNano;
        }

        public long asMillis() {
            return TimeUnit.NANOSECONDS.toMillis(timeInNano);
        }
    }
}


