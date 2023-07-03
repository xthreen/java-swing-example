import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class JobManagerUI extends JPanel {
    private final JobManager jobManager;
    private final JTextArea outputArea;
    private final InputArea inputArea;
    private static boolean adbDaemon = false;
    public boolean adbDaemonQueued = false;

    public JobManagerUI(JTextArea outputArea, JobManager jobManager) {
        super(new FlowLayout());
        this.setPreferredSize(new Dimension(600, 460));
        Utils.setCommonBodyProperties(this);
        this.outputArea = outputArea;
        this.jobManager = jobManager;
        this.inputArea = new InputArea(this, jobManager, outputArea);

        this.outputArea.setText(
                "Welcome to the Swing Concurrency Demo UI!\nPlease select a job to execute, or type a command.\nResults will appear here...\n"
        );

        addComponents();
    }

    public void requestFocusOnInputArea() {
        inputArea.requestFocusInWindow();
    }

    public static void setAdbDaemonStatus(boolean status) {
        adbDaemon = status;
    }

    public boolean getAdbDaemonStatus() {
        return adbDaemon;
    }

    private void executeJobs() {
        jobManager.executeJobs();
    }

    private void addDownloadJobButton(String url) {
        if (url == null) {
            throw new IllegalArgumentException("url must be non-null");
        }
        URL urlObj = Utils.createUrl(url);
        JButton button = new JButton("GET: " + Utils.capitalize(Utils.extractDeviceName(url)));
        Utils.setCommonBodyProperties(button);
        button.setPreferredSize(new Dimension(200, 30));
        button.addActionListener(e -> {
            outputArea.append("\n" + "Queued download job..." + "\n" + url + "\n");

            jobManager.addDownloadJob(Utils.newProgressBar(this), outputArea, urlObj);
        });
        this.add(button);
    }

    private void addSleepJobButton(int iters) {
        if (iters < 1) {
            throw new IllegalArgumentException("iters must be >= 1");
        }

        JButton button = new JButton("Sleep " + iters);
        Utils.setCommonBodyProperties(button);
        button.setPreferredSize(new Dimension(100, 30));
        button.addActionListener(e -> {
            outputArea.append("Queued sleep for " + iters + " seconds..." + "\n");
            jobManager.addSleepJob(outputArea,  iters);
        });
        this.add(button);
    }

    private void addAdbStartJobButton() {
        JButton button = new JButton("adb start-server");
        Utils.setCommonBodyProperties(button);
        button.setPreferredSize(new Dimension(200, 30));
        button.addActionListener(e -> this.adbDaemonQueued = jobManager.addAdbStartJob(outputArea));
        this.add(button);
    }

    private void addAdbDevicesJobButton() {
        JButton button = new JButton("adb devices");
        Utils.setCommonBodyProperties(button);
        button.setPreferredSize(new Dimension(200, 30));
        button.addActionListener(e -> this.adbDaemonQueued = jobManager.addCommandJob(outputArea, AllowedCommand.ADB_DEVICES.getCommand()));
        this.add(button);
    }

    private void addExecuteButton() {
        JButton button = new JButton("Execute");
        Utils.setCommonBodyProperties(button);
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

    private void addOutputComponents() {
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Utils.LIGHT_GREEN);
        outputArea.setFont(Utils.BODY_FONT);
        outputArea.setFocusable(false);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setAutoscrolls(true);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setAutoscrolls(true);
        scrollPane.setBorder(Utils.newTitledBorder("Output"));
        scrollPane.setBackground(Color.BLACK);

        this.add(scrollPane);
    }

    private void addComponents() {
        addOutputComponents();
        addJobButtons();
        this.add(inputArea);
    }
}
