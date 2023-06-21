import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JobManagerUI jManager = new JobManagerUI();
            jManager.pack();
            jManager.setVisible(true);
        });
    }
}