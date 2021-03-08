package com.example.fyp_1.model;

public class RecipeInstructionStepEquipmentTemperature {

    public int number;
    public String unit;

    public RecipeInstructionStepEquipmentTemperature() {

    }

    public RecipeInstructionStepEquipmentTemperature(int number, String unit) {
        this.number = number;
        this.unit = unit;
    }

    public int getNumber() {
        return number;
    }

    public String getUnit() {
        return unit;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}