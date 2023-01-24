package com.example.workshop2test;

public class Report {
    String tscId, itmName, time, date, year;
    double TotalPrice;

    public Report(String tscId, String itmName, String time, String date, String year, double totalPrice) {
        this.tscId = tscId;
        this.itmName = itmName;
        this.time = time;
        this.date = date;
        this.year = year;
        this.TotalPrice = totalPrice;
    }

    public String getTscId() {
        return tscId;
    }

    public void setTscId(String tscId) {
        this.tscId = tscId;
    }

    public String getItmName() {
        return itmName;
    }

    public void setItmName(String itmName) {
        this.itmName = itmName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }
}
