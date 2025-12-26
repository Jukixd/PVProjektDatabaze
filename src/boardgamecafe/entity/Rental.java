package boardgamecafe.entity;

import java.sql.Timestamp;

public class Rental {
    private Integer id;
    private int customerId;
    private Timestamp rentalDate;
    private float totalPrice;
    private String status;

    public Rental() {}

    public Rental(Integer id, int customerId, Timestamp rentalDate, float totalPrice, String status) {
        this.id = id;
        this.customerId = customerId;
        this.rentalDate = rentalDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public Timestamp getRentalDate() { return rentalDate; }
    public void setRentalDate(Timestamp rentalDate) { this.rentalDate = rentalDate; }
    public float getTotalPrice() { return totalPrice; }
    public void setTotalPrice(float totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}