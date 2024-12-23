package com.ksd.supermarket.supermarketsystem.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label timeLabel;

    @FXML
    public void initialize() {
        // Set the welcome message
        welcomeLabel.setText("Welcome to the Smart Mart!");

        // Initialize the clock and calendar
        updateDateTime();
    }

    private void updateDateTime() {
        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    LocalDateTime now = LocalDateTime.now();

                    // Format the date as a calendar style
                    String formattedDate = now.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
                    dateLabel.setText(formattedDate);

                    // Format the time as a clock
                    String formattedTime = now.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
                    timeLabel.setText(formattedTime);
                });
            }
        };

        // Schedule the task to run every second
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
}
