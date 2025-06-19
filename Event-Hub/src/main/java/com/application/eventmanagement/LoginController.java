package com.application.eventmanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class LoginController {
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private javafx.scene.control.Button backButton;

    @FXML
    private void initialize() {
        // Real-time validation for email
        emailField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!isValidEmail(newValue)) {
                emailField.setStyle("-fx-border-color: red;");
            } else {
                emailField.setStyle("-fx-border-color: #ddd;");
            }
        });

        // Real-time validation for password
        passwordField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                passwordField.setStyle("-fx-border-color: red;");
            } else {
                passwordField.setStyle("-fx-border-color: #ddd;");
            }
        });
    }

    @FXML
    private void handleLogin() throws IOException, SQLException {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email and password cannot be empty");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Error", "Please enter a valid email address");
            return;
        }

        try {
            User user = DataManager.getInstance().authenticateUser(email, password);
            if (user != null) {
                if (!user.getRole().equals("Student") && !user.getRole().equals("Admin")) {
                    showAlert("Error", "Invalid user role");
                    LOGGER.warning("Login failed for user with invalid role: " + email);
                    return;
                }
                DataManager.getInstance().setCurrentUser(user);
                LOGGER.info("User logged in successfully: " + email);

                // Redirect to dashboard (controller.fxml)
                Stage stage = (Stage) emailField.getScene().getWindow();
                Main.switchScene(stage, "/com/application/eventmanagement/fxml/controller.fxml", "Dashboard");
            } else {
                showAlert("Error", "Invalid email or password");
                LOGGER.warning("Login failed for user: " + email);
                emailField.clear(); // Clear fields on failure
                passwordField.clear();
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to authenticate user: " + e.getMessage());
            LOGGER.severe("Database error during login for user " + email + ": " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            showAlert("Application Error", "Failed to load dashboard: " + e.getMessage());
            LOGGER.severe("IO error during dashboard loading: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Main.switchScene(stage, "/com/application/eventmanagement/fxml/basic.fxml", "Welcome");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}