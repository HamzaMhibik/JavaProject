package com.example.javaproject.fxmlControllers;

import com.example.javaproject.models.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.example.javaproject.DatabaseConnection;

public class EditPatientController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    private Patient patient;

    public void setPatient(Patient patient) {
        this.patient = patient;
        // Prepopulate the fields with the patient's data
        nomField.setText(patient.getNom());
        prenomField.setText(patient.getPrenom());
    }

    @FXML
    private void onSave() {
        String newNom = nomField.getText();
        String newPrenom = prenomField.getText();

        // Update the patient in the database
        String query = "UPDATE patients SET Nom = ?, Prenom = ? WHERE idpatient = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newNom);
            stmt.setString(2, newPrenom);
            stmt.setInt(3, patient.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                showAlert(AlertType.INFORMATION, "Patient Updated", "The patient's details have been successfully updated.");
                closeWindow();
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to update the patient.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "An error occurred while updating the patient.");
        }
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
