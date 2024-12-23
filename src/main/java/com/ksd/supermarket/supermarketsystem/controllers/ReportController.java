package com.ksd.supermarket.supermarketsystem.controllers;

import com.ksd.supermarket.supermarketsystem.services.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportController {
    private static Connection connection;
    @FXML
    private PasswordField pinNum;

    @FXML
    private NumberAxis yy; // Y-axis for totalProfit
    @FXML
    private CategoryAxis xx; // X-axis for bilDate
    @FXML
    private LineChart<String, Number> chart; // Line chart for totalProfit vs bilDate
    @FXML
    private Label Rtext;

    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TableView<Report> reportTable;
    @FXML
    private TableColumn<Report, String> columDate;
    @FXML
    private TableColumn<Report, Float> columIncome;
    @FXML
    private TableColumn<Report, Float> columProfit;

    private final ObservableList<Report> reportData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        columDate.setCellValueFactory(new PropertyValueFactory<>("dateMonth"));
        columIncome.setCellValueFactory(new PropertyValueFactory<>("totalIncome"));
        columProfit.setCellValueFactory(new PropertyValueFactory<>("totalProfit"));

        // Configure the chart axes
        xx.setLabel("Bill Date");
        yy.setLabel("Total Profit");
        yy.setAutoRanging(false); // We'll set the range dynamically
    }

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
    public void dailyReort(ActionEvent actionEvent) {
        if(pinNum.getText().equals("1122")) {
            LocalDate sdate = startDate.getValue();
            LocalDate edate = endDate.getValue();
            Rtext.setText("Daily Report");

            if (sdate != null && edate != null) {
                try {
                    List<LocalDate> allDates = getAllDatesBetween(sdate, edate);
                    fetchAndAggregateReports(allDates);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("Please select both start and end dates.");
            }
        } else if (pinNum.getText().equals("")) {
            showAlert("Wrong PIN", "PIN field is empty. Please enter a valid PIN.");

        }else
        {showAlert("Wrong PIN", "Wrong PIN number. Please try again.");


        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: remove header text
        alert.setContentText(message);
        alert.showAndWait(); // Wait for the user to close the alert
    }
    private void fetchAndAggregateReports(List<LocalDate> allDates) {
        try {
            openConnection();

            String query = "SELECT bilDate, SUM(totalPrice) AS totalIncome, SUM(totalProfit) AS totalProfit " +
                    "FROM billData WHERE bilDate = ? GROUP BY bilDate";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                reportData.clear();
                chart.getData().clear(); // Clear existing chart data

                XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();
                profitSeries.setName("Total Profit");

                double maxProfit = 0; // Initialize max to track the highest totalProfit

                for (LocalDate date : allDates) {
                    preparedStatement.setString(1, date.toString());
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        String bilDate = resultSet.getString("bilDate");
                        float totalIncome = resultSet.getFloat("totalIncome");
                        float totalProfit = resultSet.getFloat("totalProfit");

                        // Update maxProfit
                        if (maxProfit < totalProfit) {
                            maxProfit = totalProfit;
                        }

                        // Add data points to the profit series
                        profitSeries.getData().add(new XYChart.Data<>(bilDate, totalProfit));

                        // Add to reportData for TableView
                        Report report = new Report(bilDate, totalIncome, totalProfit);
                        reportData.add(report);
                    }
                }

                // Add series to the LineChart
                chart.getData().add(profitSeries);

                // Update the yAxis upper bound
                updateYAxisUpperBound(maxProfit +10);

                // Set the data in the TableView
                reportTable.setItems(reportData);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching and aggregating report data.");
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void updateYAxisUpperBound(double max) {
        if (yy != null) {
            yy.setUpperBound(Math.ceil(max / 10) * 10); // Round up to the nearest 10 for better readability
            yy.setLowerBound(0); // Start from 0
        }
    }

    // Get all dates between startDate and endDate
    private List<LocalDate> getAllDatesBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }

        List<LocalDate> dates = new ArrayList<>();
        startDate.datesUntil(endDate.plusDays(1)).forEach(dates::add); // Include endDate
        return dates;
    }

    public void MonthlyReport(ActionEvent actionEvent) {
        if(pinNum.getText().equals("1122")) {
            LocalDate sdate = startDate.getValue();
            LocalDate edate = endDate.getValue();
            Rtext.setText("Monthly Report");

            if (sdate != null && edate != null) {
                try {
                    List<LocalDate> allMonths = getAllMonthsBetween(sdate, edate);
                    fetchAndAggregateMonthlyReports(allMonths);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("Please select both start and end dates.");
            }
        }else if (pinNum.getText().equals("")) {
            showAlert("Wrong PIN", "PIN field is empty. Please enter a valid PIN.");

        }else
        {showAlert("Wrong PIN", "Wrong PIN number. Please try again.");


        }

    }

    private void fetchAndAggregateMonthlyReports(List<LocalDate> allMonths) {
        try {
            openConnection();

            String query = "SELECT DATE_FORMAT(bilDate, '%Y-%m') AS month, " +
                    "SUM(totalPrice) AS totalIncome, SUM(totalProfit) AS totalProfit " +
                    "FROM billData WHERE bilDate BETWEEN ? AND ? GROUP BY month ORDER BY month";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, allMonths.get(0).withDayOfMonth(1).toString());
                preparedStatement.setString(2, allMonths.get(allMonths.size() - 1).withDayOfMonth(allMonths.get(allMonths.size() - 1).lengthOfMonth()).toString());

                reportData.clear();
                chart.getData().clear(); // Clear existing chart data

                XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();
                profitSeries.setName("Total Profit");

                double maxProfit = 0; // Track the maximum total profit for Y-axis scaling

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String month = resultSet.getString("month");
                    float totalIncome = resultSet.getFloat("totalIncome");
                    float totalProfit = resultSet.getFloat("totalProfit");

                    // Update maxProfit
                    if (maxProfit < totalProfit) {
                        maxProfit = totalProfit;
                    }

                    // Add data points to the profit series
                    profitSeries.getData().add(new XYChart.Data<>(month, totalProfit));

                    // Add to reportData for TableView
                    Report report = new Report(month, totalIncome, totalProfit);
                    reportData.add(report);
                }

                // Add series to the LineChart
                chart.getData().add(profitSeries);

                // Update the yAxis upper bound
                updateYAxisUpperBound(maxProfit + 10);

                // Set the data in the TableView
                reportTable.setItems(reportData);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching and aggregating monthly report data.");
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    // Get all months between startDate and endDate
    private List<LocalDate> getAllMonthsBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }

        List<LocalDate> months = new ArrayList<>();
        LocalDate current = startDate.withDayOfMonth(1); // Start at the beginning of the startDate's month
        while (!current.isAfter(endDate)) {
            months.add(current);
            current = current.plusMonths(1); // Move to the next month
        }
        return months;
    }

}
