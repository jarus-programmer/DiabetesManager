package com.diabetemanager.model;

import java.util.ArrayList;
import java.util.List;

public class Patient extends User {

    private List<GlucoseMeasurement> glucoseMeasurements;
    private List<Meal> meals;
    private List<PhysicalActivity> activities;

    public Patient(
            Long id,
            String username,
            String passwordHash,
            String firstName,
            String lastName) {

        super(
                id,
                username,
                passwordHash,
                firstName,
                lastName,
                Role.PATIENT
        );

        this.glucoseMeasurements = new ArrayList<>();
        this.meals = new ArrayList<>();
        this.activities = new ArrayList<>();
    }

    public List<GlucoseMeasurement> getGlucoseMeasurements() {
        return glucoseMeasurements;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public List<PhysicalActivity> getActivities() {
        return activities;
    }

    public void addGlucoseMeasurement(
            GlucoseMeasurement measurement) {

        glucoseMeasurements.add(measurement);
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public void addActivity(PhysicalActivity activity) {
        activities.add(activity);
    }
}