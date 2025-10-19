// Destiny Harris
// 10/19/25
// Recipe class — holds everything about one recipe (title, instructions, category, url, and ingredients)

import java.util.ArrayList;

public class Recipe {
    // main recipe info
    private String title;
    private String category;
    private String instructions;
    private String url;

    // list to hold multiple ingredients per recipe
    private ArrayList<Ingredient> ingredients = new ArrayList<>();

    // constructor that sets everything up when we make a new recipe
    public Recipe(String title, String category, String instructions, String url) {
        this.title = title;
        this.category = category;
        this.instructions = instructions;
        this.url = url;
    }

    // add a single ingredient to this recipe
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    // remove a single ingredient (used in edit window)
    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }

    // return all the ingredients
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    // this converts ingredients into a single string to save to file
    public String ingredientsToString() {
        StringBuilder sb = new StringBuilder();
        for (Ingredient i : ingredients) {
            // saving name, quantity, and unit separated by “:”
            sb.append(i.getName()).append(":").append(i.getQuantity()).append(":").append(i.getUnit()).append(",");
        }
        return sb.toString();
    }

    // this loads the ingredients when I open the app again
    public void loadIngredientsFromString(String data) {
        ingredients.clear();
        if (data == null || data.isEmpty()) return;

        String[] parts = data.split(",");
        for (String part : parts) {
            if (part.trim().isEmpty()) continue;
            String[] vals = part.split(":");
            if (vals.length == 3) {
                ingredients.add(new Ingredient(vals[0], vals[1], vals[2]));
            }
        }
    }

    // all the normal getters and setters
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getInstructions() { return instructions; }
    public String getUrl() { return url; }

    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public void setUrl(String url) { this.url = url; }

    // this just controls how the recipe shows up in the list
    @Override
    public String toString() {
        if (category != null && !category.isEmpty())
            return title + " (" + category + ")";
        return title;
    }
}
