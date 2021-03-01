package com.example.fyp_1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

// Model class
// Aka Java Bean/POJO
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe implements Serializable {

    public int id;
    public String title;
    public String image;
    public String missedIngredientCount;

    public Recipe() {

    }

    public Recipe(int id, String title, String image, String missedIngredientCount) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.missedIngredientCount = missedIngredientCount;
    }

    public String getMissedIngredientCount() {
        return missedIngredientCount;
    }

    public void setMissedIngredientCount(String missedIngredientCount) {
        this.missedIngredientCount = missedIngredientCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}