package com.diabetemanager;

import com.diabetemanager.model.Patient;
import com.diabetemanager.service.ActivityService;
import com.diabetemanager.service.AlertService;
import com.diabetemanager.service.GlucoseService;
import com.diabetemanager.service.MealService;
import com.diabetemanager.service.StatisticsService;
import com.diabetemanager.ui.ConsoleMenu;
import com.diabetemanager.service.MealAnalysisService;

public class Main {

    public static void main(String[] args) {

        Patient patient =
                new Patient(
                        1L,
                        "jan123",
                        "hashedPassword",
                        "Jan",
                        "Kowalski"
                );

        GlucoseService glucoseService =
                new GlucoseService();

        glucoseService.loadMeasurements(
                patient
        );

        MealService mealService =
                new MealService();

        ActivityService activityService =
                new ActivityService();

        StatisticsService statisticsService =
                new StatisticsService();

        AlertService alertService =
                new AlertService();

        MealAnalysisService mealAnalysisService =
                new MealAnalysisService(
                        glucoseService
                );

        ConsoleMenu menu =
                new ConsoleMenu(
                        patient,
                        glucoseService,
                        mealService,
                        activityService,
                        statisticsService,
                        alertService,
                        mealAnalysisService
                );

        menu.start();
    }
}