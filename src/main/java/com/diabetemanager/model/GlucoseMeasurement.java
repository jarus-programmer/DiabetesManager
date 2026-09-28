package com.diabetemanager.model;

import java.time.LocalDateTime;

public class GlucoseMeasurement {

    private Long id;
    private double glucoseLevel;
    private LocalDateTime dateTime;

    public GlucoseMeasurement(
            Long id,
            double glucoseLevel,
            LocalDateTime dateTime) {

        this.id = id;
        this.glucoseLevel = glucoseLevel;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public double getGlucoseLevel() {
        return glucoseLevel;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}