package com.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {
    @FXML
    private TextField patientNameField;
    @FXML
    private TextField patientAgeField;
    @FXML
    private TextField patientAddressField;
    @FXML
    private TextField patientPhoneField;
    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, Integer> patientIdColumn;
    @FXML
    private TableColumn<Patient, String> patientNameColumn;
    @FXML
    private TableColumn<Patient, Integer> patientAgeColumn;
    @FXML
    private TableColumn<Patient, String> patientAddressColumn;
    @FXML
    private TableColumn<Patient, String> patientPhoneColumn;
    @FXML
    private TextField patientIdField;
    @FXML
    private TextField updatePatientNameField;
    @FXML
    private TextField updatePatientAgeField;
    @FXML
    private TextField updatePatientAddressField;
    @FXML
    private TextField updatePatientPhoneField;
    @FXML
    private TextField doctorNameField;
    @FXML
    private TextField doctorSpecializationField;
    @FXML
    private TextField doctorPhoneField;
    @FXML
    private TextField appointmentPatientIdField;
    @FXML
    private TextField appointmentDoctorIdField;
    @FXML
    private TextField appointmentDateField;
    @FXML
    private TextField appointmentTimeField;
    @FXML
    private TextField staffNameField;
    @FXML
    private TextField staffRoleField;
    @FXML
    private TextField staffPhoneField;
    @FXML
    private TextField departmentNameField;
    @FXML
    private TextField departmentDescriptionField;
    @FXML
    private TextField roomNumberField;
    @FXML
    private TextField roomCapacityField;
    @FXML
    private ChoiceBox<String> roomDepartmentChoice;
    @FXML
    private TextField reportNameField;
    @FXML
    private TextField reportDescriptionField;
    @FXML
    private TextField adminUsernameField;
    @FXML
    private PasswordField adminPasswordField;
    @FXML
    private Button billingButton;
    @FXML
    private TableView<Doctor> doctorTable;
    @FXML
    private TableColumn<Doctor, Integer> doctorIdColumn;
    @FXML
    private TableColumn<Doctor, String> doctorNameColumn;
    @FXML
    private TableColumn<Doctor, String> doctorSpecializationColumn;
    @FXML
    private TableColumn<Doctor, String> doctorPhoneColumn;
    @FXML
    private Button generateReportButton;


    private ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
        generateReportButton.setOnAction(event -> openReportForm());


        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        patientAgeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        patientAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        patientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        doctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        doctorSpecializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        doctorPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        hidePatientGridView();
        hideDoctorGridView();
        loadRoomDepartmentChoices();
    }

    public void addPatient() {
        String name = patientNameField.getText();
        int age = Integer.parseInt(patientAgeField.getText());
        String address = patientAddressField.getText();
        String phone = patientPhoneField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO patients (name, age, address, phone) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, address);
            stmt.setString(4, phone);
            stmt.executeUpdate();

            loadPatients();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatient() {
        int id = Integer.parseInt(patientIdField.getText());
        String name = updatePatientNameField.getText();
        int age = Integer.parseInt(updatePatientAgeField.getText());
        String address = updatePatientAddressField.getText();
        String phone = updatePatientPhoneField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE patients SET name = ?, age = ?, address = ?, phone = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, address);
            stmt.setString(4, phone);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            loadPatients();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadPatients() {
        patientList.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM patients";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getInt("id"));
                patient.setName(rs.getString("name"));
                patient.setAge(rs.getInt("age"));
                patient.setAddress(rs.getString("address"));
                patient.setPhone(rs.getString("phone"));
                patientList.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        patientTable.setItems(patientList);
    }

    public void addDoctor() {
        String name = doctorNameField.getText();
        String specialization = doctorSpecializationField.getText();
        String phone = doctorPhoneField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO doctors (name, specialization, phone) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, specialization);
            stmt.setString(3, phone);
            stmt.executeUpdate();
            loadDoctors();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDoctors() {
        doctorList.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM doctors";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setName(rs.getString("name"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setPhone(rs.getString("phone"));
                doctorList.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        doctorTable.setItems(doctorList);
    }

    public void addAppointment() {
        int patientId = Integer.parseInt(appointmentPatientIdField.getText());
        int doctorId = Integer.parseInt(appointmentDoctorIdField.getText());
        String date = appointmentDateField.getText();
        String time = appointmentTimeField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setString(3, date);
            stmt.setString(4, time);
            stmt.executeUpdate();
            // Load or refresh appointment data if necessary
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addStaff() {
        String name = staffNameField.getText();
        String role = staffRoleField.getText();
        String phone = staffPhoneField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO staff (name, role, phone) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, role);
            stmt.setString(3, phone);
            stmt.executeUpdate();
            // Refresh staff data if necessary
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDepartment() {
        String name = departmentNameField.getText();
        String description = departmentDescriptionField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO departments (name, description) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.executeUpdate();
            // Refresh department data if necessary
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRoom() {
        int number = Integer.parseInt(roomNumberField.getText());
        int capacity = Integer.parseInt(roomCapacityField.getText());
        String department = roomDepartmentChoice.getValue();

        try (Connection conn = DatabaseConnection.getConnection()) {
            int departmentId = getDepartmentIdByName(department);
            if (departmentId == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "Department not found");
                return;
            }

            String sql = "INSERT INTO rooms (number, capacity, department_id) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, number);
            stmt.setInt(2, capacity);
            stmt.setInt(3, departmentId);
            stmt.executeUpdate();
            // Refresh room data if necessary
            showAlert(Alert.AlertType.INFORMATION, "Success", "Room added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add room");
        }
    }


    @FXML
    private void openReportForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReportForm.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Generate Report");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getDepartmentIdByName(String name) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT id FROM departments WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1; // Department not found
    }


    private void loadRoomDepartmentChoices() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT name FROM departments";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roomDepartmentChoice.getItems().add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loginAsAdmin() {
        String username = adminUsernameField.getText();
        String password = adminPasswordField.getText();

        // Authenticate admin
        if (username.equals("admin") && password.equals("password")) {
            // Provide full system access
            showAlert(Alert.AlertType.INFORMATION, "Success", "Administrator logged in");
            billingButton.setVisible(true);
            showPatientGridView();
            showDoctorGridView();
            loadDoctors();
            loadPatients();

        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid credentials");
            hidePatientGridView();
            hideDoctorGridView();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void hidePatientGridView() {
        patientTable.setVisible(false);
    }

    private void showPatientGridView() {
        patientTable.setVisible(true);
    }

    private void hideDoctorGridView() {
        doctorTable.setVisible(false);
    }

    private void showDoctorGridView() {
        doctorTable.setVisible(true);
    }

    @FXML
    private void openBillingForm() {
        // Implement the logic to open the billing form
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BillingForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Billing Form");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
