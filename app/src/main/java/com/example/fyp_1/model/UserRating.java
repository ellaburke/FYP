package com.example.fyp_1.model;

public class UserRating {

    String userID;
    Float userRating;
    String ratingID;

    public UserRating() {

    }

    public UserRating(String userID, Float userRating, String ratingID) {
        this.userID = userID;
        this.userRating = userRating;
        this.ratingID = ratingID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Float getUserRating() {
        return userRating;
    }

    public void setUserRating(Float userRating) {
        this.userRating = userRating;
    }

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }
}
