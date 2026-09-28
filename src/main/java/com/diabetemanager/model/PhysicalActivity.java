package com.diabetemanager.model;

import java.time.LocalDateTime;

public class PhysicalActivity {

    private Long id;
    private String type;
    private int durationMinutes;
    private String intensity;
    private double caloriesBurned;
    private LocalDateTime dateTime;

    public PhysicalActivity(
            Long id,
            String type,
            int durationMinutes,
            String intensity,
            double caloriesBurned,
            LocalDateTime dateTime) {

        this.id = id;
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.intensity = intensity;
        this.caloriesBurned = caloriesBurned;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getIntensity() {
        return intensity;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}