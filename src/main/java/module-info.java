module com.ksd.supermarket.supermarketsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.ksd.supermarket.supermarketsystem to javafx.fxml;
    exports com.ksd.supermarket.supermarketsystem;
    exports com.ksd.supermarket.supermarketsystem.controllers;
    opens com.ksd.supermarket.supermarketsystem.controllers to javafx.fxml;
}