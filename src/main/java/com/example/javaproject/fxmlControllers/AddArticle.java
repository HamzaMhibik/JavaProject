package com.example.javaproject.fxmlControllers;

import com.example.javaproject.DatabaseConnection;
import com.example.javaproject.fxmlControllers.Article;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddArticle {

    @FXML
    private TextField titreField;
    @FXML
    private TextArea descriptionField;

    private Articles articlesController;  // Champ pour stocker la référence au contrôleur Articles

    // Méthode pour initialiser le contrôleur Articles
    public void setArticlesController(Articles articlesController) {
        this.articlesController = articlesController;
    }

    @FXML
    private void confirmAddArticle() {
        String titre = titreField.getText();
        String description = descriptionField.getText();

        // Vérifier si les champs sont remplis
        if (titre.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        String query = "INSERT INTO articles (Titre, Description) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, titre);
            stmt.setString(2, description);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Article ajouté", "L'article a été ajouté avec succès.");
                // Fermer la fenêtre
                closeWindow();

                // Rafraîchir la liste des articles dans le contrôleur principal
                if (articlesController != null) {
                    articlesController.loadArticles();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout de l'article.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout de l'article.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) titreField.getScene().getWindow();
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
