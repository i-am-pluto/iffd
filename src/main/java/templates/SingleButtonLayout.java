package templates;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SingleButtonLayout extends JPanel {

    private IFFDButton button;

    public SingleButtonLayout(IFFDButton button) {
        this.button = button;
        this.setLayout(new GridLayout());
        this.add(this.button);
        this.setBackground(Color.decode("#232629"));
        this.setBorder(new EmptyBorder(5, 15, 5, 15));
    }
}
