package com.diabetemanager.model;

public class MealItem {

    private FoodProduct foodProduct;
    private double amountInGrams;

    public MealItem(
            FoodProduct foodProduct,
            double amountInGrams) {

        this.foodProduct = foodProduct;
        this.amountInGrams = amountInGrams;
    }

    public FoodProduct getFoodProduct() {
        return foodProduct;
    }

    public double getAmountInGrams() {
        return amountInGrams;
    }

    public double calculateCarbohydrates() {

        return foodProduct.getCarbohydratesPer100g()
                * amountInGrams
                / 100.0;
    }

    public double calculateCalories() {

        return foodProduct.getCaloriesPer100g()
                * amountInGrams
                / 100.0;
    }
}