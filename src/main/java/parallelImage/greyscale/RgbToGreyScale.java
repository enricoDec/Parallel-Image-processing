package parallelImage.greyscale;

import parallelImage.ParallelImageProcessor;
import parallelImage.ProcessorResult;
import utils.ImageUtils;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 02.11.22
 **/
public class RgbToGreyScale extends ParallelImageProcessor {

    private Map<Integer, int[]> results;

    public RgbToGreyScale(int threadPoolSize) {
        super(threadPoolSize);
    }

    @Override
    protected ExecutorService makeTask() {
        ExecutorService executor = Executors.newFixedThreadPool(getThreadPoolSize());
        results = Collections.synchronizedMap(new TreeMap<>()); // Key is rowIndex, Value is row
        for (int row = 0; row < getImage().getHeight(); row++) {
            executor.execute(new RgbToGreyscaleTask(getImgRgbArray(), results, row));
        }
        return executor;
    }

    @Override
    protected ProcessorResult retrieveResultFromTask() {
        return new ProcessorResult(ImageUtils.setRgbByRow(getImage(), results));
    }
}
