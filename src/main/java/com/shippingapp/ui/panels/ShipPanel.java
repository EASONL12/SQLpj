package main.java.com.shippingapp.ui.panels;

import main.java.com.shippingapp.dao.ShipDAO;
import main.java.com.shippingapp.model.Ship;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ShipPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ShipDAO shipDAO = new ShipDAO();

    public ShipPanel() {
        setLayout(new BorderLayout());

        // 表格列名
        String[] columns = {"ID", "船名", "类型", "载重", "状态"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        refreshTable();

        add(new JScrollPane(table), BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("新增");
        JButton editBtn = new JButton("修改");
        JButton deleteBtn = new JButton("删除");
        JButton refreshBtn = new JButton("刷新");

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // 按钮事件
        addBtn.addActionListener(e -> addShip());
        editBtn.addActionListener(e -> editShip());
        deleteBtn.addActionListener(e -> deleteShip());
        refreshBtn.addActionListener(e -> refreshTable());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Ship> ships = shipDAO.getAllShips();
        for (Ship s : ships) {
            tableModel.addRow(new Object[]{s.getShipId(), s.getName(), s.getType(), s.getCapacity(), s.getStatus()});
        }
    }

    private void addShip() {
        JTextField nameField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField capacityField = new JTextField();
        String[] statuses = {"在航", "维修", "停泊"};
        JComboBox<String> statusBox = new JComboBox<>(statuses);

        Object[] message = {
                "船名:", nameField,
                "类型:", typeField,
                "载重:", capacityField,
                "状态:", statusBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "新增船舶", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Ship ship = new Ship();
                ship.setName(nameField.getText());
                ship.setType(typeField.getText());
                ship.setCapacity(Integer.parseInt(capacityField.getText()));
                ship.setStatus((String) statusBox.getSelectedItem());
                if (shipDAO.addShip(ship)) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "添加成功！");
                } else {
                    JOptionPane.showMessageDialog(this, "添加失败！");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "载重必须是数字！");
            }
        }
    }

    private void editShip() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要修改的船舶！");
            return;
        }

        int shipId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentType = (String) tableModel.getValueAt(selectedRow, 2);
        int currentCapacity = (int) tableModel.getValueAt(selectedRow, 3);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 4);

        JTextField nameField = new JTextField(currentName);
        JTextField typeField = new JTextField(currentType);
        JTextField capacityField = new JTextField(String.valueOf(currentCapacity));
        String[] statuses = {"在航", "维修", "停泊"};
        JComboBox<String> statusBox = new JComboBox<>(statuses);
        statusBox.setSelectedItem(currentStatus);

        Object[] message = {
                "船名:", nameField,
                "类型:", typeField,
                "载重:", capacityField,
                "状态:", statusBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "修改船舶", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Ship ship = new Ship();
                ship.setShipId(shipId);
                ship.setName(nameField.getText());
                ship.setType(typeField.getText());
                ship.setCapacity(Integer.parseInt(capacityField.getText()));
                ship.setStatus((String) statusBox.getSelectedItem());
                if (shipDAO.updateShip(ship)) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "修改成功！");
                } else {
                    JOptionPane.showMessageDialog(this, "修改失败！");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "载重必须是数字！");
            }
        }
    }

    private void deleteShip() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的船舶！");
            return;
        }

        int shipId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "确定删除吗？", "删除确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (shipDAO.deleteShip(shipId)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "删除成功！");
            } else {
                JOptionPane.showMessageDialog(this, "删除失败！");
            }
        }
    }
}
