package praktikum.diplom;

public class IngredientsSet {
    private String[] ingredients;

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    IngredientsSet(String[] ingredients) {
        this.ingredients = ingredients;
    }

    private IngredientsSet() {}

}
