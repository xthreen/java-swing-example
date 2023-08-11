import javax.swing.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SleepJob extends SwingWorker<String, String> implements WorkerJob {
    private static final Logger logger = Logger.getLogger(FileDownloadJob.class.getName());
    private final JTextArea outputArea;
    private final int iters;

    public SleepJob(JTextArea outputArea, int iters) {
        if (outputArea == null) {
            throw new IllegalArgumentException("outputArea must be non-null");
        }
        this.outputArea = outputArea;
        if (iters < 1) {
            throw new IllegalArgumentException("iters must be >= 1");
        }
        this.iters = iters;
    }

    @Override
    protected String doInBackground() throws Exception {
        for (int i = 0; i <= iters; i++) {
            if (isCancelled()) {
                return "Cancelled";
            }
            this.publish("Slept for: " + (i) + " seconds");

            TimeUnit.SECONDS.sleep(1);
        }
        return "Completed";
    }

    @Override
    public void process(List<String> chunks) {
        for (String chunk : chunks) {
            outputArea.append(chunk + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        }
    }

    @Override
    protected void done() {
        try {
            String result = get();
            outputArea.append(result + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception e) {
            logger.log(Utils.LOG_LEVEL, e.getMessage(), e);
        }
    }

    public void executeJob() {
        this.execute();
    }
}
