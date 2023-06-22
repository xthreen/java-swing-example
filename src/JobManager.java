import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;

public class JobManager {
    private final Queue<WorkerJob> workerQueue = new LinkedList<>();

    public void addAdbStartJob(JTextArea outputArea) {
        workerQueue.add(new AdbCommandJob(outputArea, null));
    }

    public void addSleepJob(JTextArea outputArea, String job, int iters) {
        if (job == null) {
            throw new IllegalArgumentException("job must be non-null");
        }

        if (iters < 1) {
            throw new IllegalArgumentException("iters must be >= 1");
        }

        if (job.equals("sleep")) {
            workerQueue.add(new SleepJob(outputArea, iters));
        } else {
            throw new IllegalArgumentException("Unknown job: " + job);
        }
    }

    public void addDownloadJob(JTextArea outputArea, String job, String url) {
        if (job == null) {
            throw new IllegalArgumentException("job must be non-null");
        }

        if (url == null) {
            throw new IllegalArgumentException("url must be non-null");
        }

        if (job.equals("download")) {
            workerQueue.add(new FileDownloadJob(outputArea, url));
        } else {
            throw new IllegalArgumentException("Unknown job: " + job);
        }
    }

    public void executeJobs() {
        while (!workerQueue.isEmpty()) {
            WorkerJob worker = workerQueue.remove();
            worker.executeJob();
        }
    }

    public void shutdown(JTextArea outputArea) {
        new AdbCommandJob(outputArea, "kill-server").executeJob();
    }
}
