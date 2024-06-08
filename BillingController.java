package com.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class BillingController {
    @FXML
    private TableView<Billing> billingTable;
    @FXML
    private TableColumn<Billing, Integer> billIdColumn;
    @FXML
    private TableColumn<Billing, Integer> patientIdColumn;
    @FXML
    private TableColumn<Billing, Date> billingDateColumn;
    @FXML
    private TableColumn<Billing, String> billingDescriptionColumn;
    @FXML
    private TableColumn<Billing, Double> billingAmountColumn;
    @FXML
    private TextField patientIdField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField amountField;

    private ObservableList<Billing> billingList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        billIdColumn.setCellValueFactory(new PropertyValueFactory<>("billId"));
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        billingDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        billingDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        billingAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // Load billing information
        loadBilling();
    }

    public void addBilling() {
        try {
            // Get values from UI components
            int patientId = Integer.parseInt(patientIdField.getText());
            Date date = new Date(); // You can set the date based on your requirements
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());

            // Insert into the database
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO billing (patient_id, date, description, amount) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, patientId);
                stmt.setDate(2, new java.sql.Date(date.getTime()));
                stmt.setString(3, description);
                stmt.setDouble(4, amount);
                stmt.executeUpdate();
            }

            // Reload billing data
            loadBilling();
        } catch (NumberFormatException e) {
            // Handle invalid input format
            e.printStackTrace();
        } catch (SQLException e) {
            // Handle database errors
            e.printStackTrace();
        }
    }

    private void loadBilling() {
        billingList.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM billing";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Billing billing = new Billing();
                billing.setBillId(rs.getInt("bill_id"));
                billing.setPatientId(rs.getInt("patient_id"));
                billing.setDate(rs.getDate("date"));
                billing.setDescription(rs.getString("description"));
                billing.setAmount(rs.getDouble("amount"));
                billingList.add(billing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        billingTable.setItems(billingList);
    }

    public void deleteBilling() {
        Billing selectedBilling = billingTable.getSelectionModel().getSelectedItem();
        if (selectedBilling != null) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM billing WHERE bill_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, selectedBilling.getBillId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Remove the selected billing from the list
            billingList.remove(selectedBilling);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a bill to delete.");
            alert.showAndWait();
        }
    }

    private String generateBillingSummary() {
        StringBuilder summary = new StringBuilder("Billing Summary:\n\n");
        for (Billing billing : billingList) {
            summary.append("Bill ID: ").append(billing.getBillId())
                    .append("\nPatient ID: ").append(billing.getPatientId())
                    .append("\nDate: ").append(billing.getDate())
                    .append("\nDescription: ").append(billing.getDescription())
                    .append("\nAmount: ").append(billing.getAmount())
                    .append("\n\n");
        }
        summary.append("That's All!");
        return summary.toString();
    }

    @FXML
    private void closeForm() {
        String billingSummary = generateBillingSummary();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Billing Summary");
        alert.setHeaderText(null);
        alert.setContentText(billingSummary);
        alert.showAndWait();

        // Close the billing form
        ((Stage) billingTable.getScene().getWindow()).close();
    }
}
