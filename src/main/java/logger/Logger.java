package logger;

import jakarta.annotation.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 01.11.22
 **/
public class Logger implements Closeable {

    private static Logger instance = null;

    private final BlockingQueue<LogMessage> logQueue = new ArrayBlockingQueue<>(10);

    private volatile boolean running = false;

    private Thread t;

    private Logger() {
    }

    /**
     * Per default logger just ignores every {@link #log(String, TYPE)} call since debugMode is set to NONE. To start
     * the logger call {@link #start(OutputStream)}
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
     * Start the Logger, only one logger Thread can exist.
     *
     * @param os {@link OutputStream} or null if {@link System#out} should be used
     */
    public synchronized void start(@Nullable OutputStream os) {
        if (!running) {
            if (os == null) {
                os = System.out;
            }
            OutputStream finalOs = os;
            t = new Thread(() -> {
                while (running || !logQueue.isEmpty()) {
                    try {
                        LogMessage log = logQueue.poll(100, TimeUnit.MILLISECONDS);
                        if (log != null) {
                            SimpleDateFormat formatter = new SimpleDateFormat("[H:mm:ss] ");
                            Date date = new Date();
                            StringBuilder output = new StringBuilder(formatter.format(date));
                            switch (log.type) {
                                case INFO -> output.append("[INFO]: ");
                                case DEBUG -> output.append("[DEBUG]: ");
                                case WARNING -> output.append("[WARNING]: ");
                                case ERROR -> output.append("[ERROR]: ");
                            }
                            output.append(log.message);
                            output.append(System.lineSeparator());
                            finalOs.write(output.toString().getBytes(StandardCharsets.UTF_8));
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
                // cleanup
                try {
                    finalOs.flush();
                    finalOs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.setName("Logger Thread");
            running = true;
            t.start();
        } else {
            throw new IllegalStateException("Logger already Running");
        }
    }

    public void log(String message, TYPE type) {
        try {
            if (!message.isBlank()) {
                logQueue.put(new LogMessage(message.trim(), type));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        this.running = false;
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public enum TYPE {
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
