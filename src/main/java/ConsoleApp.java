import logger.Logger;
import parallelImage.MeasurableParallelImageProcessor;
import parallelImage.ParallelImageProcessor;
import parallelImage.ProcessorResult;
import parallelImage.ProcessorTaskType;
import parallelImage.brightness.BrightnessProcessor;
import parallelImage.greyscale.GreyScaleProcessor;
import parallelImage.histogram.HistogramProcessor;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 22.01.23
 **/
public class ConsoleApp {
    private final static int requiredArgs = 3;
    private static final String greyscaleArg = "-g";
    private static final String histogramArg = "-h";
    private static final String brightnessArg = "-b";
    private static final String brightnessValueArg = "-bv";
    private static final String debugOption = "-d";
    private static final String nonBlockingOption = "-tnb";
    private static final String blockingOption = "-tb";
    private static final String threadPoolOption = "-tp";
    private static int argPointer = 0;

    public static void main(String[] args) {
        if (args.length < requiredArgs) {
            printError("Missing Arguments.");
            help();
            System.exit(1);
        }
        CmdConfig config = parseCmds(args);
        if (config == null) {
            help();
            System.exit(1);
        }
        try {
            runConfig(config);
        } catch (IOException | InterruptedException | TimeoutException e) {
            printError(e.getMessage());
        }
    }

    private static CmdConfig parseCmds(String[] args) {
        CmdConfig config;
        CmdConfig.OPERATION operation = null;
        double brightness = 0;
        File imageFile;
        File imageOutputFile;

        // First argument should be image operation
        String arg1 = getNextArg(args);
        if (arg1.contentEquals(greyscaleArg) || arg1.contentEquals(histogramArg) || arg1.contentEquals(brightnessArg)) {
            switch (arg1) {
                case greyscaleArg -> operation = CmdConfig.OPERATION.GREYSCALE;
                case histogramArg -> operation = CmdConfig.OPERATION.HISTOGRAM;
                case brightnessArg -> {
                    operation = CmdConfig.OPERATION.BRIGHTNESS;
                    String arg2 = getNextArg(args);
                    if (arg2.contentEquals(brightnessValueArg)) {
                        try {
                            String brightnessValueArg = getNextArgNoLowerCase(args);
                            brightness = Double.parseDouble(brightnessValueArg);
                        } catch (NumberFormatException e) {
                            printError("Brightness value not parsable!");
                            return null;
                        }
                    } else {
                        printError("Brightness value not specified!");
                        return null;
                    }
                }
            }
        } else {
            return null;
        }
        // Next argument has to be Image File
        imageFile = new File(getNextArg(args));
        if (!imageFile.exists() || !imageFile.isFile()) {
            printError("Given Image File:" + imageFile.getAbsolutePath() + " does not exist or is not a File!");
            return null;
        }
        // Next argument has to be Output image File
        imageOutputFile = new File(getNextArg(args));
        // Make basic Config
        if (operation != null) {
            config = new CmdConfig(operation, imageFile, imageOutputFile);
            if (operation == CmdConfig.OPERATION.BRIGHTNESS) {
                config.setBrightness(brightness);
            }
        } else {
            printError("Argument error");
            return null;
        }
        // Get Options
        for (int i = argPointer; i < args.length - 1; i++) {
            switch (getNextArg(args)) {
                case debugOption -> config.setDoLog(true);
                case nonBlockingOption -> config.setType(ProcessorTaskType.NON_BLOCKING);
                case blockingOption -> config.setType(ProcessorTaskType.BLOCKING);
                case threadPoolOption -> {
                    String threadPoolSizeArg = getNextArgNoLowerCase(args);
                    int threadPoolSize;
                    try {
                        threadPoolSize = Integer.parseInt(threadPoolSizeArg);
                    } catch (NumberFormatException e) {
                        printError("Thread pool size value not parsable!");
                        return null;
                    }
                    config.setThreadPoolSize(threadPoolSize);
                }
                default -> {
                    printError("Illegal argument option");
                }
            }
        }
        return config;
    }

    private static String getNextArg(String[] args) {
        if (args.length <= argPointer) {
            printError("Missing Argument.");
            help();
            System.exit(1);
        }
        String arg = args[argPointer].toLowerCase().trim();
        argPointer++;
        return arg;
    }

    private static String getNextArgNoLowerCase(String[] args) {
        if (args.length <= argPointer) {
            printError("Missing Argument.");
            help();
            System.exit(1);
        }
        String arg = args[argPointer].trim();
        argPointer++;
        return arg;
    }

    private static void runConfig(CmdConfig cmdConfig) throws IOException, InterruptedException, TimeoutException {
        Logger logger = null;
        ParallelImageProcessor processor = null;
        switch (cmdConfig.getOperation()) {
            case GREYSCALE -> processor = new GreyScaleProcessor(cmdConfig.getThreadPoolSize());
            case HISTOGRAM -> processor = new HistogramProcessor(cmdConfig.getThreadPoolSize());
            case BRIGHTNESS ->
                    processor = new BrightnessProcessor(cmdConfig.getThreadPoolSize(), cmdConfig.getBrightness());
        }
        if (cmdConfig.isDoLog()) {
            logger = Logger.getInstance();
            logger.start(null);
            processor = new MeasurableParallelImageProcessor(processor);
        }
        ProcessorResult result = null;
        switch (cmdConfig.getType()) {
            case BLOCKING -> result = processor.processImage(cmdConfig.getImageFile(), ProcessorTaskType.BLOCKING);

            case NON_BLOCKING ->
                    result = processor.processImage(cmdConfig.getImageFile(), ProcessorTaskType.NON_BLOCKING);

        }
        // Save result
        if (cmdConfig.getOperation() == CmdConfig.OPERATION.HISTOGRAM) {
            result.getHistogram().saveHistogram(cmdConfig.getOutputFile());
        } else {
            ImageIO.write(result.getImage(), "png", cmdConfig.getOutputFile());
        }
        if (logger != null) {
            ((MeasurableParallelImageProcessor) processor).logExecutionTime();
            logger.close();
        }
    }

    private static void help() {
        final String arguments = "-[Image operation] [Input File] [Output File] -[options]" + System.lineSeparator();
        final String example = "-g image.png greyscaleImage.png" + System.lineSeparator();
        final String imageOperations = "Image operations can be:" + System.lineSeparator()
                + "-g (greyscale)" + System.lineSeparator()
                + "-h (histogram)" + System.lineSeparator()
                + "-b (brightness) -bv [brightness_value]" + System.lineSeparator();
        final String debug = "-d or -D to have some debug output" + System.lineSeparator();
        final String type = "-tnb (Non Blocking) or -tb (Blocking)" + System.lineSeparator();
        final String threadPoolSize = "-tp (Thread Pool) [max_thread_pool_size]" + System.lineSeparator();
        final String options =
                "Options:" + System.lineSeparator() + debug + type + threadPoolSize + System.lineSeparator();
        final String helpText = "Usage:" + System.lineSeparator() + arguments + example + imageOperations + options;
        printMessage(helpText);
    }

    private static void printMessage(String message) {
        System.out.println(message);
    }

    private static void printError(String errorMessage) {
        System.err.println(errorMessage);
    }
}
