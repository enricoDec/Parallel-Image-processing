package parallelImage.brightness;

import parallelImage.ParallelImageProcessor;
import parallelImage.ProcessorResult;
import parallelImage.ProcessorTaskType;
import parallelImage.brightness.task.BrightnessBlockingTask;
import parallelImage.brightness.task.BrightnessNonBlockingTask;
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
public class BrightnessProcessor extends ParallelImageProcessor {

    private final double brightness;

    private Map<Integer, int[]> results;

    private ProcessorTaskType type;

    public BrightnessProcessor(int threadPoolSize, double brightness) {
        super(threadPoolSize);
        this.brightness = brightness;
    }

    @Override
    protected ExecutorService makeTask(ProcessorTaskType type) {
        this.type = type;
        ExecutorService executor = Executors.newFixedThreadPool(getThreadPoolSize());
        results = Collections.synchronizedMap(new TreeMap<>()); // Key is rowIndex, Value is row
        for (int row = 0; row < getImage().getHeight(); row++) {
            switch (type) {
                case BLOCKING -> executor.execute(new BrightnessBlockingTask(getImgRgbArray(), row, brightness,
                        getImage()));
                case NON_BLOCKING -> executor.execute(new BrightnessNonBlockingTask(getImgRgbArray(), row, brightness,
                        results));
            }
        }
        return executor;
    }

    @Override
    protected ProcessorResult retrieveResultFromTask() {
        return switch (type) {
            case BLOCKING -> new ProcessorResult(getImage());
            case NON_BLOCKING -> new ProcessorResult(ImageUtils.setRgbByRow(getImage(), results));
        };
    }
}
