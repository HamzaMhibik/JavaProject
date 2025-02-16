package com.example.javaproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class HelloController {

    @FXML
    private VBox center; // Conteneur pour la zone centrale


    // Méthode appelée lorsque le bouton "Médecins" est cliqué
    @FXML
    private void onDoctorsClick() {
        loadView("Medecins.fxml");
    }

    // Méthode appelée lorsque le bouton "Patients" est cliqué
    @FXML
    private void onPatientsClick() {
        loadView("Patients.fxml");
    }

    // Méthode appelée lorsque le bouton "Articles" est cliqué
    @FXML
    private void onArticlesClick() {
        loadView("Articles.fxml");
    }

    // Méthode appelée lorsque le bouton "Historique" est cliqué
    @FXML
    private void onHistoryClick() {
        loadView("Historique.fxml");
    }

    // Méthode appelée lorsque le bouton "Calendrier" est cliqué
    @FXML
    private void onCalendarClick() {
        loadView("Calendrier.fxml");
    }

    // Méthode appelée lorsque le bouton "Tableau de Bord" est cliqué
    @FXML
    private void onDashboardClick() {
        loadView("Dashboard.fxml");
    }

    // Méthode appelée lorsque le bouton "Laboratoires" est cliqué
    @FXML
    private void onLaboratoriesClick() {
        loadView("Laboratoires.fxml");
    }

    // Méthode appelée lorsque le bouton "Discussion" est cliqué
    @FXML
    private void onDiscussionClick() {
        loadView("Discussion.fxml");
    }
    // Méthode pour charger un fichier FXML dans la zone centrale
    private void loadView(String fxmlFile) {
        System.out.println("Valeur de center: " + center); // Débogage

        if (center != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Parent root = loader.load();
                center.getChildren().clear();
                center.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Erreur : Le conteneur 'center' est null.");
        }
    }


}
