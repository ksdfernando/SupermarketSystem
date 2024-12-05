module com.ksd.supermarket.supermarketsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires itextpdf;

    opens com.ksd.supermarket.supermarketsystem to javafx.fxml;
    exports com.ksd.supermarket.supermarketsystem;

    exports com.ksd.supermarket.supermarketsystem.controllers;
    opens com.ksd.supermarket.supermarketsystem.controllers to javafx.fxml;

    // Open the services package to JavaFX for reflective access
    opens com.ksd.supermarket.supermarketsystem.services to javafx.base;
}
