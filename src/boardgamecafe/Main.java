package boardgamecafe;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection db = DatabaseConnection.getInstance();

        if (db.getConnection() != null) {
            System.out.println("Test spojení OK.");
        }
    }
}