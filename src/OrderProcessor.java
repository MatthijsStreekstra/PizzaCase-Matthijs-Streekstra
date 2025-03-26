import java.util.ArrayList;
import java.util.List;

public class OrderProcessor {
    private List<Order> orders;

    public OrderProcessor() {
        this.orders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        orders.add(order);
        System.out.println("Order received: " + order.toString());
        printOrderDetails(order); 
    }

    private void printOrderDetails(Order order) {
        System.out.println("Klant naam: " + order.getCustomerName());
        System.out.println("Klant adres: " + order.getCustomerAddress());
        System.out.println("Klant woonplaats: " + order.getCustomerResidence());
        System.out.println("Datum en tijd van de bestelling: " + order.getDateTime());
        System.out.println("Pizzas: ");

        List<Pizza> pizzas = order.getPizzas();
        for (Pizza pizza : pizzas) {
            System.out.println("Pizza: " + pizza.getName());
            System.out.println("Aantal extra toppings: " + pizza.getExtraToppings());
            System.out.println("Soorten toppings: " + pizza.getToppings());
            System.out.println("Aantal pizza's: " + pizza.getNumberPizzas());
        }
    }
}
