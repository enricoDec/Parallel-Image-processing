package parallelImage.brightness.task;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 04.12.22
 **/
public abstract class BrightnessTask {

    protected final int[][] imgRgbArray;

    protected final int rowIndex;

    protected final int brightness;

    public BrightnessTask(int[][] imgRgbArray, int rowIndex, int brightness) {
        this.imgRgbArray = imgRgbArray;
        this.rowIndex = rowIndex;
        this.brightness = brightness;
    }

    protected int[] brightnessTask(int[][] imgRgbArray, int rowIndex, int brightness) {
        int[] row = new int[imgRgbArray.length];
        for (int i = 0; i < row.length; i++) {
            int pixel = imgRgbArray[i][rowIndex];
            int red = (pixel >> 16) & 0xFF;
            int green = (pixel >> 8) & 0xFF;
            int blue = (pixel) & 0xFF;
            // increase brightness
            red *= brightness;
            green *= brightness;
            blue *= brightness;
            // truncate
            red = Math.min(Math.max(red, 0), 255);
            green = Math.min(Math.max(green, 0), 255);
            blue = Math.min(Math.max(blue, 0), 255);
            row[i] = (red << 16) | (green << 8) | blue;
        }
        return row;
    }
}
