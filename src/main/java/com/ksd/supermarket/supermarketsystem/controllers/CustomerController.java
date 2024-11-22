package com.ksd.supermarket.supermarketsystem.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CustomerController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}