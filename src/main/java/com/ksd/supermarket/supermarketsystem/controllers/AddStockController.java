package com.ksd.supermarket.supermarketsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddStockController {
    private static Connection connection;

    @FXML
    private TextField searchtext;
    @FXML
    private TextField getBarcode;
    @FXML
    private TextField getSize;
    @FXML
    private TextFlow resultTextFlow;

    String numstock ;

    // Open the database connection
    private void openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/smartmart?useUnicode=true&characterEncoding=UTF-8",
                    "root",
                    "admin");
        }
    }

    // Close the database connection
    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing database connection.");
                e.printStackTrace();
            }
        }
    }

    public void search(ActionEvent actionEvent) {
        String searchtextText = searchtext.getText();

        // Check if the search text is empty
        if (searchtextText.isEmpty()) {
            System.out.println("Search text is empty");
            return;
        }

        System.out.println("Searching for: " + searchtextText);

        try {
            openConnection();

            String searchQuery = "SELECT * FROM productdata WHERE barcode LIKE ? OR productName LIKE ? OR subCode LIKE ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(searchQuery)) {
                preparedStatement.setString(1, "%" + searchtextText + "%"); // Adding % for partial match
                preparedStatement.setString(2, "%" + searchtextText + "%");
                preparedStatement.setString(3, "%" + searchtextText + "%");

                ResultSet resultSet = preparedStatement.executeQuery();
                resultTextFlow.getChildren().clear();

                boolean productFound = false;
                while (resultSet.next()) {
                    String barcode = resultSet.getString("barcode");
                    String name = resultSet.getString("productName");
                    String subCode = resultSet.getString("subCode");
                    String sinhalaName = resultSet.getString("sinhalaName");
                    String stock = resultSet.getString("stock_KG_num");

                    System.out.println("Found product: " + name + " (" + barcode + ")");
                    Text productText = new Text("           " + subCode + "         " + name + "         " + sinhalaName + "         " + barcode + "         " + stock + "\n");
                    productText.setStyle("-fx-font-size: 16px; -fx-fill: black;");
                    numstock = stock;
                    resultTextFlow.getChildren().add(productText);
                    getBarcode.setText(barcode);
                    productFound = true;
                }

                if (!productFound) {
                    Text noResultText = new Text("No products found for: " + searchtextText);
                    noResultText.setStyle("-fx-font-size: 14px; -fx-fill: red;");
                    resultTextFlow.getChildren().add(noResultText);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during search operation.");
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void add(ActionEvent actionEvent) {
        String getSizeText = getSize.getText();
        String getbarcodeText = getBarcode.getText();

        // Validate inputs
        if (getSizeText.isEmpty()) {
            System.out.println("Size field is empty. Update cannot proceed.");
            return;
        }

        if (getbarcodeText.isEmpty()) {
            System.out.println("Barcode is empty. Update cannot proceed.");
            return;
        }

        try {
            float sizenumber = Float.parseFloat(numstock);
            float sizenumber2 = Float.parseFloat(getSizeText);


            openConnection();

            String updateQuery = "UPDATE productdata SET stock_KG_num = ? WHERE barcode = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setFloat(1, sizenumber + sizenumber2);
                preparedStatement.setString(2, getbarcodeText);

                int rowsAffected = preparedStatement.executeUpdate();
                getBarcode.clear();
                getSize.clear();
                System.out.println("xxxx");

                if (rowsAffected > 0) {
                    System.out.println("Rows updated successfully: " + rowsAffected);
                } else {
                    System.out.println("No rows updated. Please check the barcode.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid size number entered: " + getSizeText);
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database error occurred while updating stock.");
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
}
