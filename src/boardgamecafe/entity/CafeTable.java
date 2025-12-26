package boardgamecafe.entity;

public class CafeTable {
    private Integer id;
    private int seats;
    private String locationDescription;

    public CafeTable() {}

    public CafeTable(int seats, String locationDescription) {
        this.seats = seats;
        this.locationDescription = locationDescription;
    }

    public CafeTable(Integer id, int seats, String locationDescription) {
        this.id = id;
        this.seats = seats;
        this.locationDescription = locationDescription;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }
    public String getLocationDescription() { return locationDescription; }
    public void setLocationDescription(String locationDescription) { this.locationDescription = locationDescription; }

    @Override
    public String toString() {
        return "Stůl č." + id + " (" + seats + " míst) - " + locationDescription;
    }
}