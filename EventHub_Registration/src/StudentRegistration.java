// package screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class StudentRegistration extends JFrame {

    private JComboBox<String> semesterCombo;

    public StudentRegistration() {
        setTitle("EventHub - Student Registration");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 600, 30, 30));
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

        // Header with back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            // new MainFrame();
            dispose();
        });

        JLabel titleLabel = new JLabel("Student Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 20, 0);

        // Event Name Field
        JLabel eventLabel = new JLabel("Event Name");
        eventLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eventLabel.setForeground(new Color(200, 200, 200));
        formPanel.add(eventLabel, gbc);

        JTextField eventField = createStyledTextField();
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(eventField, gbc);

        // Category Selection
        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryLabel.setForeground(new Color(200, 200, 200));
        gbc.insets = new Insets(10, 0, 20, 0);
        formPanel.add(categoryLabel, gbc);

        String[] categories = {"Select Category", "BS", "MPhil", "PhD"};
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        styleComboBox(categoryCombo);
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(categoryCombo, gbc);

        // Semester Selection
        JLabel semesterLabel = new JLabel("Semester");
        semesterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        semesterLabel.setForeground(new Color(200, 200, 200));
        gbc.insets = new Insets(10, 0, 20, 0);
        formPanel.add(semesterLabel, gbc);

        semesterCombo = new JComboBox<>();
        semesterCombo.setEnabled(false); // Disabled until a category is selected
        styleComboBox(semesterCombo);
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(semesterCombo, gbc);

        // Update semester options based on category selection
        categoryCombo.addActionListener(e -> {
            String selectedCategory = categoryCombo.getSelectedItem() != null ? (String) categoryCombo.getSelectedItem() : null;
            updateSemesterOptions(selectedCategory);
        });

        // Register Button
        JButton registerButton = createActionButton("REGISTER", new Color(255, 193, 7), () -> {
            // Validation
            String eventName = eventField.getText().trim();
            String category = categoryCombo.getSelectedItem() != null ? (String) categoryCombo.getSelectedItem() : null;
            String semester = (String) semesterCombo.getSelectedItem();

            if (eventName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an event name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (category == null || category.equals("Select Category")) {
                JOptionPane.showMessageDialog(this, "Please select a category.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (semester == null) {
                JOptionPane.showMessageDialog(this, "Please select a semester.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate semester is within the correct range (redundant due to JComboBox, but included for robustness)
            try {
                int semesterNum = Integer.parseInt(semester);
                if (category.equals("BS") && (semesterNum < 1 || semesterNum > 8)) {
                    JOptionPane.showMessageDialog(this, "Semester for BS must be between 1 and 8.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (category.equals("MPhil") && (semesterNum < 1 || semesterNum > 4)) {
                    JOptionPane.showMessageDialog(this, "Semester for MPhil must be between 1 and 4.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (category.equals("PhD") && (semesterNum < 1 || semesterNum > 2)) {
                    JOptionPane.showMessageDialog(this, "Semester for PhD must be between 1 and 2.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid semester value.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // If all validations pass
            JOptionPane.showMessageDialog(this, "Registration successful!\nEvent: " + eventName + "\nCategory: " + category + "\nSemester: " + semester, "Success", JOptionPane.INFORMATION_MESSAGE);
            // Add your registration logic here (e.g., save to database)
            // new MainFrame();
            dispose();
        });
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(registerButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void updateSemesterOptions(String category) {
        semesterCombo.removeAllItems();
        if (category == null || category.equals("Select Category")) {
            semesterCombo.setEnabled(false);
        } else if (category.equals("BS")) {
            semesterCombo.setEnabled(true);
            for (int i = 1; i <= 8; i++) {
                semesterCombo.addItem(String.valueOf(i));
            }
            semesterCombo.setSelectedItem("1"); // Default to semester 1
        } else if (category.equals("MPhil")) {
            semesterCombo.setEnabled(true);
            for (int i = 1; i <= 4; i++) {
                semesterCombo.addItem(String.valueOf(i));
            }
            semesterCombo.setSelectedItem("1"); // Default to semester 1
        } else if (category.equals("PhD")) {
            semesterCombo.setEnabled(true);
            for (int i = 1; i <= 2; i++) {
                semesterCombo.addItem(String.valueOf(i));
            }
            semesterCombo.setSelectedItem("1"); // Default to semester 1
        }
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        textField.setOpaque(false);
        textField.setForeground(Color.BLACK);
        return textField;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboBox.setForeground(Color.BLACK);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
    }

    private JButton createActionButton(String text, Color bgColor, Runnable action) {
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
        button.setPreferredSize(new Dimension(0, 45));
        button.addActionListener(e -> action.run());

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