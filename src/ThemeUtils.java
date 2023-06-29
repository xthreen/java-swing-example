import javax.swing.*;
import java.awt.*;

public class ThemeUtils {
    public static void setCommonThemeElements(JComponent component) {
        component.setFont(new Font("Arial", Font.PLAIN, 12));
        component.setForeground(Color.WHITE);
        component.setBackground(Color.DARK_GRAY);
        component.setBorder(BorderFactory.createEtchedBorder());
    }
}
