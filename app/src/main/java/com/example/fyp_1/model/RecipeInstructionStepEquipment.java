package com.example.fyp_1.model;

public class RecipeInstructionStepEquipment {

    public String id;
    public String name;
    public String localizedName;
    public String image;
    public RecipeInstructionStepEquipmentTemperature temperature;

    public RecipeInstructionStepEquipment() {

    }

    public RecipeInstructionStepEquipment(String id, String name, String localizedName, String image) {
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

    public RecipeInstructionStepEquipmentTemperature getTemperature() {
        return this.temperature;
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

    public void setTemperature(RecipeInstructionStepEquipmentTemperature temperature) {
        this.temperature = temperature;
    }
}