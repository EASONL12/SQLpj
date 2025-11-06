package com.shippingapp.model;

public class Ship {
    private int shipId;
    private String name;
    private String type;
    private int capacity;
    private String status;

    public Ship() {}

    public Ship(int shipId, String name, String type, int capacity, String status) {
        this.shipId = shipId;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.status = status;
    }

    // Getter & Setter
    public int getShipId() { return shipId; }
    public void setShipId(int shipId) { this.shipId = shipId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
