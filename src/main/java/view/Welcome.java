package view;

import templates.IFFDButton;
import templates.SingleButtonLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class Welcome extends JPanel{

    public final String name;
    private Container cards;
    JLabel title;
    IFFDButton start;
    public Welcome(String name, Container cards){
        this.cards = cards;

        this.setLayout(new BorderLayout());
        this.name = name;
        title = new JLabel("Welcome to IFFD");
        title.setFont(new Font("Calibri",Font.BOLD,35));
        title.setBorder(new EmptyBorder(60,0,0,0));
        title.setForeground(Color.black);
        URL logo = getClass().getClassLoader().getResource("logo.png");
        ImageIcon welcomeImage = new ImageIcon(logo);

        title.setIcon(welcomeImage);

        this.setBackground(Color.white);
        this.add(title,BorderLayout.CENTER);

        start = new IFFDButton("Start", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout c = (CardLayout) cards.getLayout();
                c.show(cards,"SelectIngredients");
                System.out.println("here");
            }
        });

        JPanel buttonPanel = new SingleButtonLayout(start);
        this.add(buttonPanel,BorderLayout.SOUTH);


    }


}
