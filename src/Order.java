import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;

public class Order implements Serializable {
    private String customerName;
    private String customerAddress;
    private String customerResidence;
    private List<Pizza> pizzas;
    private LocalDateTime dateTime;

    public Order() {
        pizzas = new ArrayList<>();
        enterOrderFromTerminal();
    }

    @SuppressWarnings("resource") // anders komt er een alert dat scanner niet wordt geclosed.

    public void enterOrderFromTerminal() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Naam: ");
        customerName = validateInput(scanner.nextLine(), "Naam");
        if (!isValidName(customerName)) {
            throw new IllegalArgumentException("Ongeldige naam.");
        }

        System.out.print("Adres: ");
        customerAddress = validateInput(scanner.nextLine(), "Adres");
        if (!isValidAddress(customerAddress)) {
            throw new IllegalArgumentException("Ongeldig adres.");
        }

        System.out.print("Woonplaats: ");
        customerResidence = validateInput(scanner.nextLine(), "Woonplaats");
        if (!isValidName(customerResidence)) {
            throw new IllegalArgumentException("Ongeldige woonplaats.");
        }
        while (true) {
            Pizza pizza = new Pizza();

            System.out.print("Naam van de pizza: ");
            String pizzaName = scanner.nextLine();
            pizza.setName(validatePizzaName(pizzaName));

            System.out.print("Aantal extra toppings: ");
            int extraToppings = validateExtraToppings(scanner.nextLine());
            pizza.setExtraToppings(extraToppings);

            if (extraToppings > 0) {
                for (int i = 0; i < extraToppings; i++) {
                    System.out.print("Extra topping " + (i + 1) + ": ");
                    String topping = scanner.nextLine();
                    pizza.addTopping(topping);
                }
            }

            System.out.print("Aantal pizza's: ");
            int numberPizzas = validateInteger(scanner.nextLine(), "Aantal pizza's");
            if (numberPizzas > 0) {
                pizza.setNumberPizzas(numberPizzas);
            }

            pizzas.add(pizza);

            System.out.print("Wil je nog een pizza bestellen? (ja/nee): ");
            String answer = scanner.nextLine();
            if (!answer.equalsIgnoreCase("ja")) {
                break;
            }
        }

        System.out.print("Bedankt voor uw bestelling! De bestelling is verzonden.");
        scanner.close();
        dateTime = LocalDateTime.now();
    }

    public String createOrderString() {
        StringBuilder bestelling = new StringBuilder();
        bestelling.append(customerName).append("\n");
        bestelling.append(customerAddress).append("\n");
        bestelling.append(customerResidence).append("\n");

        // Voeg per pizza de informatie toe
        for (Pizza pizza : pizzas) {
            bestelling.append(pizza.getName()).append("\n");
            bestelling.append(pizza.getNumberPizzas()).append("\n");
            bestelling.append(pizza.getExtraToppings()).append("\n");

            // Voeg extra toppings toe
            for (String topping : pizza.getToppings()) {
                bestelling.append(topping).append("\n");
            }
        }

        // Voeg datum en tijd van bestelling toe
        bestelling.append(dateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .append("\n");

        return bestelling.toString();
    }

    // Getters
    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerResidence() {
        return customerResidence;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public String validateInput(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " mag niet leeg zijn.");
        }
        return input.trim(); // Trim om onnodige spaties te verwijderen.
    }

    public boolean isValidName(String name) {
        return name.matches("^[a-zA-Z\\s]+$"); // Validatie toegevoegd, geen speciale teken toegestaan
    }

    public boolean isValidAddress(String address) {
        return address.matches("^[a-zA-Z0-9\\s,.-]+$"); // Validatie toegevoegd, enkele symbolen toegestaan
    }

    public int validateInteger(String input, String fieldName) {
        try {
            int value = Integer.parseInt(input.trim());
            if (value <= 0) {
                throw new IllegalArgumentException(fieldName + " moet een positief getal zijn.");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " moet een geldig getal zijn.");
        }
    }

    public String validatePizzaName(String pizzaName) {
        if (pizzaName == null || pizzaName.trim().isEmpty()) {
            throw new IllegalArgumentException("De naam van de pizza mag niet leeg zijn.");
        }
        if (!pizzaName.matches("^[a-zA-Z0-9\\s'-]{1,50}$")) {
            throw new IllegalArgumentException(
                    "Ongeldige pizza-naam. Gebruik alleen letters, cijfers, spaties, '-' of ''', en maximaal 50 tekens.");
        }
        return pizzaName.trim();
    }

    public int validateExtraToppings(String input) {
        try {
            int extraToppings = Integer.parseInt(input.trim());
            if (extraToppings < 0) {
                throw new IllegalArgumentException("Het aantal extra toppings mag niet negatief zijn.");
            }
            if (extraToppings > 10) { // Limiet instellen
                throw new IllegalArgumentException("Je kunt maximaal 10 extra toppings toevoegen.");
            }
            return extraToppings;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Voer een geldig getal in voor het aantal extra toppings.");
        }
    }

}
