import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class JobManagerUI extends JFrame {
    private final JobManager jobManager = new JobManager();
    private boolean adbDaemonQueued = false;
    private boolean adbDaemon = false;
    private final JTextArea outputArea = new JTextArea(12, 54);

    public JobManagerUI() {
        super("Job Manager UI");
        outputArea.setText("Welcome to the Job Manager UI!\nPlease select a job to execute.\nResults will appear here...");
        ImageIcon logo = new ImageIcon("res/tesseract-logo-houndstoothed-1024x1024-alpha.png");
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(640, 320));
        this.setResizable(false);
        this.setIconImage(logo.getImage());
        this.getContentPane().setBackground(Color.DARK_GRAY);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (adbDaemon) {
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
        addComponents();
        this.pack();
    }

    private void executeJobs() {
        jobManager.executeJobs();
    }

    private void addDownloadJobButton(String url) {
        if (url == null) {
            throw new IllegalArgumentException("url must be non-null");
        }
        JButton button = new JButton("GET: " + url.substring(url.lastIndexOf('/')));
        setCommonThemeElements(button);
        button.setPreferredSize(new Dimension(200, 30));
        button.addActionListener(e -> {
            String lastOutput = Objects.requireNonNullElse(outputArea.getText(), "");
            if (!lastOutput.isEmpty()) {
                outputArea.setText(lastOutput + "\n" + "Queued download job..." + "\n" + url);
            } else {
                outputArea.setText("Queued download job..." + "\n" + url);
            }
            jobManager.addDownloadJob(outputArea, url);
        });
        this.add(button);
    }

    private void addSleepJobButton(int iters) {
        if (iters < 1) {
            throw new IllegalArgumentException("iters must be >= 1");
        }

        JButton button = new JButton("sleep " + iters);
        setCommonThemeElements(button);
        button.setPreferredSize(new Dimension(100, 30));
        button.addActionListener(e -> {
            String lastOutput = Objects.requireNonNullElse(outputArea.getText(), "");
            if (!lastOutput.isEmpty()) {
                outputArea.setText(lastOutput + "\n" + "Queued sleep job...");
            } else {
                outputArea.setText("Queued sleep job...");
            }
            jobManager.addSleepJob(outputArea,  iters);
        });
        this.add(button);
    }

    private void addAdbStartJobButton() {
        JButton button = new JButton("adb devices");
        setCommonThemeElements(button);
        button.setPreferredSize(new Dimension(200, 30));
        button.addActionListener(e -> this.adbDaemonQueued = jobManager.addAdbStartJob(outputArea));
        this.add(button);
    }

    private void addExecuteButton() {
        JButton button = new JButton("Execute");
        setCommonThemeElements(button);
        button.setPreferredSize(new Dimension(100, 30));
        button.addActionListener(e -> {
            if (adbDaemonQueued) {
                adbDaemon = true;
                adbDaemonQueued = false;
                executeJobs();
            } else {
                executeJobs();
            }
        });
        this.add(button);
    }

    private void addJobButtons() {
        addSleepJobButton(10);
        addSleepJobButton(20);
        addDownloadJobButton("https://dl.google.com/dl/android/aosp/panther-tq3a.230605.012-factory-e1c06028.zip");
        addDownloadJobButton("https://dl.google.com/dl/android/aosp/bluejay-tq3a.230605.010-factory-1d224b94.zip");
        addAdbStartJobButton();
        addExecuteButton();
    }

    private void addOutputTextArea() {
        outputArea.setBackground(Color.DARK_GRAY);
        outputArea.setForeground(Color.WHITE);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 12));
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        TitledBorder border = BorderFactory.createTitledBorder("Output");
        border.setBorder(BorderFactory.createEtchedBorder());
        border.setTitleColor(Color.LIGHT_GRAY);
        border.setTitleFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(border);
        scrollPane.setBackground(Color.DARK_GRAY);
        scrollPane.setForeground(Color.WHITE);
        this.add(scrollPane);
    }

    private void addComponents() {
        addOutputTextArea();
        addJobButtons();
    }

    private void setCommonThemeElements(JComponent component) {
        component.setFont(new Font("Arial", Font.PLAIN, 12));
        component.setForeground(Color.WHITE);
        component.setBackground(Color.DARK_GRAY);
        component.setBorder(BorderFactory.createEtchedBorder());
    }
}
