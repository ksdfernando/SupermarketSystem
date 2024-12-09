package com.ksd.supermarket.supermarketsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.*;

public class AddNewController {

    private static Connection connection;

    @FXML
    private TextField productName;

    @FXML
    private TextField sinhalaName;

    @FXML
    private TextField barcode;

    @FXML
    private TextField subCode;

    @FXML
    private TextField categoryNumber;

    @FXML
    private TextField stock;

    @FXML
    private TextField price;
    @FXML
    private TextField marketPrice;

    @FXML
    private TextField purchasePrice;

    public void EnterData(ActionEvent actionEvent) {
        try {
            // Check for null fields before proceeding
            if (productName == null || sinhalaName == null || barcode == null || subCode == null
                    || categoryNumber == null || stock == null || price == null) {
                System.out.println("One or more fields are not initialized in the FXML!");
                return;
            }

            // Initialize connection if not already connected
            if (connection == null) {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/smartmart?useUnicode=true&characterEncoding=UTF-8",
                        "root",
                        "admin");
            }

            // Retrieve input data
            String productNameText = productName.getText();
            String sinhalaNameText = sinhalaName.getText();
            String barcodeText = barcode.getText();
            String subCodeText = subCode.getText();
            String categoryNumberText = categoryNumber.getText();
            float stockValue = Float.parseFloat(stock.getText());
            float priceValue = Float.parseFloat(price.getText());
            float marketPriceValue = Float.parseFloat(marketPrice.getText());
            float purchasePriceValue = Float.parseFloat(purchasePrice.getText());

            System.out.println(productNameText + " " + sinhalaNameText + " " + barcodeText);

            // Validate input fields
//           if (productNameText.isEmpty() || sinhalaNameText.isEmpty() || barcodeText.isEmpty()
//                    || subCodeText.isEmpty() || categoryNumberText.isEmpty()) {
//                System.out.println("oooo");
//
//                return;
//            }

            // SQL Query to Insert Data
            String insertQuery = "INSERT INTO productdata (productName, sinhalaName, barcode, subCode, categoryNumber, stock_KG_num, price,MarketPrice,PurchasePrice) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?,?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                // Set parameters
                preparedStatement.setString(1, productNameText);
                preparedStatement.setString(2, sinhalaNameText);
                preparedStatement.setString(3, barcodeText);
                preparedStatement.setString(4, subCodeText);
                preparedStatement.setString(5, categoryNumberText);
                preparedStatement.setFloat(6, stockValue);
                preparedStatement.setFloat(7, priceValue);
                preparedStatement.setFloat(8, marketPriceValue);
                preparedStatement.setFloat(9, purchasePriceValue);

                // Execute the query
                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Product added successfully!");
                    alert.setContentText("Product added successfully!");
                    alert.showAndWait();

                    productName.clear();
                    sinhalaName.clear();
                    barcode.clear();
                    subCode.clear();
                    categoryNumber.clear();
                    stock.clear();
                    price.clear();
                    marketPrice.clear();
                    purchasePrice.clear();


                    productName.requestFocus();

                    System.out.println("Product added successfully!");

                } else {
                    System.out.println("Failed to add the product.");
                }
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(" Failed");
            alert.setContentText("Please fill in all fields!");
            alert.showAndWait();
            System.out.println("Please fill in all fields!");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error for debugging
        }
    }
}

