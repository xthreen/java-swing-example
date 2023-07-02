public class DownloadProgress {
    private String fileName;
    private long totalBytesRead;
    private long contentLength;
    private int percent;
    private double speed;

    DownloadProgress() {
        this.fileName = "";
        this.totalBytesRead = 0;
        this.contentLength = 0;
        this.percent = 0;
        this.speed = 0.0;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setTotalBytesRead(long totalBytesRead) {
        this.totalBytesRead = totalBytesRead;
    }
    public long getTotalBytesRead() {
        return this.totalBytesRead;
    }
    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }
    public long getContentLength() {
        return this.contentLength;
    }
    public void setPercent(int percent) {
        this.percent = percent;
    }
    public int getPercent() {
        return this.percent;
    }
    public void setSpeed(long bytesSinceLast, long timeElapsed) {
        this.speed = (bytesSinceLast * 1000.0) / timeElapsed / 1024 / 1024;
    }
    public double getSpeed() {
        return this.speed;
    }
}
