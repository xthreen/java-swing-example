import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.net.URL;

public class InputArea extends JTextField {
    private final JobManager jobManager;
    private final JTextArea outputArea;
    private final JComponent parentComponent;

    public InputArea(JComponent parentComponent, JobManager jobManager, JTextArea outputArea) {
        super();
        this.parentComponent = parentComponent;
        if (jobManager == null) {
            throw new IllegalArgumentException("jobManager must be non-null");
        }
        this.jobManager = jobManager;
        if (outputArea == null) {
            throw new IllegalArgumentException("outputArea must be non-null");
        }
        this.outputArea = outputArea;
        Utils.setCommonBodyProperties(this);
        this.setColumns(54);
        this.setPreferredSize(new Dimension(600, 48));
        this.setBorder(Utils.newTitledBorder("Input"));
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String[] text = getText().trim().toLowerCase().split(" ");
                    switch (text[0]) {
                        case "download" -> executeDownload();
                        case "sleep" -> executeSleep();
                        case "adb" -> {
                            try {
                                AllowedCommand command = AllowedCommand.tryCommand(text);
                                executeCommand(command);
                                JobManagerUI.setAdbDaemonStatus(true);
                            }
                            catch (IllegalArgumentException ex) {
                                outputArea.append("Invalid command: " + ex.getMessage() + "\n");
                            }
                        }
                        case "fastboot", "which", "where" -> {
                            try {
                                AllowedCommand command = AllowedCommand.tryCommand(text);
                                executeCommand(command);
                            }
                            catch (IllegalArgumentException ex) {
                                outputArea.append("Invalid command: " + ex.getMessage() + "\n");
                            }
                        }
                        default -> outputArea.append("Invalid command: " + getText() + "\n");
                    }
                }
            }
        });
    }

    public void executeCommand(AllowedCommand command) {
        this.setText("");
        this.jobManager.addCommandJob(outputArea, command);
        this.jobManager.executeJobs();
    }

    public void executeSleep() {
        String iters = this.getText().matches("sleep\\s+(\\d+)") ? this.getText().split(" ")[1] : this.getText();
        this.setText("");
        this.jobManager.addSleepJob(outputArea, Integer.parseInt(iters));
        this.jobManager.executeJobs();
    }

    public void executeDownload() {
        URL url = Utils.createUrl(this.getText().split(" ")[1]);
        this.setText("");
        this.jobManager.addDownloadJob(Utils.newProgressBar(parentComponent), outputArea, url);
        this.jobManager.executeJobs();
    }
}
