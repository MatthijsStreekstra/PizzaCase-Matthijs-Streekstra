import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        @SuppressWarnings("resource") // anders komt er een alert dat scanner niet wordt geclosed.
        Scanner scanner = new Scanner(System.in);
        System.out.println("Kies de communicatiemethode: 1 voor TCP, 2 voor UDP");
        int keuze = scanner.nextInt();

        if (keuze == 1) {
            startPizzaServerTCP();
            startPizzaClientTCP();
        } else if (keuze == 2) {
            startPizzaServerUDP();
            startPizzaClientUDP();
        } else {
            System.out.println("Ongeldige keuze. Probeer opnieuw.");
        }
    }

    private static void startPizzaServerTCP() {
        Thread serverThread = new Thread(() -> {
            PizzaServer.getInstance().start();
        });
        serverThread.start();
    }

    private static void startPizzaClientTCP() {
        Thread clientThread = new Thread(() -> {
            PizzaClient.main(new String[] {});
        });
        clientThread.start();
    }

    private static void startPizzaServerUDP() {
        Thread serverThread = new Thread(() -> {
            PizzaServerUDP.getInstanceUDP();
            PizzaServerUDP.start();
        });
        serverThread.start();
    }

    private static void startPizzaClientUDP() {
        // Start UDP client
        new Thread(() -> PizzaClientUDP.main(new String[] {})).start();
    }
}
