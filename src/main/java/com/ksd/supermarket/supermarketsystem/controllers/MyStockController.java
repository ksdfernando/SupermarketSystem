package com.ksd.supermarket.supermarketsystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.ksd.supermarket.supermarketsystem.services.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyStockController {
    private static Connection connection;
    @FXML
    private PasswordField pinNum;

    @FXML
    private TextField searchtext;
    @FXML
    private TextField getBarcode;
    @FXML
    private TextField getSize;
    @FXML
    private TextField newSalesPrice;
    @FXML
    private TextField newPurchasePrice;
    @FXML
    private TextField newMarketPrice;
    @FXML
    private TableView<Product> resultTable;
    @FXML
    private TableColumn<Product, String> subCodeColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> sinhalaNameColumn;
    @FXML
    private TableColumn<Product, String> barcodeColumn;
    @FXML
    private TableColumn<Product, Float> stockColumn;
    @FXML
    private TableColumn<Product, Float> salesPriceColumn;
    @FXML
    private TableColumn<Product, Float> marketPriceColumn;
    @FXML
    private TableColumn<Product, Float> purchasePriceColumn;

    private String numstock;

    private ObservableList<Product> products = FXCollections.observableArrayList();

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
            } catch (SQLException e) {
                System.out.println("Error closing database connection.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void initialize() {
        // Set up the TableView columns
        subCodeColumn.setCellValueFactory(new PropertyValueFactory<>("subCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sinhalaNameColumn.setCellValueFactory(new PropertyValueFactory<>("sinhalaName"));
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("totalQty"));
        salesPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        marketPriceColumn.setCellValueFactory(new PropertyValueFactory<>("marketPrice"));
        purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // Bind the ObservableList to the TableView
        resultTable.setItems(products);
    }

    public void search(ActionEvent actionEvent) {
        if(pinNum.getText().equals("1122")) {
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
                preparedStatement.setString(1, "%" + searchtextText + "%");
                preparedStatement.setString(2, "%" + searchtextText + "%");
                preparedStatement.setString(3, "%" + searchtextText + "%");

                ResultSet resultSet = preparedStatement.executeQuery();
                products.clear();

                boolean productFound = false;
                while (resultSet.next()) {
                    String barcode = resultSet.getString("barcode");
                    String name = resultSet.getString("productName");
                    String subCode = resultSet.getString("subCode");
                    String sinhalaName = resultSet.getString("sinhalaName");
                    Float stock = resultSet.getFloat("stock_KG_num");
                    Float marketPrice = resultSet.getFloat("MarketPrice");
                    Float salesPrice = resultSet.getFloat("price");
                    Float purchasePrice = resultSet.getFloat("PurchasePrice");

                    Product product = new Product(subCode, name, barcode, salesPrice, stock, purchasePrice, sinhalaName, null, marketPrice);
                    products.add(product);

                    getBarcode.setText(barcode);
                    numstock = stock.toString();
                    productFound = true;
                }

                if (!productFound) {
                    System.out.println("No products found for: " + searchtextText);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during search operation.");
            e.printStackTrace();
        } finally {
            closeConnection();
        }}else if (pinNum.getText().equals("")) {
        showAlert("Wrong PIN", "PIN field is empty. Please enter a valid PIN.");

    }else
    {showAlert("Wrong PIN", "Wrong PIN number. Please try again.");


    }
    }

    public void addd() {
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


                if (rowsAffected > 0) {
                    System.out.println("Stock updated successfully.");
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

    public void updatePrice(ActionEvent actionEvent) {
        String getSizeText = getSize.getText();


        String getBarcodeText = getBarcode.getText();
        String newSalesPriceText = newSalesPrice.getText();
        String newPurchasePriceText = newPurchasePrice.getText();
        String newMarketPriceText =newMarketPrice.getText();

        // Validate inputs
        if (getBarcodeText.isEmpty()) {
            System.out.println("Barcode is empty. Update cannot proceed.");
            return;
        }

        if (newSalesPriceText.isEmpty() && newPurchasePriceText.isEmpty() && newMarketPriceText.isEmpty()) {
            System.out.println("Both sales price and purchase price fields are empty.");
            return;
        }

        try {
            float sizenumber = Float.parseFloat(numstock);
            float sizenumber2 = Float.parseFloat(getSizeText);
            Float newSalesPriceValue = newSalesPriceText.isEmpty() ? null : Float.parseFloat(newSalesPriceText);
            Float newPurchasePriceValue = newPurchasePriceText.isEmpty() ? null : Float.parseFloat(newPurchasePriceText);

            openConnection();

            String updateQuery = "UPDATE productdata SET price = COALESCE(?, price), PurchasePrice = COALESCE(?, PurchasePrice),MarketPrice =COALESCE(?, MarketPrice), stock_KG_num =COALESCE(?, stock_KG_num)  WHERE barcode = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setObject(1, newSalesPriceValue, java.sql.Types.FLOAT);
                preparedStatement.setObject(2, newPurchasePriceValue, java.sql.Types.FLOAT);
                preparedStatement.setObject(3, newMarketPriceText, java.sql.Types.FLOAT);
                preparedStatement.setFloat(4, sizenumber + sizenumber2);

                preparedStatement.setString(5, getBarcodeText);

                int rowsAffected = preparedStatement.executeUpdate();
                newSalesPrice.clear();
                newPurchasePrice.clear();
                newMarketPrice.clear();
                getBarcode.clear();
                getBarcode.clear();
                getSize.clear();

                if (rowsAffected > 0) {
                    System.out.println("Price updated successfully.");
                } else {
                    System.out.println("No rows updated. Please check the barcode.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid price entered. Please enter numeric values for prices.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database error occurred while updating prices.");
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: remove header text
        alert.setContentText(message);
        alert.showAndWait(); // Wait for the user to close the alert
    }
}
