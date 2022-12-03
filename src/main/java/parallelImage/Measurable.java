package parallelImage;

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


