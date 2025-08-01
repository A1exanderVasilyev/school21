package org.example.domain.enums;

public enum FoodSubType {
    FRUIT('f', "fruit", 10),
    BREAD('f', "bread", 20),
    MEAT('f', "meat", 40);

    private final char foodSymbol;
    private final String foodName;
    private final int healthRestoreAmount;

    FoodSubType(char foodSymbol, String foodName, int healthRestoreAmount) {
        this.foodSymbol = foodSymbol;
        this.foodName = foodName;
        this.healthRestoreAmount = healthRestoreAmount;
    }

    public char getFoodSymbol() {
        return foodSymbol;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getHealthRestoreAmount() {
        return healthRestoreAmount;
    }
}
