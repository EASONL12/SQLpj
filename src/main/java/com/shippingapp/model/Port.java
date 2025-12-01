package com.shippingapp.model;

public class Port {
    private int port_id;
    private String name;
    private String country;
    private String city;

    public Port(int port_id, String name, String country, String city) {
        this.port_id = port_id;
        this.name = name;
        this.country = country;
        this.city = city;
    }

    public Port() {

    }

    public int getPort_id() {
        return port_id;
    }

    public void setPort_id(int port_id) {
        this.port_id = port_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
