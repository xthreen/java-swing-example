import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class FileDownloadJob extends SwingWorker<String, DownloadProgress> implements WorkerJob {
    private final JProgressBar progressBar;
    private final JTextArea outputArea;
    private final URL fileUrl;
    private final String fileName;
    private final String deviceName;

    public FileDownloadJob(JProgressBar progressBar, JTextArea outputArea, URL fileUrl) {
        if (outputArea == null) {
            throw new IllegalArgumentException("outputArea must be non-null");
        }
        this.outputArea = outputArea;
        if (fileUrl == null) {
            throw new IllegalArgumentException("fileUrl must be non-null");
        }
        this.fileUrl = fileUrl;
        if (progressBar == null) {
            throw new IllegalArgumentException("progressBar must be non-null");
        }
        this.fileName = Utils.extractFileName(this.fileUrl);
        this.deviceName = Utils.extractDeviceName(this.fileUrl);
        this.progressBar = progressBar;
    }

    @Override
    protected String doInBackground() {
        try {
            URLConnection connection = fileUrl.openConnection();
            HttpsURLConnection httpsConnection;

            if (connection instanceof HttpsURLConnection) {
                httpsConnection = (HttpsURLConnection) connection;
                httpsConnection.setHostnameVerifier((hostname, session) -> true);
            } else {
                throw new IllegalArgumentException("URL must be HTTPS");
            }

            httpsConnection.connect();
            DownloadProgress progress = new DownloadProgress();
            long contentLength = connection.getContentLengthLong();
            progress.setContentLength(contentLength);

            progress.setFileName(deviceName);
            this.publish(progress);

            try (InputStream in = connection.getInputStream();
                 FileOutputStream out = new FileOutputStream(fileName)) {
                Instant startTime = Instant.now();
                byte[] buffer = new byte[4096];
                long bytesRead;
                long totalBytesRead = 0;
                Instant previousTime = startTime;
                long previousBytesRead = 0;

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, (int) bytesRead);
                    totalBytesRead += bytesRead;
                    progress.setTotalBytesRead(totalBytesRead);
                    Instant currentTime = Instant.now();
                    long timeElapsed = Duration.between(currentTime, previousTime).abs().toMillis();
                    if (timeElapsed > 1000) {
                        int percent = (int) (((double) totalBytesRead / (double) contentLength) * 100);
                        progress.setPercent(percent);
                        long bytesSinceLast = totalBytesRead - previousBytesRead;
                        progress.setSpeed(bytesSinceLast, timeElapsed);
                        this.publish(progress);
                        previousTime = currentTime;
                        previousBytesRead = totalBytesRead;
                    }
                }
                in.close();
                out.close();
                return "Completed download of " + fileName + ".";
            } catch (IOException e) {
                e.printStackTrace();
                return "Stream operation threw IOException: " + e.getMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Initial operation threw IOException: " + e.getMessage();
        }
    }

    @Override
    public void process(List<DownloadProgress> chunks) {
        for (DownloadProgress chunk : chunks) {
            long totalMbRead = chunk.getTotalBytesRead() / 1024 / 1024;
            long totalMbLength = chunk.getContentLength() / 1024 / 1024;
            progressBar.setValue(chunk.getPercent());
            outputArea.append(String.format("Downloaded %d/%d MB (%d%%) of %s at %.1f MB/s\n",
                    totalMbRead, totalMbLength, chunk.getPercent(), chunk.getFileName(), chunk.getSpeed()));
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        }
    }

    @Override
    public void done() {
        try {
            String result = get();
            outputArea.append(result + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
            progressBar.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeJob() {
        progressBar.setValue(0);
        TitledBorder border = (TitledBorder) progressBar.getBorder();
        border.setTitle("Downloading " + deviceName + "...");
        progressBar.setBorder(border);
        progressBar.setVisible(true);
        this.execute();
    }
}
