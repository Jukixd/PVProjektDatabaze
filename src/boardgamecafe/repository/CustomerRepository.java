package boardgamecafe.repository;

import boardgamecafe.DatabaseConnection;
import boardgamecafe.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements CrudRepository<Customer> {

    @Override
    public Integer save(Customer customer) {
        // SQL dotaz s otazníky (prevence proti hacknutí SQL injection)
        String sql = "INSERT INTO customer (name, email, phone) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // Získání vygenerovaného ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Chyba při ukládání zákazníka: " + e.getMessage());
        }
        return null; // Chyba
    }

    @Override
    public Customer findById(int id) {
        String sql = "SELECT * FROM customer WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Mapování z tabulky do objektu
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            System.err.println("Chyba při hledání zákazníka: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Chyba při načítání seznamu zákazníků: " + e.getMessage());
        }
        return customers;
    }

    @Override
    public boolean update(Customer customer) {
        String sql = "UPDATE customer SET name = ?, email = ?, phone = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());
            stmt.setInt(4, customer.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Chyba při aktualizaci: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM customer WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Chyba při mazání: " + e.getMessage());
        }
        return false;
    }
}