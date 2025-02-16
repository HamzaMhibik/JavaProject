package com.example.javaproject.fxmlControllers;

import com.example.javaproject.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.IOException;
import javafx.scene.control.cell.PropertyValueFactory;


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
    private TableColumn<Patient, String> columnEmail;

    @FXML
    private TableColumn<Patient, String> columnTelephone;

    @FXML
    private TableColumn<Patient, String> columnMotdepasse;

    @FXML
    private TableColumn<Patient, Integer> columnAge;       // New column
    @FXML
    private TableColumn<Patient, String> columnSexe;       // New column
    @FXML
    private TableColumn<Patient, String> columnVille;      // New column
    @FXML
    private TableColumn<Patient, String> columnNomUtilisateur;  // New column

    @FXML
    private TableColumn<Patient, Void> columnAction;

    private ObservableList<Patient> patientsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        columnPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        columnMotdepasse.setCellValueFactory(new PropertyValueFactory<>("motdepasse"));
        columnAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        columnSexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        columnVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
        columnNomUtilisateur.setCellValueFactory(new PropertyValueFactory<>("nomUtilisateur"));

        loadPatients();

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
        String query = "SELECT idpatient, Nom, Prenom, Email, Telephone, Motdepasse, Age, Sexe, Ville, NomUtilisateur FROM patients";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             var rs = stmt.executeQuery()) {

            while (rs.next()) {
                patientsList.add(new Patient(
                        rs.getInt("idpatient"),
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getString("Email"),
                        rs.getString("Telephone"),
                        rs.getString("Motdepasse"),
                        rs.getInt("Age"),
                        rs.getString("Sexe"),
                        rs.getString("Ville"),
                        rs.getString("NomUtilisateur")
                ));
            }
            tableView.setItems(patientsList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePatient(Patient patient) {
        String query = "DELETE FROM patients WHERE idpatient = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, patient.getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                patientsList.remove(patient);
                showAlert(Alert.AlertType.INFORMATION, "Patient supprimé", "Le patient a été supprimé avec succès.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression du patient.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression du patient.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void showAddPatientForm() {
        try {
            // Load the FXML file for the AddPatient form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaproject/add_patient_form.fxml"));
            Parent root = loader.load();

            // Get the controller of the AddPatient form
            AddPatientController addPatientController = loader.getController();

            // Set the Patients controller in the AddPatientController
            addPatientController.setPatientsController(this);

            // Create and show the new window
            Stage stage = new Stage();
            stage.setTitle("Add New Patient");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ouverture du formulaire.");
        }
    }
    // Inside Patients.java

    public void addPatient(String nom, String prenom, String email, String telephone, String motdepasse, int age, String sexe, String ville, String nomUtilisateur) {
        String query = "INSERT INTO patients (Nom, Prenom, Email, Telephone, Motdepasse, Age, Sexe, Ville, NomUtilisateur) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setString(4, telephone);
            stmt.setString(5, motdepasse);
            stmt.setInt(6, age);
            stmt.setString(7, sexe);
            stmt.setString(8, ville);
            stmt.setString(9, nomUtilisateur);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the generated ID of the new patient
                try (var generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1); // Get the generated ID (1 is the column index for the ID)
                        // Add the new patient to the list1 with the generated ID
                        patientsList.add(new Patient(newId, nom, prenom, email, telephone, motdepasse, age, sexe, ville, nomUtilisateur));
                        showAlert(Alert.AlertType.INFORMATION, "Patient ajouté", "Le patient a été ajouté avec succès.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Le patient a été ajouté, mais l'ID généré n'a pas été récupéré.");
                    }
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout du patient.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout du patient.");
        }
    }


}
