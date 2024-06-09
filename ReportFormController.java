package com.example.project;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ReportFormController {

    @FXML
    private TextField reportTitleField;

    @FXML
    private TextArea reportDescriptionArea;

    @FXML
    private Button submitReportButton;

    @FXML
    public void initialize() {
        submitReportButton.setOnAction(event -> submitReport());
    }

    private void submitReport() {
        String title = reportTitleField.getText();
        String description = reportDescriptionArea.getText();

        if (title.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Title and Description cannot be empty");
            return;
        }

        try {
            saveReportToDatabase(title, description);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Report submitted successfully");
            clearFields();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit report");
            e.printStackTrace();
        }
    }

    private void saveReportToDatabase(String title, String description) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO reports (name, description) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.executeUpdate();
        }
    }

    private void clearFields() {
        reportTitleField.clear();
        reportDescriptionArea.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
