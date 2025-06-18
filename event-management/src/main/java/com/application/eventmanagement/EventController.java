package com.application.eventmanagement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Logger;

public class EventController {
    private static final Logger LOGGER = Logger.getLogger(EventController.class.getName());

    @FXML private TextField nameField;
    @FXML private DatePicker datePicker;
    @FXML private TextField locationField;
    @FXML private Button createButton;
    @FXML private Button updateButton;
    @FXML private Button clearButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, Integer> idColumn;
    @FXML private TableColumn<Event, String> nameColumn;
    @FXML private TableColumn<Event, LocalDate> dateColumn;
    @FXML private TableColumn<Event, String> locationColumn;

    private ObservableList<Event> eventList = FXCollections.observableArrayList();
    private Event selectedEvent = null;

    @FXML
    private void initialize() {
        // Check if current user is Admin
        User currentUser = DataManager.getInstance().getCurrentUser();
        if (currentUser == null || !currentUser.getRole().equals("Admin")) {
            showAlert("Access Denied", "Only Admins can manage events.");
            try {
                handleBack();
            } catch (IOException e) {
                LOGGER.severe("Failed to return to dashboard: " + e.getMessage());
            }
            return;
        }

        // Initialize table columns
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));

        // Load events
        loadEvents();

        // Table selection listener
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedEvent = newSelection;
                nameField.setText(selectedEvent.getName());
                datePicker.setValue(selectedEvent.getDate());
                locationField.setText(selectedEvent.getLocation());
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
            } else {
                selectedEvent = null;
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });

        // Disable buttons initially
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @FXML
    private void handleCreateEvent() {
        if (!validateInput()) return;

        try {
            Event event = new Event(
                    0, // ID will be auto-generated
                    nameField.getText().trim(),
                    datePicker.getValue(),
                    locationField.getText().trim(),
                    DataManager.getInstance().getCurrentUser().getId()
            );
            DataManager.getInstance().addEvent(event);
            showAlert("Success", "Event created successfully.");
            loadEvents();
            handleClearForm();
            LOGGER.info("Event created: " + event.getName());
        } catch (SQLException e) {
            showAlert("Error", "Failed to create event: " + e.getMessage());
            LOGGER.severe("Failed to create event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateEvent() {
        if (!validateInput() || selectedEvent == null) return;

        try {
            selectedEvent.setName(nameField.getText().trim());
            selectedEvent.setDate(datePicker.getValue());
            selectedEvent.setLocation(locationField.getText().trim());
            DataManager.getInstance().updateEvent(selectedEvent);
            showAlert("Success", "Event updated successfully.");
            loadEvents();
            handleClearForm();
            LOGGER.info("Event updated: " + selectedEvent.getName());
        } catch (SQLException e) {
            showAlert("Error", "Failed to update event: " + e.getMessage());
            LOGGER.severe("Failed to update event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteEvent() {
        if (selectedEvent == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete event: " + selectedEvent.getName() + "?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    DataManager.getInstance().deleteEvent(selectedEvent.getId());
                    showAlert("Success", "Event deleted successfully.");
                    loadEvents();
                    handleClearForm();
                    LOGGER.info("Event deleted: " + selectedEvent.getName());
                } catch (SQLException e) {
                    showAlert("Error", "Failed to delete event: " + e.getMessage());
                    LOGGER.severe("Failed to delete event: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleClearForm() {
        nameField.clear();
        datePicker.setValue(null);
        locationField.clear();
        eventTable.getSelectionModel().clearSelection();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close(); // Close the event management window
    }

    private void loadEvents() {
        try {
            eventList.setAll(DataManager.getInstance().getAllEvents());
            eventTable.setItems(eventList);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load events: " + e.getMessage());
            LOGGER.severe("Failed to load events: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert("Error", "Event name cannot be empty.");
            return false;
        }
        if (datePicker.getValue() == null) {
            showAlert("Error", "Please select a date.");
            return false;
        }
        if (locationField.getText().trim().isEmpty()) {
            showAlert("Error", "Location cannot be empty.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}