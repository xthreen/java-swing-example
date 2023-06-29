import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;

public class JobManager {
    private final Queue<SwingWorker<String, String>> workerQueue = new LinkedList<>();

    protected boolean addCommandJob(JTextArea outputArea, String[] command) {
        try {
            AllowedCommand allowedCommand = AllowedCommand.tryCommand(command);
            workerQueue.add(new CommandJob(outputArea, allowedCommand));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected boolean addAdbStartJob(JTextArea outputArea) {
        return addCommandJob(outputArea, AllowedCommand.ADB_START_SERVER.getCommand());
    }

    protected void addSleepJob(JTextArea outputArea, int iters) {
        if (iters < 1) {
            throw new IllegalArgumentException("iters must be >= 1");
        }
        workerQueue.add(new SleepJob(outputArea, iters));
    }

    protected void addDownloadJob(JTextArea outputArea, String url) {
        if (url == null) {
            throw new IllegalArgumentException("url must be non-null");
        }
        workerQueue.add(new FileDownloadJob(outputArea, url));
    }

    protected void executeJobs() {
        while (!workerQueue.isEmpty()) {
            SwingWorker<String, String> worker = workerQueue.remove();
            worker.execute();
        }
    }

    protected void shutdown(JTextArea outputArea) {
        new CommandJob(outputArea, AllowedCommand.ADB_KILL_SERVER).execute();
    }
}
