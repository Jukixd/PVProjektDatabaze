package boardgamecafe.repository;

import boardgamecafe.DatabaseConnection;
import boardgamecafe.entity.Game;
import boardgamecafe.entity.GameGenre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameRepository implements CrudRepository<Game> {

    @Override
    public Integer save(Game game) {
        String sql = "INSERT INTO game (name, genre, rental_price, is_available) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, game.getName());
            stmt.setString(2, game.getGenre().name());
            stmt.setFloat(3, game.getRentalPrice());
            stmt.setBoolean(4, game.isAvailable());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) return generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Chyba při ukládání hry: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Game findById(int id) {
        String sql = "SELECT * FROM game WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return mapRowToGame(rs);

        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Game> findAll() {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM game";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) games.add(mapRowToGame(rs));

        } catch (SQLException e) { e.printStackTrace(); }
        return games;
    }

    @Override
    public boolean update(Game game) {
        String sql = "UPDATE game SET name=?, genre=?, rental_price=?, is_available=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, game.getName());
            stmt.setString(2, game.getGenre().name());
            stmt.setFloat(3, game.getRentalPrice());
            stmt.setBoolean(4, game.isAvailable());
            stmt.setInt(5, game.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM game WHERE id=?")) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Game mapRowToGame(ResultSet rs) throws SQLException {
        return new Game(
                rs.getInt("id"),
                rs.getString("name"),
                GameGenre.valueOf(rs.getString("genre")), // String -> Enum
                rs.getFloat("rental_price"),
                rs.getBoolean("is_available")
        );
    }
}