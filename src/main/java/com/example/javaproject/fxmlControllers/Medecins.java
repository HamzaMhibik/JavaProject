package com.example.javaproject.fxmlControllers;

import com.example.javaproject.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Medecins {

    @FXML
    private TableView<Medecin> tableView;

    @FXML
    private TableColumn<Medecin, Integer> columnId;
    @FXML
    private TableColumn<Medecin, String> columnNom;
    @FXML
    private TableColumn<Medecin, String> columnPrenom;
    @FXML
    private TableColumn<Medecin, String> columnEmail;
    @FXML
    private TableColumn<Medecin, String> columnTelephone;
    @FXML
    private TableColumn<Medecin, String> columnMotdepasse;
    @FXML
    private TableColumn<Medecin, Integer> columnAge;
    @FXML
    private TableColumn<Medecin, String> columnSexe;
    @FXML
    private TableColumn<Medecin, String> columnVille;
    @FXML
    private TableColumn<Medecin, String> columnNomUtilisateur;
    @FXML
    private TableColumn<Medecin, Void> columnAction;

    @FXML
    private TextField searchField;

    private ObservableList<Medecin> medecinsList = FXCollections.observableArrayList();

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

        columnAction.setCellFactory(createDeleteButtonCellFactory());

        loadMedecins("");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadMedecins(newValue);
        });
    }

    private Callback<TableColumn<Medecin, Void>, TableCell<Medecin, Void>> createDeleteButtonCellFactory() {
        return param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Medecin selectedMedecin = getTableRow().getItem();
                    if (selectedMedecin != null) {
                        deleteMedecin(selectedMedecin);
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

    private void loadMedecins(String filter) {
        medecinsList.clear();
        String query = "SELECT idmedecin, Nom, Prenom, Email, Telephone, Motdepasse, Age, Sexe, Ville, NomUtilisateur FROM medecins WHERE Nom LIKE ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + filter + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                medecinsList.add(new Medecin(
                        rs.getInt("idmedecin"),
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
            tableView.setItems(medecinsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteMedecin(Medecin medecin) {
        String query = "DELETE FROM medecins WHERE idmedecin = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, medecin.getId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                medecinsList.remove(medecin);
                showAlert(Alert.AlertType.INFORMATION, "Médecin supprimé", "Le médecin a été supprimé avec succès.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression du médecin.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression.");
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
    private void openAddMedecinForm() {
        try {
            Stage addMedecinStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaproject/add_medecin.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            addMedecinStage.setScene(scene);
            addMedecinStage.setTitle("Ajouter un Médecin");
            addMedecinStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire d'ajout.");
        }
    }
}
