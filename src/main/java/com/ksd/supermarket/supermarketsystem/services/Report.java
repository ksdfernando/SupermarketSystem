package com.ksd.supermarket.supermarketsystem.services;

public class Report {
    private String DateMonth;
    private Float  TotalIncome;
    private Float  TotalProfit;

    public Report(String dateMonth, Float totalIncome, Float totalProfit) {
        DateMonth = dateMonth;
        TotalIncome = totalIncome;
        TotalProfit = totalProfit;
    }

    public Float getTotalIncome() {
        return TotalIncome;
    }

    public void setTotalIncome(Float totalIncome) {
        TotalIncome = totalIncome;
    }

    public String getDateMonth() {
        return DateMonth;
    }

    public void setDateMonth(String dateMonth) {
        DateMonth = dateMonth;
    }

    public Float getTotalProfit() {
        return TotalProfit;
    }

    public void setTotalProfit(Float totalProfit) {
        TotalProfit = totalProfit;
    }

    @Override
    public String toString() {
        return "Report{" +
                "DateMonth='" + DateMonth + '\'' +
                ", TotalIncome=" + TotalIncome +
                ", TotalProfit=" + TotalProfit +
                '}';
    }
}
