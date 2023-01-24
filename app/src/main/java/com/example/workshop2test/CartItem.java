package com.example.workshop2test;

public class CartItem {
    private String itemName;
    private String itemDescription;
    private String itemPrice;
    private String itemQuantity;
    private String itemTotalPrice;
    private String id;
    private String itemImage;

    public CartItem(){

    }

    public CartItem(String itemName, String itemDescription, String itemPrice, String itemQuantity) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;

    }

    public CartItem(String itemName, String itemDescription, String itemPrice, String itemQuantity, String itemTotalPrice) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemTotalPrice = itemTotalPrice;
    }

    public CartItem(String itemName, String itemDescription, String itemPrice, String itemQuantity, String itemTotalPrice, String id, String itemImage) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemTotalPrice = itemTotalPrice;
        this.id=id;
        this.itemImage = itemImage;

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(String itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
