package parallelImage.histogram;

import parallelImage.ParallelImageProcessor;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 19.11.22
 **/
public class RgbToHistogram extends ParallelImageProcessor {

    private final int[] resultRedBucket = new int[256];

    private final int[] resultGreenBucket = new int[256];

    private final int[] resultBlueBucket = new int[256];

    LinkedList<int[]> redBuckets;

    LinkedList<int[]> greenBuckets;

    LinkedList<int[]> blueBuckets;

    public RgbToHistogram(File imageFile, int threadPoolSize) {
        super(imageFile, threadPoolSize);
    }

    @Override
    public ExecutorService makeTask() {
        // TODO: could just pass the Task
        redBuckets = new LinkedList<>();
        greenBuckets = new LinkedList<>();
        blueBuckets = new LinkedList<>();
        return makeTaskBySplittingRows(redBuckets, greenBuckets, blueBuckets);
    }

    @Override
    protected Histogram retrieveResultFromTask() {
        mergeResults(redBuckets, greenBuckets, blueBuckets);
        return new Histogram(resultRedBucket, resultGreenBucket, resultBlueBucket);
    }

    private ExecutorService makeTaskBySplittingRows(LinkedList<int[]> redBuckets, LinkedList<int[]> greenBuckets, LinkedList<int[]> blueBuckets) {
        ExecutorService executor = Executors.newFixedThreadPool(getThreadPoolSize());
        // Each Thread gets 3 buckets (r, g, b)
        for (int row = 0; row < getImage().getHeight(); row++) {
            int[] redBucket = new int[256];
            redBuckets.add(redBucket);
            int[] greenBucket = new int[256];
            greenBuckets.add(greenBucket);
            int[] blueBucket = new int[256];
            blueBuckets.add(blueBucket);
            executor.execute(new RgbToHistogramTask(getImgRgbArray(), redBucket, greenBucket, blueBucket, row));
        }
        return executor;
    }

    private void mergeResults(LinkedList<int[]> redBuckets, LinkedList<int[]> greenBuckets, LinkedList<int[]> blueBuckets) {
        for (int i = 0; i < 255; i++) {
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
