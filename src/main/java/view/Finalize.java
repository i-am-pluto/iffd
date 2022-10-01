package view;

import Model.Ingredient;
import Model.IngredientsRepository;
import templates.DoubleButtonLayout;
import templates.IFFDButton;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.*;

public class Finalize extends JPanel {

    private HashMap<Ingredient,Double> ingredientPercentage;
    private Container cards;
    private JLabel title;
    private IFFDButton back,download;
    private IngredientsRepository ingredientsRepository;
    private double protien;
    private double lipid;
    private JLabel pLabel;
    private JLabel lLabel;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<JLabel> ingredientLabels;
    private HashMap<Ingredient,Double>result;

    public Finalize(Container cards, HashMap<Ingredient, Double> ingredientPercentage) throws IOException, URISyntaxException {
        this.result = new HashMap<>();
        this.ingredientLabels = new ArrayList<>();
        this.pLabel = new JLabel();
        this.lLabel = new JLabel();
        this.ingredientsRepository = new IngredientsRepository();
        this.ingredients = this.ingredientsRepository.getIngredients();
        this.ingredientsRepository.sortIngs(this.ingredients);
        this.cards = cards;
        this.ingredientPercentage = ingredientPercentage;
        if(this.ingredientPercentage.isEmpty())
            System.out.println("No data sent");
        this.setLayout(new BorderLayout());
        this.configureTitle();
        this.add(title,BorderLayout.NORTH);


        this.configureButtons();
        JPanel buttonPanel = new DoubleButtonLayout(back,download);
        this.add(buttonPanel, BorderLayout.SOUTH);

        JPanel centralPanel = configureCentralPanel();
        this.add(centralPanel,BorderLayout.CENTER);

    }

    private JPanel configureCentralPanel() {

        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BorderLayout());

        String[][] data = new String[ingredientPercentage.size()][4];
        int i = 0;
        for(Ingredient ingredient:this.ingredientPercentage.keySet()){

            data[i][0] = ingredient.getIngredient();
            data[i][1] = ingredient.getProtein().toString();
            data[i][2] = ingredient.getLipid().toString();
            data[i][3] = String.valueOf(Math.round(ingredientPercentage.get(ingredient)));

            i++;
        }

