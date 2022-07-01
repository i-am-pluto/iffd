package Model;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class IngredientsRepository {

    ArrayList<Ingredient> ingredients;

    public IngredientsRepository() throws IOException, URISyntaxException {
        this.ingredients = new ArrayList<>();
        String file = "/ingredients.csv";
        this.setData(file);
    }

    private void setData(String fileName) throws IOException, URISyntaxException {
        InputStream file = getClass().getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(file));
        br.readLine();
        String line = br.readLine();
        while (line != null) {
            String[] info  = line.split(",");
            this.ingredients.add(new Ingredient(info[0],Double.parseDouble(info[1]),Double.parseDouble(info[2])));
            line = br.readLine();
        }
    }
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
    public void sortIngs(ArrayList<Ingredient>ings){
        ings.sort(new SortIng());
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public String[] getIngredientsNames(){
        ArrayList<String> options = new ArrayList<>();
        options.add("Select an Ingredient");
        ingredients.forEach(ingredient -> options.add(ingredient.getIngredient()));
        String[] ops = new String[0];
        ops = options.toArray(ops);
        return ops;
    }

};
