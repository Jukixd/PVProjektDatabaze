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
}