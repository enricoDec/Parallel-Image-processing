import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 01.11.22
 **/
public class Logger implements Runnable {

    private static Logger instance = null;

    private final BlockingQueue<String> logQueue = new ArrayBlockingQueue<>(10);

    private static final AtomicBoolean RUNNING = new AtomicBoolean(true);

    private static OutputStream OS = System.out;

    private Logger() {
    }

    public static Logger getInstance() {
        if(instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        // TODO: 01.11.22 Not sure if synchronized is needed here
        try {
            if(RUNNING.get()) {
                logQueue.put(message);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (RUNNING.get()) {
            try {
                String message = logQueue.poll(100, TimeUnit.MILLISECONDS);
                if (message != null) {
                    OS.write(message.getBytes(StandardCharsets.UTF_8));
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Shutting Logger...");
    }

    public static void setOutputStream(OutputStream os) {
        OS = os;
    }

    public static void cleanup() {
        RUNNING.set(false);
    }

    public enum TYPE {
        INFO,
        DEBUG,
        WARNING,
        ERROR
    }
}
