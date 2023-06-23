import javax.swing.*;
import java.util.List;
import java.util.Objects;
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
            this.publish("Slept for: " + (i + 1) + " seconds");
        }
        return "Completed";
    }

    @Override
    protected void done() {
        try {
            String result = get();
            String lastOutput = Objects.requireNonNullElse(outputArea.getText(), "");
            if (!lastOutput.isEmpty()) {
                outputArea.setText(lastOutput + "\n" + result);
            } else {
                outputArea.setText(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(List<String> chunks) {
        for (String chunk : chunks) {
            String lastOutput = Objects.requireNonNullElse(outputArea.getText(), "");
            outputArea.setText(lastOutput + "\n" + chunk);
        }
    }

    public void executeJob() {
        execute();
    }
}
