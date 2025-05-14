import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("水果超市管理系统 - 登录/注册");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel userLabel = new JLabel("用户名：");
        JLabel passLabel = new JLabel("密码：");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginBtn = new JButton("登录");
        JButton registerBtn = new JButton("注册");
        JButton resetBtn = new JButton("重置");

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(passLabel);
        panel.add(passwordField);
        panel.add(loginBtn);
        panel.add(registerBtn);
        panel.add(resetBtn);

        add(panel);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> register());
        resetBtn.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
        });
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名和密码！");
            return;
        }

        if (UserDao.checkLogin(username, password)) {
            JOptionPane.showMessageDialog(this, "登录成功！");
            dispose(); // 关闭登录窗口
            new MainFrame().setVisible(true); // 打开主界面
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误！");
        }
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名和密码！");
            return;
        }

        if (UserDao.register(username, password)) {
            JOptionPane.showMessageDialog(this, "注册成功，请直接点击登录！");
        } else {
            JOptionPane.showMessageDialog(this, "注册失败，用户名可能已存在！");
        }
    }
}
