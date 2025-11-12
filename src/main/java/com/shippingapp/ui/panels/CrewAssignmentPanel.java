package com.shippingapp.ui.panels;

import com.shippingapp.dao.VoyageCrewDAO;
import com.shippingapp.model.VoyageCrew;
import com.shippingapp.util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class CrewAssignmentPanel extends JPanel {

    private VoyageCrewDAO voyageCrewDAO;

    private JTable assignmentTable;
    private DefaultTableModel tableModel;
    private JTextField voyageIdField;
    private JTextField crewIdField;
    private JTextField dutyField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton searchByVoyageButton;
    private JButton searchByCrewButton;

    public CrewAssignmentPanel() {
        // DAO 自己获取数据库连接
        try {
            this.voyageCrewDAO = new VoyageCrewDAO(DBUtil.getConnection());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "数据库连接失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        initializeUI();
        loadAllAssignments();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("船员分配信息"));

        panel.add(new JLabel("航次ID:"));
        voyageIdField = new JTextField();
        panel.add(voyageIdField);

        panel.add(new JLabel("船员ID:"));
        crewIdField = new JTextField();
        panel.add(crewIdField);

        panel.add(new JLabel("职责:"));
        dutyField = new JTextField();
        panel.add(dutyField);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("船员分配列表"));

        String[] columnNames = {"航次ID", "船员ID", "职责"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        assignmentTable = new JTable(tableModel);
        assignmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && assignmentTable.getSelectedRow() != -1) {
                fillInputFieldsFromTable(assignmentTable.getSelectedRow());
            }
        });

        panel.add(new JScrollPane(assignmentTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        addButton = new JButton("添加分配");
        updateButton = new JButton("更新职责");
        deleteButton = new JButton("删除分配");
        refreshButton = new JButton("刷新");
        searchByVoyageButton = new JButton("按航次查询");
        searchByCrewButton = new JButton("按船员查询");

        addButton.addActionListener(e -> addAssignment());
        updateButton.addActionListener(e -> updateAssignment());
        deleteButton.addActionListener(e -> deleteAssignment());
        refreshButton.addActionListener(e -> loadAllAssignments());
        searchByVoyageButton.addActionListener(e -> searchByVoyage());
        searchByCrewButton.addActionListener(e -> searchByCrew());

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(refreshButton);
        panel.add(searchByVoyageButton);
        panel.add(searchByCrewButton);

        return panel;
    }

    private void fillInputFieldsFromTable(int row) {
        voyageIdField.setText(tableModel.getValueAt(row, 0).toString());
        crewIdField.setText(tableModel.getValueAt(row, 1).toString());
        dutyField.setText(tableModel.getValueAt(row, 2).toString());
    }

    private void addAssignment() {
        try {
            int voyageId = Integer.parseInt(voyageIdField.getText().trim());
            int crewId = Integer.parseInt(crewIdField.getText().trim());
            String duty = dutyField.getText().trim();
            if (duty.isEmpty()) throw new IllegalArgumentException("职责不能为空");

            VoyageCrew vc = new VoyageCrew(voyageId, crewId, duty);
            if (voyageCrewDAO.addVoyageCrew(vc)) {
                JOptionPane.showMessageDialog(this, "添加成功！");
                clearInputFields();
                loadAllAssignments();
            } else {
                JOptionPane.showMessageDialog(this, "添加失败！");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "错误: " + e.getMessage());
        }
    }

    private void updateAssignment() {
        try {
            int voyageId = Integer.parseInt(voyageIdField.getText().trim());
            int crewId = Integer.parseInt(crewIdField.getText().trim());
            String duty = dutyField.getText().trim();
            if (duty.isEmpty()) throw new IllegalArgumentException("职责不能为空");

            VoyageCrew vc = new VoyageCrew(voyageId, crewId, duty);
            if (voyageCrewDAO.updateVoyageCrew(vc)) {
                JOptionPane.showMessageDialog(this, "更新成功！");
                clearInputFields();
                loadAllAssignments();
            } else {
                JOptionPane.showMessageDialog(this, "更新失败！");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "错误: " + e.getMessage());
        }
    }

    private void deleteAssignment() {
        try {
            int voyageId = Integer.parseInt(voyageIdField.getText().trim());
            int crewId = Integer.parseInt(crewIdField.getText().trim());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "确定删除该船员分配？", "确认删除", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION && voyageCrewDAO.deleteVoyageCrew(voyageId, crewId)) {
                JOptionPane.showMessageDialog(this, "删除成功！");
                clearInputFields();
                loadAllAssignments();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "错误: " + e.getMessage());
        }
    }

    private void searchByVoyage() {
        try {
            String input = JOptionPane.showInputDialog(this, "请输入航次ID:");
            if (input != null && !input.trim().isEmpty()) {
                int voyageId = Integer.parseInt(input.trim());
                List<VoyageCrew> list = voyageCrewDAO.getCrewByVoyage(voyageId);
                updateTable(list);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "错误: " + e.getMessage());
        }
    }

    private void searchByCrew() {
        try {
            String input = JOptionPane.showInputDialog(this, "请输入船员ID:");
            if (input != null && !input.trim().isEmpty()) {
                int crewId = Integer.parseInt(input.trim());
                List<VoyageCrew> list = voyageCrewDAO.getVoyagesByCrew(crewId);
                updateTable(list);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "错误: " + e.getMessage());
        }
    }

    private void loadAllAssignments() {
        try {
            List<VoyageCrew> list = voyageCrewDAO.getAllVoyageCrew();
            updateTable(list);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载失败: " + e.getMessage());
        }
    }

    private void updateTable(List<VoyageCrew> list) {
        tableModel.setRowCount(0);
        for (VoyageCrew vc : list) {
            tableModel.addRow(new Object[]{vc.getVoyageId(), vc.getCrewId(), vc.getDuty()});
        }
    }

    private void clearInputFields() {
        voyageIdField.setText("");
        crewIdField.setText("");
        dutyField.setText("");
    }

    // 外部刷新接口
    public void refreshData() {
        loadAllAssignments();
    }
}
