package view;

import Model.Ingredient;
import Model.IngredientsRepository;
import templates.DoubleButtonLayout;
import templates.IFFDButton;
import templates.Nv;

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
import java.security.KeyPair;
import java.util.*;
import java.util.stream.Collectors;

public class Design extends JPanel {

    private HashMap<Ingredient,Double> cache;
    private Container cards;
    private ArrayList<Ingredient> ingredients;

    private HashMap<JSpinner, Number> inputFields;
    private HashMap<Ingredient, Double> ingredientPercentage;
    private HashMap<JSpinner, Ingredient> ingredientSpinner;
    private HashMap<JButton, JSpinner> triggeredField;
    private HashMap<JSpinner, JButton> targettedButton;

    private double protien;
    private double lipid;
    private JLabel pLabel;
    private JLabel lLabel;
    private ImageIcon lockIcon;
    private ImageIcon unlockIcon;
    private IngredientsRepository ingredientsRepository;
    JLabel title;
    JLabel subTitle;
    IFFDButton finalize, back;

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

    public Design(Container cards, ArrayList<Ingredient> ingredients, HashMap<Ingredient, Double> cache) throws IOException, URISyntaxException {
        this.triggeredField = new HashMap<>();
        this.inputFields = new HashMap<>();
        this.targettedButton = new HashMap<>();
        this.ingredientPercentage = new HashMap<>();
        this.ingredientsRepository = new IngredientsRepository();
        this.ingredientSpinner = new HashMap<>();
        this.pLabel = new JLabel();
        this.lLabel = new JLabel();
        this.cards = cards;
        this.ingredients = ingredients;
        this.cache = new HashMap<>();




        if(ingredients!=null){
            final double[] change = {0};
            ingredients.forEach(ingredient -> {
                double v = ((double)100)/ingredients.size();
                // v = 16.667
                System.out.println("v -> "+v);
                ingredientPercentage.put(ingredient, (double) ((int) v));
                System.out.println("vaue -> "+(double) ((int) v));
                // 16
                change[0] +=Math.abs(v-(double)((int)v));
            });
            System.out.println(change[0]);
            //
            change[0] = Math.round(change[0]);
            for(Ingredient ingredient: ingredientPercentage.keySet()){

                if(change[0]==0)
                    break;

                ingredientPercentage.put(ingredient,ingredientPercentage.get(ingredient)+1);
                change[0]--;
            }

        }

        if(ingredients==null && !cache.isEmpty()){
            this.ingredients = new ArrayList<>();
            for(Ingredient i:cache.keySet()){
                this.ingredients.add(i);
                this.cache.put(i,cache.get(i));
                this.ingredientPercentage.put(i,cache.get(i));
            }
            this.ingredientsRepository.sortIngs(this.ingredients);
        }

        this.configureTitle();

        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);
        this.configureTitle();
        this.add(title, BorderLayout.NORTH);
        this.configureButtons();

        JPanel buttonPanel = new DoubleButtonLayout(back, finalize);
        this.add(buttonPanel, BorderLayout.SOUTH);

