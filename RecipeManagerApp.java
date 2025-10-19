// Destiny Harris
// 10/19/25
// Main JavaFX App — This is basically where everything connects together.
// I worked on this the most, making sure all the buttons and screens actually work.

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.net.URI;

public class RecipeManagerApp extends Application {
    private Label recipeCountLabel; // just keeps track of how many recipes I have saved

    @Override
    public void start(Stage primaryStage) {
        // load saved users/recipes when the app starts
        UserManager.loadUsers();
        showLoginWindow(primaryStage);
    }

    // First screen that shows when app starts
    private void showLoginWindow(Stage primaryStage) {
        Stage loginStage = new Stage();
        loginStage.setTitle("Welcome to Recipe Manager");

        Label title = new Label("🍴 Recipe Manager");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1565c0;");

        Label label = new Label("Select or Enter Username:");
        ComboBox<String> userComboBox = new ComboBox<>(UserManager.getUsernames());
        userComboBox.setEditable(true); // so I can type a new name
        userComboBox.setPromptText("Enter your name");

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(120);
        loginButton.setOnAction(e -> {
            String username = userComboBox.getEditor().getText().trim();
            if (username.isEmpty()) {
                showAlert("Error", "Please enter a username.");
            } else {
                UserManager.setCurrentUser(username);
                loginStage.close();
                showMainApp(primaryStage);
            }
        });

        VBox card = new VBox(15, title, label, userComboBox, loginButton);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));

        StackPane root = new StackPane(card);
        Scene scene = new Scene(root, 450, 320);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        loginStage.setScene(scene);
        loginStage.show();
    }

    // The main part of the app (where the recipes and form are shown)
    private void showMainApp(Stage primaryStage) {
        primaryStage.setTitle("Recipe Manager - " + UserManager.getCurrentUser());

        // Fields for adding a new recipe
        TextField titleField = new TextField();
        titleField.setPromptText("Recipe Title");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        TextArea instructionsField = new TextArea();
        instructionsField.setPromptText("Enter cooking instructions...");

        // Ingredient section (to add multiple ingredients)
        ListView<Ingredient> ingredientList = new ListView<>();
        TextField ingName = new TextField();
        ingName.setPromptText("Ingredient name");
        TextField ingQty = new TextField();
        ingQty.setPromptText("Quantity");
        TextField ingUnit = new TextField();
        ingUnit.setPromptText("Unit");

        Button addIngredientButton = new Button("Add Ingredient");
        addIngredientButton.setOnAction(e -> {
            // adds ingredient if name is not empty
            if (!ingName.getText().isEmpty()) {
                Ingredient ing = new Ingredient(ingName.getText(), ingQty.getText(), ingUnit.getText());
                ingredientList.getItems().add(ing);
                // clears fields so I can type the next one
                ingName.clear(); ingQty.clear(); ingUnit.clear();
            }
        });

        VBox ingredientBox = new VBox(8, new Label("Ingredients:"), ingredientList,
                new HBox(5, ingName, ingQty, ingUnit, addIngredientButton));
        VBox.setVgrow(ingredientList, Priority.ALWAYS);

        // I put the instructions and ingredients side by side
        HBox instructionsAndIngredients = new HBox(10,
                new VBox(new Label("Instructions:"), instructionsField), ingredientBox);
        HBox.setHgrow(instructionsAndIngredients, Priority.ALWAYS);
        VBox.setVgrow(instructionsField, Priority.ALWAYS);

        TextField urlField = new TextField();
        urlField.setPromptText("Recipe URL (optional)");

        Button addButton = new Button("Add Recipe");
        Button deleteButton = new Button("Delete Selected");
        Button logoutButton = new Button("Logout");

        HBox buttonBox = new HBox(10, addButton, deleteButton, logoutButton);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        // search bar for recipes
        TextField searchField = new TextField();
        searchField.setPromptText("Search recipes...");

        ListView<Recipe> listView = new ListView<>(UserManager.getCurrentUserRecipes());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase();
            // filters recipes by title/category/instructions
            listView.setItems(UserManager.getCurrentUserRecipes().filtered(
                    recipe -> recipe.getTitle().toLowerCase().contains(filter)
                            || recipe.getCategory().toLowerCase().contains(filter)
                            || recipe.getInstructions().toLowerCase().contains(filter)
            ));
        });

        // This makes each recipe show with its title + a clickable URL
        listView.setCellFactory(param -> new ListCell<>() {
            private final Hyperlink link = new Hyperlink();
            private final Label titleLabel = new Label();
            private final VBox layout = new VBox(titleLabel, link);
            {
                layout.setSpacing(2);
            }

            @Override
            protected void updateItem(Recipe recipe, boolean empty) {
                super.updateItem(recipe, empty);
                if (empty || recipe == null) {
                    setGraphic(null);
                } else {
                    titleLabel.setText(recipe.toString());
                    String url = recipe.getUrl();
                    if (url != null && !url.isEmpty()) {
                        link.setText(url);
                        link.setOnAction(e -> {
                            try { Desktop.getDesktop().browse(new URI(url)); }
                            catch (Exception ex) { showAlert("Error", "Could not open link."); }
                        });
                    } else {
                        link.setText("");
                        link.setOnAction(null);
                    }
                    setGraphic(layout);
                }
            }
        });

        recipeCountLabel = new Label();
        updateRecipeCount();

        VBox listBox = new VBox(10, new Label("Saved Recipes:"), searchField, listView, recipeCountLabel);
        VBox.setVgrow(listView, Priority.ALWAYS);

        // this section holds all the input fields
        VBox inputBox = new VBox(10, titleField, categoryField, instructionsAndIngredients, urlField, buttonBox);
        VBox.setVgrow(instructionsAndIngredients, Priority.ALWAYS);

        HBox root = new HBox(20, inputBox, listBox);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f5f6fa;");
        HBox.setHgrow(inputBox, Priority.ALWAYS);
        HBox.setHgrow(listBox, Priority.ALWAYS);

        // Add recipe button (saves new one)
        addButton.setOnAction(e -> {
            String title = titleField.getText();
            String category = categoryField.getText();
            String instructions = instructionsField.getText();
            String url = urlField.getText();

            if (title.isEmpty()) {
                showAlert("Error", "Please enter a recipe title.");
                return;
            }

            Recipe recipe = new Recipe(title, category, instructions, url);
            recipe.getIngredients().addAll(ingredientList.getItems());
            UserManager.getCurrentUserRecipes().add(recipe);
            UserManager.saveUsers(); // saves to file
            updateRecipeCount();

            // clear everything for the next one
            titleField.clear(); categoryField.clear(); instructionsField.clear(); urlField.clear();
            ingredientList.getItems().clear();
        });

        // Delete selected recipe
        deleteButton.setOnAction(e -> {
            Recipe selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                UserManager.getCurrentUserRecipes().remove(selected);
                UserManager.saveUsers();
                updateRecipeCount();
            }
        });

        // logout button (goes back to login)
        logoutButton.setOnAction(e -> {
            UserManager.saveUsers();
            UserManager.logout();
            primaryStage.close();
            showLoginWindow(new Stage());
        });

        // double-click recipe to open the edit window
        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                Recipe selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openEditWindow(selected);
                }
            }
        });

        // final scene setup
        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        // saves automatically when closing
        primaryStage.setOnCloseRequest(e -> UserManager.saveUsers());
    }

    // updates the little “you have X recipes” label
    private void updateRecipeCount() {
        int count = UserManager.getCurrentUserRecipes().size();
        recipeCountLabel.setText("You have " + count + (count == 1 ? " recipe." : " recipes."));
    }

    // Edit window — pops up when you double-click a recipe
    private void openEditWindow(Recipe recipe) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Recipe");

        TextField urlField = new TextField(recipe.getUrl());
        urlField.setPromptText("Recipe URL (optional)");

        TextArea instructions = new TextArea(recipe.getInstructions());
        ListView<Ingredient> ingList = new ListView<>();
        ingList.getItems().addAll(recipe.getIngredients());

        // inputs to add new ingredients here too
        TextField ingName = new TextField();
        ingName.setPromptText("Ingredient name");
        TextField ingQty = new TextField();
        ingQty.setPromptText("Quantity");
        TextField ingUnit = new TextField();
        ingUnit.setPromptText("Unit");

        Button addIng = new Button("Add");
        addIng.setOnAction(e -> {
            if (!ingName.getText().isEmpty()) {
                Ingredient ing = new Ingredient(ingName.getText(), ingQty.getText(), ingUnit.getText());
                recipe.addIngredient(ing);
                ingList.getItems().add(ing);
                ingName.clear(); ingQty.clear(); ingUnit.clear();
                UserManager.saveUsers(); // learned this needs to be here to persist right away
            }
        });

        Button removeIng = new Button("Delete Selected");
        removeIng.setOnAction(e -> {
            Ingredient selected = ingList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                recipe.removeIngredient(selected);
                ingList.getItems().remove(selected);
                UserManager.saveUsers();
            }
        });

        Button openLink = new Button("Open Link");
        openLink.setOnAction(e -> {
            String url = urlField.getText();
            if (url != null && !url.isEmpty()) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    showAlert("Error", "Could not open link.");
                }
            }
        });

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            recipe.setInstructions(instructions.getText());
            recipe.setUrl(urlField.getText());
            UserManager.saveUsers();
            editStage.close();
        });

        // layout for editing ingredients
        VBox ingControls = new VBox(10,
                new HBox(5, ingName, ingQty, ingUnit, addIng),
                removeIng);
        VBox ingBox = new VBox(8, new Label("Ingredients:"), ingList, ingControls);
        VBox.setVgrow(ingList, Priority.ALWAYS);

        VBox layout = new VBox(12,
                new Label("Recipe URL:"), urlField, openLink,
                new Label("Instructions:"), instructions,
                ingBox,
                saveButton
        );
        layout.setPadding(new Insets(15));
        VBox.setVgrow(instructions, Priority.ALWAYS);

        Scene scene = new Scene(layout, 650, 550);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        editStage.setScene(scene);
        editStage.show();
    }

    // small helper function for popup alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
