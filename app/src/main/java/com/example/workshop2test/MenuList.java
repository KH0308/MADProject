package com.example.workshop2test;

public class MenuList {
    private String businessID, foodId, foodName, description, price, foodPic;

    public MenuList(){
    }

    public MenuList(String businessID, String foodId, String foodName, String description, String price, String foodPic) {
        //public MenuList(String businessID, String foodName, String description, String price, String foodPic)
        this.businessID = businessID;
        this.foodId = foodId;
        this.foodName = foodName;
        this.description = description;
        this.price = price;
        this.foodPic = foodPic;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFoodPic() {
        return foodPic;
    }

    public void setFoodPic(String foodPic) {
        this.foodPic = foodPic;
    }
}
