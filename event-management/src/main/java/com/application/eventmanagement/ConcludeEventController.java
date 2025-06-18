package com.application.eventmanagement;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;

public class ConcludeEventController {
    @FXML private ChoiceBox<Event> eventChoice;
    @FXML private Slider ratingSlider;
    @FXML private TextArea feedbackField;
    @FXML private Button submitButton;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        try {
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
            ratingSlider.setMin(1);
            ratingSlider.setMax(5);
            ratingSlider.setMajorTickUnit(1);
            ratingSlider.setShowTickMarks(true);
            ratingSlider.setShowTickLabels(true);
            ratingSlider.setValue(3);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load events: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSubmit() {
        Event event = eventChoice.getValue();
        int rating = (int) ratingSlider.getValue();
        String feedback = feedbackField.getText().trim();

        // Validation
        if (event == null) {
            showAlert("Invalid Input", "Please select an event.");
            return;
        }
        if (feedback.isEmpty() || feedback.length() > 500) {
            showAlert("Invalid Input", "Feedback must be non-empty and less than 500 characters.");
            return;
        }

        try {
            User currentUser = DataManager.getInstance().getCurrentUser();
            if (currentUser == null) {
                showAlert("Error", "User not logged in.");
                return;
            }
            Feedback feedbackObj = new Feedback(
                    event.getId(), // Use event ID instead of Event object
                    currentUser.getId(), // Use user ID instead of User object
                    rating,
                    feedback
            );
            DataManager.getInstance().addFeedback(feedbackObj);

            showAlert("Success", "Feedback submitted successfully.");
            eventChoice.setValue(null);
            ratingSlider.setValue(3);
            feedbackField.clear();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to submit feedback: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Main.switchScene(stage, "/com/application/eventmanagement/fxml/controller.fxml", "Dashboard");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}