package com.shippingapp.dao;

import com.shippingapp.model.Ship;
import com.shippingapp.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipDAO {

    // 查询所有船舶
    public List<Ship> getAllShips() {
        List<Ship> ships = new ArrayList<>();
        String sql = "SELECT * FROM Ship";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ship ship = new Ship(
                        rs.getInt("ship_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("capacity"),
                        rs.getString("status")
                );
                ships.add(ship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ships;
    }

    // 新增船舶
    public boolean addShip(Ship ship) {
        String sql = "INSERT INTO Ship(name, type, capacity, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ship.getName());
            ps.setString(2, ship.getType());
            ps.setInt(3, ship.getCapacity());
            ps.setString(4, ship.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改船舶
    public boolean updateShip(Ship ship) {
        String sql = "UPDATE Ship SET name=?, type=?, capacity=?, status=? WHERE ship_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ship.getName());
            ps.setString(2, ship.getType());
            ps.setInt(3, ship.getCapacity());
            ps.setString(4, ship.getStatus());
            ps.setInt(5, ship.getShipId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除船舶
    public boolean deleteShip(int shipId) {
        String sql = "DELETE FROM Ship WHERE ship_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shipId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
