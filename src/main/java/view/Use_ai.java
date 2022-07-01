package view;

import Model.Ingredient;
import templates.DoubleButtonLayout;
import templates.IFFDButton;
import templates.ThreeButtonLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Use_ai extends JPanel {

    private Container cards;
    private ArrayList<Ingredient> ingredients;

    private JLabel title;
    private IFFDButton finalize, back;

    public Use_ai(Container cards, ArrayList<Ingredient> ingredients) {
        this.cards = cards;
        this.ingredients = ingredients;

        this.configureTitle();

        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);
        this.configureTitle();
        this.add(title, BorderLayout.NORTH);
        this.configureButtons();

        JPanel buttonPanel = new DoubleButtonLayout(back, finalize);
        this.add(buttonPanel, BorderLayout.SOUTH);

        JLabel input = new JLabel("Required Protein Percentage % - ");
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0.00, 100.00, 1);
        JSpinner i = new JSpinner(spinnerModel);
        JPanel inp = new JPanel();
        inp.setLayout(new GridLayout(1,2));
        inp.add(input);
        inp.add(i);

    }

    private void configureButtons() {

        finalize = new IFFDButton("Finalize", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        back = new IFFDButton("Back", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout c = (CardLayout) cards.getLayout();
                c.show(cards,"Select Ingredients");
            }
        });

    }

    private void configureTitle() {
        title = new JLabel("Use AI", SwingConstants.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 35));
        title.setBorder(new EmptyBorder(20, 0, 0, 0));
        title.setForeground(Color.black);
    }
}
