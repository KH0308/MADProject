package com.example.workshop2test;

import java.sql.Date;
import java.sql.Timestamp;

public class HistoryList {
    //private String odrUserID, totalPrice, orderStatus, timestamp;
    private String payID, odruserID, payOpt, totalPay, payTime, payDate, payStatus;
    public HistoryList(){
    }

    public HistoryList(String payID, String odruserID, String payOpt, String totalPay, String payTime,String payDate,String payStatus) {
        this.payID = payID;
        this.odruserID = odruserID;
        this.payOpt = payOpt;
        this.totalPay = totalPay;
        this.payTime = payTime;
        this.payDate = payDate;
        this.payStatus = payStatus;

    }

    public String getPayID() {
        return payID;
    }

    public void setPayID(String payID) {
        this.payID = payID;
    }

    public String getOdruserID() {
        return odruserID;
    }

    public void setOdruserID(String odruserID) {
        this.odruserID = odruserID;
    }

    public String getPayOpt() {
        return payOpt;
    }

    public void setPayOpt(String payOpt) {
        this.payOpt = payOpt;
    }

    public String getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(String totalPay) {
        this.totalPay = totalPay;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }



}
