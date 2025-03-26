import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pizza implements Serializable {
    private String name;
    private List<String> toppings;
    private int extraToppings;
    private int numberPizzas;
    
    public Pizza() {
        toppings = new ArrayList<>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberPizzas() {
        return numberPizzas;
    }

    public void setNumberPizzas(int numberPizzas) {
        this.numberPizzas = numberPizzas;
    }

    public int getExtraToppings() {
        return extraToppings;
    }

    public void setExtraToppings(int extraToppings) {
        this.extraToppings = extraToppings;
    }

    public void addTopping(String topping) {
        toppings.add(topping);
    }

    public List<String> getToppings() {
        return toppings;
    }
}
