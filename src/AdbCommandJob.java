import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AdbCommandJob extends SwingWorker<String, String> implements WorkerJob {
    private final JTextArea outputArea;
    private final String command;

    public AdbCommandJob(JTextArea outputArea, String command) {
        if (outputArea == null) {
            throw new IllegalArgumentException("outputArea must be non-null");
        }
        this.outputArea = outputArea;
        this.command = Objects.requireNonNullElse(command, "devices");
    }
    @Override
    protected String doInBackground() {
        try {
            publish("Executing command: " + command);
            Process p = this.buildProcess();
            try (InputStream in = p.getInputStream();
                    InputStream err = p.getErrorStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        publish(new String(buffer, 0, bytesRead));
                    }
                    while ((bytesRead = err.read(buffer)) != -1) {
                        publish(new String(buffer, 0, bytesRead));
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid input: " + e);
                }
            p.waitFor(10, TimeUnit.SECONDS);
            return "Completed";
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid input: " + e);
        }
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
            outputArea.append(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void executeJob() {
        this.outputArea.setText("");
        execute();
    }

    private Process buildProcess() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("adb", command);
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
        return pb.start();
    }
}
