package com.application.eventmanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class Controller {
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());

    @FXML private Label welcomeLabel;
    @FXML private Button eventButton;
    @FXML private Button registrationButton;
    @FXML private Button uploadMaterialButton;
    @FXML private Button concludeEventButton;
    @FXML private Button logoutButton;

    @FXML
    private void initialize() {
        // Show "Manage Events" button only for Admins
        User currentUser = DataManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
            if (!currentUser.getRole().equals("Admin")) {
                eventButton.setVisible(false);
                eventButton.setManaged(false); // Remove from layout
            }
        } else {
            LOGGER.warning("No current user found on dashboard initialization");
            welcomeLabel.setText("Welcome to the Dashboard");
        }
    }

    @FXML
    private void handleEventButton() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/application/eventmanagement/fxml/event.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            scene.getStylesheets().add(getClass().getResource("/com/application/eventmanagement/css/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Event Management");
            stage.setResizable(false);
            stage.show();
            LOGGER.info("Opened Event Management screen");
        } catch (IOException e) {
            showAlert("Error", "Failed to load event management screen: " + e.getMessage());
            LOGGER.severe("Failed to load event.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegistrationButton() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/application/eventmanagement/fxml/registration.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            scene.getStylesheets().add(getClass().getResource("/com/application/eventmanagement/css/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Registration");
            stage.setResizable(false);
            stage.show();
            LOGGER.info("Opened Registration screen");
        } catch (IOException e) {
            showAlert("Error", "Failed to load registration screen: " + e.getMessage());
            LOGGER.severe("Failed to load registration.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUploadMaterialButton() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/application/eventmanagement/fxml/upload_material.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            scene.getStylesheets().add(getClass().getResource("/com/application/eventmanagement/css/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Upload Material");
            stage.setResizable(false);
            stage.show();
            LOGGER.info("Opened Upload Material screen");
        } catch (IOException e) {
            showAlert("Error", "Failed to load upload material screen: " + e.getMessage());
            LOGGER.severe("Failed to load upload_material.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleConcludeEventButton() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/application/eventmanagement/fxml/conclude_event.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            scene.getStylesheets().add(getClass().getResource("/com/application/eventmanagement/css/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Conclude Event");
            stage.setResizable(false);
            stage.show();
            LOGGER.info("Opened Conclude Event screen");
        } catch (IOException e) {
            showAlert("Error", "Failed to load conclude event screen: " + e.getMessage());
            LOGGER.severe("Failed to load conclude_event.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogoutButton() {
        try {
            // Clear current user
            DataManager.getInstance().setCurrentUser(null);
            LOGGER.info("User logged out successfully");

            // Redirect to welcome screen
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Main.switchScene(stage, "/com/application/eventmanagement/fxml/basic.fxml", "Welcome");
        } catch (IOException e) {
            showAlert("Error", "Failed to load welcome screen: " + e.getMessage());
            LOGGER.severe("Failed to load basic.fxml: " + e.getMessage());
            e.printStackTrace();
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