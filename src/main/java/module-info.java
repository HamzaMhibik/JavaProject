module com.example.javaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires java.sql;  // Add this line to use JDBC (MySQL)

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.javaproject to javafx.fxml;
    exports com.example.javaproject;

    exports com.example.javaproject.fxmlControllers to javafx.fxml;
    opens com.example.javaproject.fxmlControllers to javafx.fxml;
    opens com.example.javaproject.models to javafx.base;

}
