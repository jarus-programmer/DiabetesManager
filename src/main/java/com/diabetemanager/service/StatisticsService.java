package com.diabetemanager.service;

import com.diabetemanager.model.GlucoseMeasurement;
import com.diabetemanager.model.Patient;

import java.util.List;

public class StatisticsService {

    public double calculateAverageGlucose(Patient patient) {

        List<GlucoseMeasurement> measurements =
                patient.getGlucoseMeasurements();

        if (measurements.isEmpty()) {
            return 0;
        }

        return measurements.stream()
                .mapToDouble(GlucoseMeasurement::getGlucoseLevel)
                .average()
                .orElse(0);
    }

    public double getMinimumGlucose(Patient patient) {

        List<GlucoseMeasurement> measurements =
                patient.getGlucoseMeasurements();

        if (measurements.isEmpty()) {
            return 0;
        }

        return measurements.stream()
                .mapToDouble(GlucoseMeasurement::getGlucoseLevel)
                .min()
                .orElse(0);
    }

    public double getMaximumGlucose(Patient patient) {

        List<GlucoseMeasurement> measurements =
                patient.getGlucoseMeasurements();

        if (measurements.isEmpty()) {
            return 0;
        }

        return measurements.stream()
                .mapToDouble(GlucoseMeasurement::getGlucoseLevel)
                .max()
                .orElse(0);
    }

    public long countMeasurementsOutsideRange(
            Patient patient,
            double min,
            double max) {

        return patient.getGlucoseMeasurements()
                .stream()
                .filter(measurement ->
                        measurement.getGlucoseLevel() < min ||
                                measurement.getGlucoseLevel() > max
                )
                .count();
    }
}