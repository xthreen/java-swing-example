import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App {
    private final JobManager jobManager = new JobManager();
    private final JTextArea outputArea = new JTextArea(12, 54);
    private final JFrame frame = new JFrame();

    App(String hostOs) {
        frame.setTitle(Utils.capitalize(hostOs) + " Swing Concurrency Demo");
        frame.setPreferredSize(new Dimension(640, 400));
        frame.setResizable(false);
        ImageIcon logo = new ImageIcon("res/x3n-tesseract-1024x1024-alpha.png");
        frame.setIconImage(logo.getImage());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JobManagerUI jobManagerUI = new JobManagerUI(outputArea, jobManager);
        frame.add(jobManagerUI);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (jobManagerUI.getAdbDaemonStatus()) {
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
        jobManagerUI.requestFocusOnInputArea();
        frame.setVisible(true);
    }

    private void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            System.out.println("Unable to set system look and feel");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Unable to find class for system look and feel");
        }
        catch (InstantiationException e) {
            System.out.println("Unable to instantiate system look and feel");
        }
        catch (IllegalAccessException e) {
            System.out.println("Unable to access system look and feel");
        }
        String hostOs = System.getProperty("os.name").toLowerCase();
        SwingUtilities.invokeLater(() -> new App(hostOs).show());
    }
}