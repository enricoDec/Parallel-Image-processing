import jakarta.annotation.Nonnull;
import parallelImage.ProcessorTaskType;

import java.io.File;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 22.01.23
 **/
public class CmdConfig {
    private final OPERATION operation;
    private final File imageFile;
    private final File outputFile;
    // Optional
    private boolean doLog = false;
    private boolean isInteractive = false;


    // Not Optional
    private ProcessorTaskType type = ProcessorTaskType.NON_BLOCKING;
    private int threadPoolSize = Runtime.getRuntime().availableProcessors();
    private double brightness = 0;

    public CmdConfig(@Nonnull OPERATION operation, @Nonnull File imageFile, @Nonnull File outputFile) throws IllegalArgumentException {
        if (operation == null || imageFile == null || outputFile == null) {
            throw new IllegalArgumentException();
        }
        this.operation = operation;
        this.imageFile = imageFile;
        this.outputFile = outputFile;
    }

    public boolean isDoLog() {
        return doLog;
    }

    public void setDoLog(boolean doLog) {
        this.doLog = doLog;
    }

    public boolean isInteractive() {
        return isInteractive;
    }

    public void setInteractive(boolean interactive) {
        isInteractive = interactive;
    }

    public ProcessorTaskType getType() {
        return type;
    }

    public void setType(ProcessorTaskType type) {
        this.type = type;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }

    public OPERATION getOperation() {
        return operation;
    }

    public File getImageFile() {
        return imageFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    protected enum OPERATION {
        BRIGHTNESS,
        HISTOGRAM,
        GREYSCALE
    }
}
