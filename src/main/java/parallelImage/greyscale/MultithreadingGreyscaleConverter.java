package parallelImage.greyscale;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultithreadingGreyscaleConverter {
    public static BufferedImage convertToGreyscale(File file) throws IOException {
        // read the image from the file
        BufferedImage image = ImageIO.read(file);
        int width = image.getWidth();
        int height = image.getHeight();
        // create a thread pool with as many threads as there are processors
        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        // divide the image into N equally-sized sections, where N is the number of threads
        int sectionHeight = height / Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            // calculate the start and end y coordinates of the current section
            int startY = i * sectionHeight;
            int endY = (i + 1) * sectionHeight;
            // submit a new task to the thread pool to convert the current section of the image to greyscale
            threadPool.submit(() -> {
                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < width; x++) {
                        int p = image.getRGB(x, y);
                        int a = (p >> 24) & 0xff;
                        int r = (p >> 16) & 0xff;
                        int g = (p >> 8) & 0xff;
                        int b = p & 0xff;
                        // calculate average
                        int avg = (r + g + b) / 3;
                        // replace RGB value with avg
                        p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                        image.setRGB(x, y, p);
                    }
                }
            });
        }
        // shut down the thread pool and wait for all tasks to complete
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // handle interruption here
        }
        return image;
    }
}
