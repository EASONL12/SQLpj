package com.shippingapp.dao;

import com.shippingapp.model.Route;
import com.shippingapp.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO {
    // 查询所有航线
    public List<Route> getAllRoutes() {
        List<Route> routes = new ArrayList<>();
        String sql = "SELECT * FROM Route";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Route route = new Route(
                        rs.getInt("route_id"),
                        rs.getInt("start_port_id"),
                        rs.getInt("end_port_id"),
                        rs.getInt("distance")
                );
                routes.add(route);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }

    //新增航线
    public boolean addRoute(Route route) {
        String sql = "INSERT INTO Route(start_port_id,end_port_id,distance)VALUES(?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, route.getStartPortId());
            ps.setInt(2, route.getEndPortId());
            ps.setInt(3, route.getDistance());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改航线
    public boolean updateRoute(Route route) {
        String sql = "UPDATE Route SET start_port_id=?, end_port_id=?, distance=? WHERE route_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, route.getStartPortId());
            ps.setInt(2, route.getEndPortId());
            ps.setInt(3, route.getDistance());
            ps.setInt(4, route.getRouteId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除航线
    public boolean deleteRoute(int routeId) {
        String sql = "DELETE FROM Route WHERE route_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, routeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //根据ID查询距离
    public int getDistanceById(int routeId) {
        String sql = "SELECT distance FROM Route WHERE route_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, routeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("distance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
