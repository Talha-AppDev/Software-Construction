package com.application.eventmanagement;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class SignupController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<String> programChoice;
    @FXML private ChoiceBox<Integer> semesterChoice;
    @FXML private Button signupButton;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        programChoice.getItems().addAll("BS", "MPhil", "PhD");
        programChoice.setValue("BS");
        semesterChoice.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);

        // Real-time validation for username
        usernameField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                usernameField.setStyle("-fx-border-color: red;");
            } else {
                usernameField.setStyle("-fx-border-color: #ddd;");
            }
        });

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
            if (!isValidPassword(newValue)) {
                passwordField.setStyle("-fx-border-color: red;");
            } else {
                passwordField.setStyle("-fx-border-color: #ddd;");
            }
        });
    }

    @FXML
    private void handleSignup() throws IOException, SQLException {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String program = programChoice.getValue();
        Integer semester = semesterChoice.getValue();

        // Input validation
        if (username.isEmpty()) {
            showAlert("Invalid Username", "Username cannot be empty.");
            return;
        }
        if (!isValidEmail(email)) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }
        if (!isValidPassword(password)) {
            showAlert("Invalid Password", "Password must be at least 8 characters with letters, numbers, and symbols.");
            return;
        }
        if (program == null || semester == null) {
            showAlert("Invalid Input", "Please select program and semester.");
            return;
        }

        // Create and store user (role is always "Student")
        User user = new User(username, email, password, "Student", program, semester);
        DataManager.getInstance().addUser(user);

        showAlert("Success", "Signup successful! Please login.");
        Stage stage = (Stage) signupButton.getScene().getWindow();
        Main.switchScene(stage, "/com/application/eventmanagement/fxml/login.fxml", "Login");
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

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return Pattern.matches(passwordRegex, password);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}