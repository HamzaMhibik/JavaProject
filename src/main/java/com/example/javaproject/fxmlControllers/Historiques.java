package com.example.javaproject.fxmlControllers;

import com.example.javaproject.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Historiques {

    @FXML
    private TextField searchField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Historique> tableView;

    @FXML
    private TableColumn<Historique, Integer> columnId;

    @FXML
    private TableColumn<Historique, String> columnUtilisateur;

    @FXML
    private TableColumn<Historique, String> columnDate;

    @FXML
    private TableColumn<Historique, String> columnAction;

    private ObservableList<Historique> historiquesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnUtilisateur.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        columnAction.setCellFactory(new Callback<TableColumn<Historique, String>, TableCell<Historique, String>>() {
            @Override
            public TableCell<Historique, String> call(TableColumn<Historique, String> param) {
                return new TableCell<Historique, String>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Historique selectedHistorique = getTableRow().getItem();
                            if (selectedHistorique != null) {
                                deleteHistorique(selectedHistorique);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
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

        loadHistoriques();

        FilteredList<Historique> filteredData = new FilteredList<>(historiquesList, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(historique -> {
                if ((newValue == null || newValue.isEmpty()) && datePicker.getValue() == null) {
                    return true;
                }
                String lowerCaseFilter = (newValue != null) ? newValue.toLowerCase() : "";
                boolean matchesUser = historique.getUtilisateur().toLowerCase().contains(lowerCaseFilter);

                if (datePicker.getValue() != null) {
                    String selectedDate = datePicker.getValue().toString(); // Conversion de LocalDate en String (format yyyy-MM-dd)
                    return matchesUser && historique.getDate().equals(selectedDate);
                }
                return matchesUser;
            });
        });

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(historique -> {
                if ((searchField.getText() == null || searchField.getText().isEmpty()) && newValue == null) {
                    return true;
                }
                String lowerCaseFilter = (searchField.getText() != null) ? searchField.getText().toLowerCase() : "";
                boolean matchesUser = historique.getUtilisateur().toLowerCase().contains(lowerCaseFilter);

                if (newValue != null) {
                    String selectedDate = newValue.toString(); // Conversion de LocalDate en String (format yyyy-MM-dd)
                    return matchesUser && historique.getDate().equals(selectedDate);
                }
                return matchesUser;
            });
        });

        SortedList<Historique> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    private void loadHistoriques() {
        String query = "SELECT idhistorique, utilisateur, date FROM historiques";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             var rs = stmt.executeQuery()) {

            while (rs.next()) {
                historiquesList.add(new Historique(rs.getInt("idhistorique"), rs.getString("utilisateur"), rs.getString("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteHistorique(Historique historique) {
        String query = "DELETE FROM historiques WHERE idhistorique = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, historique.getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                historiquesList.remove(historique);
                showAlert(Alert.AlertType.INFORMATION, "Historique supprimé", "L'historique a été supprimé avec succès.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression de l'historique.");
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
}