import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CommandJob extends SwingWorker<String, String> implements WorkerJob {
    private final JTextArea outputArea;
    private final String[] command;

    public CommandJob(JTextArea outputArea, String[] command) {
        if (outputArea == null) {
            throw new IllegalArgumentException("outputArea must be non-null");
        }
        this.outputArea = outputArea;
        this.command = Objects.requireNonNullElse(command, new String[] {"adb", "devices", "-l"});
    }

    @Override
    protected String doInBackground() {
        try {
            publish("Executing command: " + Arrays.toString(command).replaceAll("[\\[\\]]", ""));
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
            return "Command process completed.";
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid input: " + e);
        }
    }

    @Override
    protected void process(List<String> chunks) {
        for (String chunk : chunks) {
            String lastOutput = Objects.requireNonNullElse(outputArea.getText(), "");
            outputArea.setText(lastOutput + "\n" + chunk);
        }
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

    public void executeJob() {
        execute();
    }

    private Process buildProcess() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
        return pb.start();
    }
}
