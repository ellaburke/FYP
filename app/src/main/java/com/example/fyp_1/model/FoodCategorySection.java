
package com.example.fyp_1.model;

import com.example.fyp_1.MyKitchenItem;

import java.util.List;

public class FoodCategorySection {

    private String sectionName;
    private List<MyKitchenItem> sectionItem;

    public FoodCategorySection(String sectionName, List<MyKitchenItem> sectionItem) {
        this.sectionName = sectionName;
        this.sectionItem = sectionItem;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<MyKitchenItem> getSectionItem() {
        return sectionItem;
    }

    public void setSectionItem(List<MyKitchenItem> sectionItem) {
        this.sectionItem = sectionItem;
    }

    @Override
    public String toString() {
        return "FoodCategorySection{" +
                "sectionName='" + sectionName + '\'' +
                ", sectionItem=" + sectionItem +
                '}';
    }
}