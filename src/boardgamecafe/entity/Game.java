package boardgamecafe.entity;

public class Game {
    private Integer id;
    private String name;
    private GameGenre genre;
    private float rentalPrice;
    private boolean isAvailable;

    public Game() {}

    public Game(String name, GameGenre genre, float rentalPrice, boolean isAvailable) {
        this.name = name;
        this.genre = genre;
        this.rentalPrice = rentalPrice;
        this.isAvailable = isAvailable;
    }

    public Game(Integer id, String name, GameGenre genre, float rentalPrice, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.rentalPrice = rentalPrice;
        this.isAvailable = isAvailable;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public GameGenre getGenre() { return genre; }
    public void setGenre(GameGenre genre) { this.genre = genre; }
    public float getRentalPrice() { return rentalPrice; }
    public void setRentalPrice(float rentalPrice) { this.rentalPrice = rentalPrice; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return name + " (" + genre + ") - " + rentalPrice + " Kč";
    }
}