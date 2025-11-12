// VoyageCrew.java - 在 com.shippingapp.model 包中
package com.shippingapp.model;

public class VoyageCrew {
    private int voyageId;
    private int crewId;
    private String duty; // 具体职责

    // 构造方法
    public VoyageCrew() {}

    public VoyageCrew(int voyageId, int crewId, String duty) {
        this.voyageId = voyageId;
        this.crewId = crewId;
        this.duty = duty;
    }

    // getter 和 setter 方法
    public int getVoyageId() { return voyageId; }
    public void setVoyageId(int voyageId) { this.voyageId = voyageId; }

    public int getCrewId() { return crewId; }
    public void setCrewId(int crewId) { this.crewId = crewId; }

    public String getDuty() { return duty; }
    public void setDuty(String duty) { this.duty = duty; }
}