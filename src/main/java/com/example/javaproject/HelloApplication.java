package com.example.javaproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin-dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);  // Taille initiale de la fenêtre
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("Admin Dashboard");
        stage.setScene(scene);

        // Maximiser la fenêtre sans passer en plein écran
        stage.setMaximized(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
