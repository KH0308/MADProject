package com.example.workshop2test;

public class HistoryListDetails {
    private String ImgShop, nameShop, NameItem, time, date, status, itemID, payID, bsnID;
    private String qty;
    private String prc;

    public HistoryListDetails() {

    }

    public HistoryListDetails(String imgShop, String nameShop, String nameItem, String qty, String prc, String time, String date, String status) {
        this.ImgShop = imgShop;
        this.nameShop = nameShop;
        this.NameItem = nameItem;
        this.qty = qty;
        this.prc = prc;
        this.time = time;
        this.date = date;
        this.status = status;

    }


    public HistoryListDetails(String imgShop, String nameShop, String nameItem, String qty, String prc, String time, String date, String status, String itemID, String payID, String bsnID) {
        this.ImgShop = imgShop;
        this.nameShop = nameShop;
        this.NameItem = nameItem;
        this.qty = qty;
        this.prc = prc;
        this.time = time;
        this.date = date;
        this.status = status;
        this.itemID = itemID;
        this.payID = payID;
        this.bsnID = bsnID;
    }



    public String getDetailImgShop() {
        return ImgShop;
    }

    public void setDetailImgShop(String imgShop) {
        ImgShop = imgShop;
    }

    public String getDetailNameShop() {
        return nameShop;
    }

    public void setDetailNameShop(String nameShop) {
        this.nameShop = nameShop;
    }

    public String getDetailItemName() {
        return NameItem;
    }

    public void setDetailItemName(String nameItem) {
        NameItem = nameItem;
    }

    public String getDetailQuantity() {
        return qty;
    }

    public void setDetailQuantity(String qty) {
        this.qty = qty;
    }

    public String getDetailPrice() {
        return prc;
    }

    public void setDetailPrice(String prc) {
        this.prc = prc;
    }

    public String getDetailTime() {
        return time;
    }

    public void setDetailTime(String time) {
        this.time = time;
    }

    public String getDetailDate() {
        return date;
    }

    public void setDetailDate(String date) {
        this.date = date;
    }

    public String getDetailStatus() {
        return status;
    }

    public void setDetailStatus(String status) {this.status = status; }

    public void setDetailPayID(String payID) {
        this.status = payID;
    }

    public String getDetailPayID() {
        return payID;
    }

    public void setDetailItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getDetailItemID() {
        return itemID;
    }

    public void setDetailBsnID(String bsnID) {
        this.bsnID = bsnID;
    }

    public String getDetailBsnID() {
        return bsnID;
    }



}
