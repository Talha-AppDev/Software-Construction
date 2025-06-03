package screen;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMainUI extends JFrame {

    public AdminMainUI() {
        setTitle("Event Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel with right aligned button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(245, 245, 245));

        JButton addButton = new JButton("Add New Event.");
        styleButton(addButton);

        // Add a wrapper panel to hold button aligned to right
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(addButton);

        topPanel.add(buttonWrapper, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Background for main content (optional)
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180)); // Steel blue
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

 

        // Rounded corners
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            protected void paintBackground(Graphics g, AbstractButton b, Color bgColor) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 20, 20);
                g2.dispose();
            }

            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                paintBackground(g, b, b.getBackground());
                super.paint(g, c);
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {     new  Application();

                
            }
        });
    }

    public static void main(String[] args) {
        // Use system look and feel for better native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            AdminMainUI frame = new AdminMainUI();
            frame.setVisible(true);
        });
    }
}
