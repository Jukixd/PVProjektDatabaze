package boardgamecafe.service;


import boardgamecafe.entity.Customer;
import boardgamecafe.entity.Game;
import boardgamecafe.entity.GameGenre;
import boardgamecafe.entity.CafeTable;
import boardgamecafe.repository.CustomerRepository;
import boardgamecafe.repository.GameRepository;
import boardgamecafe.repository.CafeTableRepository;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataImporter {

    public void importCustomers(String filePath) {
        CustomerRepository repo = new CustomerRepository();
        int count = 0;

        System.out.println("--- Začínám import zákazníků ---");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 3) {
                    String name = parts[0].trim();
                    String email = parts[1].trim();
                    String phone = parts[2].trim();
                    try {
                        repo.save(new Customer(name, email, phone));
                        count++;
                    } catch (Exception e) {
                        System.err.println("Nepodařilo se uložit řádek: " + line);
                    }
                }
            }
            System.out.println("Import dokončen. Úspěšně vloženo: " + count);
        } catch (IOException e) {
            System.err.println("Chyba při čtení souboru: " + e.getMessage());
        }
    }
    private final CafeTableRepository tableRepo = new CafeTableRepository();

    public void importTables(String filePath) {
        System.out.println("--- Začínám import stolů ---");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    int capacity = Integer.parseInt(parts[0].trim());
                    String description = parts[1].trim();

                    CafeTable table = new CafeTable(capacity, description);
                    tableRepo.save(table);
                    count++;
                }
            }
            System.out.println("Import dokončen. Úspěšně vloženo: " + count);
        } catch (IOException e) {
            System.out.println("Chyba při čtení souboru stolů: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Chyba formátu čísla v CSV stolů.");
        }
    }

    public void importGames(String filePath) {
        GameRepository repo = new GameRepository();
        int count = 0;

        System.out.println("--- Začínám import her ---");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 3) {
                    try {
                        String name = parts[0].trim();
                        GameGenre genre = GameGenre.valueOf(parts[1].trim().toUpperCase());
                        float price = Float.parseFloat(parts[2].trim());

                        repo.save(new Game(name, genre, price, true));
                        count++;
                    } catch (Exception e) {
                        System.err.println("Chyba dat na řádku: " + line + " (" + e.getMessage() + ")");
                    }
                }
            }
            System.out.println("Import dokončen. Úspěšně vloženo: " + count);
        } catch (IOException e) {
            System.err.println("Chyba při čtení souboru: " + e.getMessage());
        }
    }
}