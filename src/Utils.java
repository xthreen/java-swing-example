import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.net.URI;
import java.net.URL;

public class Utils {
    public static final String HOST_OS = System.getProperty("os.name").toLowerCase();
    public static final Font BODY_FONT = new Font("Fira Sans", Font.PLAIN, 12);
    public static final Font HEADER_FONT = new Font("Big Shoulders Text", Font.BOLD, 14);
    public static final Color BRAND_COLOR = new Color(99, 165, 38);
    public static final Color LIGHT_GREEN = new Color(179, 219, 143);

    public static void setCommonBodyProperties(JComponent component) {
        component.setFont(BODY_FONT);
        component.setForeground(Color.WHITE);
        component.setBackground(Color.BLACK);
        component.setBorder(BorderFactory.createEtchedBorder());
    }

    public static void setCommonBodyProperties(JButton button) {
        button.setFont(BODY_FONT);
        button.setForeground(Color.WHITE);
        if (Utils.HOST_OS.contains("windows")) {
            button.setBorderPainted(false);
            button.setBackground(BRAND_COLOR);
        } else {
            button.setBackground(Color.DARK_GRAY);
            button.setBorder(BorderFactory.createEtchedBorder());
        }
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

    public static JProgressBar newProgressBar(JComponent parent) {
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(580, 30));
        progressBar.setFocusable(false);
        TitledBorder progressBorder = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Progress", TitledBorder.LEFT, TitledBorder.TOP);
        if (Utils.HOST_OS.contains("windows")) {
            progressBorder.setTitleColor(Color.BLACK);
        } else {
            progressBorder.setTitleColor(Color.LIGHT_GRAY);
        }
        progressBorder.setTitleFont(HEADER_FONT);
        progressBar.setBorder(progressBorder);

        progressBar.setBackground(Color.DARK_GRAY);
        progressBar.setForeground(Color.GREEN);
        progressBar.setValue(0);
        parent.add(progressBar);

        return progressBar;
    }

    public static TitledBorder newTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Utils.LIGHT_GREEN), title, TitledBorder.LEFT, TitledBorder.TOP);
        border.setTitleColor(BRAND_COLOR);
        border.setTitleFont(HEADER_FONT);
        return border;
    }
}
