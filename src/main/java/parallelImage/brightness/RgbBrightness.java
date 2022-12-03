package parallelImage.brightness;

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
 * @since : 22.11.22
 **/
public class RgbBrightness extends ParallelImageProcessor {

    private final int brightness;

    private Map<Integer, int[]> results;

    public RgbBrightness(int threadPoolSize, int brightness) {
        super(threadPoolSize);
        this.brightness = brightness;
    }

    @Override
    protected ExecutorService makeTask() {
        ExecutorService executor = Executors.newFixedThreadPool(getThreadPoolSize());
        results = Collections.synchronizedMap(new TreeMap<>()); // Key is rowIndex, Value is row
        for (int row = 0; row < getImage().getHeight(); row++) {
            executor.execute(new RgbBrightnessTask(brightness, getImgRgbArray(), results, row));
        }
        return executor;
    }

    @Override
    protected ProcessorResult retrieveResultFromTask() {
        return new ProcessorResult(ImageUtils.setRgbByRow(getImage(), results));
    }
}
