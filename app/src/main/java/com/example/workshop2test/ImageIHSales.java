package com.example.workshop2test;

import java.io.Serializable;

public class ImageIHSales implements Serializable {

    String shopId, itemID, imageItem, imageName;

    public ImageIHSales(String shopId, String itemID, String imageItem, String imageName) {
        this.shopId = shopId;
        this.itemID = itemID;
        this.imageItem = imageItem;
        this.imageName = imageName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageItem() {
        return imageItem;
    }

    public void setImageItem(String imageItem) {
        this.imageItem = imageItem;
    }
}
