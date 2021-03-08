package com.example.fyp_1.model;

public class RecipeInstructionStepIngredient {

    public String id;
    public String name;
    public String localizedName;
    public String image;

    public RecipeInstructionStepIngredient() {

    }

    public RecipeInstructionStepIngredient(String id, String name, String localizedName, String image) {
        this.name = name;
        this.id = id;
        this.localizedName = localizedName;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public String getImage() {
        return image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    public void setImage(String image) {
        this.image = image;
    }
}