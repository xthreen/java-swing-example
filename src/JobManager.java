import javax.swing.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

public class JobManager {
    private final Queue<WorkerJob> workerQueue = new LinkedList<>();

    protected boolean addCommandJob(JTextArea outputArea, String[] command) {
        try {
            AllowedCommand allowedCommand = AllowedCommand.tryCommand(command);
            workerQueue.add(new CommandJob(outputArea, allowedCommand));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    protected boolean addCommandJob(JTextArea outputArea, AllowedCommand command) {
        try {
            workerQueue.add(new CommandJob(outputArea, command));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected boolean addAdbStartJob(JTextArea outputArea) {
        return addCommandJob(outputArea, AllowedCommand.ADB_START_SERVER);
    }

    protected void addSleepJob(JTextArea outputArea, int iters) {
        if (iters < 1) {
            throw new IllegalArgumentException("iters must be >= 1");
        }
        workerQueue.add(new SleepJob(outputArea, iters));
    }

    protected void addDownloadJob(JProgressBar progressBar, JTextArea outputArea, URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url must be non-null");
        }
        workerQueue.add(new FileDownloadJob(progressBar, outputArea, url));
    }

    protected void executeJobs() {
        while (!workerQueue.isEmpty()) {
            WorkerJob worker = workerQueue.remove();
            worker.executeJob();
        }
    }

    protected void shutdown(JTextArea outputArea) {
        new CommandJob(outputArea, AllowedCommand.ADB_KILL_SERVER).execute();
    }
}
