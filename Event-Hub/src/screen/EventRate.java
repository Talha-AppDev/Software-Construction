package screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import controller.Controller;

public class EventRate extends JFrame {

    private int rating = 0;
    private JButton[] stars; // Changed from JLabel to JButton
    private JLabel ratingLabel; // New label for displaying rating

    public EventRate() {
        setTitle("EventHub - Rate Event");
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
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainFrame();
                dispose();
            }
        });

        JLabel titleLabel = new JLabel("Rate This Event", SwingConstants.CENTER);
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

        // Event Name
        JLabel eventLabel = new JLabel("Event Name", SwingConstants.CENTER);
        eventLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        eventLabel.setForeground(Color.WHITE);
        formPanel.add(eventLabel, gbc);

        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        starPanel.setOpaque(false);
        stars = new JButton[5];
        for (int i = 0; i < 5; i++) {
            stars[i] = createStarButton("\u2605"); // Unicode star
            final int starIndex = i + 1;
            stars[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setRating(starIndex);
                }
            });
            starPanel.add(stars[i]);
        }
        gbc.insets = new Insets(20, 0, 10, 0);
        formPanel.add(starPanel, gbc);

        // Rating Display Label
        ratingLabel = new JLabel("No Rating", SwingConstants.CENTER);
        ratingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ratingLabel.setForeground(new Color(200, 200, 200));
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(ratingLabel, gbc);

        // Feedback Label
        JLabel feedbackLabel = new JLabel("Your Feedback");
        feedbackLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        feedbackLabel.setForeground(new Color(200, 200, 200));
        gbc.insets = new Insets(10, 0, 20, 0);
        formPanel.add(feedbackLabel, gbc);

        // Feedback Text Area
        JTextArea feedbackArea = new JTextArea(5, 20);
        feedbackArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        feedbackArea.setBackground(Color.WHITE);
        feedbackArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(scrollPane, gbc);

       // Submit Button
       JButton submitButton = createActionButton("SUBMIT", new Color(255, 193, 7), new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Get user entered data
        if (validateInput(feedbackArea.getText())) {
            Controller eventrate = new Controller();
            String feedback = feedbackArea.getText();
            // Log data before sending to Controller (for debugging)
            System.out.println("Submitting to Controller: Rating=" + rating + ", Feedback=\"" + feedback + "\"");
            String response = eventrate.rateEvent(rating, feedback);
            // Display response
            JOptionPane.showMessageDialog(EventRate.this,
                "Rating: " + rating + (rating == 1 ? " Star" : " Stars") + "\n" +
                "Feedback: " + feedback + "\n" +
                "Message: " + response,
                response.startsWith("Error") ? "Submission Error" : "Submission Successful",
                response.startsWith("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
            // Debug: Print stored submissions from Controller
            System.out.println("Stored submissions in Controller: " + Controller.getRatingSubmissions());
            if (!response.startsWith("Error")) {
                new MainFrame();
                dispose();
            }
        }
    }
});
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(submitButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void setRating(int selectedRating) {
        rating = selectedRating;
        for (int i = 0; i < 5; i++) {
            stars[i].setForeground(i < rating ? new Color(255, 193, 7) : new Color(200, 200, 200));
        }
        ratingLabel.setText(rating == 0 ? "No Rating" : rating + (rating == 1 ? " Star" : " Stars"));
    }


  private boolean validateInput(String feedback) {
        if (rating == 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a rating.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (feedback.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please provide feedback.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
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

     private JButton createStarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 30)); // Font supporting stars
        button.setForeground(new Color(200, 200, 200)); // Default grey
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(40, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setCursor(Cursor.getDefaultCursor());
            }
        });
        return button;
    }

    private JButton createActionButton(String text, Color bgColor, ActionListener action) {
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
        button.addActionListener(action);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setCursor(Cursor.getDefaultCursor());
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EventRate();
            }
        });
    }
}