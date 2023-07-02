/*
WorkerJob Interface - This interface is a wrapper for SwingWorker, providing a generic type for
the JobManager Queue. Implement executeJob() and call SwingWorker.execute().
* */
public interface WorkerJob {
    void executeJob();
}
