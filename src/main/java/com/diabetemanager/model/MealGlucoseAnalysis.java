package com.diabetemanager.model;

public class MealGlucoseAnalysis {

    private Meal meal;
    private GlucoseMeasurement beforeMealMeasurement;
    private GlucoseMeasurement afterMealMeasurement;

    public MealGlucoseAnalysis(
            Meal meal,
            GlucoseMeasurement beforeMealMeasurement,
            GlucoseMeasurement afterMealMeasurement) {

        this.meal = meal;
        this.beforeMealMeasurement = beforeMealMeasurement;
        this.afterMealMeasurement = afterMealMeasurement;
    }

    public Meal getMeal() {
        return meal;
    }

    public GlucoseMeasurement getBeforeMealMeasurement() {
        return beforeMealMeasurement;
    }

    public GlucoseMeasurement getAfterMealMeasurement() {
        return afterMealMeasurement;
    }

    public Double getGlucoseDifference() {

        if (afterMealMeasurement == null
                || beforeMealMeasurement == null) {

            return null;
        }

        return afterMealMeasurement.getGlucoseLevel()
                - beforeMealMeasurement.getGlucoseLevel();
    }

    public boolean hasAfterMealMeasurement() {

        return afterMealMeasurement != null;
    }
}