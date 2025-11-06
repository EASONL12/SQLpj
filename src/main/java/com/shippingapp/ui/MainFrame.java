package main.java.com.shippingapp.ui;

import main.java.com.shippingapp.ui.panels.ShipPanel;

import javax.swing.*;

public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;

    public MainFrame() {
        setTitle("航线与船舶管理系统");
        setSize(800, 600);
        setLocationRelativeTo(null); // 窗口居中
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();

        // 添加船舶管理模块
        ShipPanel shipPanel = new ShipPanel();
        tabbedPane.addTab("船舶管理", shipPanel);

        // 这里可以后续加入其他模块，比如 RoutePanel、VoyagePanel...
        // tabbedPane.addTab("航线管理", new RoutePanel());

        add(tabbedPane);
    }
}
