package com.example.workshop2test;
import java.lang.String;

public class ListItemOrder {
    private String transID;
    private String shpID;
    private String shopName;
    private String itmID;
    private String itmName;
    private String itmDescription;
    private double itmPrice;
    private Integer itmQuantity;
    private double itmTotalPrice;
    private String itmImage;

    public ListItemOrder(){

    }

    public ListItemOrder(String transID, String shpID, String shopName, String itmID, String itmName, String itmDescription,
                         double itmPrice, Integer itmQuantity, double itmTotalPrice) {
        this.transID = transID;
        this.shpID = shpID;
        this.shopName = shopName;
        this.itmID = itmID;
        this.itmName = itmName;
        this.itmDescription = itmDescription;
        this.itmPrice = itmPrice;
        this.itmQuantity = itmQuantity;
        this.itmTotalPrice = itmTotalPrice;

    }

    public ListItemOrder(String transID, String shpID, String shopName, String itmID, String itmName, String itmDescription,
                         double itmPrice, Integer itmQuantity, double itmTotalPrice, String itmImage) {
        this.transID = transID;
        this.shpID = shpID;
        this.shopName = shopName;
        this.itmID = itmID;
        this.itmName = itmName;
        this.itmDescription = itmDescription;
        this.itmPrice = itmPrice;
        this.itmQuantity = itmQuantity;
        this.itmTotalPrice = itmTotalPrice;
        this.itmImage = itmImage;

    }

    public String getShpID() {
        return shpID;
    }

    public void setShpID(String shpID) {
        this.shpID = shpID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public String getItmID() {
        return itmID;
    }

    public void setItmID(String itmID) {
        this.itmID = itmID;
    }

    public String getItmName() {
        return itmName;
    }

    public void setItmName(String itmName) {
        this.itmName = itmName;
    }

    public String getItmDescription() {
        return itmDescription;
    }

    public void setItmDescription(String itmDescription) {
        this.itmDescription = itmDescription;
    }

    public double getItmPrice() {
        return itmPrice;
    }

    public void setItmPrice(double itmPrice) {
        this.itmPrice = itmPrice;
    }

    public Integer getItmQuantity() {
        return itmQuantity;
    }

    public void setItmQuantity(Integer itmQuantity) {
        this.itmQuantity = itmQuantity;
    }

    public double getItmTotalPrice() {
        return itmTotalPrice;
    }

    public void setItmTotalPrice(double itmTotalPrice) {
        this.itmTotalPrice = itmTotalPrice;
    }

    public String getItmImage() {
        return itmImage;
    }

    public void setItmImage(String itmImage) {
        this.itmImage = itmImage;
    }

}
