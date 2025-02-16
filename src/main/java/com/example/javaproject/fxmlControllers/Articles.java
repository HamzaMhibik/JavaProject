package com.example.javaproject.fxmlControllers;

import com.example.javaproject.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.fxml.FXMLLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;
import javafx.scene.control.cell.PropertyValueFactory;

public class Articles {

    @FXML
    private TableView<Article> tableView;
    @FXML
    private TableColumn<Article, Integer> columnId;
    @FXML
    private TableColumn<Article, String> columnTitre;
    @FXML
    private TableColumn<Article, String> columnDescription;
    @FXML
    private TableColumn<Article, Void> columnAction;
    @FXML
    private TextField searchField;

    private ObservableList<Article> articlesList = FXCollections.observableArrayList();
    private ObservableList<Article> filteredList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        columnAction.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Article, Void> call(TableColumn<Article, Void> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Article selectedArticle = getTableRow().getItem();
                            if (selectedArticle != null) {
                                deleteArticle(selectedArticle);
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

        loadArticles();

        // Ajout d'un listener pour filtrer les articles en fonction de la recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterArticles(newValue));
    }

    // Rendre cette méthode publique pour qu'elle soit accessible ailleurs
    public void loadArticles() {
        String query = "SELECT idarticle, Titre, Description FROM articles";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             var rs = stmt.executeQuery()) {

            articlesList.clear(); // Effacer la liste actuelle pour éviter les doublons
            while (rs.next()) {
                articlesList.add(new Article(rs.getInt("idarticle"), rs.getString("Titre"), rs.getString("Description")));
            }
            filteredList.setAll(articlesList);
            tableView.setItems(filteredList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterArticles(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            filteredList.setAll(articlesList);
        } else {
            filteredList.setAll(articlesList.stream()
                    .filter(article -> article.getTitre().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList()));
        }
    }

    private void deleteArticle(Article article) {
        String query = "DELETE FROM articles WHERE idarticle = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, article.getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                articlesList.remove(article);
                filterArticles(searchField.getText());
                showAlert(Alert.AlertType.INFORMATION, "Article Deleted", "The article has been successfully deleted.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the article.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the article.");
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
    private void openAddArticleWindow() {
        try {
            // Charge le fichier FXML pour le formulaire d'ajout d'article
            Stage addArticleStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaproject/add_article.fxml"));
            AnchorPane root = loader.load(); // Remplace VBox par AnchorPane si nécessaire
            Scene scene = new Scene(root);
            addArticleStage.setScene(scene);
            addArticleStage.setTitle("Ajouter un Article");
            addArticleStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre d'ajout.");
        }
    }
}
