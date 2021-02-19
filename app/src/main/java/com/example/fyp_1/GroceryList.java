package com.example.fyp_1;

public class GroceryList {
    String itemName;
    String userID;

    public GroceryList() {

    }

    public GroceryList(String itemName, String userID) {
        this.itemName = itemName;
        this.userID = userID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
