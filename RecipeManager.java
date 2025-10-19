// Destiny Harris
//10/9/25

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.Map;

public class RecipeManager {
    private Map<String, ObservableList<Recipe>> userRecipes = new HashMap<>();

    public ObservableList<Recipe> getRecipesForUser(String username) {
        userRecipes.putIfAbsent(username, FXCollections.observableArrayList());
        return userRecipes.get(username);
    }

    public void addRecipe(String username, Recipe recipe) {
        getRecipesForUser(username).add(recipe);
    }

    public void deleteRecipe(String username, Recipe recipe) {
        getRecipesForUser(username).remove(recipe);
    }
}

