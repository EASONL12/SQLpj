package com.shippingapp.ui;

import javax.swing.*;
import java.awt.*;
import com.shippingapp.ui.panels.DashboardPanel;
import com.shippingapp.ui.panels.ShipPanel;
import com.shippingapp.ui.panels.VoyagePanel;
import com.shippingapp.ui.panels.CrewPanel;
import com.shippingapp.ui.panels.CrewAssignmentPanel;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("航运管理系统");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("首页", new DashboardPanel());
        tabbedPane.addTab("船舶管理", new ShipPanel());
        tabbedPane.addTab("航次管理", new VoyagePanel());
        tabbedPane.addTab("船员管理", new CrewPanel());

        // 船员分配面板直接使用无参构造
        tabbedPane.addTab("船员分配", new CrewAssignmentPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}
