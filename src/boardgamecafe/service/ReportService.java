package boardgamecafe.service;

import boardgamecafe.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ReportService {


    public void createViews() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            String sqlView1 = "CREATE OR REPLACE VIEW view_customer_stats AS " +
                    "SELECT c.name, " +
                    "COUNT(DISTINCT r.id) as rental_count, " +
                    "COUNT(ri.id) as games_count, " +
                    "COALESCE(SUM(r.total_price), 0) as total_spent " +
                    "FROM customer c " +
                    "LEFT JOIN rental r ON c.id = r.customer_id " +
                    "LEFT JOIN rental_item ri ON r.id = ri.rental_id " +
                    "GROUP BY c.id, c.name " +
                    "ORDER BY total_spent DESC";
            stmt.executeUpdate(sqlView1);

            String sqlView2 = "CREATE OR REPLACE VIEW view_genre_stats AS " +
                    "SELECT g.genre, COUNT(ri.id) as rental_count, COALESCE(AVG(g.rental_price), 0) as avg_price " +
                    "FROM game g " +
                    "LEFT JOIN rental_item ri ON g.id = ri.game_id " +
                    "GROUP BY g.genre " +
                    "ORDER BY rental_count DESC";
            stmt.executeUpdate(sqlView2);

            System.out.println("[Systém] SQL Views (3-table join) aktualizovány.");

        } catch (Exception e) {
            System.err.println("Chyba Views: " + e.getMessage());
        }
    }
    public void printGeneralReport() {
        System.out.println("\n-----------------------------------------");
        System.out.println("       BOARD GAME CAFE - DENNÍ REPORT       ");
        System.out.println("-----------------------------------------");

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("\n--- TOP ZÁKAZNÍCI ---");
            System.out.printf("%-20s | %-10s | %-10s%n", "Jméno", "Výpůjček", "Utraceno");
            System.out.println("----------------------------------------------");

            ResultSet rs1 = stmt.executeQuery("SELECT * FROM view_customer_stats");
            while (rs1.next()) {
                System.out.printf("%-20s | %-10d | %-10.2f Kč%n",
                        rs1.getString("name"),
                        rs1.getInt("rental_count"),
                        rs1.getDouble("total_spent"));
            }

            System.out.println("\n--- STATISTIKA ŽÁNRŮ ---");
            System.out.printf("%-15s | %-10s | %-15s%n", "Žánr", "Půjčeno", "Průměrná cena");
            System.out.println("----------------------------------------------");

            ResultSet rs2 = stmt.executeQuery("SELECT * FROM view_genre_stats");
            while (rs2.next()) {
                System.out.printf("%-15s | %-10d | %-10.2f Kč%n",
                        rs2.getString("genre"),
                        rs2.getInt("rental_count"),
                        rs2.getDouble("avg_price"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("----------------------------------------\n");
    }
}