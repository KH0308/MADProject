package com.example.workshop2test;

public class Menu {
    String MenuItemId, bsnId, menuId, categoryId, nameItem, description, imgItm;
    double price;

    public Menu(String imgItm, String menuItemId, String bsnId, String menuId, String categoryId,
                String nameItem, String description, double price) {
        this.imgItm = imgItm;
        this.MenuItemId = menuItemId;
        this.bsnId = bsnId;
        this.menuId = menuId;
        this.categoryId = categoryId;
        this.nameItem = nameItem;
        this.description = description;
        this.price = price;
    }

    public String getImgItm() {
        return imgItm;
    }

    public void setImgItm(String imgItm) {
        this.imgItm = imgItm;
    }

    public String getMenuItemId() {
        return MenuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        MenuItemId = menuItemId;
    }

    public String getBsnId() {
        return bsnId;
    }

    public void setBsnId(String bsnId) {
        this.bsnId = bsnId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
