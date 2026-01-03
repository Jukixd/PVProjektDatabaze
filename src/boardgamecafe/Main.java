package boardgamecafe;

import boardgamecafe.entity.Customer;
import boardgamecafe.entity.Game;
import boardgamecafe.repository.CustomerRepository;
import boardgamecafe.repository.GameRepository;
import boardgamecafe.service.DataImporter;
import boardgamecafe.service.RentalService;
import boardgamecafe.service.ReportService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final CustomerRepository customerRepo = new CustomerRepository();
    private static final GameRepository gameRepo = new GameRepository();
    private static final RentalService rentalService = new RentalService();
    private static final ReportService reportService = new ReportService();
    private static final DataImporter importer = new DataImporter();

    public static void main(String[] args) {
        System.out.println("Vítejte v systému BOARD GAME CAFE!");
        reportService.createViews();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> listAllData();
                case "2" -> createNewRental();
                case "3" -> showReports();
                case "4" -> importData();
                case "5" -> {
                    System.out.println("Ukončuji aplikaci. Nashledanou!");
                    running = false;
                }
                default -> System.out.println("Neplatná volba, zkuste to znovu.");
            }
            System.out.println("\nStiskněte Enter pro pokračování...");
            scanner.nextLine();
        }
    }
    private static void printMenu() {
        System.out.println("\n--- HLAVNÍ MENU ---");
        System.out.println("1. Zobrazit Zákazníky a Hry");
        System.out.println("2. Nová výpůjčka (Transakce)");
        System.out.println("3. Generovat denní report");
        System.out.println("4. Importovat data z CSV");
        System.out.println("5. Konec");
        System.out.print("Vaše volba: ");
    }

    private static void listAllData() {
        System.out.println("\n--- SEZNAM ZÁKAZNÍKŮ ---");
        List<Customer> customers = customerRepo.findAll();
        if (customers.isEmpty()) System.out.println("Žádní zákazníci.");
        else customers.forEach(System.out::println);

        System.out.println("\n--- SEZNAM HER ---");
        List<Game> games = gameRepo.findAll();
        if (games.isEmpty()) System.out.println("Žádné hry.");
        else games.forEach(System.out::println);

        System.out.println();
        rentalService.printAllRentals();
    }

    private static void createNewRental() {
        System.out.println("\n--- NOVÁ VÝPŮJČKA ---");
        try {
            // 1. Výběr zákazníka
            System.out.print("Zadejte ID zákazníka: ");
            int customerId = Integer.parseInt(scanner.nextLine());

            if (customerRepo.findById(customerId) == null) {
                System.out.println("CHYBA: Zákazník s ID " + customerId + " neexistuje!");
                return;
            }

            // 2. Výběr her
            List<Integer> gameIds = new ArrayList<>();
            while (true) {
                System.out.print("Zadejte ID hry (nebo 'ok' pro dokončení): ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("ok")) break;

                try {
                    int gameId = Integer.parseInt(input);
                    if (gameRepo.findById(gameId) != null) {
                        gameIds.add(gameId);
                        System.out.println("Hra přidána do košíku.");
                    } else {
                        System.out.println("CHYBA: Hra s ID " + gameId + " neexistuje.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Neplatné číslo.");
                }
            }

            // 3. Provedení transakce
            if (gameIds.isEmpty()) {
                System.out.println("Nebyly vybrány žádné hry. Výpůjčka zrušena.");
            } else {
                boolean success = rentalService.createRental(customerId, gameIds);
                if (success) {
                    System.out.println("Výpůjčka byla úspěšně uložena!");
                } else {
                    System.out.println("Chyba při ukládání výpůjčky.");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("CHYBA: Musíte zadat číslo!");
        }
    }

    private static void showReports() {
        reportService.printGeneralReport();
    }

    private static void importData() {
        System.out.println("Importuji data ze složky /data ...");
        importer.importCustomers("data/customers.csv");
        importer.importGames("data/games.csv");
        System.out.println("Hotovo.");
    }
}