import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandJob extends SwingWorker<String, String> implements WorkerJob {
    private final JTextArea outputArea;
    private final AllowedCommand command;

    public CommandJob(JTextArea outputArea, AllowedCommand command) {
        if (outputArea == null) {
            throw new IllegalArgumentException("outputArea must be non-null");
        }
        this.outputArea = outputArea;
        this.command = command;
    }

    @Override
    protected String doInBackground() {
        try {
            this.publish("\n" + "Executing command: " + Arrays.toString(command.getCommand()).replaceAll("[\\[\\]]", ""));
            Process p = this.buildProcess();
            try (InputStream in = p.getInputStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        this.publish(new String(buffer, 0, bytesRead));
                    }
                } catch (Exception e) {
                    throw new Exception(e);
                }
            p.waitFor(10, TimeUnit.SECONDS);
            return "Command process completed.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Command process failed: " + e.getMessage();
        }
    }

    @Override
    protected void process(List<String> chunks) {
        for (String chunk : chunks) {
            outputArea.append("\n" + chunk);
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        }
    }

    @Override
    protected void done() {
        try {
            String result = get();
            outputArea.append("\n" + result);
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeJob() {
        this.execute();
    }

    private Process buildProcess() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command.getCommand());
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
        return pb.start();
    }
}
