package com.example.fyp_1.Chat;

public class ChatMessage {

    private String text;
    private String name;
    private String photoUrl;
    private String imageUrl;
    private String fromIdUser;
    private String toIdUser;

    public ChatMessage() {

    }

    public ChatMessage(String text, String name, String photoUrl, String imageUrl, String fromIdUser, String toIdUser) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.imageUrl = imageUrl;
        this.fromIdUser = fromIdUser;
        this.toIdUser = toIdUser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFromIdUser() {
        return fromIdUser;
    }

    public void setFromIdUser(String fromIdUser) {
        this.fromIdUser = fromIdUser;
    }

    public String getToIdUser() {
        return toIdUser;
    }

    public void setToIdUser(String toIdUser) {
        this.toIdUser = toIdUser;
    }
}
