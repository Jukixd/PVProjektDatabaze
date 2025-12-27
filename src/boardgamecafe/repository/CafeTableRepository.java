package boardgamecafe.repository;

import boardgamecafe.DatabaseConnection;
import boardgamecafe.entity.CafeTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CafeTableRepository implements CrudRepository<CafeTable> {

    @Override
    public Integer save(CafeTable table) {
        String sql = "INSERT INTO cafe_table (seats, location_description) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, table.getSeats());
            stmt.setString(2, table.getLocationDescription());

            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public CafeTable findById(int id) {
        String sql = "SELECT * FROM cafe_table WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new CafeTable(rs.getInt("id"), rs.getInt("seats"), rs.getString("location_description"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<CafeTable> findAll() {
        List<CafeTable> list = new ArrayList<>();
        String sql = "SELECT * FROM cafe_table";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new CafeTable(rs.getInt("id"), rs.getInt("seats"), rs.getString("location_description")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public boolean update(CafeTable table) {
        String sql = "UPDATE cafe_table SET seats=?, location_description=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, table.getSeats());
            stmt.setString(2, table.getLocationDescription());
            stmt.setInt(3, table.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM cafe_table WHERE id=?")) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}