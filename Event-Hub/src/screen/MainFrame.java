package screen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    // Constructor
    public MainFrame() {
        // Set frame properties
        setTitle("Welcome Screen");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setLayout(new BorderLayout());

        // Add the top panel to the frame
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(0, 350)); // Set height for spacing
        // Create a title label
        JLabel titleLabel = new JLabel("Welcome to the Application", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create Login button
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Application();
                dispose();  // Close the MainFrame
            }
        });

        // Create Signup button
        JButton signupButton = new JButton("Signup");
        signupButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(signupButton);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new Signup(); // Open the Signup screen
                dispose();  // Close the MainFrame
            }
        });

        // Add the panel to the frame
        add(buttonPanel, BorderLayout.CENTER);

        // Set frame visibility
        setVisible(true);
    }
}
