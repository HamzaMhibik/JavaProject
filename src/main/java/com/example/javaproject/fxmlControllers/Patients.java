package com.example.javaproject.fxmlControllers;

import com.example.javaproject.DatabaseConnection;
import com.example.javaproject.models.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Patients {

    @FXML
    private TableView<Patient> tableView;

    @FXML
    private TableColumn<Patient, Integer> columnId;

    @FXML
    private TableColumn<Patient, String> columnNom;

    @FXML
    private TableColumn<Patient, String> columnPrenom;

    @FXML
    private TableColumn<Patient, Void> columnAction; // New column for the button

    private ObservableList<Patient> patientsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        columnPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        loadPatients();

        // Create the delete button in each row
        columnAction.setCellFactory(new Callback<TableColumn<Patient, Void>, javafx.scene.control.TableCell<Patient, Void>>() {
            @Override
            public javafx.scene.control.TableCell<Patient, Void> call(TableColumn<Patient, Void> param) {
                return new javafx.scene.control.TableCell<Patient, Void>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Patient selectedPatient = getTableRow().getItem();
                            if (selectedPatient != null) {
                                deletePatient(selectedPatient);
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        });
    }

    private void loadPatients() {
        String query = "SELECT idpatient, Nom, Prenom FROM patients";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             var rs = stmt.executeQuery()) {

            while (rs.next()) {
                patientsList.add(new Patient(rs.getInt("idpatient"), rs.getString("Nom"), rs.getString("Prenom")));
            }
            tableView.setItems(patientsList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Handle the deletion of the selected patient
    private void deletePatient(Patient patient) {
        String query = "DELETE FROM patients WHERE idpatient = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, patient.getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // Remove the patient from the TableView
                patientsList.remove(patient);
                showAlert(AlertType.INFORMATION, "Patient Deleted", "The patient has been successfully deleted.");
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to delete the patient.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "An error occurred while deleting the patient.");
        }
    }

    // Show alert dialogs
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
