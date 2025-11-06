package com.shippingapp.ui;

import javax.swing.*;
import java.awt.*;
import com.shippingapp.ui.panels.DashboardPanel;
import com.shippingapp.ui.panels.ShipPanel;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("航线与船舶管理系统");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("首页", new DashboardPanel());
        tabbedPane.addTab("船舶管理", new ShipPanel());
        // 后续添加：航线、航次、船员、报表等

        add(tabbedPane, BorderLayout.CENTER);
    }
}
