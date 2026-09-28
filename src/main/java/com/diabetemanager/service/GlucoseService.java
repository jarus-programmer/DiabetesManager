package com.diabetemanager.service;

import com.diabetemanager.model.GlucoseMeasurement;
import com.diabetemanager.model.Patient;
import com.diabetemanager.repository.FileGlucoseRepository;
import com.diabetemanager.repository.GlucoseRepository;
import com.diabetemanager.util.ValidationUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class GlucoseService {

    private final GlucoseRepository repository;

    public GlucoseService() {

        this.repository =
                new FileGlucoseRepository();
    }

    public GlucoseService(
            GlucoseRepository repository) {

        this.repository = repository;
    }

    public void loadMeasurements(
            Patient patient) {

        if (patient == null) {

            throw new IllegalArgumentException(
                    "Pacjent nie może być null."
            );
        }

        patient
                .getGlucoseMeasurements()
                .clear();

        patient
                .getGlucoseMeasurements()
                .addAll(
                        repository.load()
                );

        patient
                .getGlucoseMeasurements()
                .sort(
                        Comparator.comparing(
                                GlucoseMeasurement::getDateTime
                        )
                );
    }

    public void addMeasurement(
            Patient patient,
            GlucoseMeasurement measurement) {

        if (patient == null) {

            throw new IllegalArgumentException(
                    "Pacjent nie może być null."
            );
        }

        if (measurement == null) {

            throw new IllegalArgumentException(
                    "Pomiar nie może być null."
            );
        }

        ValidationUtils.validateGlucose(
                measurement.getGlucoseLevel()
        );

        patient.addGlucoseMeasurement(
                measurement
        );

        try {

            repository.save(
                    patient.getGlucoseMeasurements()
            );

        } catch (RuntimeException e) {

            patient
                    .getGlucoseMeasurements()
                    .remove(measurement);

            throw e;
        }
    }

    public List<GlucoseMeasurement> getMeasurements(
            Patient patient) {

        if (patient == null) {

            throw new IllegalArgumentException(
                    "Pacjent nie może być null."
            );
        }

        return patient.getGlucoseMeasurements();
    }

    public List<GlucoseMeasurement> getRecentMeasurements(
            Patient patient,
            Duration timeWindow) {

        if (patient == null) {

            throw new IllegalArgumentException(
                    "Pacjent nie może być null."
            );
        }

        if (timeWindow == null
                || timeWindow.isNegative()
                || timeWindow.isZero()) {

            throw new IllegalArgumentException(
                    "Przedział czasu musi być większy od 0."
            );
        }

        LocalDateTime now =
                LocalDateTime.now();

        LocalDateTime cutoff =
                now.minus(timeWindow);

        return patient
                .getGlucoseMeasurements()
                .stream()
                .filter(measurement -> {

                    LocalDateTime dateTime =
                            measurement.getDateTime();

                    return !dateTime.isBefore(cutoff)
                            && !dateTime.isAfter(now);
                })
                .sorted(
                        Comparator.comparing(
                                GlucoseMeasurement::getDateTime
                        ).reversed()
                )
                .toList();
    }

    public void removeMeasurement(
            Patient patient,
            Long measurementId) {

        if (patient == null) {

            throw new IllegalArgumentException(
                    "Pacjent nie może być null."
            );
        }

        if (measurementId == null) {

            throw new IllegalArgumentException(
                    "ID pomiaru nie może być null."
            );
        }

        boolean removed =
                patient
                        .getGlucoseMeasurements()
                        .removeIf(
                                measurement ->
                                        measurement
                                                .getId()
                                                .equals(
                                                        measurementId
                                                )
                        );

        if (removed) {

            repository.save(
                    patient.getGlucoseMeasurements()
            );
        }
    }

    public GlucoseMeasurement findMeasurementById(
            Patient patient,
            Long measurementId) {

        if (patient == null) {

            throw new IllegalArgumentException(
                    "Pacjent nie może być null."
            );
        }

        if (measurementId == null) {

            throw new IllegalArgumentException(
                    "ID pomiaru nie może być null."
            );
        }

        for (GlucoseMeasurement measurement :
                patient.getGlucoseMeasurements()) {

            if (measurement
                    .getId()
                    .equals(measurementId)) {

                return measurement;
            }
        }

        return null;
    }
}