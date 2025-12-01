package com.shippingapp.dao;

import com.shippingapp.model.Port;
import com.shippingapp.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PortDAO {
    // 查询所有港口
    public List<Port> getAllPorts() {
        List<Port> ports = new ArrayList<>();
        String sql = "SELECT * FROM Port";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Port port = new Port(
                        rs.getInt("port_id"),
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getString("city")
                );
                ports.add(port);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ports;
    }

    //新增港口
    public boolean addPort(Port port){
        String sql="INSERT INTO Port(name,country,city)VALUES(?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, port.getName());
            ps.setString(2, port.getCountry());
            ps.setString(3, port.getCity());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //修改港口
    public boolean updatePort(Port port){
        String sql="UPDATE Port SET name=?, country=?, city=? WHERE port_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, port.getName());
            ps.setString(2,port.getCountry());
            ps.setString(3,port.getCity());
            ps.setInt(4,port.getPort_id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //删除港口
    public boolean deletePort(int portId){
        String sql="DELETE FROM Port WHERE port_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,portId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
