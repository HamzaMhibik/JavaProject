package com.example.javaproject.fxmlControllers;

import com.example.javaproject.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class Discussions {

    @FXML
    private TextField searchField;  // Search input field

    @FXML
    private TableView<Discussion> tableView;

    @FXML
    private TableColumn<Discussion, Integer> columnId;

    @FXML
    private TableColumn<Discussion, String> columnNom;

    @FXML
    private TableColumn<Discussion, String> columnContenu;

    @FXML
    private TableColumn<Discussion, Void> columnAction; // Nouvelle colonne pour le bouton "Delete"

    private ObservableList<Discussion> discussionsList = FXCollections.observableArrayList();
    private ObservableList<Discussion> filteredList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        columnContenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));

        // Ajouter une colonne avec un bouton "Delete"
        columnAction.setCellFactory(new Callback<TableColumn<Discussion, Void>, javafx.scene.control.TableCell<Discussion, Void>>() {
            @Override
            public javafx.scene.control.TableCell<Discussion, Void> call(TableColumn<Discussion, Void> param) {
                return new javafx.scene.control.TableCell<Discussion, Void>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Discussion selectedDiscussion = getTableRow().getItem();
                            if (selectedDiscussion != null) {
                                deleteDiscussion(selectedDiscussion);
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

        loadDiscussions();

        // Add listener for search field to filter the table dynamically
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterDiscussions(newValue));
    }

    private void loadDiscussions() {
        String query = "SELECT iddiscussion, nom, contenu FROM discussions";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             var rs = stmt.executeQuery()) {

            while (rs.next()) {
                discussionsList.add(new Discussion(rs.getInt("iddiscussion"), rs.getString("nom"), rs.getString("contenu")));
            }
            filteredList.setAll(discussionsList); // Initially, display all discussions
            tableView.setItems(filteredList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Filter discussions based on search text
    private void filterDiscussions(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            filteredList.setAll(discussionsList); // Show all items if search text is empty
        } else {
            filteredList.setAll(discussionsList.stream()
                    .filter(discussion -> discussion.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                            discussion.getContenu().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList())); // Filter based on name or content
        }
    }

    // Méthode pour supprimer une discussion
    private void deleteDiscussion(Discussion discussion) {
        String query = "DELETE FROM discussions WHERE iddiscussion = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, discussion.getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                discussionsList.remove(discussion); // Supprime la discussion de la TableView
                filterDiscussions(searchField.getText()); // Reapply search filter after deletion
                showAlert(Alert.AlertType.INFORMATION, "Discussion supprimée", "La discussion a été supprimée avec succès.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression de la discussion.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression.");
        }
    }

    // Méthode pour afficher des alertes
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
