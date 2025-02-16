package com.example.javaproject.fxmlControllers;

import com.example.javaproject.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddArticle {

    @FXML
    private TextField titreField;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button saveButton;

    // Cette méthode est appelée lorsque l'utilisateur clique sur le bouton "Enregistrer"
    public void saveArticle() {
        String titre = titreField.getText();
        String description = descriptionField.getText();

        // Vérifier que les champs ne sont pas vides
        if (titre.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        // Requête pour insérer un nouvel article dans la base de données
        String query = "INSERT INTO articles (Titre, Description) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, titre);
            stmt.setString(2, description);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // Afficher un message de succès
                showAlert(Alert.AlertType.INFORMATION, "Succès", "L'article a été ajouté avec succès.");

                // Appeler la méthode pour recharger la liste des articles dans Articles
                Articles articlesController = getArticlesController();
                if (articlesController != null) {
                    articlesController.loadArticles();  // Recharger les articles
                }

                // Fermer la fenêtre d'ajout (facultatif)
                saveButton.getScene().getWindow().hide();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout de l'article.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout de l'article.");
        }
    }

    // Fonction pour récupérer le contrôleur de la vue des articles
    private Articles getArticlesController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaproject/articles.fxml"));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Fonction pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
