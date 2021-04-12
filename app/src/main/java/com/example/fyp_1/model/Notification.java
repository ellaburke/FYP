package com.example.fyp_1.model;

public class Notification {

    String itemName;
    String itemURL;
    String notificationType;
    String senderUserID;
    String recieverUserID;
    String notificationID;
    String listingState;
    String senderCollectorName;

    public Notification() {

    }

    public Notification(String itemName, String itemURL, String notificationType, String senderUserID, String recieverUserID, String notificationID, String listingState, String senderCollectorName) {
        this.itemName = itemName;
        this.itemURL = itemURL;
        this.notificationType = notificationType;
        this.senderUserID = senderUserID;
        this.recieverUserID = recieverUserID;
        this.notificationID = notificationID;
        this.listingState = listingState;
        this.senderCollectorName = senderCollectorName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemURL() {
        return itemURL;
    }

    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getSenderUserID() {
        return senderUserID;
    }

    public void setSenderUserID(String senderUserID) {
        this.senderUserID = senderUserID;
    }

    public String getRecieverUserID() {
        return recieverUserID;
    }

    public void setRecieverUserID(String recieverUserID) {
        this.recieverUserID = recieverUserID;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getListingState() {
        return listingState;
    }

    public void setListingState(String listingState) {
        this.listingState = listingState;
    }

    public String getSenderCollectorName() {
        return senderCollectorName;
    }

    public void setSenderCollectorName(String senderCollectorName) {
        this.senderCollectorName = senderCollectorName;
    }
}
