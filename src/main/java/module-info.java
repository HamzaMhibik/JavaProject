module com.example.javaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;  // Pour JDBC (MySQL)

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // Ouvre uniquement pour JavaFX FXML
    opens com.example.javaproject to javafx.fxml;
    opens com.example.javaproject.fxmlControllers to javafx.base, javafx.fxml;

    // Exporte seulement les packages nécessaires
    exports com.example.javaproject;
}
