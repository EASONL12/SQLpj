package com.shippingapp.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import com.shippingapp.util.DBUtil;

public class ReportPanel extends JPanel {

    private JTabbedPane tabbedPane;
    private DefaultTableModel shipModel;
    private DefaultTableModel routeModel;
    private DefaultTableModel crewModel;

    private JTable shipTable;
    private JTable routeTable;
    private JTable crewTable;

    public ReportPanel() {
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        shipTable = new JTable();
        routeTable = new JTable();
        crewTable = new JTable();

        tabbedPane.addTab("船舶次数", createShipUsagePanel());
        tabbedPane.addTab("航线执行", createRouteExecutionPanel());
        tabbedPane.addTab("船员出勤", createCrewAttendancePanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ------------------ 船舶利用率 ------------------
    private JPanel createShipUsagePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"船舶编号", "船名", "出航次数"};
        shipModel = new DefaultTableModel(columns, 0);
        shipTable.setModel(shipModel);
        shipTable.setRowHeight(25);

        panel.add(createToolbar(shipTable, "船舶利用率"), BorderLayout.NORTH);
        panel.add(new JScrollPane(shipTable), BorderLayout.CENTER);

        loadShipData();
        return panel;
    }

    private void loadShipData() {
        shipModel.setRowCount(0); // 清空表格
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT s.ship_id, s.name, COUNT(v.voyage_id) AS voyage_count " +
                    "FROM Ship s LEFT JOIN Voyage v ON s.ship_id=v.ship_id " +
                    "GROUP BY s.ship_id, s.name";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    shipModel.addRow(new Object[]{
                            rs.getInt("ship_id"),
                            rs.getString("name"),
                            rs.getInt("voyage_count")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ------------------ 航线执行 ------------------
    private JPanel createRouteExecutionPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"航线编号", "起始港", "目的港", "执行次数"};
        routeModel = new DefaultTableModel(columns, 0);
        routeTable.setModel(routeModel);
        routeTable.setRowHeight(25);

        panel.add(createToolbar(routeTable, "航线执行"), BorderLayout.NORTH);
        panel.add(new JScrollPane(routeTable), BorderLayout.CENTER);

        loadRouteData();
        return panel;
    }

    private void loadRouteData() {
        routeModel.setRowCount(0);
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT r.route_id, sp.name AS start_port, ep.name AS end_port, COUNT(v.voyage_id) AS execute_count " +
                    "FROM Route r " +
                    "LEFT JOIN Port sp ON r.start_port_id=sp.port_id " +
                    "LEFT JOIN Port ep ON r.end_port_id=ep.port_id " +
                    "LEFT JOIN Voyage v ON r.route_id=v.route_id " +
                    "GROUP BY r.route_id, sp.name, ep.name";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    routeModel.addRow(new Object[]{
                            rs.getInt("route_id"),
                            rs.getString("start_port"),
                            rs.getString("end_port"),
                            rs.getInt("execute_count")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ------------------ 船员出勤 ------------------
    private JPanel createCrewAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"船员编号", "姓名", "岗位", "出勤次数"};
        crewModel = new DefaultTableModel(columns, 0);
        crewTable.setModel(crewModel);
        crewTable.setRowHeight(25);

        panel.add(createToolbar(crewTable, "船员出勤"), BorderLayout.NORTH);
        panel.add(new JScrollPane(crewTable), BorderLayout.CENTER);

        loadCrewData();
        return panel;
    }

    private void loadCrewData() {
        crewModel.setRowCount(0);
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT c.crew_id, c.name, c.role, COUNT(vc.voyage_id) AS attendance_count " +
                    "FROM Crew c LEFT JOIN Voyage_Crew vc ON c.crew_id=vc.crew_id " +
                    "GROUP BY c.crew_id, c.name, c.role";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    crewModel.addRow(new Object[]{
                            rs.getInt("crew_id"),
                            rs.getString("name"),
                            rs.getString("role"),
                            rs.getInt("attendance_count")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ------------------ 工具栏（刷新 + 导出） ------------------
    private JPanel createToolbar(JTable table, String title) {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("刷新");
        JButton exportBtn = new JButton("导出 CSV");

        refreshBtn.addActionListener(e -> {
            switch (title) {
                case "船舶利用率": loadShipData(); break;
                case "航线执行": loadRouteData(); break;
                case "船员出勤": loadCrewData(); break;
            }
        });

        exportBtn.addActionListener(e -> exportTableToCSV(table, title));

        toolbar.add(refreshBtn);
        toolbar.add(exportBtn);
        return toolbar;
    }

    // ------------------ CSV 导出方法 ------------------
    private void exportTableToCSV(JTable table, String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存 " + title + " 为 CSV");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (FileWriter csv = new FileWriter(fileChooser.getSelectedFile() + ".csv")) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                // 写表头
                for (int i = 0; i < model.getColumnCount(); i++) {
                    csv.write(model.getColumnName(i));
                    if (i != model.getColumnCount() - 1) csv.write(",");
                }
                csv.write("\n");

                // 写数据
                for (int r = 0; r < model.getRowCount(); r++) {
                    for (int c = 0; c < model.getColumnCount(); c++) {
                        csv.write(model.getValueAt(r, c).toString());
                        if (c != model.getColumnCount() - 1) csv.write(",");
                    }
                    csv.write("\n");
                }
                JOptionPane.showMessageDialog(this, title + " 已成功导出 CSV！");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "导出失败：" + ex.getMessage());
            }
        }
    }
}
