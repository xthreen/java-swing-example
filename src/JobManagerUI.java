import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class JobManagerUI extends JPanel {
    private final JobManager jobManager;
    private final JTextArea outputArea;
    private boolean adbDaemonQueued = false;
    private boolean adbDaemon = false;
    Font bodyFont = new Font("Fira Sans", Font.PLAIN, 12);
    Font headerFont = new Font("Big Shoulders Text", Font.BOLD, 14);

    public JobManagerUI(JTextArea outputArea, JobManager jobManager) {
        super(new FlowLayout());
        ThemeUtils.setCommonThemeElements(this);

        this.outputArea = outputArea;
        this.jobManager = jobManager;

        outputArea.setText("Welcome to the Job Manager UI!\nPlease select a job to execute.\nResults will appear here...\n");
        addComponents();

    }

    public boolean isAdbDaemonRunning() {
        return adbDaemon;
    }

    private void executeJobs() {
        jobManager.executeJobs();
    }

    private void addDownloadJobButton(String url) {
        if (url == null) {
            throw new IllegalArgumentException("url must be non-null");
        }
        JButton button = new JButton("GET: " + url.substring(url.lastIndexOf('/')));
        ThemeUtils.setCommonThemeElements(button);
        button.setPreferredSize(new Dimension(200, 30));
        button.addActionListener(e -> {
            outputArea.append("\n" + "Queued download job..." + "\n" + url + "\n");

            jobManager.addDownloadJob(outputArea, url);
        });
        this.add(button);
    }

    private void addSleepJobButton(int iters) {
        if (iters < 1) {
            throw new IllegalArgumentException("iters must be >= 1");
        }

        JButton button = new JButton("sleep " + iters);
        ThemeUtils.setCommonThemeElements(button);
        button.setPreferredSize(new Dimension(100, 30));
        button.addActionListener(e -> {
            outputArea.append("Queued sleep for " + iters + " seconds..." + "\n");
            jobManager.addSleepJob(outputArea,  iters);
        });
        this.add(button);
    }

    private void addAdbStartJobButton() {
        JButton button = new JButton("adb start-server");
        ThemeUtils.setCommonThemeElements(button);
        button.setPreferredSize(new Dimension(200, 30));
        button.addActionListener(e -> this.adbDaemonQueued = jobManager.addAdbStartJob(outputArea));
        this.add(button);
    }

    private void addAdbDevicesJobButton() {
        JButton button = new JButton("adb devices");
        ThemeUtils.setCommonThemeElements(button);
        button.setPreferredSize(new Dimension(200, 30));
        button.addActionListener(e -> this.adbDaemonQueued = jobManager.addCommandJob(outputArea, AllowedCommand.ADB_DEVICES.getCommand()));
        this.add(button);
    }

    private void addExecuteButton() {
        JButton button = new JButton("Execute");
        ThemeUtils.setCommonThemeElements(button);
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
        addAdbDevicesJobButton();
        addExecuteButton();
    }

    private void addOutputTextArea() {
        outputArea.setBackground(Color.DARK_GRAY);
        outputArea.setForeground(Color.WHITE);
        outputArea.setFont(bodyFont);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        TitledBorder border = BorderFactory.createTitledBorder("Output");
        border.setBorder(BorderFactory.createEtchedBorder());
        border.setTitleColor(Color.LIGHT_GRAY);
        border.setTitleFont(headerFont);
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
}
