package com.shippingapp.dao;

import com.shippingapp.util.DBUtil;
import java.sql.*;

public class UserDAO {
    public static boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // 有结果说明验证成功
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

