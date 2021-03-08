package com.example.fyp_1.model;

public class RecipeInstructions {

    public String name;
    public RecipeInstructionStep[] steps;

    public RecipeInstructions() {

    }

    public RecipeInstructions(String name, RecipeInstructionStep[] steps) {
        this.name = name;
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RecipeInstructionStep[] getSteps() {
        return this.steps;
    }

    public void setSteps(RecipeInstructionStep[] steps) {
        this.steps = steps;
    }
}