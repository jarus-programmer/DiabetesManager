package com.diabetemanager.service;

import com.diabetemanager.model.Meal;
import com.diabetemanager.model.MealItem;
import com.diabetemanager.model.Patient;
import com.diabetemanager.util.ValidationUtils;

import java.util.List;

public class MealService {

    public void addMeal(
            Patient patient,
            Meal meal) {

        if (patient == null) {
            throw new IllegalArgumentException(
                    "Pacjent nie może być null."
            );
        }

        if (meal == null) {
            throw new IllegalArgumentException(
                    "Posiłek nie może być null."
            );
        }

        ValidationUtils.validateNotBlank(
                meal.getName(),
                "Nazwa posiłku"
        );

        patient.addMeal(meal);
    }

    public List<Meal> getMeals(Patient patient) {

        if (patient == null) {
            throw new IllegalArgumentException(
                    "Pacjent nie może być null."
            );
        }

        return patient.getMeals();
    }

    public void addMealItem(
            Meal meal,
            MealItem item) {

        if (meal == null) {
            throw new IllegalArgumentException(
                    "Posiłek nie może być null."
            );
        }

        if (item == null) {
            throw new IllegalArgumentException(
                    "Element posiłku nie może być null."
            );
        }

        ValidationUtils.validatePositive(
                item.getAmountInGrams(),
                "Ilość produktu"
        );

        meal.addItem(item);
    }

    public void removeMeal(
            Patient patient,
            Long mealId) {

        if (patient == null) {
            throw new IllegalArgumentException(
                    "Pacjent nie może być null."
            );
        }

        if (mealId == null) {
            throw new IllegalArgumentException(
                    "ID posiłku nie może być null."
            );
        }

        patient.getMeals().removeIf(
                meal ->
                        meal.getId().equals(mealId)
        );
    }

    public Meal findMealById(
            Patient patient,
            Long mealId) {

        if (patient == null) {
            throw new IllegalArgumentException(
                    "Pacjent nie może być null."
            );
        }

        if (mealId == null) {
            throw new IllegalArgumentException(
                    "ID posiłku nie może być null."
            );
        }

        for (Meal meal : patient.getMeals()) {

            if (meal.getId().equals(mealId)) {
                return meal;
            }
        }

        return null;
    }

    public double calculateTotalCarbohydrates(
            Patient patient) {

        return patient.getMeals()
                .stream()
                .mapToDouble(Meal::getTotalCarbohydrates)
                .sum();
    }

    public double calculateTotalCalories(
            Patient patient) {

        return patient.getMeals()
                .stream()
                .mapToDouble(Meal::getTotalCalories)
                .sum();
    }
}