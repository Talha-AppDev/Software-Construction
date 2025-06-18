package com.application.eventmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            // Initialize database connection
            DatabaseConnection.getConnection();

            // Load the welcome screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/application/eventmanagement/fxml/basic.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/com/application/eventmanagement/css/styles.css").toExternalForm());

            primaryStage.setTitle("Event Management System - Welcome");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Make window non-resizable for consistency
            primaryStage.show();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        DatabaseConnection.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Helper method to switch scenes
    public static void switchScene(Stage stage, String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(Main.class.getResource("/com/application/eventmanagement/css/styles.css").toExternalForm());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}