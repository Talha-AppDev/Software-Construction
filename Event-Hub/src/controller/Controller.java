package controller;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    // Store rating submissions for visibility
    private static final List<String> ratingSubmissions = new ArrayList<>();

    public String signup(String[] user) {
        String name = user[0];
        String email = user[1];
        String password = user[2];
        String confirmPassword = user[3];
        String role = user[4];

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return "Error: Name, email, or password cannot be empty.";
        }
        if (!password.equals(confirmPassword)) {
            return "Error: Passwords do not match.";
        }

        System.out.println("Registered: Name=" + name + ", Email=" + email + ", Role=" + role);
        return "Registration successful for " + name + " as " + role + ".";
    }

    public String rateEvent(int rating, String feedback) {
        // Validation
        if (rating < 1 || rating > 5) {
            return "Error: Rating must be between 1 and 5 stars.";
        }
        if (feedback == null || feedback.trim().isEmpty()) {
            return "Error: Feedback cannot be empty.";
        }

        // Log data to console
        String submission = "Event Rated: Rating=" + rating + " stars, Feedback=\"" + feedback + "\"";
        System.out.println(submission);
        
        // Store in list for visibility
        ratingSubmissions.add(submission);
        
        // Optional: Log to file (commented out)
        /*
        try (FileWriter fw = new FileWriter("ratings.log", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(submission);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
        */

        return "Rating submitted: " + rating + (rating == 1 ? " star" : " stars") + " with feedback.";
    }

    // Method to access stored submissions (for debugging/visibility)
    public static List<String> getRatingSubmissions() {
        return new ArrayList<>(ratingSubmissions);
    }
}