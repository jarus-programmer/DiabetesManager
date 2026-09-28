package com.diabetemanager.model;

public class FoodProduct {

    private Long id;
    private String name;
    private double carbohydratesPer100g;
    private double caloriesPer100g;

    public FoodProduct(
            Long id,
            String name,
            double carbohydratesPer100g,
            double caloriesPer100g) {

        this.id = id;
        this.name = name;
        this.carbohydratesPer100g = carbohydratesPer100g;
        this.caloriesPer100g = caloriesPer100g;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCarbohydratesPer100g() {
        return carbohydratesPer100g;
    }

    public double getCaloriesPer100g() {
        return caloriesPer100g;
    }
}