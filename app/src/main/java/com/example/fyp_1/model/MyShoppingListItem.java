package com.example.fyp_1.model;

public class MyShoppingListItem {

    String name;
    String shoppingListItemId;
    String userId;
    String itemCategory;

    public MyShoppingListItem() {

    }

    public MyShoppingListItem(String name, String shoppingListItemId, String userId, String itemCategory) {
        this.name = name;
        this.shoppingListItemId = shoppingListItemId;
        this.userId = userId;
        this.itemCategory = itemCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShoppingListItemId() {
        return shoppingListItemId;
    }

    public void setShoppingListItemId(String shoppingListItemId) {
        this.shoppingListItemId = shoppingListItemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }
}
