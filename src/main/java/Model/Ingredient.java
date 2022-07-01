package Model;

public class Ingredient {

    private String ingredient;
    private Double protein;
    private Double lipid;

    public Ingredient(String ingredient, Double protein, Double lipid) {
        this.ingredient = ingredient;
        this.protein = protein;
        this.lipid = lipid;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getLipid() {
        return lipid;
    }

    public void setLipid(Double lipid) {
        this.lipid = lipid;
    }
}
