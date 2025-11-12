// CrewDAO.java
package com.shippingapp.dao;

import com.shippingapp.model.Crew;
import com.shippingapp.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrewDAO {

    // 添加船员
    public boolean addCrew(Crew crew) {
        String sql = "INSERT INTO Crew (name, role, phone) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, crew.getName());
            pstmt.setString(2, crew.getRole());
            pstmt.setString(3, crew.getPhone());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取所有船员
    public List<Crew> getAllCrew() {
        List<Crew> crewList = new ArrayList<>();
        String sql = "SELECT * FROM Crew ORDER BY name";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Crew crew = extractCrewFromResultSet(rs);
                crewList.add(crew);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return crewList;
    }

    // 更新船员信息
    public boolean updateCrew(Crew crew) {
        String sql = "UPDATE Crew SET name = ?, role = ?, phone = ? WHERE crew_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, crew.getName());
            pstmt.setString(2, crew.getRole());
            pstmt.setString(3, crew.getPhone());
            pstmt.setInt(4, crew.getCrewId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除船员
    public boolean deleteCrew(int crewId) {
        String sql = "DELETE FROM Crew WHERE crew_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, crewId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取岗位分布统计
    public Map<String, Integer> getRoleDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        String sql = "SELECT role, COUNT(*) as count FROM Crew GROUP BY role ORDER BY count DESC";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                distribution.put(rs.getString("role"), rs.getInt("count"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distribution;
    }

    // 根据角色筛选船员
    public List<Crew> getCrewByRole(String role) {
        List<Crew> crewList = new ArrayList<>();
        String sql = "SELECT * FROM Crew WHERE role = ? ORDER BY name";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, role);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Crew crew = extractCrewFromResultSet(rs);
                crewList.add(crew);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return crewList;
    }

    // 搜索船员（按姓名）
    public List<Crew> searchCrewByName(String name) {
        List<Crew> crewList = new ArrayList<>();
        String sql = "SELECT * FROM Crew WHERE name LIKE ? ORDER BY name";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Crew crew = extractCrewFromResultSet(rs);
                crewList.add(crew);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return crewList;
    }

    private Crew extractCrewFromResultSet(ResultSet rs) throws SQLException {
        Crew crew = new Crew();
        crew.setCrewId(rs.getInt("crew_id"));
        crew.setName(rs.getString("name"));
        crew.setRole(rs.getString("role"));
        crew.setPhone(rs.getString("phone"));
        return crew;
    }
}
