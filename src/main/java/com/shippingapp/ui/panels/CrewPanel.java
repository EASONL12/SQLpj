// CrewPanel.java - 在 com.shippingapp.ui.panels 包中
package com.shippingapp.ui.panels;

import com.shippingapp.model.Crew;
import com.shippingapp.dao.CrewDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class CrewPanel extends JPanel {
    private JTable crewTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> roleFilter;
    private JTextField searchField;
    private CrewDAO crewDAO;
    private JTabbedPane tabbedPane;

    public CrewPanel() {
        crewDAO = new CrewDAO();
        initializeUI();
        loadCrewData("全部");
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // 创建选项卡
        tabbedPane = new JTabbedPane();

        // 选项卡1：船员信息管理
        tabbedPane.addTab("船员信息", createCrewManagementPanel());

        // 选项卡2：岗位分布
        tabbedPane.addTab("岗位分布", createRoleDistributionPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createCrewManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 顶部操作面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("新增船员");
        JButton editButton = new JButton("修改信息");
        JButton deleteButton = new JButton("删除船员");
        JButton refreshButton = new JButton("刷新");

        // 筛选和搜索
        roleFilter = new JComboBox<>(new String[]{"全部", "船长", "大副", "二副", "轮机长", "水手", "厨师", "医生"});
        searchField = new JTextField(15);
        JButton searchButton = new JButton("搜索");

        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);
        topPanel.add(refreshButton);
        topPanel.add(new JLabel("岗位筛选:"));
        topPanel.add(roleFilter);
        topPanel.add(new JLabel("姓名搜索:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // 表格
        String[] columnNames = {"船员ID", "姓名", "岗位", "联系电话"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        crewTable = new JTable(tableModel);
        crewTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(crewTable);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 事件监听
        addButton.addActionListener(e -> showAddCrewDialog());
        editButton.addActionListener(e -> showEditCrewDialog());
        deleteButton.addActionListener(e -> deleteSelectedCrew());
        refreshButton.addActionListener(e -> loadCrewData((String) roleFilter.getSelectedItem()));
        roleFilter.addActionListener(e -> loadCrewData((String) roleFilter.getSelectedItem()));
        searchButton.addActionListener(e -> searchCrewByName());

        return panel;
    }

    private JPanel createRoleDistributionPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshStatsButton = new JButton("刷新统计");
        topPanel.add(refreshStatsButton);

        // 使用表格显示岗位分布
        String[] columnNames = {"岗位", "人数", "占比"};
        DefaultTableModel statsTableModel = new DefaultTableModel(columnNames, 0);
        JTable statsTable = new JTable(statsTableModel);
        statsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(statsTable);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 加载统计数据
        refreshStatsButton.addActionListener(e -> loadRoleDistribution(statsTableModel));
        loadRoleDistribution(statsTableModel);

        return panel;
    }

    private void loadCrewData(String roleFilter) {
        tableModel.setRowCount(0);

        List<Crew> crewList;
        if ("全部".equals(roleFilter)) {
            crewList = crewDAO.getAllCrew();
        } else {
            crewList = crewDAO.getCrewByRole(roleFilter);
        }

        for (Crew crew : crewList) {
            Object[] rowData = {
                    crew.getCrewId(),
                    crew.getName(),
                    crew.getRole(),
                    crew.getPhone()
            };
            tableModel.addRow(rowData);
        }
    }

    private void loadRoleDistribution(DefaultTableModel statsTableModel) {
        statsTableModel.setRowCount(0);

        Map<String, Integer> distribution = crewDAO.getRoleDistribution();
        int total = distribution.values().stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            String role = entry.getKey();
            int count = entry.getValue();
            double percentage = total > 0 ? (count * 100.0 / total) : 0;

            Object[] rowData = {
                    role,
                    count,
                    String.format("%.1f%%", percentage)
            };
            statsTableModel.addRow(rowData);
        }
    }

    private void showAddCrewDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "新增船员", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));

        // 表单组件
        dialog.add(new JLabel("姓名:"));
        JTextField nameField = new JTextField();
        dialog.add(nameField);

        dialog.add(new JLabel("岗位:"));
        JComboBox<String> roleComboBox = new JComboBox<>(
                new String[]{"船长", "大副", "二副", "轮机长", "水手", "厨师", "医生"});
        dialog.add(roleComboBox);

        dialog.add(new JLabel("联系电话:"));
        JTextField phoneField = new JTextField();
        dialog.add(phoneField);

        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");
        dialog.add(saveButton);
        dialog.add(cancelButton);

        saveButton.addActionListener(e -> {
            if (saveNewCrew(nameField.getText(), (String) roleComboBox.getSelectedItem(), phoneField.getText())) {
                dialog.dispose();
                loadCrewData((String) roleFilter.getSelectedItem());
                // 刷新统计选项卡
                loadRoleDistribution((DefaultTableModel) ((JTable) ((JScrollPane)
                        ((JPanel) tabbedPane.getComponentAt(1)).getComponent(1)).getViewport().getView()).getModel());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void showEditCrewDialog() {
        int selectedRow = crewTable.getSelectedRow();
        if (selectedRow >= 0) {
            int crewId = (int) tableModel.getValueAt(selectedRow, 0);
            String currentName = (String) tableModel.getValueAt(selectedRow, 1);
            String currentRole = (String) tableModel.getValueAt(selectedRow, 2);
            String currentPhone = (String) tableModel.getValueAt(selectedRow, 3);

            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "修改船员信息", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new GridLayout(5, 2, 10, 10));

            dialog.add(new JLabel("姓名:"));
            JTextField nameField = new JTextField(currentName);
            dialog.add(nameField);

            dialog.add(new JLabel("岗位:"));
            JComboBox<String> roleComboBox = new JComboBox<>(
                    new String[]{"船长", "大副", "二副", "轮机长", "水手", "厨师", "医生"});
            roleComboBox.setSelectedItem(currentRole);
            dialog.add(roleComboBox);

            dialog.add(new JLabel("联系电话:"));
            JTextField phoneField = new JTextField(currentPhone);
            dialog.add(phoneField);

            JButton saveButton = new JButton("保存");
            JButton cancelButton = new JButton("取消");
            dialog.add(saveButton);
            dialog.add(cancelButton);

            saveButton.addActionListener(e -> {
                if (updateCrew(crewId, nameField.getText(), (String) roleComboBox.getSelectedItem(), phoneField.getText())) {
                    dialog.dispose();
                    loadCrewData((String) roleFilter.getSelectedItem());
                }
            });

            cancelButton.addActionListener(e -> dialog.dispose());
            dialog.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "请选择要修改的船员");
        }
    }

    private void deleteSelectedCrew() {
        int selectedRow = crewTable.getSelectedRow();
        if (selectedRow >= 0) {
            int crewId = (int) tableModel.getValueAt(selectedRow, 0);
            String crewName = (String) tableModel.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "确定要删除船员 " + crewName + " 吗？", "确认删除", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (crewDAO.deleteCrew(crewId)) {
                    JOptionPane.showMessageDialog(this, "删除成功！");
                    loadCrewData((String) roleFilter.getSelectedItem());
                } else {
                    JOptionPane.showMessageDialog(this, "删除失败！");
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "请选择要删除的船员");
        }
    }

    private void searchCrewByName() {
        String name = searchField.getText().trim();
        if (!name.isEmpty()) {
            tableModel.setRowCount(0);
            List<Crew> crewList = crewDAO.searchCrewByName(name);

            for (Crew crew : crewList) {
                Object[] rowData = {
                        crew.getCrewId(),
                        crew.getName(),
                        crew.getRole(),
                        crew.getPhone()
                };
                tableModel.addRow(rowData);
            }

            if (crewList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "未找到匹配的船员");
            }
        } else {
            loadCrewData((String) roleFilter.getSelectedItem());
        }
    }

    private boolean saveNewCrew(String name, String role, String phone) {
        if (name.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "姓名和岗位不能为空");
            return false;
        }

        Crew crew = new Crew(name, role, phone);
        if (crewDAO.addCrew(crew)) {
            JOptionPane.showMessageDialog(this, "船员添加成功！");
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "船员添加失败！");
            return false;
        }
    }

    private boolean updateCrew(int crewId, String name, String role, String phone) {
        if (name.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "姓名和岗位不能为空");
            return false;
        }

        Crew crew = new Crew();
        crew.setCrewId(crewId);
        crew.setName(name);
        crew.setRole(role);
        crew.setPhone(phone);

        if (crewDAO.updateCrew(crew)) {
            JOptionPane.showMessageDialog(this, "信息更新成功！");
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "信息更新失败！");
            return false;
        }
    }
}
