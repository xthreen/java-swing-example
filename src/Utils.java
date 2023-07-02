import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.net.URI;
import java.net.URL;

public class Utils {
    public static final Font BODY_FONT = new Font("Fira Sans", Font.PLAIN, 12);
    public static final Font HEADER_FONT = new Font("Big Shoulders Text", Font.BOLD, 14);
    public static void setCommonBodyProperties(JComponent component) {
        component.setFont(BODY_FONT);
        component.setForeground(Color.WHITE);
        component.setBackground(Color.DARK_GRAY);
        component.setBorder(BorderFactory.createEtchedBorder());
    }
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    public static URL createUrl(String fileUrl) {
        try {
            return URI.create(fileUrl).toURL();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL: " + fileUrl, e);
        }
    }
    public static String extractFileName(URL fileUrl) {
        String fileName = fileUrl.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }
    public static String extractDeviceName(URL fileUrl) {
        String fileName = fileUrl.getFile();
        fileName = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.indexOf('-'));
        return fileName;
    }
    public static String extractDeviceName(String fileUrl) {
        fileUrl = fileUrl.substring(fileUrl.lastIndexOf('/') + 1, fileUrl.indexOf('-'));
        return fileUrl;
    }
    public static JProgressBar newProgressBar(JComponent component) {
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setVisible(false);
        progressBar.setBackground(Color.DARK_GRAY);
        progressBar.setForeground(Color.GREEN);
        progressBar.setPreferredSize(new Dimension(600, 30));
        progressBar.setFocusable(false);

        TitledBorder progressBorder = BorderFactory.createTitledBorder("Progress");
        progressBorder.setTitleColor(Color.LIGHT_GRAY);
        progressBorder.setTitleFont(Utils.HEADER_FONT);
        progressBorder.setBorder(BorderFactory.createEtchedBorder());
        progressBar.setBorder(progressBorder);
        progressBar.setValue(0);
        component.add(progressBar);

        return progressBar;
    }

    public static TitledBorder newTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title, TitledBorder.LEFT, TitledBorder.TOP);
        border.setTitleColor(Color.LIGHT_GRAY);
        border.setTitleFont(Utils.HEADER_FONT);
        return border;
    }
}
