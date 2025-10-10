// Destiny Harris
//10/9/25


import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String title;
    private List<Ingredient> ingredients;
    private String instructions;
    private String category;

    public Recipe(String title, String instructions, String category) {
        this.title = title;
        this.instructions = instructions;
        this.category = category;
        this.ingredients = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getCategory() {
        return category;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    @Override
    public String toString() {
        return title + " (" + category + ")";
    }
}

