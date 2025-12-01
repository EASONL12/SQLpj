package com.shippingapp.model;

public class Route {
    private int routeId;
    private int startPortId;
    private int endPortId;
    private int distance;

    public Route(){}

    public Route(int routeId, int startPortId, int endPortId, int distance) {
        this.routeId = routeId;
        this.startPortId = startPortId;
        this.endPortId = endPortId;
        this.distance = distance;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getStartPortId() {
        return startPortId;
    }

    public void setStartPortId(int startPortId) {
        this.startPortId = startPortId;
    }

    public int getEndPortId() {
        return endPortId;
    }

    public void setEndPortId(int endPortId) {
        this.endPortId = endPortId;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
