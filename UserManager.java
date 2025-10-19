// Destiny Harris
// 10/19/25
// UserManager class — handles everything to do with users, saving and loading their recipes

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.HashMap;

public class UserManager {
    // this is the file where I save everything (just a simple text file)
    private static final String FILE_NAME = "users.txt";

    // stores each user’s recipes — kind of like a mini database in memory
    private static HashMap<String, ObservableList<Recipe>> userData = new HashMap<>();

    // keeps track of whoever is logged in right now
    private static String currentUser;

    // sets which user is currently using the app
    public static void setCurrentUser(String username) {
        currentUser = username;
        // if this user doesn't exist yet, create a new empty list for them
        userData.putIfAbsent(username, FXCollections.observableArrayList());
    }

    // just returns the logged in user's name
    public static String getCurrentUser() {
        return currentUser;
    }

    // gets the actual list of recipes for this user
    public static ObservableList<Recipe> getCurrentUserRecipes() {
        return userData.get(currentUser);
    }

    // loads all users + their recipes from the text file (runs when app starts)
    public static void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            String user = null;

            while ((line = reader.readLine()) != null) {
                // each user is stored starting with “USER:”
                if (line.startsWith("USER:")) {
                    user = line.substring(5).trim();
                    userData.putIfAbsent(user, FXCollections.observableArrayList());
                } 
                // recipes are stored under that user
                else if (user != null && line.startsWith("RECIPE:")) {
                    String[] parts = line.substring(7).split("\\|");
                    // I save everything like title|category|instructions|url|ingredients
                    if (parts.length >= 5) {
                        Recipe recipe = new Recipe(parts[0], parts[1], parts[2].replace("\\n", "\n"), parts[3]);
                        recipe.loadIngredientsFromString(parts[4]); // rebuilds ingredient list
                        userData.get(user).add(recipe);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No saved users yet (that’s fine for first run).");
        }
    }

    // saves all users + their recipes back into the file (called a lot)
    public static void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            // loop through every user and their recipes
            for (String user : userData.keySet()) {
                writer.println("USER:" + user);
                for (Recipe recipe : userData.get(user)) {
                    writer.println("RECIPE:" + recipe.getTitle() + "|" +
                                   recipe.getCategory() + "|" +
                                   recipe.getInstructions().replace("\n", "\\n") + "|" +
                                   recipe.getUrl() + "|" +
                                   recipe.ingredientsToString());
                }
            }
        } catch (IOException e) {
            // I added this just to help if I ever get file errors
            e.printStackTrace();
        }
    }

    // used to populate the drop-down box on login
    public static ObservableList<String> getUsernames() {
        return FXCollections.observableArrayList(userData.keySet());
    }

    // called when someone logs out (resets user)
    public static void logout() {
        currentUser = null;
    }
}
