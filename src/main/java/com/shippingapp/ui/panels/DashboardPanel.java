package com.shippingapp.ui.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

import com.shippingapp.util.DBUtil;

public class DashboardPanel extends JPanel {

    private JLabel shipCountLabel;
    private JLabel routeCountLabel;
    private JLabel voyageCountLabel;
    private JTextArea announcementArea;

    public DashboardPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        // 顶部标题
        JLabel titleLabel = new JLabel("航运管理系统 - 控制面板", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 66, 99));
        add(titleLabel, BorderLayout.NORTH);

        // 中部统计卡片区
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);

        shipCountLabel = createStatCard("船舶总数", "0", new Color(52, 152, 219));
        routeCountLabel = createStatCard("航线数量", "0", new Color(46, 204, 113));
        voyageCountLabel = createStatCard("航次数量", "0", new Color(241, 196, 15));

        statsPanel.add(shipCountLabel.getParent());
        statsPanel.add(routeCountLabel.getParent());
        statsPanel.add(voyageCountLabel.getParent());

        add(statsPanel, BorderLayout.CENTER);

        // 底部公告区
        announcementArea = new JTextArea("欢迎使用航运管理系统！\n系统公告将在此显示。");
        announcementArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        announcementArea.setForeground(Color.DARK_GRAY);
        announcementArea.setEditable(false);
        announcementArea.setBorder(BorderFactory.createTitledBorder("系统公告"));
        announcementArea.setBackground(new Color(250, 250, 250));

        add(announcementArea, BorderLayout.SOUTH);

        loadStatistics();
    }

    private JLabel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        card.setPreferredSize(new Dimension(200, 100));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setForeground(Color.GRAY);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("微软雅黑", Font.BOLD, 26));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        this.add(card);
        return valueLabel;
    }

    private void loadStatistics() {
        try (Connection conn = DBUtil.getConnection()) {
            shipCountLabel.setText(String.valueOf(getCount(conn, "Ship")));
            routeCountLabel.setText(String.valueOf(getCount(conn, "Route")));
            voyageCountLabel.setText(String.valueOf(getCount(conn, "Voyage")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getCount(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
}
