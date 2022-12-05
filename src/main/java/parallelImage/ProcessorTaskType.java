package parallelImage;

/**
 * Defines different types of Tasks that can be used to process an image. The Result is always the same, the performance
 * may differ.
 *
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 03.12.22
 **/
public enum ProcessorTaskType {

    /**
     * All Tasks share the image and write the results directly on the image. This should save execution time overall
     * but make Tasks have to block
     */
    BLOCKING,

    /**
     * Each Task delivers after executing a result that is merged at the end (the result is merged in one Thread). This
     * has the advantage that each Thread can completely work parallel, but requires more resources after to merge the
     * results.
     */
    NON_BLOCKING
}

