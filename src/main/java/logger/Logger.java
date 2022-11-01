package logger;

import jakarta.annotation.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 01.11.22
 **/
public class Logger implements Runnable {

    private static Logger instance = null;

    private final BlockingQueue<LogMessage> logQueue = new ArrayBlockingQueue<>(10);

    private volatile boolean running = false;

    private TYPE debugMode = TYPE.NONE;

    private OutputStream os = System.out;

    private Logger() {
    }

    /**
     * Per default logger just ignores every {@link #log(String, TYPE)} call since debugMode is set to NONE.
     * To start the logger call {@link #start(TYPE, OutputStream)}
     *
     * @return Instance of logger (Singleton)
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    /**
     * Start the Logger,
     *
     * @param debugMode debugMode of {@link TYPE}
     * @param os        {@link OutputStream} or null if {@link System#out} should be used
     */
    public synchronized void start(TYPE debugMode, @Nullable OutputStream os) {
        if (!running) {
            this.debugMode = debugMode;
            if (os != null) {
                this.os = os;
            }
            Thread t = new Thread(this, "Logger Thread");
            t.start();
            running = true;
        } else {
            throw new IllegalStateException("Logger already Running");
        }
    }

    public void log(String message, TYPE type) {
        // TODO: 01.11.22 Not sure if synchronized is needed here
        try {
            if (debugMode != TYPE.NONE) {
                logQueue.put(new LogMessage(message, type));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                LogMessage log = logQueue.poll(100, TimeUnit.MILLISECONDS);
                if (log != null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("[H:mm:ss] ");
                    Date date = new Date();
                    StringBuilder output = new StringBuilder(formatter.format(date));
                    switch (log.type) {
                        case NONE -> throw new IllegalStateException("Message of Type " + TYPE.NONE + " should not exist!");
                        case INFO -> output.append("[INFO]: ");
                        case DEBUG -> output.append("[DEBUG]: ");
                        case WARNING -> output.append("[WARNING]: ");
                        case ERROR -> output.append("[ERROR]: ");
                    }
                    output.append(log.message);
                    output.append(System.lineSeparator());
                    os.write(output.toString().getBytes(StandardCharsets.UTF_8));
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cleanup() {
        this.running = false;
    }

    public enum TYPE {
        NONE, // no output
        INFO,
        DEBUG,
        WARNING,
        ERROR
    }

    private static class LogMessage {
        public final String message;
        public final TYPE type;

        private LogMessage(String message, TYPE type) {
            this.message = message;
            this.type = type;
        }
    }
}
