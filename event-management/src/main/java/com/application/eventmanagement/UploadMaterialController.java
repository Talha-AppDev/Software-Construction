package com.application.eventmanagement;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UploadMaterialController {
    @FXML private ListView<FileMetadata> fileListView;
    @FXML private ChoiceBox<Event> eventChoice; // Added for event selection
    @FXML private Button uploadButton;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        try {
            // Set up event choice box
            eventChoice.setItems(FXCollections.observableArrayList(DataManager.getInstance().getEvents()));
            eventChoice.setConverter(new StringConverter<Event>() {
                @Override
                public String toString(Event event) {
                    return event != null ? event.getName() : "";
                }
                @Override
                public Event fromString(String string) {
                    return null; // No conversion needed
                }
            });

            refreshFileList();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Files to Upload");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(uploadButton.getScene().getWindow());

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            for (File file : selectedFiles) {
                if (file.length() > 5 * 1024 * 1024) { // 5MB limit
                    showAlert("File Too Large", file.getName() + " exceeds 5MB limit");
                    continue;
                }

                try {
                    Event selectedEvent = eventChoice.getValue();
                    if (selectedEvent == null) {
                        showAlert("Error", "Please select an event.");
                        continue;
                    }
                    User currentUser = DataManager.getInstance().getCurrentUser();
                    if (currentUser == null) {
                        showAlert("Error", "User not logged in.");
                        continue;
                    }
                    FileMetadata metadata = new FileMetadata(
                            selectedEvent.getId(),
                            file.getName(),
                            file.getAbsolutePath(),
                            currentUser.getId()
                    );
                    DataManager.getInstance().addFileMetadata(metadata); // Fixed method name
                } catch (SQLException e) {
                    showAlert("Database Error", "Failed to save file: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            try {
                refreshFileList();
                showAlert("Success", "Files uploaded successfully");
            } catch (SQLException e) {
                showAlert("Database Error", "Failed to refresh file list: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Main.switchScene(stage, "/com/application/eventmanagement/fxml/controller.fxml", "Dashboard");
    }

    private void refreshFileList() throws SQLException {
        User currentUser = DataManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            List<FileMetadata> files = DataManager.getInstance().getFilesByUser(currentUser.getId());
            fileListView.setItems(FXCollections.observableArrayList(files));
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}