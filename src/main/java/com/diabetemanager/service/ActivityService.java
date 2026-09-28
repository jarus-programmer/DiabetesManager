package com.diabetemanager.service;

import com.diabetemanager.model.Patient;
import com.diabetemanager.model.PhysicalActivity;
import com.diabetemanager.util.ValidationUtils;

import java.util.List;

public class ActivityService {

    public void addActivity(
            Patient patient,
            PhysicalActivity activity) {

        if (patient == null) {
            throw new IllegalArgumentException(
                    "Pacjent nie może być null."
            );
        }

        if (activity == null) {
            throw new IllegalArgumentException(
                    "Aktywność nie może być null."
            );
        }

        ValidationUtils.validateNotBlank(
                activity.getType(),
                "Rodzaj aktywności"
        );

        ValidationUtils.validatePositive(
                activity.getDurationMinutes(),
                "Czas trwania"
        );

        ValidationUtils.validatePositive(
                activity.getCaloriesBurned(),
                "Spalone kalorie"
        );

        patient.addActivity(activity);
    }

    public List<PhysicalActivity> getActivities(
            Patient patient) {

        return patient.getActivities();
    }

    public void removeActivity(
            Patient patient,
            Long activityId) {

        if (activityId == null) {
            throw new IllegalArgumentException(
                    "ID aktywności nie może być null."
            );
        }

        patient.getActivities().removeIf(
                activity ->
                        activity.getId().equals(activityId)
        );
    }

    public PhysicalActivity findActivityById(
            Patient patient,
            Long activityId) {

        if (activityId == null) {
            throw new IllegalArgumentException(
                    "ID aktywności nie może być null."
            );
        }

        for (PhysicalActivity activity :
                patient.getActivities()) {

            if (activity.getId().equals(activityId)) {
                return activity;
            }
        }

        return null;
    }

    public int calculateTotalDuration(
            Patient patient) {

        return patient.getActivities()
                .stream()
                .mapToInt(PhysicalActivity::getDurationMinutes)
                .sum();
    }

    public double calculateTotalCaloriesBurned(
            Patient patient) {

        return patient.getActivities()
                .stream()
                .mapToDouble(PhysicalActivity::getCaloriesBurned)
                .sum();
    }
}