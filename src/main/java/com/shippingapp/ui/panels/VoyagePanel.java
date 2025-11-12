package com.shippingapp.ui.panels;

import com.shippingapp.model.Voyage;
import com.shippingapp.dao.VoyageDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VoyagePanel extends JPanel {
    private JTable voyageTable;
    private DefaultTableModel tableModel;
    private VoyageDAO voyageDAO;
    private JTabbedPane tabbedPane;

    public VoyagePanel() {
        voyageDAO = new VoyageDAO();
        initializeUI();
        loadVoyages();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // 创建选项卡
        tabbedPane = new JTabbedPane();

        // 选项卡1：所有航次
        tabbedPane.addTab("所有航次", createAllVoyagesPanel());

        // 选项卡2：历史航次
        tabbedPane.addTab("历史航次", createHistoryVoyagesPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createAllVoyagesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 顶部按钮面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("安排新航次");
        JButton refreshButton = new JButton("刷新");

        topPanel.add(addButton);
        topPanel.add(refreshButton);

        // 表格
        String[] columnNames = {"航次ID", "船舶", "船舶类型", "航线", "计划离港", "计划到港", "实际到港"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        voyageTable = new JTable(tableModel);
        voyageTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(voyageTable);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 事件监听
        addButton.addActionListener(e -> showAddVoyageDialog());
        refreshButton.addActionListener(e -> loadVoyages());

        return panel;
    }

    private JPanel createHistoryVoyagesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshHistoryButton = new JButton("刷新历史记录");
        topPanel.add(refreshHistoryButton);

        String[] columnNames = {"航次ID", "船舶", "航线", "离港时间", "计划到港", "实际到港"};
        DefaultTableModel historyTableModel = new DefaultTableModel(columnNames, 0);
        JTable historyTable = new JTable(historyTableModel);
        historyTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(historyTable);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshHistoryButton.addActionListener(e -> loadHistoryVoyages(historyTableModel));
        loadHistoryVoyages(historyTableModel);

        return panel;
    }

    private void loadVoyages() {
        tableModel.setRowCount(0);
        List<Voyage> voyages = voyageDAO.getAllVoyages();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Voyage voyage : voyages) {
            Object[] rowData = {
                    voyage.getVoyageId(),
                    voyage.getShipName(),
                    voyage.getShipType(),
                    voyage.getRouteInfo(),
                    voyage.getDepartureTime() != null ? sdf.format(voyage.getDepartureTime()) : "",
                    voyage.getArrivalTime() != null ? sdf.format(voyage.getArrivalTime()) : "",
                    voyage.getActualArrivalTime() != null ? sdf.format(voyage.getActualArrivalTime()) : "未到达"
            };
            tableModel.addRow(rowData);
        }
    }

    private void loadHistoryVoyages(DefaultTableModel historyTableModel) {
        historyTableModel.setRowCount(0);
        List<Voyage> historyVoyages = voyageDAO.getHistoryVoyages();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Voyage voyage : historyVoyages) {
            Object[] rowData = {
                    voyage.getVoyageId(),
                    voyage.getShipName(),
                    voyage.getRouteInfo(),
                    voyage.getDepartureTime() != null ? sdf.format(voyage.getDepartureTime()) : "",
                    voyage.getArrivalTime() != null ? sdf.format(voyage.getArrivalTime()) : "",
                    voyage.getActualArrivalTime() != null ? sdf.format(voyage.getActualArrivalTime()) : ""
            };
            historyTableModel.addRow(rowData);
        }
    }

    private void showAddVoyageDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "安排新航次", true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));

        // 表单组件
        dialog.add(new JLabel("选择船舶:"));
        JComboBox<String> shipComboBox = new JComboBox<>();
        dialog.add(shipComboBox);

        dialog.add(new JLabel("选择航线:"));
        JComboBox<String> routeComboBox = new JComboBox<>();
        dialog.add(routeComboBox);

        dialog.add(new JLabel("计划离港时间:"));
        JTextField departureField = new JTextField(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        dialog.add(departureField);

        dialog.add(new JLabel("计划到港时间:"));
        JTextField arrivalField = new JTextField();
        dialog.add(arrivalField);

        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");
        dialog.add(saveButton);
        dialog.add(cancelButton);

        // 加载数据
        loadShipsToComboBox(shipComboBox);
        loadRoutesToComboBox(routeComboBox);

        saveButton.addActionListener(e -> {
            if (saveVoyage(shipComboBox, routeComboBox, departureField.getText(), arrivalField.getText())) {
                dialog.dispose();
                loadVoyages();
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void loadShipsToComboBox(JComboBox<String> comboBox) {
        List<String[]> ships = voyageDAO.getAvailableShips();
        comboBox.removeAllItems();
        comboBox.addItem("请选择船舶");
        for (String[] ship : ships) {
            comboBox.addItem(ship[1] + " (" + ship[2] + ") - ID:" + ship[0]);
        }
    }

    private void loadRoutesToComboBox(JComboBox<String> comboBox) {
        List<String[]> routes = voyageDAO.getAllRoutes();
        comboBox.removeAllItems();
        comboBox.addItem("请选择航线");
        for (String[] route : routes) {
            comboBox.addItem(route[1] + " - " + route[2] + " - ID:" + route[0]);
        }
    }

    private boolean saveVoyage(JComboBox<String> shipCombo, JComboBox<String> routeCombo, String departure, String arrival) {
        String shipText = (String) shipCombo.getSelectedItem();
        String routeText = (String) routeCombo.getSelectedItem();

        if ("请选择船舶".equals(shipText) || "请选择航线".equals(routeText)) {
            JOptionPane.showMessageDialog(this, "请选择船舶和航线");
            return false;
        }

        try {
            int shipId = Integer.parseInt(shipText.split("ID:")[1]);
            int routeId = Integer.parseInt(routeText.split("ID:")[1]);

            Voyage voyage = new Voyage();
            voyage.setShipId(shipId);
            voyage.setRouteId(routeId);
            voyage.setDepartureTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(departure));
            voyage.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(arrival));

            if (voyageDAO.addVoyage(voyage)) {
                JOptionPane.showMessageDialog(this, "航次安排成功！");
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "航次安排失败！");
                return false;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "输入格式错误: " + ex.getMessage());
            return false;
        }
    }
}