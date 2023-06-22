// Swing UI JFrame for JobManager + SleepJob demo
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JobManagerUI extends JFrame {
    private final JobManager jobManager = new JobManager();

    final JTextArea outputArea = new JTextArea(12, 48);

    public void addSleepJob(String job, int iters) {
        if (job == null) {
            throw new IllegalArgumentException("job must be non-null");
        }
        if (iters < 1) {
            throw new IllegalArgumentException("iters must be >= 1");
        }
        outputArea.setText("Queued sleep job...");
        jobManager.addSleepJob(outputArea, job, iters);
    }

    public void addDownloadJob(String job, String url) {
        if (job == null) {
            throw new IllegalArgumentException("job must be non-null");
        }
        if (url == null) {
            throw new IllegalArgumentException("url must be non-null");
        }
        outputArea.setText("Queued download job...");
        outputArea.append("\n" + url);
        jobManager.addDownloadJob(outputArea, job, url);
    }

    public void executeJobs() {
        jobManager.executeJobs();
    }

    public void addDownloadJobButton(String job, String url) {
        if (job == null) {
            throw new IllegalArgumentException("job must be non-null");
        }
        if (url == null) {
            throw new IllegalArgumentException("url must be non-null");
        }
        JButton button = new JButton("GET: " + url.substring(url.lastIndexOf('/')));
        setCommonThemeElements(button);
        button.setPreferredSize(new Dimension(200, 30));
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
        setCommonThemeElements(button);
        button.setPreferredSize(new Dimension(100, 30));
        button.addActionListener(e -> addSleepJob(job, iters));
        add(button);
    }

    public void addAdbStartJobButton() {
        JButton button = new JButton("adb devices");
        setCommonThemeElements(button);
        button.setPreferredSize(new Dimension(200, 30));
        button.addActionListener(e -> jobManager.addAdbStartJob(outputArea));
        add(button);
    }

    public void addJobButtons() {
        addSleepJobButton("sleep", 10);
        addDownloadJobButton("download", "https://dl.google.com/dl/android/aosp/panther-tq3a.230605.012-factory-e1c06028.zip");
        addAdbStartJobButton();
    }

    public void addExecuteButton() {
        JButton button = new JButton("Execute");
        setCommonThemeElements(button);
        button.setPreferredSize(new Dimension(100, 30));
        button.addActionListener(e -> executeJobs());
        add(button);
    }

    public void addOutputTextArea() {
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.WHITE);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 12));
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setAutoscrolls(true);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane);
    }

    public void addComponents() {
        addOutputTextArea();
        addJobButtons();
        addExecuteButton();
    }

    public void setCommonThemeElements(JComponent component) {
        component.setFont(new Font("Arial", Font.PLAIN, 12));
        component.setForeground(Color.WHITE);
        component.setBackground(Color.DARK_GRAY);
        component.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    }

    public JobManagerUI() {
        super("Job Manager UI");
        ImageIcon logo = new ImageIcon("res/tesseract-logo-houndstoothed-1024x1024-alpha.png");
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(640, 260));
        this.setSize(new Dimension(640, 260));
        this.setIconImage(logo.getImage());
        this.getContentPane().setBackground(Color.DARK_GRAY);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    jobManager.shutdown(outputArea);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
        addComponents();
        this.pack();
    }
}
