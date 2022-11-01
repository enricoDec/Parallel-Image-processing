import java.util.concurrent.BlockingQueue;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 01.11.22
 **/
public class RgbToGreyscaleTask implements Runnable {

    private BlockingQueue<int[]> queue;

    public RgbToGreyscaleTask(BlockingQueue<int[]> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

    }
}
