package boardgamecafe.service;

import boardgamecafe.entity.Customer;
import boardgamecafe.entity.Game;
import boardgamecafe.entity.GameGenre;
import boardgamecafe.repository.CustomerRepository;
import boardgamecafe.repository.GameRepository;

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

    // Import her
    public void importGames(String filePath) {
        GameRepository repo = new GameRepository();
        int count = 0;

        System.out.println("--- Začínám import her ---");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Rozdělení: "Název,Žánr,Cena"
                String[] parts = line.split(",");

                if (parts.length >= 3) {
                    try {
                        String name = parts[0].trim();
                        // Převedeme text (např. "RPG") na Enum
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