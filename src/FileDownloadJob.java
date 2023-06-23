import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class FileDownloadJob extends SwingWorker<String, String> implements WorkerJob {
    private final JTextArea outputArea;
    private final URL fileUrl;

    public FileDownloadJob(JTextArea outputArea, String fileUrl) {
        if (outputArea == null) {
            throw new IllegalArgumentException("outputArea must be non-null");
        }
        this.outputArea = outputArea;
        if (fileUrl == null) {
            throw new IllegalArgumentException("fileUrl must be non-null");
        }
        this.fileUrl = createUrl(fileUrl);
    }

    private URL createUrl(String fileUrl) {
        try {
            return URI.create(fileUrl).toURL();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL: " + fileUrl, e);
        }
    }

    @Override
    protected String doInBackground() {
        try {
            URLConnection connection = fileUrl.openConnection();
            connection.connect();
            long contentLength = connection.getContentLengthLong();
            String fileName = fileUrl.getFile();
            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            publish("Downloading " + fileName + " (" + contentLength / 1024 / 1024 + " MB)...");

            try (InputStream in = connection.getInputStream();
                FileOutputStream out = new FileOutputStream(fileName)) {
                    Instant startTime = Instant.now();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    int totalBytesRead = 0;
                    Instant previousTime = startTime;
                    long previousBytesRead = 0;

                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;
                        Instant currentTime = Instant.now();
                        long timeElapsed = Duration.between(currentTime, previousTime).abs().toMillis();
                        if (timeElapsed > 1000) {
                            int percent = (int) (((double) totalBytesRead / (double) contentLength) * 100);
                            long bytesSinceLast = totalBytesRead - previousBytesRead;
                            double speed = (bytesSinceLast * 1000.0) / timeElapsed / 1024 / 1024;
                            String speedStr = String.format("%.2f", speed);
                            publish("Downloaded " + totalBytesRead + " bytes of " + contentLength + " (" + percent + "%), speed: " + speedStr + " MB/s");
                            previousTime = currentTime;
                            previousBytesRead = totalBytesRead;
                        }
                    }
                    in.close();
                    out.close();
                    return "Completed";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Failed";
                }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    @Override
    public void done() {
        try {
            String result = get();
            outputArea.setText(result + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(List<String> chunks) {
        for (String chunk : chunks) {
            outputArea.setText(chunk + "\n");
        }
    }

    public void executeJob() {
        execute();
    }
}
