// VoyageCrewDAO.java - 在 com.shippingapp.dao 包中
package com.shippingapp.dao;

import com.shippingapp.model.VoyageCrew;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoyageCrewDAO {
    private Connection connection;

    public VoyageCrewDAO(Connection connection) {
        this.connection = connection;
    }

    // 添加船员分配
    public boolean addVoyageCrew(VoyageCrew voyageCrew) throws SQLException {
        String sql = "INSERT INTO Voyage_Crew (voyage_id, crew_id, duty) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, voyageCrew.getVoyageId());
            stmt.setInt(2, voyageCrew.getCrewId());
            stmt.setString(3, voyageCrew.getDuty());
            return stmt.executeUpdate() > 0;
        }
    }

    // 根据航班ID和船员ID删除分配
    public boolean deleteVoyageCrew(int voyageId, int crewId) throws SQLException {
        String sql = "DELETE FROM Voyage_Crew WHERE voyage_id = ? AND crew_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, voyageId);
            stmt.setInt(2, crewId);
            return stmt.executeUpdate() > 0;
        }
    }

    // 更新船员职责
    public boolean updateVoyageCrew(VoyageCrew voyageCrew) throws SQLException {
        String sql = "UPDATE Voyage_Crew SET duty = ? WHERE voyage_id = ? AND crew_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, voyageCrew.getDuty());
            stmt.setInt(2, voyageCrew.getVoyageId());
            stmt.setInt(3, voyageCrew.getCrewId());
            return stmt.executeUpdate() > 0;
        }
    }

    // 根据航班ID和船员ID查询
    public VoyageCrew getVoyageCrew(int voyageId, int crewId) throws SQLException {
        String sql = "SELECT * FROM Voyage_Crew WHERE voyage_id = ? AND crew_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, voyageId);
            stmt.setInt(2, crewId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new VoyageCrew(
                        rs.getInt("voyage_id"),
                        rs.getInt("crew_id"),
                        rs.getString("duty")
                );
            }
            return null;
        }
    }

    // 根据航班ID查询所有船员分配
    public List<VoyageCrew> getCrewByVoyage(int voyageId) throws SQLException {
        List<VoyageCrew> crewList = new ArrayList<>();
        String sql = "SELECT * FROM Voyage_Crew WHERE voyage_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, voyageId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                crewList.add(new VoyageCrew(
                        rs.getInt("voyage_id"),
                        rs.getInt("crew_id"),
                        rs.getString("duty")
                ));
            }
        }
        return crewList;
    }

    // 根据船员ID查询所有航班分配
    public List<VoyageCrew> getVoyagesByCrew(int crewId) throws SQLException {
        List<VoyageCrew> voyageList = new ArrayList<>();
        String sql = "SELECT * FROM Voyage_Crew WHERE crew_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, crewId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                voyageList.add(new VoyageCrew(
                        rs.getInt("voyage_id"),
                        rs.getInt("crew_id"),
                        rs.getString("duty")
                ));
            }
        }
        return voyageList;
    }

    // 查询所有船员分配
    public List<VoyageCrew> getAllVoyageCrew() throws SQLException {
        List<VoyageCrew> voyageCrewList = new ArrayList<>();
        String sql = "SELECT * FROM Voyage_Crew";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                voyageCrewList.add(new VoyageCrew(
                        rs.getInt("voyage_id"),
                        rs.getInt("crew_id"),
                        rs.getString("duty")
                ));
            }
        }
        return voyageCrewList;
    }
}