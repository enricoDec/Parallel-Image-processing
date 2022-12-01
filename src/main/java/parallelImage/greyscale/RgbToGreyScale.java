package parallelImage.greyscale;

import parallelImage.ParallelImageProcessor;
import utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.File;
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

    public RgbToGreyScale(File imageFile, int threadPoolSize) {
        super(imageFile, threadPoolSize);
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
    protected BufferedImage retrieveResultFromTask() {
        return ImageUtils.setRgbByRow(getImage(), results);
    }
}
