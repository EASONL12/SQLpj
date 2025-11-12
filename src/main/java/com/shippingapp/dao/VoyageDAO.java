package com.shippingapp.dao;

import com.shippingapp.model.Voyage;
import com.shippingapp.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoyageDAO {

    // 安排新航次
    public boolean addVoyage(Voyage voyage) {
        String sql = "INSERT INTO Voyage (ship_id, route_id, departure_time, arrival_time) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, voyage.getShipId());
            pstmt.setInt(2, voyage.getRouteId());
            pstmt.setTimestamp(3, new Timestamp(voyage.getDepartureTime().getTime()));
            pstmt.setTimestamp(4, new Timestamp(voyage.getArrivalTime().getTime()));

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取所有航次
    public List<Voyage> getAllVoyages() {
        List<Voyage> voyages = new ArrayList<>();
        String sql = "SELECT v.*, s.name as ship_name, s.type as ship_type, " +
                "p1.name as departure_port, p2.name as arrival_port " +
                "FROM Voyage v " +
                "JOIN Ship s ON v.ship_id = s.ship_id " +
                "JOIN Route r ON v.route_id = r.route_id " +
                "JOIN Port p1 ON r.start_port_id = p1.port_id " +
                "JOIN Port p2 ON r.end_port_id = p2.port_id " +
                "ORDER BY v.departure_time DESC";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Voyage voyage = extractVoyageFromResultSet(rs);
                voyages.add(voyage);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voyages;
    }

    // 获取历史航次（已完成的）
    public List<Voyage> getHistoryVoyages() {
        List<Voyage> voyages = new ArrayList<>();
        String sql = "SELECT v.*, s.name as ship_name, s.type as ship_type, " +
                "p1.name as departure_port, p2.name as arrival_port " +
                "FROM Voyage v " +
                "JOIN Ship s ON v.ship_id = s.ship_id " +
                "JOIN Route r ON v.route_id = r.route_id " +
                "JOIN Port p1 ON r.start_port_id = p1.port_id " +
                "JOIN Port p2 ON r.end_port_id = p2.port_id " +
                "WHERE v.actual_arrival_time IS NOT NULL " +
                "ORDER BY v.actual_arrival_time DESC";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Voyage voyage = extractVoyageFromResultSet(rs);
                voyages.add(voyage);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voyages;
    }

    // 获取可用船舶
    public List<String[]> getAvailableShips() {
        List<String[]> ships = new ArrayList<>();
        String sql = "SELECT ship_id, name, type FROM Ship WHERE status = '在航' OR status = '停泊'";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String[] ship = {
                        String.valueOf(rs.getInt("ship_id")),
                        rs.getString("name"),
                        rs.getString("type")
                };
                ships.add(ship);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ships;
    }

    // 获取所有航线
    public List<String[]> getAllRoutes() {
        List<String[]> routes = new ArrayList<>();
        String sql = "SELECT r.route_id, p1.name as start_port, p2.name as end_port, r.distance " +
                "FROM Route r " +
                "JOIN Port p1 ON r.start_port_id = p1.port_id " +
                "JOIN Port p2 ON r.end_port_id = p2.port_id";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String[] route = {
                        String.valueOf(rs.getInt("route_id")),
                        rs.getString("start_port") + " → " + rs.getString("end_port"),
                        rs.getString("distance") + "公里"
                };
                routes.add(route);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }

    // 根据航次ID获取航次信息
    public Voyage getVoyageById(int voyageId) {
        String sql = "SELECT v.*, s.name as ship_name, s.type as ship_type, " +
                "p1.name as departure_port, p2.name as arrival_port " +
                "FROM Voyage v " +
                "JOIN Ship s ON v.ship_id = s.ship_id " +
                "JOIN Route r ON v.route_id = r.route_id " +
                "JOIN Port p1 ON r.start_port_id = p1.port_id " +
                "JOIN Port p2 ON r.end_port_id = p2.port_id " +
                "WHERE v.voyage_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, voyageId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractVoyageFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Voyage extractVoyageFromResultSet(ResultSet rs) throws SQLException {
        Voyage voyage = new Voyage();
        voyage.setVoyageId(rs.getInt("voyage_id"));
        voyage.setShipId(rs.getInt("ship_id"));
        voyage.setRouteId(rs.getInt("route_id"));
        voyage.setDepartureTime(rs.getTimestamp("departure_time"));
        voyage.setArrivalTime(rs.getTimestamp("arrival_time"));
        voyage.setActualArrivalTime(rs.getTimestamp("actual_arrival_time"));

        // 关联信息
        voyage.setShipName(rs.getString("ship_name"));
        voyage.setShipType(rs.getString("ship_type"));
        voyage.setDeparturePort(rs.getString("departure_port"));
        voyage.setArrivalPort(rs.getString("arrival_port"));
        voyage.setRouteInfo(voyage.getDeparturePort() + " → " + voyage.getArrivalPort());

        return voyage;
    }
}