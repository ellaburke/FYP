package com.example.fyp_1;

public class MyKitchenItem {

    String itemName;
    String itemCategory;
    String itemAmount;
    String userId;
    String itemId;

    public MyKitchenItem() {

    }

    public MyKitchenItem(String itemName, String itemCategory, String itemAmount, String userId, String itemId) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemAmount = itemAmount;
        this.userId = userId;
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
