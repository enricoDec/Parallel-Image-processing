import logger.Logger;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import parallelImage.MeasurableParallelImageProcessor;
import parallelImage.ProcessorTaskType;
import parallelImage.greyscale.GreyScaleProcessor;
import utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 06.12.22
 **/
public class OpenCvTest {

    static {
        //Loading the OpenCV core library

        ImageIO.setUseCache(false);
    }

    private final File file =
            new File(ClassLoader.getSystemResource("images/original/nature/4.nature_mega.jpeg").getFile());

    @Test
    public void brightnessTest() {

    }

    @Test
    public void openCvGreyscaleTest() {
        try {
            long startTime = System.currentTimeMillis();
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            //Reading the image
            System.out.println("Lib loading time: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();
            Mat src = Imgcodecs.imread(file.getAbsolutePath());
            //Creating the empty destination matrix
            Mat dst = new Mat();
            //Converting the image to grey scale
            Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);
            System.out.println("Image conversion time: " + (System.currentTimeMillis() - startTime) + "ms");
            //Writing the image
            startTime = System.currentTimeMillis();
            Imgcodecs.imwrite("src/main/resources/images/opencv/greyscale.jpg", dst);
            System.out.println("Image write time: " + (System.currentTimeMillis() - startTime) + "ms");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Test
    public void ownGreyscaleTest() {
        try {
            Logger.getInstance().start(Logger.TYPE.INFO, null);
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            MeasurableParallelImageProcessor processor =
                    new MeasurableParallelImageProcessor(new GreyScaleProcessor(2));
            processor.processImage(ImageUtils.mat2BufferedImage(Imgcodecs.imread(file.getAbsolutePath()), ".jpeg"),
                    ProcessorTaskType.BLOCKING);
            System.out.println("Image conversion time: " + processor.getTotalProcessingTime().asMillis() + "ms");
            processor.logExecutionTime();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    @Test
    public void histogramTest() throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        BufferedImage image = ImageUtils.mat2BufferedImage(Imgcodecs.imread(file.getAbsolutePath()), ".jpeg");
        long startTime = System.currentTimeMillis();
        ImageUtils.imageToRgbArray(image);
        System.out.println("Time: " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
