package boardgamecafe;

import java.sql.Connection;
import java.sql.Statement;

public class DbSetup {
    public static void main(String[] args) {
        System.out.println("Začínám vytvářet tabulky...");

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS customer (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100) UNIQUE NOT NULL," +
                    "phone VARCHAR(20))");
            System.out.println("- Tabulka 'customer' OK");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS game (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "genre ENUM('STRATEGIE', 'RODINNA', 'PARTY', 'RPG') NOT NULL," +
                    "rental_price FLOAT NOT NULL," +
                    "is_available BOOLEAN DEFAULT TRUE)");
            System.out.println("- Tabulka 'game' OK");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cafe_table (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "seats INT NOT NULL," +
                    "location_description VARCHAR(255))");
            System.out.println("- Tabulka 'cafe_table' OK");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rental (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "customer_id INT NOT NULL," +
                    "rental_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "total_price FLOAT," +
                    "status VARCHAR(20) DEFAULT 'ACTIVE'," +
                    "FOREIGN KEY (customer_id) REFERENCES customer(id))");
            System.out.println("- Tabulka 'rental' OK");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rental_item (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "rental_id INT NOT NULL," +
                    "game_id INT NOT NULL," +
                    "FOREIGN KEY (rental_id) REFERENCES rental(id)," +
                    "FOREIGN KEY (game_id) REFERENCES game(id))");
            System.out.println("- Tabulka 'rental_item' OK");

            System.out.println("HOTOVO! Všechny tabulky jsou vytvořeny.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}