        JPanel centerPanel = configureCenterPanel();
        this.add(centerPanel, BorderLayout.CENTER);



    }

    private JPanel configureCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(40, 120, 40, 120));
        JPanel ingsPanel = new JPanel();
        ingsPanel.setLayout(new GridLayout(ingredients.size()+1, 1));
        JPanel newP = new JPanel();
        newP.setLayout(new BorderLayout());
        newP.add(subTitle,BorderLayout.NORTH);
        ingsPanel.add(newP);
        this.ingredients.forEach(ingredient -> {
            JPanel eachIngredient = new JPanel();
//            eachIngredient.setBounds(100, 50, 80, 20);
            eachIngredient.setLayout(new GridLayout(1, 2));
            eachIngredient.setBorder(new EmptyBorder(3, 10, 3, 10));
            JLabel ingredientName = new JLabel(ingredient.getIngredient());
            ingredientName.setFont(new Font("Calibri", Font.BOLD, 12));
            eachIngredient.add(ingredientName);

            double value = ingredientPercentage.get(ingredient);
            if(this.cache.containsKey(ingredient)){
                value = this.cache.get(ingredient);
            }

            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(value, 0.00, 100.00, 1);
            JSpinner inputField = new JSpinner(spinnerModel);
            inputField.setFont(new Font("Calibri", Font.PLAIN, 10));
            JComponent field = (JSpinner.DefaultEditor)inputField.getEditor();
            Dimension prefSize = field.getPreferredSize();
            prefSize = new Dimension(100, 20);
            field.setPreferredSize(prefSize);
            inputField.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {



                    JSpinner input = ((JSpinner) e.getSource());
                    if (!input.isEnabled()) {
                        return;
                    }
                    double nV = Double.parseDouble(input.getValue().toString());
                    double oV = Double.parseDouble(inputFields.get(input).toString());
                    inputFields.put((JSpinner) e.getSource(), (Number) ((JSpinner) e.getSource()).getValue());
                    ingredientPercentage.put(ingredientSpinner.get(input), (Double) input.getValue());
                    double change = nV - oV;
                    int count = 0;
                    for (JSpinner i : inputFields.keySet()) {
                        if (i.isEnabled() && !i.equals(e.getSource())) {
                            count++;
                        }
                    }
                    if (count == 0) {
                        input.setEnabled(false);
                        input.setValue(Math.round(oV));
                        inputFields.put((JSpinner) e.getSource(), oV);
                        ingredientPercentage.put(ingredientSpinner.get(input), oV);
                        return;
                    } else {

                        ArrayList<JSpinner> to_change = new ArrayList<>();
                        for (JSpinner i : inputFields.keySet()) {
                            if (i.isEnabled() && !i.equals(e.getSource())) {
                                to_change.add(i);
                                i.setEnabled(false);
                            }
                        }

                        ArrayList<Nv>hm = new ArrayList<>();
                        for(JSpinner i: to_change){
                            System.out.println("err");
                            Nv newel = new Nv();
                            newel.setI(i);
                            double t = Double.parseDouble(inputFields.get(i).toString());
                            newel.setValue((int) t);
                            System.out.println("err");
                            hm.add(newel);
                        }
                        while(change>0) {
                            hm.sort((o1, o2) -> {
                                if (o1.getValue()== o2.getValue())
                                    return 0;
                                else if (o1.getValue()< o2.getValue())
                                    return 1;
                                else
                                    return -1;

                            });
                            hm.get(0).setValue(hm.get(0).getValue()-1);
                            if(hm.get(0).getValue() <0){
                                input.setEnabled(false);
                                input.setValue(Math.round(oV));
                                input.setEnabled(true);
                                inputFields.put((JSpinner) e.getSource(), oV);
                                ingredientPercentage.put(ingredientSpinner.get(input), oV);
                                for (JSpinner ing : to_change) {
                                    ing.setEnabled(true);
                                }
                                return;
                            }
                            change--;
                        }
                        while(change<0){
                            hm.sort(new Comparator<Nv>() {
                                @Override
                                public int compare(Nv o1, Nv o2) {

                                    if (o1.getValue()== o2.getValue())
                                        return 0;
                                    else if (o1.getValue()> o2.getValue())
                                        return 1;
                                    else
                                        return -1;

                                }
                            });
                            hm.get(0).setValue(hm.get(0).getValue()+1);
                            if(hm.get(0).getValue() >100){
                                input.setEnabled(false);
                                input.setValue(Math.round(oV));
                                input.setEnabled(true);
                                inputFields.put((JSpinner) e.getSource(), oV);
                                ingredientPercentage.put(ingredientSpinner.get(input), oV);
                                for (JSpinner ing : to_change) {
                                    ing.setEnabled(true);
                                }
                                return;
                            }
                            change++;
                        }

                        hm.forEach(nv -> {
                            nv.getI().setValue(nv.getValue());
                            inputFields.put(nv.getI(), nv.getValue());
                            ingredientPercentage.put(ingredientSpinner.get(nv.getI()), Double.parseDouble(String.valueOf(nv.getValue())));
                        });

                        for (JSpinner i : to_change) {
                            i.setEnabled(true);
                        }
                        setResultLabels();
                    }
                }
            });

            this.inputFields.put(inputField, ingredientPercentage.get(ingredient));
