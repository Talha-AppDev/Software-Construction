package com.application.eventmanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class BasicController {
    @FXML
    private Button loginButton; // Injected from basic.fxml
    @FXML
    private Button signupButton; // Injected from basic.fxml

    @FXML
    private void handleLoginButton() {
        try {
            // Get the current stage from the login button's scene
            Stage currentStage = (Stage) loginButton.getScene().getWindow();

            // Load the login screen
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/application/eventmanagement/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            scene.getStylesheets().add(getClass().getResource("/com/application/eventmanagement/css/styles.css").toExternalForm());
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            // Close the current welcome window
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load login screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleSignupButton() {
        try {
            // Get the current stage from the signup button's scene
            Stage currentStage = (Stage) signupButton.getScene().getWindow();

            // Load the signup screen
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/application/eventmanagement/fxml/signup.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            scene.getStylesheets().add(getClass().getResource("/com/application/eventmanagement/css/styles.css").toExternalForm());
            stage.setTitle("Sign Up");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            // Close the current welcome window
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load signup screen: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}