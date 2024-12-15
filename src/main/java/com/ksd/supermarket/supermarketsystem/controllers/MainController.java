package com.ksd.supermarket.supermarketsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

    @FXML
    private StackPane contentArea;

    public void addNew(ActionEvent actionEvent) throws IOException {
        System.out.println("AddNew");

        Parent fxml = FXMLLoader.load(getClass().getResource("/com/ksd/supermarket/supermarketsystem/views/addNew.fxml"));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);


    }
    public void addStock(ActionEvent actionEvent) throws IOException {
        System.out.println("AddStock");

        Parent fxml = FXMLLoader.load(getClass().getResource("/com/ksd/supermarket/supermarketsystem/views/addStock.fxml"));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);


    }



    public void biling(ActionEvent actionEvent) throws IOException {
        System.out.println("Billing");


        Stage newStage = new Stage();
        newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/ksd/supermarket/supermarketsystem/views/billing.fxml"))));
        newStage.setTitle("Billing");

        newStage.initModality(Modality.WINDOW_MODAL);

        newStage.setMaximized(true);
        newStage.show();

    }

    public void Settings(ActionEvent actionEvent) throws IOException {
        System.out.println("Settigns");

        Parent fxml = FXMLLoader.load(getClass().getResource("/com/ksd/supermarket/supermarketsystem/views/settigns.fxml"));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);

    }

    public void Mystock(ActionEvent actionEvent) throws IOException {
        System.out.println("my stock");

        Parent fxml = FXMLLoader.load(getClass().getResource("/com/ksd/supermarket/supermarketsystem/views/myStock.fxml"));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(fxml);
    }
}
