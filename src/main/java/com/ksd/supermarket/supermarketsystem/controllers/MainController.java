package com.ksd.supermarket.supermarketsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

    @FXML
    private StackPane contentArea;

    public void addNew(ActionEvent actionEvent) throws IOException {
        System.out.println("qqq");

        Parent fxml = FXMLLoader.load(getClass().getResource("/com/ksd/supermarket/supermarketsystem/views/addNew.fxml"));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);


    }
    public void addStock(ActionEvent actionEvent) throws IOException {
        System.out.println("qqq");

        Parent fxml = FXMLLoader.load(getClass().getResource("/com/ksd/supermarket/supermarketsystem/views/addStock.fxml"));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);


    }
}
