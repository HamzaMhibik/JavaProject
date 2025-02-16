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
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        columnAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        loadLaboratoires();

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

        SortedList<Laboratoire> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

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

    @FXML
    private void openAddLaboratoireForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaproject/addLaboratoire.fxml"));
            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(loader.load()));
            stage.setTitle("Ajouter un laboratoire");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
