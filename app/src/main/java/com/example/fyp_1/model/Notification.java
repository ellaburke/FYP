package com.example.fyp_1.model;

public class Notification {

    String itemName;
    String itemURL;
    String notificationType;
    String senderUserID;
    String recieverUserID;
    String notificationID;

    public Notification() {

    }

    public Notification(String itemName, String itemURL, String notificationType, String senderUserID, String recieverUserID, String notificationID) {
        this.itemName = itemName;
        this.itemURL = itemURL;
        this.notificationType = notificationType;
        this.senderUserID = senderUserID;
        this.recieverUserID = recieverUserID;
        this.notificationID = notificationID;
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

}
