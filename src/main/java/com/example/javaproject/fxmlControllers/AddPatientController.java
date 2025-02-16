package com.example.javaproject.fxmlControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPatientController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField motdepasseField;

    @FXML
    private TextField ageField;           // New field for Age
    @FXML
    private TextField sexeField;          // New field for Sexe
    @FXML
    private TextField villeField;         // New field for Ville
    @FXML
    private TextField nomUtilisateurField; // New field for NomUtilisateur

    private Patients patientsController;

    // Method to set the Patients controller in this controller
    public void setPatientsController(Patients patientsController) {
        this.patientsController = patientsController;
    }

    @FXML
    private void confirmAddPatient() {
        // Get the values from the text fields
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String telephone = telephoneField.getText();
        String motdepasse = motdepasseField.getText();
        String ageText = ageField.getText();
        String sexe = sexeField.getText();
        String ville = villeField.getText();
        String nomUtilisateur = nomUtilisateurField.getText();

        // Validate the age field is an integer
        int age = 0;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'âge doit être un nombre entier.");
            return;  // Stop if the age is invalid
        }

        // Check if all fields are filled in
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty() || motdepasse.isEmpty() ||
                ageText.isEmpty() || sexe.isEmpty() || ville.isEmpty() || nomUtilisateur.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        // If the patientsController is not null, add the patient to the list and the database
        if (patientsController != null) {
            patientsController.addPatient(nom, prenom, email, telephone, motdepasse, age, sexe, ville, nomUtilisateur);
        }

        // Close the window after adding the patient
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    // Method to show an alert dialog with the given type, title, and message
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
