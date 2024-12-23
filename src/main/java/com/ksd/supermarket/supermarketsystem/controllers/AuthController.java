package com.ksd.supermarket.supermarketsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthController {

    private static Connection connection;

    @FXML
    private TextField inputname;
    @FXML
    private PasswordField inputpassword;


    public void gotoMain(ActionEvent actionEvent) {
        try {

            // Check or establish the database connection
            if (connection == null) {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/smartmart", "root", "admin");
            }

            // Validate login credentials
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM logindata  ");

            if (resultSet.next()) {
                String username1 = resultSet.getString(1);
                String password1 = resultSet.getString(2);

                resultSet.next();
                String username = resultSet.getString(1);
                String password = resultSet.getString(2);


                if ((inputname.getText().equals(username) && inputpassword.getText().equals(password)) || (inputname.getText().equals(username1) && inputpassword.getText().equals(password1)) ) {
                    System.out.println("Login successful");


                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/com/ksd/supermarket/supermarketsystem/views/main.fxml"));
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();


                        Scene scene = new Scene(root);
                        stage.setScene(scene);


                        stage.setMaximized(true);


                        stage.show();
                    } catch (IOException e) {
                        System.out.println("Failed to load main.fxml: " + e.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Loading Error");
                        alert.setHeaderText("Could not load the main view");
                        alert.setContentText("Please contact support.");
                        alert.showAndWait();
                    }
                } else {
                    // Incorrect credentials
                    System.out.println("Login failed");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText("Incorrect Username or Password");
                    alert.setContentText("Please try again.");
                    alert.showAndWait();

                    inputname.clear();
                    inputpassword.clear();
                   inputname.requestFocus();
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Cannot connect to the database");
            alert.setContentText("Please check your database connection and try again.");
            alert.showAndWait();
        }
    }
}
