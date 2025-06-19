package com.application.eventmanagement;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UploadMaterialController {
    @FXML private ListView<FileMetadata> fileListView;
    @FXML private ChoiceBox<Event> eventChoice;
    @FXML private Button uploadButton;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        try {
            // Set up event choice box
            eventChoice.setItems(FXCollections.observableArrayList(DataManager.getInstance().getEvents()));
            eventChoice.setConverter(new StringConverter<>() {
                @Override
                public String toString(Event event) {
                    return event != null ? event.getName() : "";
                }

                @Override
                public Event fromString(String string) {
                    return null; // Not needed
                }
            });

            // Set custom cell factory for image preview
            fileListView.setCellFactory(param -> new ListCell<>() {
                private final ImageView imageView = new ImageView();
                private final Label fileNameLabel = new Label();
                private final VBox vbox = new VBox(imageView, fileNameLabel);

                {
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    imageView.setPreserveRatio(true);
                    vbox.setSpacing(5);
                    vbox.setPadding(new Insets(5));
                }

                @Override
                protected void updateItem(FileMetadata file, boolean empty) {
                    super.updateItem(file, empty);

                    if (empty || file == null) {
                        setGraphic(null);
                    } else {
                        fileNameLabel.setText(file.getFileName());
                        File localFile = new File(file.getFilePath());
                        if (localFile.exists() && isImage(file.getFileName())) {
                            imageView.setImage(new Image(localFile.toURI().toString()));
                        } else {
                            imageView.setImage(null); // or a placeholder image if you prefer
                        }
                        setGraphic(vbox);
                    }
                }
            });

            // Load initial file list
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
                if (file.length() > 5 * 1024 * 1024) {
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

                    DataManager.getInstance().addFileMetadata(metadata);
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isImage(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".gif");
    }
}
