// Destiny Harris
//10/9/25


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RecipeManager {
    private ObservableList<Recipe> recipes = FXCollections.observableArrayList();

    public ObservableList<Recipe> getRecipes() {
        return recipes;
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
    }

    public Recipe findRecipeByTitle(String title) {
        for (Recipe recipe : recipes) {
            if (recipe.getTitle().equalsIgnoreCase(title)) {
                return recipe;
            }
        }
        return null;
    }
}

