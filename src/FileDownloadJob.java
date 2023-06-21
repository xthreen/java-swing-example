import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

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

            try (InputStream in = connection.getInputStream();
                FileOutputStream out = new FileOutputStream(fileName)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    int totalBytesRead = 0;

                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;
                        int percent = (int) ((totalBytesRead / (float) contentLength) * 100);
                        if (percent % 2 == 0) {
                            publish("Downloaded " + totalBytesRead + " bytes (" + percent + "%)");
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
            this.processFinalResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(List<String> chunks) {
        this.processIntermediateResult(chunks);
    }

    public void executeJob() {
        execute();
    }

    public void processIntermediateResult(List<String> chunks) {
        for (String chunk : chunks) {
            outputArea.setText(chunk + "\n");
        }
    }

    public void processFinalResult(String result) {
        outputArea.setText(result + "\n");
    }
}
