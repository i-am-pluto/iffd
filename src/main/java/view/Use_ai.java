package view;

import Model.Ingredient;
import templates.DoubleButtonLayout;
import templates.IFFDButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import static java.lang.Math.abs;

public class Use_ai extends JPanel {
    private Container cards;
    private ArrayList<Ingredient> ingredients;
    private TreeMap<String, Double> proteinLookup;
    private HashMap<String,Ingredient> ingredientName_ingredient;
    private double[][] phermones;
    private double[] proteins;
    private JLabel title;
    private IFFDButton finalize, back;
private double requiredPercentage;
    private ImageIcon lockIcon;
    private ImageIcon unlockIcon;
    private HashMap<Ingredient,Double> ingredientPercentage;
    private double goal;

    private Ingredient[] ings;
    private double[] res;
    public Use_ai(Container cards, ArrayList<Ingredient> ingredients) {
        ings = new Ingredient[ingredients.size()];
        res = new double[ingredients.size()];
        proteins = new double[ingredients.size()];
        this.ingredientPercentage = new HashMap<>();
        phermones = new double[ingredients.size()][101];
        this.ingredientName_ingredient = new HashMap<>();
        this.cards = cards;
        this.ingredients = ingredients;
        this.proteinLookup = new TreeMap<>();
        this.configureTitle();

        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);
        this.configureTitle();
        this.add(title, BorderLayout.NORTH);
        this.configureButtons();

