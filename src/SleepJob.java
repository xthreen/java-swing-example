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
        for (int i = 0; i < iters; i++) {
            if (isCancelled()) {
                return "Cancelled";
            }
            publish("Iteration " + i);
            TimeUnit.SECONDS.sleep(1);
        }
        return "Completed";
    }

    @Override
    protected void done() {
        try {
            String result = get();
            this.processFinalResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(List<String> chunks) {
        this.processIntermediateResult(chunks);
    }

    public void executeJob() {
        execute();
    }

    public void processIntermediateResult(List<String> chunks) {
        for (String chunk : chunks) {
            outputArea.setText(chunk + "\n");
        }
    }

    public void processFinalResult(String result) {
        outputArea.setText(result + "\n");
    }
}
