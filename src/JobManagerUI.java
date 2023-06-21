// Swing UI JFrame for JobManager + SleepJob demo
import javax.swing.*;
import java.awt.*;

public class JobManagerUI extends JFrame {
    private final JobManager jobManager = new JobManager();

    final JTextArea outputArea = new JTextArea(10, 50);

    public void addSleepJob(String job, int iters) {
        if (job == null) {
            throw new IllegalArgumentException("job must be non-null");
        }
        if (iters < 1) {
            throw new IllegalArgumentException("iters must be >= 1");
        }
        jobManager.addSleepJob(outputArea, job, iters);
    }

    public void addDownloadJob(String job, String url) {
        if (job == null) {
            throw new IllegalArgumentException("job must be non-null");
        }
        if (url == null) {
            throw new IllegalArgumentException("url must be non-null");
        }
        jobManager.addDownloadJob(outputArea, job, url);
    }

    public void executeJobs() {
        jobManager.executeJobs();
    }

    private void setBtnDefaults(JButton button) {
        button.setPreferredSize(new Dimension(100, 30));
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEtchedBorder());
    }

    public void addDownloadJobButton(String job, String url) {
        if (job == null) {
            throw new IllegalArgumentException("job must be non-null");
        }
        if (url == null) {
            throw new IllegalArgumentException("url must be non-null");
        }

        JButton button = new JButton(job + " " + url);
        setBtnDefaults(button);
        button.addActionListener(e -> addDownloadJob(job, url));
        add(button);
    }

    public void addSleepJobButton(String job, int iters) {
        if (job == null) {
            throw new IllegalArgumentException("job must be non-null");
        }
        if (iters < 1) {
            throw new IllegalArgumentException("iters must be >= 1");
        }

        JButton button = new JButton(job + " " + iters);
        setBtnDefaults(button);
        button.addActionListener(e -> addSleepJob(job, iters));
        add(button);
    }

    public void addJobButtons() {
        addSleepJobButton("sleep", 10);
        addDownloadJobButton("download", "https://install.copperhead.co/releases/files/panther-factory-2023.05.29.01.zip");
    }

    public void addExecuteButton() {
        JButton button = new JButton("Execute");
        setBtnDefaults(button);
        button.addActionListener(e -> executeJobs());
        add(button);
    }

    public void addOutputTextArea() {
        outputArea.setEditable(false);
        outputArea.setPreferredSize(new Dimension(480, 200));
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.WHITE);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        outputArea.setBorder(BorderFactory.createEtchedBorder());
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setAutoscrolls(true);
        add(new JScrollPane(outputArea));
    }

    public void addComponents() {
        addOutputTextArea();
        addJobButtons();
        addExecuteButton();
    }

    public JobManagerUI() {
        super("Job Manager UI");
        ImageIcon logo = new ImageIcon("res/tesseract-logo-houndstoothed-1024x1024-alpha.png");
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(640, 480));
        this.setSize(new Dimension(640, 480));
        this.setIconImage(logo.getImage());
        this.getContentPane().setBackground(Color.DARK_GRAY);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponents();
    }
}
