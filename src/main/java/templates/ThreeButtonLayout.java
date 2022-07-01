package templates;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ThreeButtonLayout extends JPanel {

    private IFFDButton button1, button2,button3;

    public ThreeButtonLayout(IFFDButton button1, IFFDButton button2, IFFDButton button3) {
        this.button1 = button1;
        this.button2 = button2;
        this.button3 = button3;

        this.setLayout(new GridLayout(1,3));
        this.add(this.button1);
        this.add(this.button2);
        this.add(this.button3);
        this.setBackground(Color.decode("#232629"));
        this.setBorder(new EmptyBorder(5, 15, 5, 15));

    }
}
