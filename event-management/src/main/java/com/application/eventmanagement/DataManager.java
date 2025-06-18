package com.application.eventmanagement;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DataManager {
    private static final Logger LOGGER = Logger.getLogger(DataManager.class.getName());
    private static DataManager instance = null;
    private User currentUser;

    private DataManager() {}

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        LOGGER.info("Current user set to: " + (user != null ? user.getEmail() : "null"));
    }

    // User Management
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password, role, program, semester) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getProgram());
            pstmt.setObject(6, user.getSemester(), Types.INTEGER);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
            LOGGER.info("User added: " + user.getEmail());
        } catch (SQLException e) {
            LOGGER.severe("Failed to add user: " + user.getEmail() + " - " + e.getMessage());
            throw e;
        }
    }

    public User authenticateUser(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("program"),
                            rs.getInt("semester")
                    );
                }
            }
            LOGGER.warning("Authentication failed for email: " + email);
            return null;
        } catch (SQLException e) {
            LOGGER.severe("Authentication error for email: " + email + " - " + e.getMessage());
            throw e;
        }
    }

    public User getUser(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("program"),
                            rs.getInt("semester")
                    );
                }
            }
            LOGGER.warning("No user found with ID: " + id);
            return null;
        } catch (SQLException e) {
            LOGGER.severe("Failed to retrieve user ID: " + id + " - " + e.getMessage());
            throw e;
        }
    }

    // Event Management
    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("location"),
                        rs.getInt("created_by")
                ));
            }
            LOGGER.info("Retrieved " + events.size() + " events");
            return events;
        } catch (SQLException e) {
            LOGGER.severe("Failed to retrieve events: " + e.getMessage());
            throw e;
        }
    }
    public void addEvent(Event event) throws SQLException {
        String sql = "INSERT INTO events (name, date, location, created_by) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, event.getName());
            pstmt.setDate(2, Date.valueOf(event.getDate()));
            pstmt.setString(3, event.getLocation());
            pstmt.setInt(4, event.getCreatedBy());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    event.setId(rs.getInt(1));
                }
            }
            LOGGER.info("Event added: " + event.getName());
        } catch (SQLException e) {
            LOGGER.severe("Failed to add event: " + event.getName() + " - " + e.getMessage());
            throw e;
        }
    }

    public void updateEvent(Event event) throws SQLException {
        String sql = "UPDATE events SET name = ?, date = ?, location = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event.getName());
            pstmt.setDate(2, Date.valueOf(event.getDate()));
            pstmt.setString(3, event.getLocation());
            pstmt.setInt(4, event.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Event updated: " + event.getName());
            } else {
                LOGGER.warning("No event found with ID: " + event.getId());
            }
        } catch (SQLException e) {
            LOGGER.severe("Failed to update event: " + event.getName() + " - " + e.getMessage());
            throw e;
        }
    }

    public void deleteEvent(int eventId) throws SQLException {
        String sql = "DELETE FROM events WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Event deleted: ID " + eventId);
            } else {
                LOGGER.warning("No event found with ID: " + eventId);
            }
        } catch (SQLException e) {
            LOGGER.severe("Failed to delete event ID: " + eventId + " - " + e.getMessage());
            throw e;
        }
    }

    public List<Event> getEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("location"),
                        rs.getInt("created_by")
                ));
            }
            LOGGER.info("Retrieved " + events.size() + " events");
            return events;
        } catch (SQLException e) {
            LOGGER.severe("Failed to retrieve events: " + e.getMessage());
            throw e;
        }
    }

    public Event getEvent(int id) throws SQLException {
        String sql = "SELECT * FROM events WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Event(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDate("date").toLocalDate(),
                            rs.getString("location"),
                            rs.getInt("created_by")
                    );
                }
            }
            LOGGER.warning("No event found with ID: " + id);
            return null;
        } catch (SQLException e) {
            LOGGER.severe("Failed to retrieve event ID: " + id + " - " + e.getMessage());
            throw e;
        }
    }

    // Registration Management
    public void addRegistration(Registration registration) throws SQLException {
        String sql = "INSERT INTO registrations (user_id, event_id, registration_date, program, semester) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, registration.getUserId());
            pstmt.setInt(2, registration.getEventId());
            pstmt.setDate(3, Date.valueOf(registration.getRegistrationDate()));
            pstmt.setString(4, registration.getProgram());
            pstmt.setInt(5, registration.getSemester());
            pstmt.executeUpdate();
            LOGGER.info("Registration added for user ID: " + registration.getUserId() + ", event ID: " + registration.getEventId());
        } catch (SQLException e) {
            LOGGER.severe("Failed to add registration: " + e.getMessage());
            throw e;
        }
    }

    public List<Registration> getRegistrations() throws SQLException {
        List<Registration> registrations = new ArrayList<>();
        String sql = "SELECT * FROM registrations";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                registrations.add(new Registration(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("event_id"),
                        rs.getDate("registration_date").toLocalDate(),
                        rs.getString("program"),
                        rs.getInt("semester")
                ));
            }
            LOGGER.info("Retrieved " + registrations.size() + " registrations");
            return registrations;
        } catch (SQLException e) {
            LOGGER.severe("Failed to retrieve registrations: " + e.getMessage());
            throw e;
        }
    }

    public void deleteRegistration(Registration registration) throws SQLException {
        String sql = "DELETE FROM registrations WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, registration.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Registration deleted: ID " + registration.getId());
            } else {
                LOGGER.warning("No registration found with ID: " + registration.getId());
            }
        } catch (SQLException e) {
            LOGGER.severe("Failed to delete registration ID: " + registration.getId() + " - " + e.getMessage());
            throw e;
        }
    }

    // File Metadata Management
    public void addFileMetadata(FileMetadata metadata) throws SQLException {
        String sql = "INSERT INTO file_metadata (event_id, file_name, file_path, uploaded_by) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, metadata.getEventId());
            pstmt.setString(2, metadata.getFileName());
            pstmt.setString(3, metadata.getFilePath());
            pstmt.setInt(4, metadata.getUploadedBy());
            pstmt.executeUpdate();
            LOGGER.info("File metadata added for event ID: " + metadata.getEventId());
        } catch (SQLException e) {
            LOGGER.severe("Failed to add file metadata: " + e.getMessage());
            throw e;
        }
    }

    public List<FileMetadata> getFilesByUser(int userId) throws SQLException {
        List<FileMetadata> files = new ArrayList<>();
        String sql = "SELECT * FROM file_metadata WHERE uploaded_by = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    files.add(new FileMetadata(
                            rs.getInt("id"),
                            rs.getInt("event_id"),
                            rs.getString("file_name"),
                            rs.getString("file_path"),
                            rs.getInt("uploaded_by")
                    ));
                }
            }
            LOGGER.info("Retrieved " + files.size() + " files for user ID: " + userId);
            return files;
        } catch (SQLException e) {
            LOGGER.severe("Failed to retrieve files for user ID: " + userId + " - " + e.getMessage());
            throw e;
        }
    }

    public List<FileMetadata> getFilesForEvent(int eventId) throws SQLException {
        List<FileMetadata> files = new ArrayList<>();
        String sql = "SELECT * FROM file_metadata WHERE event_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    files.add(new FileMetadata(
                            rs.getInt("id"),
                            rs.getInt("event_id"),
                            rs.getString("file_name"),
                            rs.getString("file_path"),
                            rs.getInt("uploaded_by")
                    ));
                }
            }
            LOGGER.info("Retrieved " + files.size() + " files for event ID: " + eventId);
            return files;
        } catch (SQLException e) {
            LOGGER.severe("Failed to retrieve files for event ID: " + eventId + " - " + e.getMessage());
            throw e;
        }
    }

    // Feedback Management
    public void addFeedback(Feedback feedback) throws SQLException {
        String sql = "INSERT INTO feedback (event_id, user_id, rating, comments) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, feedback.getEventId());
            pstmt.setInt(2, feedback.getUserId());
            pstmt.setInt(3, feedback.getRating());
            pstmt.setString(4, feedback.getComments());
            pstmt.executeUpdate();
            LOGGER.info("Feedback added for event ID: " + feedback.getEventId());
        } catch (SQLException e) {
            LOGGER.severe("Failed to add feedback: " + e.getMessage());
            throw e;
        }
    }

    public List<Feedback> getFeedbackForEvent(int eventId) throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE event_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    feedbackList.add(new Feedback(
                            rs.getInt("id"),
                            rs.getInt("event_id"),
                            rs.getInt("user_id"),
                            rs.getInt("rating"),
                            rs.getString("comments")
                    ));
                }
            }
            LOGGER.info("Retrieved " + feedbackList.size() + " feedback entries for event ID: " + eventId);
            return feedbackList;
        } catch (SQLException e) {
            LOGGER.severe("Failed to retrieve feedback for event ID: " + eventId + " - " + e.getMessage());
            throw e;
        }
    }
}