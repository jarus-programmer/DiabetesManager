package com.diabetemanager.service;

import com.diabetemanager.model.GlucoseMeasurement;
import com.diabetemanager.model.Meal;
import com.diabetemanager.model.MealGlucoseAnalysis;
import com.diabetemanager.model.Patient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class MealAnalysisService {

    private final GlucoseService glucoseService;

    public MealAnalysisService(
            GlucoseService glucoseService) {

        this.glucoseService = glucoseService;
    }

    public MealGlucoseAnalysis analyzeMeal(
            Patient patient,
            Long mealId,
            Duration afterMealWindow) {

        Meal meal = findMealById(
                patient,
                mealId
        );

        if (meal == null) {
            return null;
        }

        GlucoseMeasurement beforeMeal =
                glucoseService.findMeasurementById(
                        patient,
                        meal.getGlucoseMeasurementId()
                );

        if (beforeMeal == null) {
            return null;
        }

        GlucoseMeasurement afterMeal =
                findFirstMeasurementAfterMeal(
                        patient,
                        meal.getDateTime(),
                        afterMealWindow
                );

        return new MealGlucoseAnalysis(
                meal,
                beforeMeal,
                afterMeal
        );
    }

    public List<MealGlucoseAnalysis> analyzeAllMeals(
            Patient patient,
            Duration afterMealWindow) {

        return patient.getMeals()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Meal::getDateTime
                        )
                )
                .map(meal ->
                        analyzeMeal(
                                patient,
                                meal.getId(),
                                afterMealWindow
                        )
                )
                .filter(analysis -> analysis != null)
                .toList();
    }

    private Meal findMealById(
            Patient patient,
            Long mealId) {

        for (Meal meal :
                patient.getMeals()) {

            if (meal.getId().equals(mealId)) {
                return meal;
            }
        }

        return null;
    }

    private GlucoseMeasurement findFirstMeasurementAfterMeal(
            Patient patient,
            LocalDateTime mealTime,
            Duration afterMealWindow) {

        LocalDateTime endTime =
                mealTime.plus(afterMealWindow);

        List<GlucoseMeasurement> measurements =
                patient.getGlucoseMeasurements()
                        .stream()
                        .filter(measurement -> {

                            LocalDateTime measurementTime =
                                    measurement.getDateTime();

                            return measurementTime.isAfter(mealTime)
                                    && !measurementTime.isAfter(endTime);
                        })
                        .sorted(
                                Comparator.comparing(
                                        GlucoseMeasurement::getDateTime
                                )
                        )
                        .toList();

        if (measurements.isEmpty()) {
            return null;
        }

        return measurements.get(0);
    }
}