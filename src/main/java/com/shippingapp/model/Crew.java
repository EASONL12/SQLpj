// Crew.java - 在 com.shippingapp.model 包中
package com.shippingapp.model;

public class Crew {
    private int crewId;
    private String name;
    private String role;
    private String phone;
    private String status; // 新增：状态（在岗/休假/离职）

    // 构造方法
    public Crew() {}

    public Crew(String name, String role, String phone) {
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.status = "在岗";
    }

    // getter 和 setter 方法
    public int getCrewId() { return crewId; }
    public void setCrewId(int crewId) { this.crewId = crewId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}