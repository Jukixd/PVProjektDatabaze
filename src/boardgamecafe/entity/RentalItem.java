package boardgamecafe.entity;

public class RentalItem {
    private Integer id;
    private int rentalId;
    private int gameId;

    public RentalItem() {}

    public RentalItem(int rentalId, int gameId) {
        this.rentalId = rentalId;
        this.gameId = gameId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }
    public int getGameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }
}