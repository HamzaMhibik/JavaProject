package com.example.javaproject.fxmlControllers;

import com.example.javaproject.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddLaboratoire {

    @FXML private TextField nomField;
    @FXML private TextField adresseField;

    @FXML
    public void addLaboratoire() {  // Méthode addLaboratoire rendue publique
        String nom = nomField.getText();
        String adresse = adresseField.getText();

        String query = "INSERT INTO laboratoires (Nom, Adresse) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nom);
            stmt.setString(2, adresse);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Laboratoire ajouté avec succès.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout du laboratoire.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout du laboratoire.");
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
    public void closeWindow() {  // Méthode closeWindow rendue publique
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
}
