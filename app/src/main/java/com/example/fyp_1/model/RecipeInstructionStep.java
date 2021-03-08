package com.example.fyp_1.model;

public class RecipeInstructionStep {

    public int number;
    public String step;
    public RecipeInstructionStepIngredient[] ingredients;
    public RecipeInstructionStepEquipment[] equipment;
    public RecipeInstructionStepLength length;

    public RecipeInstructionStep() {

    }

    public RecipeInstructionStep(int number,
                                 String step,
                                 RecipeInstructionStepIngredient[] ingredients,
                                 RecipeInstructionStepEquipment[] equipment,
                                 RecipeInstructionStepLength length) {
        this.number = number;
        this.step = step;
        this.ingredients = ingredients;
        this.equipment = equipment;
        this.length = length;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public RecipeInstructionStepIngredient[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(RecipeInstructionStepIngredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public RecipeInstructionStepEquipment[] getEquipment() {
        return equipment;
    }

    public void setEquipment(RecipeInstructionStepEquipment[] equipment) {
        this.equipment = equipment;
    }

    public RecipeInstructionStepLength getLength() {
        return length;
    }

    public void setLength(RecipeInstructionStepLength length) {
        this.length = length;
    }
}