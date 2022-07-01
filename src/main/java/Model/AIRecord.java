package Model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class AIRecord {

    ArrayList<Ingredient> ingredients;
    HashMap<Ingredient,Integer>proportions;
    Double phermone;

    public AIRecord(ArrayList<Ingredient> ingredients, HashMap<Ingredient, Integer> proportions, Double phermone) throws IOException, URISyntaxException {
        this.ingredients = ingredients;
        this.proportions = proportions;
        this.phermone = phermone;
        this.ingredients = new ArrayList<>();
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public HashMap<Ingredient, Integer> getProportions() {
        return proportions;
    }

    public void setProportions(HashMap<Ingredient, Integer> proportions) {
        this.proportions = proportions;
    }

    public Double getPhermone() {
        return phermone;
    }

    public void setPhermone(Double phermone) {
        this.phermone = phermone;
    }
}
