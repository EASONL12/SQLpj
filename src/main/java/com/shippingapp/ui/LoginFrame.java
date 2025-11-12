package com.shippingapp.ui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("航线与船舶管理系统 - 登录");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel titleLabel = new JLabel("航线与船舶管理系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(e -> login());

        add(titleLabel);
        add(labeledPanel("用户名：", usernameField));
        add(labeledPanel("密  码：", passwordField));
        add(loginButton);
    }

    private JPanel labeledPanel(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (com.shippingapp.dao.UserDAO.validateUser(username, password)) {
            JOptionPane.showMessageDialog(this, "登录成功！");
            dispose(); // 关闭登录窗口
            new MainFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误！");
        }
    }
}