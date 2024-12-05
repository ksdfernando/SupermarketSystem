package com.ksd.supermarket.supermarketsystem.controllers;



import com.ksd.supermarket.supermarketsystem.services.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BillingController implements Initializable  {

@FXML
    private TextField codeNum;
@FXML
    private TextField qty;
@FXML
    private TextField cash;
@FXML
    private TextFlow change;
@FXML
    private TextFlow totalAmount;
@FXML
private TableView<Product> tableView;
@FXML
 private TableColumn<Product,String> subCode;
@FXML
  private TableColumn<Product,String> name;
@FXML
private TableColumn<Product,String> barcode;
@FXML
private TableColumn<Product,Float> unitPrice;
@FXML
private TableColumn<Product,Float> totalPrice;
@FXML
private TableColumn<Product,Float> totalQty;
@FXML
private TableColumn<Product,Float> myQTY;
@FXML
private TableColumn<Product,String> sinhalaName;


    float qtynum =1;
    float totalamount =0;
    int cc=0;


    ObservableList<Product> productList = FXCollections.observableArrayList(

    );

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        subCode.setCellValueFactory(new PropertyValueFactory<>("subCode"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        unitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        totalQty.setCellValueFactory(new PropertyValueFactory<>("totalQty"));
        sinhalaName.setCellValueFactory(new PropertyValueFactory<>("sinhalaName"));
        myQTY.setCellValueFactory(new PropertyValueFactory<>("myQTY"));



        System.out.println(productList);

        try {
            tableView.setItems(productList);
            System.out.println("Data successfully set!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }








private static Connection connection;
    private void openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/smartmart?useUnicode=true&characterEncoding=UTF-8",
                    "root",
                    "admin");
        }
    }
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








    public void codeEnter(KeyEvent keyEvent) {
        // Check if the Enter key was pressed
        if (keyEvent.getCode() == KeyCode.ENTER) {

            inputval();

            codeNum.clear();
            qty.clear();

        }


        if (keyEvent.getCode() == KeyCode.SHIFT) {
            String codeNumText = codeNum.getText(); // Retrieve text from the TextField
            System.out.println(codeNumText);        // Print the text to the console

            qty.requestFocus();
        }

    }

    public void qtyEnter    (KeyEvent keyEvent) {


        if (keyEvent.getCode() == KeyCode.ENTER) {



            ///////
            inputval();

            /////////////////////

            qty.clear();
            codeNum.clear();
            codeNum.requestFocus();
        }
    }
    public void inputval() {
        if(qty.getText().equals(null) || qty.getText().equals("")){
            qty.setText("1");
        }
        String codeNumText = codeNum.getText();


        String qtyText = qty.getText();
        float qtyfloat = Float.parseFloat(qtyText);
        if (codeNumText.isEmpty()) {
            System.out.println("Search text is empty");
            return;
        }
        ////// same Product remove
        ObservableList<Product> data = tableView.getItems();
        for (Product product : data) {

            if(codeNumText.equals(product.getSubCode()) || codeNumText.equals(product.getBarcode())){
                System.out.println("eee");
               float x= product.getTotalQty();
                System.out.println(x);
                System.out.println(qtyfloat);
                product.setTotalQty(qtyfloat+x);
                qtyfloat = qtyfloat+x;
                tableView.getItems().remove(product);

                System.out.println(product.getTotalQty());
            }
        }
       ////

            try {
                openConnection();

                String searchQuery = "SELECT * FROM productdata WHERE barcode = ?  OR subCode = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(searchQuery)) {
                    preparedStatement.setString(1, codeNumText); // Adding % for partial match
                    preparedStatement.setString(2, codeNumText);


                    ResultSet resultSet = preparedStatement.executeQuery();


                    boolean productFound = false;
                    while (resultSet.next()) {

                        String barcode = resultSet.getString("barcode");
                        String name = resultSet.getString("productName");
                        String subCode = resultSet.getString("subCode");
                        String sinhalaName = resultSet.getString("sinhalaName");
                        String stock = resultSet.getString("stock_KG_num");
                        Float stockfloat = Float.parseFloat(stock);
                        String price = resultSet.getString("price");
                        qtynum = Float.parseFloat(price);

                        productList.add(new Product(subCode, name, barcode, qtynum, qtyfloat, qtyfloat * qtynum, sinhalaName, stockfloat));
                        totalamount += qtyfloat * qtynum;
                        System.out.println();
                        String totalamountText = String.valueOf(totalamount);
                        Text productText = new Text(totalamountText);
                        productText.setStyle("-fx-font-size: 26px; -fx-fill: black; -fx-font-weight: bold; ");

                        totalAmount.getChildren().clear();
                        totalAmount.getChildren().add(productText);


                    }
//
//
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


    }

    public void calChage(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String cashtext= cash.getText();
            float cash=Float.parseFloat(cashtext);
            String cashmm=Float.toString(cash - totalamount);

           Text cashamount=new Text(cashmm);
            cashamount.setStyle("-fx-font-size: 26px; -fx-fill: black; -fx-font-weight: bold; ");

            change.getChildren().clear();
            change.getChildren().add(cashamount);


        }
    }

//    public void  check() {
//        ObservableList<Product> data = tableView.getItems();
//        for (Product product : data){
//            String checkBarcode = product.getBarcode();
//            String checkSubCode = product.getSubCode();
//
//            if(checkBarcode.equals(codeNum.getText())||checkSubCode.equals(codeNum.getText())){
//
//
//                float checkqty=product.getTotalQty();
//               float  newqty=Float.parseFloat(qty.getText()) + checkqty;
//                System.out.println(newqty);
//
//                System.out.println(product.getTotalQty());
//
//                cc=1;
//            }
//
//        }
//
//    }
    public void Bil(ActionEvent actionEvent) {
        ObservableList<Product> data = tableView.getItems();



        int i = 0;

        // Build receipt content
        StringBuilder receiptContent = new StringBuilder();
        receiptContent.append("Store Name\n");
        receiptContent.append("Address Line 1\n");
        receiptContent.append("Address Line 2\n");
        receiptContent.append("-------------------------------\n");
        receiptContent.append("PRODUCT     QTY   U/PRICE  T/PRICE\n");

        for (Product product : data) {

            i++;
            receiptContent.append(
                    product.getName() +"\n"+
                    product.getMyQTY() +
                    product.getUnitPrice() +
                    product.getTotalPrice());



            float sizenumber2 = product.getTotalQty();
            String getbarcodeText = product.getBarcode();
            float sizenumber = product.getMyQTY();

            try {
                openConnection();

                String updateQuery = "UPDATE productdata SET stock_KG_num = ? WHERE barcode = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setFloat(1, sizenumber - sizenumber2);
                    preparedStatement.setString(2, getbarcodeText);
                     product.setMyQTY(sizenumber-sizenumber2);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Stock updated for product: " + product.getName());
                    } else {
                        System.out.println("No rows updated. Please check the barcode.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid size number entered: ");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Database error occurred while updating stock.");
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }

        receiptContent.append("-------------------------------\n");
        receiptContent.append("Total     "+totalamount);
        receiptContent.append("\n");
        receiptContent.append("Cash      "+cash.getText());
        receiptContent.append("\n");
        float gg =Float.parseFloat(cash.getText())-totalamount;
        receiptContent.append("Chage     "+gg);



        receiptContent.append("-------------------------------\n");
        receiptContent.append("Thank you for shopping!\n");

        // Send data to the printer
       // sendToPrinter(receiptContent.toString());

        tableView.getItems().clear();
        cash.clear();
        change.getChildren().clear();
        totalAmount.getChildren().clear();
        totalamount=0;
    }
//        ObservableList<Product> data = tableView.getItems();
//
//
//
//        System.out.println("\n \n \n");
//            System.out.println("  PRODUCT  QTY   U/PRICE       T/PRICE"   );
//        int i=0;
//        for (Product product : data) {
//
//            System.out.println(++i  +"  "+ product.getName() + " \n " + product.getMyQTY()+"           " +product.getTotalQty() +"     " + product.getUnitPrice()+"     " + product.getTotalPrice() );
//            float sizenumber2 = product.getTotalQty();
//            String getbarcodeText = product.getBarcode();
//            float sizenumber= product.getMyQTY();
//
//            try {
//
//                openConnection();
//
//                String updateQuery = "UPDATE productdata SET stock_KG_num = ? WHERE barcode = ?";
//                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
//                    preparedStatement.setFloat(1, sizenumber - sizenumber2);
//                    preparedStatement.setString(2, getbarcodeText);
//
//                    int rowsAffected = preparedStatement.executeUpdate();
//
//                    if (rowsAffected > 0) {
//
//                    } else {
//                        System.out.println("No rows updated. Please check the barcode.");
//                    }
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid size number entered: " );
//                e.printStackTrace();
//            } catch (SQLException e) {
//                System.out.println("Database error occurred while updating stock.");
//                e.printStackTrace();
//            } finally {
//                closeConnection();
//            }
//        }
//        tableView.getItems().clear();
//        cash.clear();
//        change.getChildren().clear();
//        totalAmount.getChildren().clear();
//
//
//
//    }

    private void sendToPrinter(String data) {
        try (Socket socket = new Socket("192.168.0.100", 9100)) { // Replace with your printer's IP and port
            OutputStream out = socket.getOutputStream();

            // ESC/POS commands
            String initialize = "\u001B@"; // Initialize printer
            String cutPaper = "\u001DVA\u0001"; // Cut paper command

            // Send data
            out.write(initialize.getBytes());
            out.write(data.getBytes("UTF-8")); // Send receipt content
            out.write(cutPaper.getBytes());

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public void Bil(ActionEvent actionEvent) {
//        ObservableList<Product> data = tableView.getItems();
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bill.txt"))) {
//            writer.write("BILL DETAILS\n");
//            writer.write("============================\n");
//
//            int i = 0;
//            double totalAmount = 0;
//
//            for (Product product : data) {
//                writer.write((++i) + ". Product: " + product.getName() + "\n");
//                writer.write("   Qty: " + product.getTotalQty() + ", Unit Price: " + product.getUnitPrice() + ", Total Price: " + product.getTotalPrice() + "\n\n");
//                totalAmount += product.getTotalPrice();
//            }
//
//            writer.write("============================\n");
//            writer.write("TOTAL AMOUNT: " + totalAmount + "\n");
//            writer.write("============================\n");
//
//            System.out.println("Bill successfully generated: bill.txt");
//        } catch (IOException e) {
//            System.err.println("Error writing the bill: " + e.getMessage());
//        }
//    }

    public void newbil(ActionEvent actionEvent) throws IOException {



        Stage newStage = new Stage();
        newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/ksd/supermarket/supermarketsystem/views/billing.fxml"))));
        newStage.setTitle("Billing");

        newStage.initModality(Modality.WINDOW_MODAL);


        newStage.show();
    }


    public void rowdel(ActionEvent actionEvent) {
        Object selectedItem = tableView.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this item?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    tableView.getItems().remove(selectedItem);
                    if (selectedItem instanceof Product) { // Replace YourCustomClass with the actual class name
                        Product item = (Product) selectedItem;


                        System.out.println(item.getTotalPrice());

                   float totalAmountFloat=totalamount;

                   Text nextTotal = new Text(String.valueOf(totalAmountFloat-item.getTotalPrice()));
                   totalamount=totalAmountFloat-item.getTotalPrice();
                   nextTotal.setStyle("-fx-font-size: 26px; -fx-fill: black; -fx-font-weight: bold; ");
                    totalAmount.getChildren().clear();
                    totalAmount.getChildren().add(nextTotal);

                }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No item selected to delete!");
            alert.showAndWait();
        }
    }
}
