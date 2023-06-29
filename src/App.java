import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App {
    private final JobManager jobManager = new JobManager();
    private final JTextArea outputArea = new JTextArea(12, 54);
    private final JFrame frame = new JFrame("Job Manager");

    App() {
        frame.setPreferredSize(new Dimension(640, 400));
        frame.setResizable(false);
        ImageIcon logo = new ImageIcon("res/tesseract-logo-houndstoothed-1024x1024-alpha.png");
        frame.setIconImage(logo.getImage());

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JobManagerUI jobManagerUI = new JobManagerUI(outputArea, jobManager);
        frame.add(jobManagerUI);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (jobManagerUI.isAdbDaemonRunning()) {
                    try {
                        jobManager.shutdown(outputArea);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    System.exit(0);
                } else {
                    System.exit(0);
                }
            }
        });
        frame.pack();
        frame.setVisible(true);
    }

    public void show(boolean visible) {
        frame.setVisible(visible);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().show(true));
    }
}