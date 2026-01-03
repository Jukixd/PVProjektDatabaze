package boardgamecafe.service;

import boardgamecafe.DatabaseConnection;

import java.sql.*;
import java.util.List;

public class RentalService {

    public boolean createRental(int customerId, List<Integer> gameIds) {
        Connection conn = null;
        PreparedStatement stmtRental = null;
        PreparedStatement stmtItem = null;
        PreparedStatement stmtPrice = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            float totalPrice = 0;
            String priceSql = "SELECT rental_price FROM game WHERE id = ?";
            stmtPrice = conn.prepareStatement(priceSql);

            for (Integer gameId : gameIds) {
                stmtPrice.setInt(1, gameId);
                ResultSet rs = stmtPrice.executeQuery();
                if (rs.next()) {
                    totalPrice += rs.getFloat("rental_price");
                }
            }

            String rentalSql = "INSERT INTO rental (customer_id, total_price, rental_date) VALUES (?, ?, NOW())";
            stmtRental = conn.prepareStatement(rentalSql, Statement.RETURN_GENERATED_KEYS);
            stmtRental.setInt(1, customerId);
            stmtRental.setFloat(2, totalPrice);
            stmtRental.executeUpdate();

            int rentalId = -1;
            ResultSet generatedKeys = stmtRental.getGeneratedKeys();
            if (generatedKeys.next()) {
                rentalId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("nepodařilo se vytvořit výpůjčku ->> chybí ID.");
            }

            String itemSql = "INSERT INTO rental_item (rental_id, game_id) VALUES (?, ?)";
            stmtItem = conn.prepareStatement(itemSql);

            for (Integer gameId : gameIds) {
                stmtItem.setInt(1, rentalId);
                stmtItem.setInt(2, gameId);
                stmtItem.addBatch();
            }
            stmtItem.executeBatch();

            conn.commit();
            System.out.println("Transakce OK: Výpůjčka č." + rentalId + " byla vytvořena za " + totalPrice + " Kč.");
            return true;

        } catch (SQLException e) {
            System.err.println("Chyba transakce! Vracím změny zpět (Rollback). " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void printAllRentals() {
        System.out.println("--- SEZNAM VÝPŮJČEK ---");
        String sqlRental = "SELECT r.id, r.rental_date, r.total_price, c.name " +
                "FROM rental r " +
                "JOIN customer c ON r.customer_id = c.id " +
                "ORDER BY r.id DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlRental)) {

            boolean hasRentals = false;
            while (rs.next()) {
                hasRentals = true;
                int rentalId = rs.getInt("id");
                System.out.printf("Výpůjčka #%d | %s | %s | Celkem: %.2f Kč%n",
                        rentalId,
                        rs.getString("name"),
                        rs.getTimestamp("rental_date"),
                        rs.getFloat("total_price"));

                printGamesForRental(conn, rentalId);
                System.out.println("--------------------------------------------------");
            }

            if (!hasRentals) {
                System.out.println("Žádné výpůjčky v databázi.");
            }

        } catch (SQLException e) {
            System.err.println("Chyba při výpisu výpůjček: " + e.getMessage());
        }
    }

    private void printGamesForRental(Connection conn, int rentalId) throws SQLException {
        String sqlItems = "SELECT g.name FROM rental_item ri " +
                "JOIN game g ON ri.game_id = g.id " +
                "WHERE ri.rental_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sqlItems)) {
            stmt.setInt(1, rentalId);
            ResultSet rs = stmt.executeQuery();
            System.out.print("   Hry: ");
            boolean first = true;
            while (rs.next()) {
                if (!first) System.out.print(", ");
                System.out.print(rs.getString("name"));
                first = false;
            }
            System.out.println();
        }
    }
}