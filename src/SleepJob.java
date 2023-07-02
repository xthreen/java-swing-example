import javax.swing.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SleepJob extends SwingWorker<String, String> implements WorkerJob {
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
            TimeUnit.SECONDS.sleep(1);
            this.publish("Slept for: " + (i) + " seconds");
        }
        return "Completed";
    }

    @Override
    public void process(List<String> chunks) {
        for (String chunk : chunks) {
            outputArea.append(chunk + "\n");
        }
    }

    @Override
    protected void done() {
        try {
            String result = get();
            outputArea.append(result + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeJob() {
        this.execute();
    }
}
