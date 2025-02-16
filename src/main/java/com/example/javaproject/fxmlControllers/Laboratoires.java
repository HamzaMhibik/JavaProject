package com.example.javaproject.fxmlControllers;

import javafx.scene.control.cell.PropertyValueFactory;
import com.example.javaproject.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Laboratoires {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Laboratoire> tableView;

    @FXML
    private TableColumn<Laboratoire, Integer> columnId;

    @FXML
    private TableColumn<Laboratoire, String> columnNom;

    @FXML
    private TableColumn<Laboratoire, String> columnAdresse;

    @FXML
    private TableColumn<Laboratoire, Void> columnAction;

    private ObservableList<Laboratoire> laboratoiresList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuration des colonnes de la table
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        columnAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        // Charger les laboratoires depuis la base de données
        loadLaboratoires();

        // Filtrage des laboratoires en fonction de la recherche
        FilteredList<Laboratoire> filteredData = new FilteredList<>(laboratoiresList, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(laboratoire -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return laboratoire.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        laboratoire.getAdresse().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Trier les résultats filtrés
        SortedList<Laboratoire> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);

        // Ajouter le bouton de suppression à chaque ligne
        columnAction.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Laboratoire selectedLaboratoire = getTableRow().getItem();
                    if (selectedLaboratoire != null) {
                        deleteLaboratoire(selectedLaboratoire);
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
        });
    }

    // Charger les laboratoires depuis la base de données
    private void loadLaboratoires() {
        String query = "SELECT idlaboratoire, Nom, Adresse FROM laboratoires";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             var rs = stmt.executeQuery()) {

            while (rs.next()) {
                laboratoiresList.add(new Laboratoire(rs.getInt("idlaboratoire"), rs.getString("Nom"), rs.getString("Adresse")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un laboratoire
    private void deleteLaboratoire(Laboratoire laboratoire) {
        String query = "DELETE FROM laboratoires WHERE idlaboratoire = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, laboratoire.getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                laboratoiresList.remove(laboratoire);
                showAlert(Alert.AlertType.INFORMATION, "Laboratoire Supprimé", "Le laboratoire a été supprimé avec succès.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression du laboratoire.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression.");
        }
    }

    // Afficher une alerte pour informer l'utilisateur
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Ouvrir le formulaire pour ajouter un laboratoire
    @FXML
    private void openAddLaboratoireForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaproject/add_laboratoire_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(loader.load()));
            stage.setTitle("Ajouter un laboratoire");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
