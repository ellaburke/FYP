package com.example.fyp_1.model;

import java.util.List;

public class RecipeInstructions {

    public String name;
    public String steps;
    public String number;
    public String step;
    public List ingredients;
    public String id;
    public List equipment;

    public RecipeInstructions() {

    }

    public RecipeInstructions(String name, String steps, String number, String step, List ingredients, String id, List equipment) {
        this.name = name;
        this.steps = steps;
        this.number = number;
        this.step = step;
        this.ingredients = ingredients;
        this.id = id;
        this.equipment = equipment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public List getIngredients() {
        return ingredients;
    }

    public void setIngredients(List ingredients) {
        this.ingredients = ingredients;
    }

    public List getEquipment() {
        return equipment;
    }

    public void setEquipment(List equipment) {
        this.equipment = equipment;
    }
}
