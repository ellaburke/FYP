package com.example.fyp_1.model;

import java.util.List;

public class Listing {

    String name;
    String description;
    String category;
    String expiryDate;
    String location;
    String pickUpTime;
    String listingImageURL;
    private String userId;
    String listingId;
    int requestTotal;

    public Listing() {

    }

    public Listing(String itemToAdd, String userId, List<String> myGroceryList) {
        name = null;
        location = null;
        listingImageURL = null;
    }

    public Listing(String listingId, String name, String description, String category, String expiryDate, String location, String pickUpTime, String listingImageURL, String userId, int requestTotal) {
        this.listingId = listingId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.expiryDate = expiryDate;
        this.location = location;
        this.pickUpTime = pickUpTime;
        this.listingImageURL = listingImageURL;
        this.userId = userId;
        this.requestTotal = requestTotal;

    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }


    public String getListingImageURL() {
        return listingImageURL;
    }

    public void setListingImageURL(String listingImageURL) {
        this.listingImageURL = listingImageURL;
    }


    public int getRequestTotal() {
        return requestTotal;
    }

    public void setRequestTotal(int requestTotal) {
        this.requestTotal = requestTotal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
