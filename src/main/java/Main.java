import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 31.10.22
 **/
public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("src/main/resources/images/animal/1.kitten_small.jpg");
        BufferedImage image =  ImageIO.read(file);
        image.getRGB(1,1);


    }
}
