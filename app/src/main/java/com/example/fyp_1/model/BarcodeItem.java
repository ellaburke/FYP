package com.example.fyp_1.model;

public class BarcodeItem {

    public int id;
    public String title;
    public String images;
    public int number;
    public String unit;
    public String aisle;

    public BarcodeItem() {

    }

    public BarcodeItem(int id, String title, String images, int number, String unit, String aisle) {
        this.id = id;
        this.title = title;
        this.images = images;
        this.number = number;
        this.unit = unit;
        this.aisle = aisle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }
}
