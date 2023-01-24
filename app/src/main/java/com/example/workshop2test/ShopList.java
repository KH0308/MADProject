package com.example.workshop2test;

public class ShopList {
    private String businessID, shopName, LogoShop;

    public ShopList(){
    }

    public ShopList(String businessID, String shopName, String LogoShop) {

        this.businessID = businessID;
        this.shopName = shopName;
        this.LogoShop = LogoShop;

    }

    public String getLogoShop() {
        return LogoShop;
    }

    public void setLogoShop(String logoShop) {
        LogoShop = logoShop;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public String getshopName() {
        return shopName;
    }

    public void setshopName(String shopName) {
        this.shopName = shopName;
    }

}
