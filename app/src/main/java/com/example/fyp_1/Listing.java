package com.example.fyp_1;

public class Listing {

    String name;
    String description;
    String category;
    String expiryDate;
    String location;
    String pickUpTime;
    String keepListedFor;
    String listingImageURL;
    String foodTypeWantOrOffering;
    private String userId;
    String listingId;


    public Listing() {
        name = null;
        location = null;
        listingImageURL = null;
    }

    public Listing(String listingId, String name, String description, String category, String expiryDate, String location, String pickUpTime, String keepListedFor, String listingImageURL, String foodTypeWantOrOffering, String userId) {
        this.listingId = listingId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.expiryDate = expiryDate;
        this.location = location;
        this.pickUpTime = pickUpTime;
        this.keepListedFor = keepListedFor;
        this.listingImageURL = listingImageURL;
        this.foodTypeWantOrOffering = foodTypeWantOrOffering;
        this.userId = userId;

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

    public String getKeepListedFor() {
        return keepListedFor;
    }

    public void setKeepListedFor(String keepListedFor) {
        this.keepListedFor = keepListedFor;
    }

    public String getListingImageURL() {
        return listingImageURL;
    }

    public void setListingImageURL(String listingImageURL) {
        this.listingImageURL = listingImageURL;
    }

    public String getFoodTypeWantOrOffering() {
        return foodTypeWantOrOffering;
    }

    public void setFoodTypeWantOrOffering(String foodTypeWantOrOffering) {
        this.foodTypeWantOrOffering = foodTypeWantOrOffering;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
