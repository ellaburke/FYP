package com.example.fyp_1.model;

public class UserReuseTotal {

    String userID;
    int reuseNumber;
    String reuseID;

    public UserReuseTotal() {

    }

    public UserReuseTotal(String userID, int reuseNumber, String reuseID) {
        this.userID = userID;
        this.reuseNumber = reuseNumber;
        this.reuseID = reuseID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getReuseNumber() {
        return reuseNumber;
    }

    public void setReuseNumber(int reuseNumber) {
        this.reuseNumber = reuseNumber;
    }

    public String getReuseID() {
        return reuseID;
    }

    public void setReuseID(String reuseID) {
        this.reuseID = reuseID;
    }
}
