package com.shippingapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    // Windows 身份验证 + 默认实例
    private static final String URL = "jdbc:sqlserver://localhost;databaseName=ShippingDB;integratedSecurity=true;encrypt=false;";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456";

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 测试连接是否成功
     */
    public static void main(String[] args) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT GETDATE() AS CurrentTime")) {

            if (rs.next()) {
                System.out.println("数据库连接成功！当前时间：" + rs.getString("CurrentTime"));
            }

        } catch (SQLException e) {
            System.err.println("数据库连接失败！");
            e.printStackTrace();
        }
    }
}
