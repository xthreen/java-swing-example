public class DownloadProgress {
    private String fileName = "";
    private long totalBytesRead = 0;
    private long contentLength = 0;
    private int percent = 0;
    private double speed = 0.0;
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
