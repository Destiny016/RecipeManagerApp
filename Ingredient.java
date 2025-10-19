// Destiny Harris
// 10/9/25
// Ingredient class — holds info for each recipe ingredient

public class Ingredient {
    // storing the basic details for each ingredient
    private String name;
    private String quantity;
    private String unit;

    // constructor to set up a new ingredient
    public Ingredient(String name, String quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    // simple getters and setters for the fields
    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // mainly for showing ingredients in the list
    @Override
    public String toString() {
        // shows something like “2 cups sugar”
        return quantity + " " + unit + " " + name;
    }
}