        Arrays.sort(data, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return o1[0].compareTo(o2[0]);
            }
        });

        String[] columnNames = { "Ingredient Name", "Protein %", "Lipid %", "Proportion %" };


        JPanel table = new JPanel();
        table.setLayout(new GridLayout(ingredientPercentage.size()+1,1,0,0));
        JPanel headerRow = new JPanel();
        headerRow.setLayout(new GridLayout(1,4,0,0));
        for(i=0;i<4;i++){
            JPanel col = new JPanel();
            Border line2 = new LineBorder(Color.BLACK,3);
            col.setBorder(line2);
            JLabel colData = new JLabel(columnNames[i]);
            colData.setFont(new Font("Calibri", Font.BOLD, 12));
            col.add(colData);
            headerRow.add(col);
        }
        table.add(headerRow);

        for(String[]d : data){
            JPanel row = new JPanel();
            row.setLayout(new GridLayout(1,4,0,0));
            for(i=0;i<4;i++){
                JPanel col = new JPanel();
                Border line2 = new LineBorder(Color.BLACK,1);
                col.setBorder(line2);
                JLabel colData = new JLabel(d[i]);
                colData.setFont(new Font("Calibri", Font.PLAIN, 10));
                col.add(colData);
                row.add(col);
            }
            table.add(row);
        }

        Border line = new LineBorder(Color.BLACK,1);
        Border margin = new EmptyBorder(10, 80, 10, 80);
        Border compound = new CompoundBorder(margin,line);
        table.setBorder(compound);

        centralPanel.add(table,BorderLayout.NORTH);

        this.setResultLabels();

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(16,1));

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0.00, 0.00, 100.00, 1.0);
        JSpinner inputField = new JSpinner(spinnerModel);
        inputField.setFont(new Font("Calibri", Font.PLAIN, 12));
        inputField.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int i = 0;
                for(Ingredient ingredient: ingredientPercentage.keySet()){
//                    System.out.println(ingredientPercentage.get(ingredient));
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(3);
                    df.setMinimumFractionDigits(3);
                    double weight = ingredientPercentage.get(ingredient)*((double )inputField.getValue())/100;
                    ingredientLabels.get(i).setText(ingredient.getIngredient()+" = "+df.format(weight)+" kg");
                    result.put(ingredient,weight);
                    i++;
                }
            }
        });
        JPanel inputKg = new JPanel();
        inputKg.setLayout(new GridLayout(1,2));
        JLabel inputLabel = new JLabel("Weight of Feed to be Prepared in Kg ");
        inputLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        inputKg.add(inputLabel);
        inputKg.add(inputField);
        inputKg.setBorder(new EmptyBorder(0,120,0,120));
        southPanel.add(pLabel);
        southPanel.add(lLabel);
        southPanel.add(inputKg);

        for(Ingredient ingredient: ingredientPercentage.keySet()){

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(3);
            df.setMinimumFractionDigits(3);
            double weight = ingredientPercentage.get(ingredient)*((double )inputField.getValue())/100;

            result.put(ingredient,weight);
            JLabel label = new JLabel(ingredient.getIngredient()+" = "+df.format(weight)+" kg");
            label.setFont(new Font("Calibri", Font.BOLD, 12));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(new EmptyBorder(2,10,2,10));
            southPanel.add(label);
            this.ingredientLabels.add(label);
        }
        centralPanel.add(southPanel,BorderLayout.SOUTH);
        return centralPanel;
    }

    private void setResultLabels() {
        this.generateResult();
        lLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pLabel.setText("Protein = " + (double) Math.round(protien * 100) / 100 + "%");
        pLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        lLabel.setText("Lipid = " + (double) Math.round(lipid * 100) / 100 + "%");
        lLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        System.out.println(pLabel.getText());
    }

    private void generateResult() {
        this.protien = 0;
        this.lipid = 0;
        this.ingredientPercentage.keySet().forEach(ingredient -> {
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

                JPanel designCard = null;
                try {
                    designCard = new Design(cards, null,ingredientPercentage);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
                cards.add(designCard,"design");
                CardLayout c = (CardLayout) cards.getLayout();
                c.show(cards,"design");
            }
        });

        download = new IFFDButton("Download", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame parentComponent = new JFrame();
                JFileChooser fileChooser= new JFileChooser();
                FileNameExtensionFilter filter =  new FileNameExtensionFilter("CSV FILES", "csv", "spreadsheet");
                fileChooser.setFileFilter(filter);
                // Some init code, if you need one, like setting title
                int returnVal = fileChooser.showOpenDialog(parentComponent);
                String path = new String();
                if ( returnVal == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();

                    path = fileToSave.getPath()+".csv";

                    try{
                        JOptionPane.showMessageDialog(null, "File Downloaded");
                    }
                    catch(Exception error){
                        JOptionPane.showMessageDialog(null, error);
                    }
                }

                File resultFile = new File(path);
                FileWriter resWriter;
                try {
                    resultFile.createNewFile();
                    resWriter = new FileWriter(path);
                    resWriter.write("Ingredient Name"+","+"Protein %"+","+"Lipid %"+","+"Proportion"+","+"Weight (Kg)\n");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                for (Map.Entry mapElement : result.entrySet()){
                    Ingredient key = (Ingredient) mapElement.getKey();
                    String proteinPercent = key.getProtein().toString();
                    String lipidPercentage = key.getLipid().toString();
                    String proportion = ingredientPercentage.get(key).toString();
                    String weight = mapElement.getValue().toString();

                    String r = key.getIngredient()+","+proteinPercent+","+lipidPercentage+","+proportion+","+weight +"\n";
                    try {
                        resWriter.write(r);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                try {
                    resWriter.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }

    private void configureTitle() {
        title = new JLabel("Finalize Feed", SwingConstants.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 35));
        title.setBorder(new EmptyBorder(20, 0, 0, 0));
        title.setForeground(Color.black);
    }
}
