package screen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    // Constructor
    public Login() {
        // Set frame properties
        setTitle("Login Screen");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setLayout(new BorderLayout());

        // Create a panel for the form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;



          // Add Email field
          JLabel emailLabel = new JLabel("Email:");
          gbc.gridx = 0;
          gbc.gridy = 1;
          formPanel.add(emailLabel, gbc);
  
          JTextField emailField = new JTextField(20);
          gbc.gridx = 1;
          formPanel.add(emailField, gbc);
  

        // Add Password field
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);




        // Add Submit Button
        JButton submitButton = new JButton("Submit");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get user entered data 
                String[] user = new String[2];
                user[0] = emailField.getText();
                user[1] = new String(passwordField.getPassword());

                // Controller reg = new Controller();
                // String response =  reg.registration(user);
                //     JOptionPane.showMessageDialog(Login.this,
                //         "Name: " + user[0] + "\n" +
                //         "Password: " + user[1] + "\n" +
                //         "Email: " + user[2] + "\n" +
                //         "Phone #: " + user[3] + "\n" +
                //         "Address " + user[4] + "\n" +
                //         "Gender: " + user[5] + "\n" +
                //         "Message: " + response,
                //         "Registration Successful", JOptionPane.INFORMATION_MESSAGE);

                        
                    new  Application();

                
            }
        });

        // Add form panel to the frame
        add(formPanel, BorderLayout.CENTER);

        // Set frame visibility
        setVisible(true);
    }

    // // Main method for testing
    // public static void main(String[] args) {
    //     new Login();
    // }
}
