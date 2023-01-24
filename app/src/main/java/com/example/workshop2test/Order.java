package com.example.workshop2test;

public class Order {
    String TransID, menuName, ShopID, StatusOdr;
    Integer quantity;
    double price;

    public Order(String TransID, String menuName, String ShopID, int quantity, double price,
                 String StatusOdr) {
        this.TransID = TransID;
        this.menuName = menuName;
        this.ShopID = ShopID;
        this.quantity = quantity;
        this.price = price;
        this.StatusOdr = StatusOdr;
    }

    public String getShopID() {
        return ShopID;
    }

    public void setShopID(String shopID) {
        ShopID = shopID;
    }

    public String getTransID() {
        return TransID;
    }

    public void setTransID(String transID) {
        TransID = transID;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatusOdr() {
        return StatusOdr;
    }

    public void setStatusOdr(String statusOdr) {
        StatusOdr = statusOdr;
    }
}
