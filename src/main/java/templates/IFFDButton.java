package templates;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class IFFDButton extends JButton {

    private String name;
    private ActionListener actionListener;

    public IFFDButton(String name, ActionListener actionListener){
        this.setText(name);
        this.setFont(new Font("Calibri",Font.BOLD,20));
        Border line = new LineBorder(Color.decode("#f48024"));
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        this.setBorder(compound);
        this.setForeground(Color.black);
        this.setBackground(Color.decode("#EFD780"));

        this.actionListener = actionListener;
        this.addActionListener(actionListener);

    }


}
