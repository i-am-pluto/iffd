package view;

import Model.Ingredient;
import Model.IngredientsRepository;
import com.intellij.ui.components.JBScrollPane;
import templates.IFFDButton;
import templates.ThreeButtonLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class SelectIngredients extends JPanel {

    public final String name;
    private Container cards;
    private IngredientsRepository ingredientsRepository;
    private ArrayList<Ingredient> options;
    private ArrayList<JComboBox<String>> ingredientButtons;
    JLabel title;
    IFFDButton useAI, design, back;


    public SelectIngredients(String name, Container cards) throws IOException, URISyntaxException {
        this.name = name;
        this.cards = cards;
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);
        this.configureTitle();
        this.add(title, BorderLayout.NORTH);
        this.configureButtons();

//        scrollPane.setBounds(50, 30, 300, 50);



        JPanel buttonPanel = new ThreeButtonLayout(back, useAI, design);
        this.add(buttonPanel, BorderLayout.SOUTH);

        ingredientsRepository = new IngredientsRepository();
        this.options = ingredientsRepository.getIngredients();
        // set the buttons
        String[] ops = ingredientsRepository.getIngredientsNames();

        this.ingredientButtons = new ArrayList<>();
        this.configureDropDownLists(ops);

        // add the buttons
        JPanel ingredientsPanel = new JPanel();
        ingredientsPanel.setLayout(new GridLayout(10, 1, 20, 5));
        ingredientsPanel.setBackground(Color.white);
        ingredientsPanel.setBorder(new EmptyBorder(40, 120, 40, 120));
        for (JComboBox b : this.ingredientButtons) {
            ingredientsPanel.add(b);
        }
        this.add(ingredientsPanel, BorderLayout.CENTER);
    }

    private void configureDropDownLists(String[] ops) {
        ArrayList<String> res = new ArrayList<String>(10);
        for (int i = 0; i < 10; i++) {
            JComboBox<String> temp = new JComboBox<>(ops);
            temp.setFont(new Font("Calibri", Font.BOLD, 15));
            temp.setBackground(Color.white);
            temp.setForeground(Color.black);
            ingredientButtons.add(temp);
        }
    }

    private void configureButtons() {
        design = new IFFDButton("Design", new ActionListener() {
            class SortIng implements Comparator<Ingredient> {

                // Method
                // Sorting in ascending order of roll number
                @Override
                public int compare(Ingredient a, Ingredient b)
                {
                    String str1 = a.getIngredient();
                    String str2 = b.getIngredient();
                    int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                    if (res == 0) {
                        res = str1.compareTo(str2);
                    }
                    return res;
                }
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Ingredient>ings = saveData();
                ings.sort(new SortIng());
                JPanel designCard = null;
                try {
                    designCard = new Design(cards,ings, null);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }

                JScrollPane scrollPane = new JScrollPane( designCard );

                cards.add(scrollPane,"design");
                CardLayout c = (CardLayout) cards.getLayout();
                c.show(cards,"design");
            }


        });
        useAI = new IFFDButton("Use AI", new ActionListener() {
            class SortIng implements Comparator<Ingredient> {

                    // Method
                    // Sorting in ascending order of roll number
                    @Override
                    public int compare(Ingredient a, Ingredient b)
                    {
                        String str1 = a.getIngredient();
                        String str2 = b.getIngredient();
                        int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                        if (res == 0) {
                            res = str1.compareTo(str2);
                        }
                        return res;
                    }
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    ArrayList<Ingredient>ings = saveData();
                    ings.sort(new SortIng());
                    JPanel useAiCard = null;
                    useAiCard= new Use_ai(cards,ings);

                    cards.add(useAiCard,"useAi");
                    CardLayout c = (CardLayout) cards.getLayout();
                    c.show(cards,"useAi");
            }
        });

        back = new IFFDButton("Back", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout c = (CardLayout) cards.getLayout();
                c.show(cards,"welcome");
            }
        });
    }
    private ArrayList<Ingredient> saveData() {
        ArrayList<Ingredient>data = new ArrayList<>();
        this.ingredientButtons.forEach(ingredientButton -> {
            if(ingredientButton.getSelectedIndex()!=0) {
                data.add(this.options.get(ingredientButton.getSelectedIndex() - 1));
            }
        });
        Set<Ingredient> s = new HashSet<>(data);
        data.clear();
        data.addAll(s);
        return data;
    }
    private void configureTitle() {
        title = new JLabel("Select Ingredients", SwingConstants.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 35));
        title.setBorder(new EmptyBorder(20, 0, 0, 0));
        title.setForeground(Color.black);

    }
};
