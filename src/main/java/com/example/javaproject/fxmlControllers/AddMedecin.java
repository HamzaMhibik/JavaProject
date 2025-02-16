// AddMedecinController.java
package com.example.javaproject.fxmlControllers;

import com.example.javaproject.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddMedecin {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField motDePasseField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField sexeField;
    @FXML
    private TextField villeField;
    @FXML
    private TextField nomUtilisateurField;

    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;

    @FXML
    private void ajouterMedecin() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String telephone = telephoneField.getText();
        String motDePasse = motDePasseField.getText();
        String age = ageField.getText();
        String sexe = sexeField.getText();
        String ville = villeField.getText();
        String nomUtilisateur = nomUtilisateurField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty() || motDePasse.isEmpty() || age.isEmpty() || sexe.isEmpty() || ville.isEmpty() || nomUtilisateur.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        String query = "INSERT INTO medecins (Nom, Prenom, Email, Telephone, Motdepasse, Age, Sexe, Ville, NomUtilisateur) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setString(4, telephone);
            stmt.setString(5, motDePasse);
            stmt.setInt(6, Integer.parseInt(age));
            stmt.setString(7, sexe);
            stmt.setString(8, ville);
            stmt.setString(9, nomUtilisateur);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Médecin ajouté", "Le médecin a été ajouté avec succès.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout du médecin.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout du médecin.");
        }
    }

    @FXML
    private void fermerFenetre() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
