package com.shippingapp.ui.panels;

import com.shippingapp.dao.RouteDAO;
import com.shippingapp.model.Route;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class RoutePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private RouteDAO routeDAO = new RouteDAO();

    public RoutePanel(){
        setLayout(new BorderLayout());
        // 表格列名
        String[] columns = {"ID", "出发港口", "目标港口", "航线距离"};
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
        JButton searchBtn = new JButton("查询");

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(searchBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addRoute());
        editBtn.addActionListener(e -> editRoute());
        refreshBtn.addActionListener(e -> refreshTable());
        deleteBtn.addActionListener(e -> deleteRoute());
        searchBtn.addActionListener(e -> searchDistance());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Route> routes= routeDAO.getAllRoutes();
        for (Route s : routes) {
            tableModel.addRow(new Object[]{s.getRouteId(),s.getStartPortId(),s.getEndPortId(),s.getDistance()});
        }
    }

    private void addRoute(){
        JTextField stPortIdField = new JTextField();
        JTextField endPortIdField = new JTextField();
        JTextField distanceField = new JTextField();

        Object[] message = {
                "出发港口:", stPortIdField,
                "目标港口：", endPortIdField,
                "航线距离：", distanceField,
        };
        int option = JOptionPane.showConfirmDialog(this, message, "新增航线", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Route route=new Route();
                route.setStartPortId(Integer.parseInt(stPortIdField.getText()));
                route.setEndPortId(Integer.parseInt(endPortIdField.getText()));
                route.setDistance(Integer.parseInt(distanceField.getText()));
                if (routeDAO.addRoute(route)) {
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

    private void editRoute() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要修改的航线！");
            return;
        }

        int routeId = (int) tableModel.getValueAt(selectedRow, 0);
        int currentStartPortId = (int) tableModel.getValueAt(selectedRow, 1);
        int currentEndPortId = (int) tableModel.getValueAt(selectedRow, 2);
        int currentDistance = (int) tableModel.getValueAt(selectedRow, 3);

        JTextField stPortIdField = new JTextField(String.valueOf(currentStartPortId));
        JTextField endPortIdField = new JTextField(String.valueOf(currentEndPortId));
        JTextField distanceField = new JTextField(String.valueOf(currentDistance));

        Object[] message = {
                "出发港口:", stPortIdField,
                "目标港口：", endPortIdField,
                "航线距离：", distanceField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "修改航线", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Route route = new Route();
                route.setRouteId(routeId);
                route.setStartPortId(Integer.parseInt(stPortIdField.getText()));
                route.setEndPortId(Integer.parseInt(endPortIdField.getText()));
                route.setDistance(Integer.parseInt(distanceField.getText()));

                if (routeDAO.updateRoute(route)) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "修改成功！");
                } else {
                    JOptionPane.showMessageDialog(this, "修改失败！");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入有效的数字！");
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void deleteRoute() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的航线！");
            return;
        }

        int routeId = (int) tableModel.getValueAt(selectedRow, 0);
        int startPortId = (int) tableModel.getValueAt(selectedRow, 1);
        int endPortId = (int) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要删除航线【ID：" + routeId + "，出发港：" + startPortId + "，目标港：" + endPortId + "】吗？",
                "删除确认",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (routeDAO.deleteRoute(routeId)) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "删除成功！");
                } else {
                    JOptionPane.showMessageDialog(this, "删除失败！");
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void searchDistance(){
        JTextField idField=new JTextField();
        Object[] message = {
                "航线ID:", idField,
        };
        int option = JOptionPane.showConfirmDialog(this, message, "查询航线距离", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int routeID=Integer.parseInt(idField.getText().trim());
                int distance=routeDAO.getDistanceById(routeID);

                if (distance!=0) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "航线ID为"+routeID+"的距离为"+distance+"海里");
                } else {
                    JOptionPane.showMessageDialog(this, "该航线不存在！");
                }
            }catch (NumberFormatException e){
                JOptionPane.showMessageDialog(this,"请输入有效数字");
            }catch (RuntimeException ex){
                throw new RuntimeException(ex);
            }
        }
    }
}
