package com.shippingapp.ui.panels;

import com.shippingapp.dao.PortDAO;
import com.shippingapp.model.Port;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PortPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private PortDAO portDAO = new PortDAO();

    public PortPanel() {
        setLayout(new BorderLayout());

        String[] columns = {"ID", "港口名", "所在国家", "所在城市"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        refreshTable();

        add(new JScrollPane(table), BorderLayout.CENTER);

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

        addBtn.addActionListener(e -> addPort());
        editBtn.addActionListener(e -> editPort());
        deleteBtn.addActionListener(e -> deletePort());
        refreshBtn.addActionListener(e -> refreshTable());
    }


    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Port> ports = portDAO.getAllPorts();
        for (Port s : ports) {
            tableModel.addRow(new Object[]{s.getPort_id(), s.getName(), s.getCountry(), s.getCity()});
        }
    }

    private void addPort() {
        JTextField nameField = new JTextField();
        JTextField countryField = new JTextField();
        JTextField cityField = new JTextField();

        Object[] message = {
                "港口名:", nameField,
                "所属国家:", countryField,
                "所属城市:", cityField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "新增港口", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Port port = new Port();
                port.setName(nameField.getText());
                port.setCountry(countryField.getText());
                port.setCity(cityField.getText());
                if (portDAO.addPort(port)) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "添加成功！");
                } else {
                    JOptionPane.showMessageDialog(this, "添加失败！");
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void editPort() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要修改的港口！");
            return;
        }


        int portId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentCountry = (String) tableModel.getValueAt(selectedRow, 2);
        String currentCity = (String) tableModel.getValueAt(selectedRow, 3);

        JTextField nameField = new JTextField(currentName);
        JTextField countryField = new JTextField(currentCountry);
        JTextField cityField = new JTextField(currentCity);

        Object[] message = {
                "港口名称:", nameField,
                "所属国家:", countryField,
                "所在城市:", cityField
        };


        int option = JOptionPane.showConfirmDialog(this, message, "修改港口", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Port port = new Port();
                port.setPort_id(portId);
                port.setName(nameField.getText());
                port.setCountry(countryField.getText());
                port.setCity(cityField.getText());

                if (portDAO.updatePort(port)) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "修改成功！");
                } else {
                    JOptionPane.showMessageDialog(this, "修改失败！");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "修改出错：" + ex.getMessage());
            }
        }
    }
    private void deletePort() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的港口！");
            return;
        }

        int portId = (int) tableModel.getValueAt(selectedRow, 0);
        String portName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要删除港口【" + portName + "】吗？\n此操作不可撤销！",
                "删除确认",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (portDAO.deletePort(portId)) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "港口删除成功！");
                } else {
                    JOptionPane.showMessageDialog(this, "港口删除失败！\n可能该港口正在被使用或不存在。");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "删除出错：" + ex.getMessage());
            }
        }
    }
}
