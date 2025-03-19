package texteditor.ui;

import texteditor.CMClientApp;
import texteditor.CMClientEventHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private CMClientApp clientApp;

    public LoginUI(CMClientApp clientApp) {
        this.clientApp = clientApp;
        setTitle("KU 공유 텍스트 편집기 - 로그인 (클라이언트 1)");
        setSize(500, 400); // 창 크기 조정 (더 컴팩트하게)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(200, 200);
        setLayout(new BorderLayout());

        // FlatLaf 테마 적용
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatIntelliJLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔹 메인 패널
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(45, 45, 45)); // 다크 모드 배경

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // 🔹 제목 (아이디/패스워드 위에 배치)
        JLabel titleLabel = new JLabel("KU 공유 텍스트 편집기", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32)); // 🔹 크기 키움
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // 아이디 필드
        JLabel usernameLabel = new JLabel("아이디");
        usernameLabel.setForeground(Color.WHITE);
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                usernameField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // 비밀번호 필드
        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setForeground(Color.WHITE);
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                passwordField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // 로그인 버튼
        loginButton = new JButton("로그인");
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(30, 144, 255)); // Royal Blue
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(150, 40)); // 🔹 버튼 크기 증가

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                clientApp.login(username, password);

                CMClientEventHandler eventHandler = clientApp.getEventHandler();

                ClientStatusUI statusUI = new ClientStatusUI(clientApp, username);
                eventHandler.setStatusUI(statusUI);

                dispose();
                SwingUtilities.invokeLater(() -> statusUI.setVisible(true));
            }
        });

        // 🔹 UI 배치 (타이틀 추가 후 아이디/비번 배치)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc); // 🔹 제목 추가

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(loginButton, gbc);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        CMClientApp clientApp = new CMClientApp();
        clientApp.start();
        SwingUtilities.invokeLater(() -> new LoginUI(clientApp).setVisible(true));
    }
}