//            eachIngredient.add(inputField);
            this.ingredientSpinner.put(inputField, ingredient);
            JButton lock_unlock = new IFFDButton("", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton button = ((JButton) e.getSource());
                    System.out.println(triggeredField.get(button).isEnabled()+" "+button.getIcon().equals(lockIcon));
                    if (triggeredField.get(button).isEnabled()) {
//                        button.setText("Unlock");
                        button.setIcon(unlockIcon);
                        triggeredField.get(button).setEnabled(false);
                        int count = 0;
                        JSpinner oneLeft = triggeredField.get(e.getSource());
                        for (JSpinner i : inputFields.keySet()) {
                            if (i.isEnabled()) {
                                count++;
                                oneLeft = i;
                            }
                        }
                        if (count == 1) {
                            oneLeft.setEnabled(false);
                            targettedButton.get(oneLeft).setEnabled(false);
                        }

                    } else if (!triggeredField.get(button).isEnabled()) {
//                        button.setText("Lock");
                        button.setIcon(lockIcon);
                        triggeredField.get(button).setEnabled(true);
                        int count = 0;
                        JSpinner oneLeft = triggeredField.get(button);
                        for (JSpinner i : inputFields.keySet()) {
                            if (!targettedButton.get(i).isEnabled()) {
                                count++;
                                oneLeft = i;
                            }
                        }
                        if (count == 1) {
                            oneLeft.setEnabled(true);
                            targettedButton.get(oneLeft).setEnabled(true);
                        }
                    }
                }
            });

            URL lock = getClass().getClassLoader().getResource("lock.png");
            lockIcon = new ImageIcon(new ImageIcon(lock).getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
            URL unlock = getClass().getClassLoader().getResource("unlock.png");
            unlockIcon = new ImageIcon(new ImageIcon(unlock).getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
            lock_unlock.setIcon(lockIcon);
            this.triggeredField.put(lock_unlock, inputField);
            this.targettedButton.put(inputField, lock_unlock);

            JPanel button_and_input = new JPanel();
            button_and_input.add(inputField);
            button_and_input.add(lock_unlock);
            eachIngredient.add(button_and_input);
            ingsPanel.add(eachIngredient);
        });

        this.setResultLabels();
        JPanel results = new JPanel();
        results.setLayout(new GridLayout(2, 1));
        results.add(pLabel);
        results.add(lLabel);
        centerPanel.add(ingsPanel, BorderLayout.NORTH);
        centerPanel.add(results, BorderLayout.SOUTH);

        return centerPanel;
    }

    private void setResultLabels() {
        this.generateResult();
        lLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pLabel.setText("Protien = " + (double) Math.round(protien * 100) / 100 + "%");
        pLabel.setFont(new Font("Calibri", Font.BOLD, 15));
        lLabel.setText("Lipid = " + (double) Math.round(lipid * 100) / 100 + "%");
        lLabel.setFont(new Font("Calibri", Font.BOLD, 15));
    }

    private void generateResult() {
        this.protien = 0;
        this.lipid = 0;
        this.ingredients.forEach(ingredient -> {
            double p = ingredient.getProtein();
            double l = ingredient.getLipid();
            this.protien += p * ((double) ingredientPercentage.get(ingredient) / 100);
            this.lipid += l * ((double) ingredientPercentage.get(ingredient) / 100);
        });
    }

    private void configureButtons() {
        back = new IFFDButton("Back", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout c = (CardLayout) cards.getLayout();
                c.show(cards,"SelectIngredients");
            }
        });
        finalize = new IFFDButton("Finalize", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel finalize1 = null;
                try {
                    finalize1 = new Finalize(cards,ingredientPercentage);
                } catch (IOException | URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
                JScrollPane scrollPane = new JScrollPane( finalize1);
                cards.add(scrollPane,"finalize");
                CardLayout c = (CardLayout) cards.getLayout();
                c.show(cards,"finalize");
            }
        });

    }

    private void configureTitle() {
        title = new JLabel("Design Feed", SwingConstants.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 35));
        title.setBorder(new EmptyBorder(20, 0, 0, 0));
        title.setForeground(Color.black);

        subTitle = new JLabel("Percentage", SwingConstants.CENTER);
        subTitle.setFont(new Font("Arial", Font.BOLD, 20));
        subTitle.setBorder(new EmptyBorder(0, 120, 20, 120));
        subTitle.setForeground(Color.black);

    }

    private HashMap<JSpinner, Integer> sortByValue(HashMap<JSpinner, Integer> hm)
    {
        HashMap<JSpinner, Integer> temp
                = hm.entrySet()
                .stream()
                .sorted((i1, i2)
                        -> i1.getValue().compareTo(
                        i2.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        return temp;
    }

}
