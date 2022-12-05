package parallelImage.histogram;

import parallelImage.ParallelImageProcessor;
import parallelImage.ProcessorResult;
import parallelImage.ProcessorTaskType;
import parallelImage.histogram.task.HistogramBlockingTask;
import parallelImage.histogram.task.HistogramNonBlockingTask;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 19.11.22
 **/
public class HistogramProcessor extends ParallelImageProcessor {

    private final int[] resultRedBucket = new int[256];

    private final int[] resultGreenBucket = new int[256];

    private final int[] resultBlueBucket = new int[256];

    LinkedList<int[]> redBuckets;

    LinkedList<int[]> greenBuckets;

    LinkedList<int[]> blueBuckets;

    private ProcessorTaskType type;

    private Histogram histogram;

    public HistogramProcessor(int threadPoolSize) {
        super(threadPoolSize);
    }

    @Override
    protected ExecutorService makeTask(ProcessorTaskType type) {
        this.type = type;
        ExecutorService executor = Executors.newFixedThreadPool(getThreadPoolSize());
        switch (type) {
            case BLOCKING -> this.histogram = new Histogram();
            case NON_BLOCKING -> {
                redBuckets = new LinkedList<>();
                greenBuckets = new LinkedList<>();
                blueBuckets = new LinkedList<>();
            }
        }
        for (int row = 0; row < getImage().getHeight(); row++) {
            switch (type) {
                case BLOCKING -> {
                    // Each Thread shares 3 buckets
                    executor.execute(new HistogramBlockingTask(getImgRgbArray(), row, histogram));
                }
                case NON_BLOCKING -> {
                    // Each Thread gets 3 buckets (r, g, b)
                    int[] redBucket = new int[256];
                    redBuckets.add(redBucket);
                    int[] greenBucket = new int[256];
                    greenBuckets.add(greenBucket);
                    int[] blueBucket = new int[256];
                    blueBuckets.add(blueBucket);
                    executor.execute(new HistogramNonBlockingTask(getImgRgbArray(), row, redBucket, greenBucket,
                            blueBucket));
                }
            }
        }
        return executor;
    }

    @Override
    protected ProcessorResult retrieveResultFromTask() {
        return switch (type) {
            case BLOCKING -> new ProcessorResult(getImage(), histogram);
            case NON_BLOCKING -> {
                mergeResults(redBuckets, greenBuckets, blueBuckets);
                yield new ProcessorResult(getImage(), new Histogram(resultRedBucket, resultGreenBucket,
                        resultBlueBucket));
            }
        };
    }

    private void mergeResults(LinkedList<int[]> redBuckets, LinkedList<int[]> greenBuckets,
                              LinkedList<int[]> blueBuckets) {
        for (int i = 0; i < 256; i++) {
            for (int[] bucket : redBuckets) {
                resultRedBucket[i] += bucket[i];
            }
            for (int[] bucket : greenBuckets) {
                resultGreenBucket[i] += bucket[i];
            }
            for (int[] bucket : blueBuckets) {
                resultBlueBucket[i] += bucket[i];
            }
        }
    }
}
