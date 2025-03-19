package texteditor.ui;

import texteditor.CMClientApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ClientStatusUI2 extends JFrame {
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private JButton logoutButton;
    private CMClientApp clientApp;
    private String username;

    public ClientStatusUI2(CMClientApp clientApp, String username) {
        this.clientApp = clientApp;
        this.username = username;

        setTitle("클라이언트 접속 정보 (클라이언트 2)");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(900,100);
        setLayout(new BorderLayout());

        // FlatLaf 테마 적용
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatIntelliJLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 메인 패널
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(50, 50, 50));

        // 왼쪽 패널 (큰 텍스트 필드)
        JTextArea largeTextArea = new JTextArea();
        largeTextArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        largeTextArea.setBackground(Color.WHITE);
        largeTextArea.setForeground(Color.WHITE);
        JScrollPane textScrollPane = new JScrollPane(largeTextArea);

        // 오른쪽 패널 (사용자 출입창)
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(new Color(50, 50, 50));

        JLabel titleLabel = new JLabel("실시간 접속 정보");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userList.setBackground(new Color(60, 60, 60));
        userList.setForeground(Color.WHITE);
        userList.setSelectionBackground(new Color(100, 149, 237));

        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

        logoutButton = new JButton("로그아웃");
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(255, 69, 58));
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientApp.logout();
                dispose();
                new LoginUI2(clientApp).setVisible(true);
            }
        });

        userPanel.add(titleLabel, BorderLayout.NORTH);
        userPanel.add(scrollPane, BorderLayout.CENTER);
        userPanel.add(logoutButton, BorderLayout.SOUTH);

        mainPanel.add(textScrollPane);
        mainPanel.add(userPanel);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void updateUserList(List<String> users, String eventUser, boolean isEntering) {
        SwingUtilities.invokeLater(() -> {
            if (isEntering) {
                userListModel.addElement(eventUser + " 님이 입장하셨습니다.");
            } else {
                userListModel.addElement(eventUser + " 님이 퇴장하셨습니다.");
            }
        });
    }
}
