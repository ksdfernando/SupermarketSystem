package com.ksd.supermarket.supermarketsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SettignsController {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/smartmart";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "admin";

    @FXML
    private TextField name; // Current username
    @FXML
    private PasswordField password; // Current password
    @FXML
    private TextField nName; // New username
    @FXML
    private PasswordField nPassword; // New password

    @FXML
    public void updateCredentials(ActionEvent event) {
        String currentName = name.getText();
        String currentPassword = password.getText();
        String newName = nName.getText();
        String newPassword = nPassword.getText();

        if (currentName.isEmpty() || currentPassword.isEmpty() || newName.isEmpty() || newPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required!");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String query = "UPDATE logindata SET username = ?, password = ? WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, newName);
            statement.setString(2, newPassword);
            statement.setString(3, currentName);
            statement.setString(4, currentPassword);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Credentials updated successfully.");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid current username or password.");
                clearFields();
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update credentials: " + e.getMessage());
        }
    }

    private void clearFields() {
        name.clear();
        password.clear();
        nName.clear();
        nPassword.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