        JPanel buttonPanel = new DoubleButtonLayout(back, finalize);
        this.add(buttonPanel, BorderLayout.SOUTH);



        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(80, 120, 80, 120));
        JPanel ingsPanel = new JPanel();
        ingsPanel.setLayout(new GridLayout(2, 1));

        JLabel subTitle = new JLabel("Builder", SwingConstants.CENTER);
        subTitle.setFont(new Font("Arial", Font.BOLD, 20));
        subTitle.setBorder(new EmptyBorder(0, 120, 20, 120));
        subTitle.setForeground(Color.black);


        JPanel newP = new JPanel();
        newP.setLayout(new BorderLayout());
        newP.add(subTitle,BorderLayout.NORTH);
        ingsPanel.add(newP);

        JPanel eachIngredient = new JPanel();
        eachIngredient.setBounds(100, 50, 80, 20);
        eachIngredient.setLayout(new GridLayout(1, 2));
        eachIngredient.setBorder(new EmptyBorder(3, 10, 3, 10));
        JLabel ingredientName = new JLabel("Enter Required Protein Percentage : ");
        ingredientName.setFont(new Font("Calibri", Font.BOLD, 12));
        eachIngredient.add(ingredientName);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0.00, 100.00, 1);
        JSpinner inputField = new JSpinner(spinnerModel);
        inputField.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                goal = (double) ((JSpinner) e.getSource()).getValue();
            }
        });
        inputField.setFont(new Font("Calibri", Font.PLAIN, 10));
        JComponent field = (JSpinner.DefaultEditor)inputField.getEditor();
        Dimension prefSize = field.getPreferredSize();
        prefSize = new Dimension(100, 20);
        field.setPreferredSize(prefSize);

        JButton lock_unlock = new IFFDButton("", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = ((JButton) e.getSource());
                System.out.println(inputField.isEnabled()+" "+button.getIcon().equals(lockIcon));
                if (inputField.isEnabled()) {
                    button.setIcon(unlockIcon);
                    inputField.setEnabled(false);
                    JSpinner oneLeft = inputField;

                } else if (!inputField.isEnabled()) {
                    button.setIcon(lockIcon);
                    inputField.setEnabled(true);
                    JSpinner oneLeft = inputField;
                }
            }
        });

        URL lock = getClass().getClassLoader().getResource("lock.png");
        lockIcon = new ImageIcon(new ImageIcon(lock).getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
        URL unlock = getClass().getClassLoader().getResource("unlock.png");
        unlockIcon = new ImageIcon(new ImageIcon(unlock).getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
        lock_unlock.setIcon(lockIcon);


        JPanel button_and_input = new JPanel();
        button_and_input.add(inputField);
        button_and_input.add(lock_unlock);
        eachIngredient.add(button_and_input);
        ingsPanel.add(eachIngredient);

        centerPanel.add(ingsPanel, BorderLayout.NORTH);

        this.add(centerPanel, BorderLayout.CENTER);


        // store the ingredients in a hash map

        int i =0;
        for(Ingredient ingredient:ingredients){
            proteinLookup.put(ingredient.getIngredient(),ingredient.getProtein());
            ingredientName_ingredient.put(ingredient.getIngredient(),ingredient);
            ings[i] = ingredient;
            proteins[i++] = ingredient.getProtein();
        }

        // get the ingredients if they are locally present
        getPhermones();


        


    }

    private void getPhermones() {




    }


    // protein lookup, phermones
    private double evaporation;
    private int per = 2;
    boolean simulate(int currPercentage,int index,double currentProtien){
        if (index == ingredients.size() && currPercentage >= 95)
        return true;
    else if (index == ingredients.size())
            return false;

        double temp = Math.random();
        // guessing a number between 0 and 1 //
        ArrayList<Double>roulette = new ArrayList<>();
        ArrayList<Double> node = new ArrayList<>();
        ArrayList<Integer> percentage = new ArrayList<>();
        for (int i = 0; i < 100 / per; i++)
        {

            double t = phermones[index][per * (i + 1)];
            double diff = abs(goal - (currentProtien + (per * (i + 1)) * proteins[index] / 100));
            double t1 = (double) 1 / diff + Math.max(t - evaporation, 0.0);
            int t2 = per * (i + 1);
            node.add(t1);
            percentage.add(t2);
        }

        double total = 0;
        for (int k = 0; k < node.size(); k++)
        {
            total += node.get(k);
        }

        roulette.add(node.get(0) / total);
        for (int k = 1; k < node.size(); k++)
        {
            roulette.add(roulette.get(k - 1) + node.get(k) / total);
        }

        int i = LowerBound(roulette, temp);
        if (i == roulette.size())
            i--;

        if (currPercentage + percentage.get(i) > 100)
            return false;
        if (simulate(percentage.get(i) + currPercentage,index + 1 , currentProtien + (per * (i + 1)) * ((double)proteins[index]) / 100))
        {
            phermones[index][per * (i + 1)] += 0.1;
            res[index] = percentage.get(i);
            return true;
        }
        else
        {
            return false;
        }
    }
    void aco(){

        evaporation = 0;
        int count =0;
        while (count<5)
        {
            for (int i = 0; i < 1e5; i++)
            {
                simulate(0,0,0);
                evaporation += 0.005;
            }
            double ans = 0;
            for (int i = 0; i < res.length; i++)
            {
                ans += res[i] * proteins[i] / 100;
            }
            if (abs(ans - goal) <= 10)
                break;
            System.out.println(ans+" "+count+" "+"failed");

            count++;
        }

    }
    void generateResult(){


        aco();
        double ans  = 0;
        for(int i=0;i<res.length;i++){
            ingredientPercentage.put(ings[i],res[i]);
            System.out.print(res[i]+" ");
        }
        System.out.print("\nans\n");

    }

    private void configureButtons() {

        finalize = new IFFDButton("Finalize", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateResult();
                JPanel finalize = null;
                try {
                    finalize = new Finalize(cards,ingredientPercentage);
                } catch (IOException | URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
                cards.add(finalize,"finalize");
                CardLayout c = (CardLayout) cards.getLayout();
                c.show(cards,"finalize");
            }
        });

        back = new IFFDButton("Back", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout c = (CardLayout) cards.getLayout();
                c.show(cards,"SelectIngredients");
            }
        });

    }

    private void configureTitle() {
        title = new JLabel("Use AI", SwingConstants.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 35));
        title.setBorder(new EmptyBorder(20, 0, 0, 0));
        title.setForeground(Color.black);
    }




    static int LowerBound(ArrayList<Double> a, double x) { // x is the target value or key
        int l=-1,r=a.size();
        while(l+1<r) {
            int m=(l+r)>>>1;
            if(a.get(m)>=x) r=m;
            else l=m;
        }
        return r;
    }

}
