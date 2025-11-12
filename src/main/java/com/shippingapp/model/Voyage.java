package com.shippingapp.model;

import java.util.Date;

public class Voyage {
    private int voyageId;
    private int shipId;
    private int routeId;
    private Date departureTime;
    private Date arrivalTime;
    private Date actualArrivalTime;

    // 关联信息
    private String shipName;
    private String shipType;
    private String departurePort;
    private String arrivalPort;
    private String routeInfo;

    public Voyage() {}

    // getter 和 setter
    public int getVoyageId() { return voyageId; }
    public void setVoyageId(int voyageId) { this.voyageId = voyageId; }

    public int getShipId() { return shipId; }
    public void setShipId(int shipId) { this.shipId = shipId; }

    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }

    public Date getDepartureTime() { return departureTime; }
    public void setDepartureTime(Date departureTime) { this.departureTime = departureTime; }

    public Date getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(Date arrivalTime) { this.arrivalTime = arrivalTime; }

    public Date getActualArrivalTime() { return actualArrivalTime; }
    public void setActualArrivalTime(Date actualArrivalTime) { this.actualArrivalTime = actualArrivalTime; }

    public String getShipName() { return shipName; }
    public void setShipName(String shipName) { this.shipName = shipName; }

    public String getShipType() { return shipType; }
    public void setShipType(String shipType) { this.shipType = shipType; }

    public String getDeparturePort() { return departurePort; }
    public void setDeparturePort(String departurePort) { this.departurePort = departurePort; }

    public String getArrivalPort() { return arrivalPort; }
    public void setArrivalPort(String arrivalPort) { this.arrivalPort = arrivalPort; }

    public String getRouteInfo() { return routeInfo; }
    public void setRouteInfo(String routeInfo) { this.routeInfo = routeInfo; }
}
