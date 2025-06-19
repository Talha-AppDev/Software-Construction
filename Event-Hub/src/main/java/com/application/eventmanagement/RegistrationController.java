package com.application.eventmanagement;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;

public class RegistrationController {
    @FXML private ChoiceBox<Event> eventChoice;
    @FXML private ChoiceBox<String> programChoice;
    @FXML private ChoiceBox<Integer> semesterChoice;
    @FXML private Button registerButton;
    @FXML private TableView<Registration> registrationTable;
    @FXML private TableColumn<Registration, String> eventColumn;
    @FXML private TableColumn<Registration, String> programColumn;
    @FXML private TableColumn<Registration, Integer> semesterColumn;
    @FXML private Button deleteButton;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        try {
            // Set up event choice box
            setupEventChoiceBox();

            // Set up program and semester choices
            setupProgramAndSemesterChoices();

            // Configure table columns
            configureTableColumns();

            // Load registrations for current user
            refreshRegistrations();

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupEventChoiceBox() throws SQLException {
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
    }

    private void setupProgramAndSemesterChoices() {
        programChoice.getItems().addAll("BS", "MPhil", "PhD");
        programChoice.setValue("BS");

        semesterChoice.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);
        semesterChoice.setValue(1);
    }

    private void configureTableColumns() {
        eventColumn.setCellValueFactory(cellData ->
        {
            try {
                return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEvent().getName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        programColumn.setCellValueFactory(new PropertyValueFactory<>("program"));

        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));

        // Make table columns resize to fit content
        eventColumn.prefWidthProperty().bind(registrationTable.widthProperty().multiply(0.4));
        programColumn.prefWidthProperty().bind(registrationTable.widthProperty().multiply(0.3));
        semesterColumn.prefWidthProperty().bind(registrationTable.widthProperty().multiply(0.2));
    }

    @FXML
    private void handleRegister() {
        try {
            // Validate input
            if (!validateRegistrationInput()) {
                return;
            }

            // Create and save registration
            Registration registration = createRegistration();
            DataManager.getInstance().addRegistration(registration);

            // Refresh UI
            refreshRegistrations();
            clearForm();

            showAlert("Success", "Successfully registered for the event!");

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to register: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handleDeleteRegistration() {
        Registration selectedRegistration = registrationTable.getSelectionModel().getSelectedItem();
        if (selectedRegistration == null) {
            showAlert("Error", "Please select a registration to delete.");
            return;
        }

        try {
            System.out.println("Attempting to delete registration: " + selectedRegistration); // Debug output
            DataManager.getInstance().deleteRegistration(selectedRegistration);
            System.out.println("Registration deleted successfully"); // Debug output
            refreshRegistrations();
            showAlert("Success", "Registration deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Deletion failed: " + e.getMessage()); // Enhanced error logging
            showAlert("Database Error", "Failed to delete registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateRegistrationInput() {
        if (eventChoice.getValue() == null) {
            showAlert("Invalid Input", "Please select an event.");
            return false;
        }

        if (programChoice.getValue() == null || semesterChoice.getValue() == null) {
            showAlert("Invalid Input", "Please select both program and semester.");
            return false;
        }

        // Check if already registered
        User currentUser = DataManager.getInstance().getCurrentUser();
        Event selectedEvent = eventChoice.getValue();

        try {
            boolean alreadyRegistered = DataManager.getInstance().getRegistrations().stream()
                    .anyMatch(r -> {
                        try {
                            return r.getUser().getId() == currentUser.getId() &&
                                    r.getEvent().getId() == selectedEvent.getId();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

            if (alreadyRegistered) {
                showAlert("Invalid Input", "You are already registered for this event.");
                return false;
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to check existing registrations.");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private Registration createRegistration() {
        User currentUser = DataManager.getInstance().getCurrentUser();
        Event selectedEvent = eventChoice.getValue();
        String program = programChoice.getValue();
        int semester = semesterChoice.getValue();

        return new Registration(currentUser, selectedEvent, program, semester);
    }


    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Main.switchScene(stage, "/com/application/eventmanagement/fxml/controller.fxml", "Dashboard");
    }

    private void refreshRegistrations() {
        try {
            User currentUser = DataManager.getInstance().getCurrentUser();
            if (currentUser != null) {
                registrationTable.setItems(FXCollections.observableArrayList(
                        DataManager.getInstance().getRegistrations().stream()
                                .filter(r -> {
                                    try {
                                        return r.getUser().getId() == currentUser.getId();
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .toList()
                ));
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to refresh registrations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        eventChoice.setValue(null);
        programChoice.setValue("BS");
        semesterChoice.setValue(1);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}