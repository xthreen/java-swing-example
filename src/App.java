import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());
    private final JobManager jobManager = new JobManager();
    private final JTextArea outputArea = new JTextArea(12, 54);
    private final JFrame frame = new JFrame();

    App() {
        frame.setTitle(Utils.capitalize(Utils.HOST_OS) + " Swing Concurrency Demo");
        frame.setPreferredSize(new Dimension(720, 540));
        frame.setResizable(false);
        ImageIcon logo = new ImageIcon("res/tesseract.png");
        frame.setIconImage(logo.getImage());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JobManagerUI jobManagerUI = new JobManagerUI(outputArea, jobManager);
        JScrollPane mainScrollPane = new JScrollPane(jobManagerUI);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(mainScrollPane);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (jobManagerUI.getAdbDaemonStatus()) {
                    try {
                        jobManager.shutdown(outputArea);
                    } catch (Exception ex) {
                        logger.log(Utils.LOG_LEVEL, "Exception while shutting down job manager", ex);
                    }
                    System.exit(0);
                } else {
                    System.exit(0);
                }
            }
        });
        SwingUtilities.updateComponentTreeUI(frame);
        frame.pack();
        jobManagerUI.requestFocusOnInputArea();
        frame.setVisible(true);
    }

    private void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            Utils.setLogLevel(Level.parse(args[0]));
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            logger.log(Utils.LOG_LEVEL, "Unable to set system look and feel", e);
            System.exit(1);
        }
        catch (ClassNotFoundException e) {
            logger.log(Utils.LOG_LEVEL, "Unable to find class for system look and feel", e);
            System.exit(1);
        }
        catch (InstantiationException e) {
            logger.log(Utils.LOG_LEVEL, "Unable to instantiate system look and feel", e);
            System.exit(1);
        }
        catch (IllegalAccessException e) {
            logger.log(Utils.LOG_LEVEL, "Unable to access system look and feel", e);
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> new App().show());
    }
}