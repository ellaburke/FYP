package com.example.fyp_1.model;

public class GroceryShop {

    private String shopName;
    private double lat;
    private double lng;

    public GroceryShop() {

    }

    public GroceryShop(String shopName, double lat, double lng) {
        this.shopName = shopName;
        this.lat = lat;
        this.lng = lng;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
