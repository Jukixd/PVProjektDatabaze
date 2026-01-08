package boardgamecafe;

import boardgamecafe.entity.*;
import boardgamecafe.repository.*;
import boardgamecafe.service.*;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CustomerRepository customerRepo = new CustomerRepository();
    private static final GameRepository gameRepo = new GameRepository();
    private static final CafeTableRepository tableRepo = new CafeTableRepository();
    private static final RentalService rentalService = new RentalService();
    private static final ReportService reportService = new ReportService();
    private static final DataImporter importer = new DataImporter();

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   > BOARD GAME CAFE MANAGER <");
        System.out.println("==========================================");

        try {
            initializeSystem();
        } catch (Exception e) {
            System.out.println("\n---KRITICKÁ CHYBA SPUŠTĚNÍ---");
            System.out.println("Nepodařilo se připojit k databázi.");
            System.out.println("Zkontrolujte soubor 'config.properties' (heslo, uživatel, url).");
            System.out.println("\nAplikace bude ukončena.");
            return;
        }


        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    showDataSubMenu();
                    waitForEnter();
                }
                case "2" -> {
                    createRentalProcess(scanner);
                    waitForEnter();
                }
                case "3" -> {
                    reportService.printGeneralReport();
                    waitForEnter();
                }
                case "4" -> {
                    runImport();
                    waitForEnter();
                }
                case "5" -> {
                    deleteGame();
                    waitForEnter();
                }
                case "6" -> {
                    updateGamePrice();
                    waitForEnter();
                }
                case "7" -> {
                    System.out.println("Ukončuji systém... Na shledanou!");
                    running = false;
                }
                default -> {
                    System.out.println("Neplatná volba.");
                    waitForEnter();
                }
            }
        }
    }

    private static void initializeSystem() {
        reportService.createViews();
    }
    private static void printMainMenu() {
        System.out.println("\n\n");
        System.out.println("--- HLAVNÍ MENU ---");
        System.out.println("1.  Zobrazit data (Zákazníci, Hry...)");
        System.out.println("2.  Nová výpůjčka");
        System.out.println("3.  Report tržeb");
        System.out.println("4.  Import dat (CSV)");
        System.out.println("5.  Smazat hru");
        System.out.println("6. Upravit cenu hry");
        System.out.println("7.  Konec");
        System.out.print("Vaše volba: ");
    }

    private static void waitForEnter() {
        System.out.println("\nStiskněte [ENTER] pro návrat do menu...");
        scanner.nextLine();
    }

    private static void showDataSubMenu() {
        System.out.println("\n--- CO CHCETE ZOBRAZIT? ---");
        System.out.println("a) Seznam zákazníků");
        System.out.println("b) Katalog her");
        System.out.println("c) Stoly");
        System.out.println("d) Historie výpůjček");
        System.out.println("e) VŠE NAJEDNOU");
        System.out.print("Vyberte (a-e): ");

        String subChoice = scanner.nextLine().toLowerCase();

        switch (subChoice) {
            case "a" -> {
                System.out.println("\n--- ZÁKAZNÍCI ---");
                List<Customer> list = customerRepo.findAll();
                if (list.isEmpty()) System.out.println("(Žádná data)");
                else list.forEach(System.out::println);
            }
            case "b" -> {
                System.out.println("\n--- HRY ---");
                List<Game> list = gameRepo.findAll();
                if (list.isEmpty()) System.out.println("(Žádná data)");
                else list.forEach(System.out::println);
            }
            case "c" -> {
                System.out.println("\n--- STOLY ---");
                List<CafeTable> list = tableRepo.findAll();
                if (list.isEmpty()) System.out.println("(Žádná data)");
                else list.forEach(System.out::println);
            }
            case "d" -> {
                System.out.println("\n--- VÝPŮJČKY ---");
                rentalService.printAllRentals();
            }
            case "e" -> {
                System.out.println("\n--- KOMPLETNÍ VÝPIS DAT ---");
                customerRepo.findAll().forEach(System.out::println);
                System.out.println("---------------------------");
                gameRepo.findAll().forEach(System.out::println);
                System.out.println("---------------------------");
                tableRepo.findAll().forEach(System.out::println);
                System.out.println("---------------------------");
                rentalService.printAllRentals();
            }
            default -> System.out.println(" Neznámá volba, vracím se do menu.");
        }
    }

    private static void createRentalProcess(Scanner scanner) {
        System.out.println("\n=== NOVÁ VÝPŮJČKA ===");
        System.out.print("Zadejte ID zákazníka: ");
        int customerId;
        try {
            customerId = Integer.parseInt(scanner.nextLine());
            if (customerRepo.findById(customerId) == null) {
                System.out.println("Chyba: Zákazník s tímto ID neexistuje.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Chyba: ID musí být číslo.");
            return;
        }

        System.out.println("\n--- Výběr stolu ---");
        List<CafeTable> tables = tableRepo.findAll();
        if (tables.isEmpty()) {
            System.out.println("Pozor: Nemáte definované žádné stoly. Importujte 'tables.csv'.");
            return;
        }
        for (CafeTable t : tables) {
            System.out.println(t.getId() + ". " + t.getLocationDescription() + " (Kapacita: " + t.getSeats() + ")");
        }

        System.out.print("Zadejte ID stolu: ");
        int tableId;
        try {
            tableId = Integer.parseInt(scanner.nextLine());
            boolean tableExists = tables.stream().anyMatch(t -> t.getId() == tableId);
            if (!tableExists) {
                System.out.println("hyba: Stůl s tímto ID neexistuje.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Chyba: ID stolu musí být číslo.");
            return;
        }
        List<Integer> gameIds = new ArrayList<>();
        System.out.println("\n--- Výběr her (napište 'ok' pro ukončení) ---");
        while (true) {
            System.out.print("Zadejte ID hry: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("ok")) {
                break;
            }
            try {
                int gameId = Integer.parseInt(input);
                Game g = gameRepo.findById(gameId);
                if (g != null) {
                    if (g.isAvailable()) {
                        gameIds.add(gameId);
                        System.out.println("Hra '" + g.getName() + "' přidána do košíku.");
                    } else {
                        System.out.println("⚠Hra není dostupná (je půjčená).");
                    }
                } else {
                    System.out.println("Hra s tímto ID neexistuje.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Neplatný vstup, zadejte číslo nebo 'ok'.");
            }
        }

        if (gameIds.isEmpty()) {
            System.out.println("Košík je prázdný, výpůjčka zrušena.");
            return;
        }
        boolean success = rentalService.createRental(customerId, tableId, gameIds);
        if (success) {
            System.out.println("Výpůjčka byla úspěšně uložena!");
        } else {
            System.out.println("Nastala chyba při ukládání výpůjčky.");
        }
    }
    private static void runImport() {
        System.out.println("\n--- IMPORT DAT ---");
        importer.importCustomers("data/customers.csv");
        importer.importGames("data/games.csv");
        importer.importTables("data/tables.csv");
        System.out.println("Hotovo");
    }
    private static void deleteGame() {
        System.out.println("\n--- SMAZAT HRU ---");
        System.out.print("ID hry ke smazání: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (gameRepo.delete(id)) {
                System.out.println("Hra smazána.");
            } else {
                System.out.println("Nelze smazat (hra neexistuje nebo je půjčená).");
            }
        } catch (Exception e) {
            System.out.println("Chyba: " + e.getMessage());
        }
    }
    private static void updateGamePrice() {
        System.out.println("\n--- UPRAVIT CENU ---");
        System.out.print("ID hry: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Game game = gameRepo.findById(id);
            if (game != null) {
                System.out.println("Hra: " + game.getName() + " | Cena: " + game.getRentalPrice());
                System.out.print("Nová cena: ");
                float price = Float.parseFloat(scanner.nextLine());
                game.setRentalPrice(price);

                if (gameRepo.update(game)) {
                    System.out.println("Cena změněna.");
                } else {
                    System.out.println("Chyba DB.");
                }
            } else {
                System.out.println("Hra nenalezena.");
            }
        } catch (Exception e) {
            System.out.println("Chyba vstupu.");
        }
    }
}