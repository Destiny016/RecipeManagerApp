// Destiny Harris
// 10/9/25


import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RecipeManagerApp extends Application {

    private RecipeManager manager = new RecipeManager();
    private ListView<Recipe> recipeListView = new ListView<>();

    @Override
    public void start(Stage stage) {
        // --- UI ELEMENTS ---
        TextField titleField = new TextField();
        titleField.setPromptText("Recipe Title");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        TextArea instructionsArea = new TextArea();
        instructionsArea.setPromptText("Enter cooking instructions...");
        instructionsArea.setPrefRowCount(5);

        Button addButton = new Button("Add Recipe");
        Button deleteButton = new Button("Delete Selected");

        // --- EVENT #1: ADD RECIPE ---
        addButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String category = categoryField.getText().trim();
            String instructions = instructionsArea.getText().trim();

            if (title.isEmpty() || instructions.isEmpty()) {
                showAlert("Please fill in the title and instructions!");
                return;
            }

            Recipe newRecipe = new Recipe(title, instructions, category);
            manager.addRecipe(newRecipe);
            recipeListView.setItems(manager.getRecipes());

            titleField.clear();
            categoryField.clear();
            instructionsArea.clear();
        });

        // --- EVENT #2: DELETE RECIPE ---
        deleteButton.setOnAction(e -> {
            Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                manager.removeRecipe(selected);
            } else {
                showAlert("Please select a recipe to delete!");
            }
        });

        // --- LAYOUT ---
        VBox inputBox = new VBox(10, titleField, categoryField, instructionsArea, new HBox(10, addButton, deleteButton));
        inputBox.setPadding(new Insets(10));

        VBox listBox = new VBox(10, new Label("Saved Recipes:"), recipeListView);
        listBox.setPadding(new Insets(10));

        HBox root = new HBox(20, inputBox, listBox);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 700, 400);
        stage.setTitle("Recipe Manager App");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

