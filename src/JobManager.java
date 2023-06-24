import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;

public class JobManager {
    private final Queue<WorkerJob> workerQueue = new LinkedList<>();

    protected boolean addAdbStartJob(JTextArea outputArea) {
        try {
            workerQueue.add(new CommandJob(outputArea, new String[] { "adb", "devices", "-l" }));
        } catch (Exception e) {
            return false;
        }
        return true;
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
            WorkerJob worker = workerQueue.remove();
            worker.executeJob();
        }
    }

    protected void shutdown(JTextArea outputArea) {
        new CommandJob(outputArea, new String[] { "adb", "kill-server" }).executeJob();
    }
}
