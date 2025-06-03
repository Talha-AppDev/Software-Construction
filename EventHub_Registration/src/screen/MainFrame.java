package screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("EventHub - Welcome");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 1000, 700, 30, 30));
        setLayout(new BorderLayout());

        // Main Panel with Gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(63, 81, 181);
                Color color2 = new Color(101, 31, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(0, 200));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Welcome to EventHub", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 200, 20, 200); // Center buttons with padding

        JButton loginButton = createActionButton("LOGIN", new Color(255, 193, 7));
        loginButton.addActionListener(new LoginActionListener(this));
        buttonPanel.add(loginButton, gbc);

        JButton signupButton = createActionButton("SIGNUP", new Color(255, 193, 7));
        signupButton.addActionListener(new SignupActionListener(this));
        buttonPanel.add(signupButton, gbc);

        JButton studentRegButton = createActionButton("STUDENT REGISTRATION", new Color(255, 193, 7));
        studentRegButton.addActionListener(new StudentRegistrationActionListener(this));
        buttonPanel.add(studentRegButton, gbc);

        JButton viewRegButton = createActionButton("VIEW REGISTRATIONS", new Color(255, 193, 7));
        viewRegButton.addActionListener(new RegistrationViewerActionListener(this));
        buttonPanel.add(viewRegButton, gbc);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Inner class for Login button action listener
    class LoginActionListener implements ActionListener {
        MainFrame parent;
        LoginActionListener(MainFrame parent) { this.parent = parent; }
        public void actionPerformed(ActionEvent evt) {
            new Login();
            parent.dispose();
        }
    }

    // Inner class for Signup button action listener
    class SignupActionListener implements ActionListener {
        MainFrame parent;
        SignupActionListener(MainFrame parent) { this.parent = parent; }
        public void actionPerformed(ActionEvent evt) {
            new Signup();
            parent.dispose();
        }
    }

    // Inner class for Student Registration button action listener
    class StudentRegistrationActionListener implements ActionListener {
        MainFrame parent;
        StudentRegistrationActionListener(MainFrame parent) { this.parent = parent; }
        public void actionPerformed(ActionEvent evt) {
            new StudentRegistration();
            parent.dispose();
        }
    }

    // Inner class for View Registrations button action listener
    class RegistrationViewerActionListener implements ActionListener {
        MainFrame parent;
        RegistrationViewerActionListener(MainFrame parent) { this.parent = parent; }
        public void actionPerformed(ActionEvent evt) {
            new RegistrationViewer();
            parent.dispose();
        }
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            private Color normalColor = bgColor;
            private Color hoverColor = new Color(
                Math.min(bgColor.getRed() + 30, 255),
                Math.min(bgColor.getGreen() + 30, 255),
                Math.min(bgColor.getBlue() + 30, 255)
            );

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hoverColor : normalColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(300, 50));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setCursor(Cursor.getDefaultCursor());
            }
        });
        return button;
    }
}