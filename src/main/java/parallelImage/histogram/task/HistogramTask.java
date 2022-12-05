package parallelImage.histogram.task;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 04.12.22
 **/
public abstract class HistogramTask {

    protected final int[][] imgRgbArray;

    protected final int rowIndex;

    public HistogramTask(int[][] imgRgbArray, int rowIndex) {
        this.imgRgbArray = imgRgbArray;
        this.rowIndex = rowIndex;
    }
}
