package com.example.javaproject.fxmlControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
    private TextField ageField;           // Nouveau champ pour l'âge
    @FXML
    private ComboBox<String> sexeComboBox;  // Utilisation de ComboBox pour le sexe
    @FXML
    private TextField villeField;         // Nouveau champ pour la ville
    @FXML
    private TextField nomUtilisateurField; // Nouveau champ pour le nom d'utilisateur

    private Patients patientsController;

    // Méthode pour définir le contrôleur Patients dans ce contrôleur
    public void setPatientsController(Patients patientsController) {
        this.patientsController = patientsController;
    }

    @FXML
    private void initialize() {
        // Initialiser le ComboBox avec les options de sexe
        sexeComboBox.getItems().addAll("Homme", "Femme");
    }

    @FXML
    private void confirmAddPatient() {
        // Récupérer les valeurs des champs du formulaire
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String telephone = telephoneField.getText();
        String motdepasse = motdepasseField.getText();
        String ageText = ageField.getText();
        String sexe = sexeComboBox.getValue();  // Utilisation du ComboBox pour le sexe
        String ville = villeField.getText();
        String nomUtilisateur = nomUtilisateurField.getText();

        // Valider que tous les champs sont remplis
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty() || motdepasse.isEmpty() ||
                ageText.isEmpty() || sexe == null || ville.isEmpty() || nomUtilisateur.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        // Valider que l'âge est un nombre entier
        int age = 0;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'âge doit être un nombre entier.");
            return;
        }

        // Si le contrôleur Patients est défini, ajouter le patient à la liste et à la base de données
        if (patientsController != null) {
            patientsController.addPatient(nom, prenom, email, telephone, motdepasse, age, sexe, ville, nomUtilisateur);
        }

        // Fermer la fenêtre après l'ajout
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }


    // Méthode pour gérer l'annulation
    @FXML
    private void handleCancel() {
        // Fermer la fenêtre sans rien faire
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    // Méthode pour afficher une alerte avec le type, le titre et le message donnés
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
