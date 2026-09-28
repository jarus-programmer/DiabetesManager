package com.diabetemanager.service;

import com.diabetemanager.model.GlucoseMeasurement;
import com.diabetemanager.model.Patient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AlertService {

    public List<String> generateAlerts(
            Patient patient,
            double minGlucose,
            double maxGlucose,
            int repeatedMeasurements,
            Duration maximumGap) {

        List<String> alerts = new ArrayList<>();

        alerts.addAll(
                checkOutOfRangeMeasurements(
                        patient,
                        minGlucose,
                        maxGlucose
                )
        );

        alerts.addAll(
                checkRepeatedAbnormalMeasurements(
                        patient,
                        minGlucose,
                        maxGlucose,
                        repeatedMeasurements
                )
        );

        alerts.addAll(
                checkMeasurementGaps(
                        patient,
                        maximumGap
                )
        );

        alerts.addAll(
                checkNoRecentMeasurement(
                        patient,
                        maximumGap
                )
        );

        return alerts;
    }

    private List<String> checkOutOfRangeMeasurements(
            Patient patient,
            double minGlucose,
            double maxGlucose) {

        List<String> alerts = new ArrayList<>();

        for (GlucoseMeasurement measurement :
                patient.getGlucoseMeasurements()) {

            double glucose = measurement.getGlucoseLevel();

            if (glucose < minGlucose) {

                alerts.add(
                        "Informacja: pomiar " +
                                glucose +
                                " jest poniżej ustawionego zakresu."
                );

            } else if (glucose > maxGlucose) {

                alerts.add(
                        "Informacja: pomiar " +
                                glucose +
                                " jest powyżej ustawionego zakresu."
                );
            }
        }

        return alerts;
    }

    private List<String> checkRepeatedAbnormalMeasurements(
            Patient patient,
            double minGlucose,
            double maxGlucose,
            int requiredMeasurements) {

        List<String> alerts = new ArrayList<>();

        List<GlucoseMeasurement> measurements =
                getSortedMeasurements(patient);

        int highCount = 0;
        int lowCount = 0;

        for (GlucoseMeasurement measurement : measurements) {

            double glucose = measurement.getGlucoseLevel();

            if (glucose > maxGlucose) {

                highCount++;
                lowCount = 0;

                if (highCount == requiredMeasurements) {

                    alerts.add(
                            "Informacja: wykryto serię " +
                                    requiredMeasurements +
                                    " kolejnych pomiarów powyżej ustawionego zakresu."
                    );
                }

            } else if (glucose < minGlucose) {

                lowCount++;
                highCount = 0;

                if (lowCount == requiredMeasurements) {

                    alerts.add(
                            "Informacja: wykryto serię " +
                                    requiredMeasurements +
                                    " kolejnych pomiarów poniżej ustawionego zakresu."
                    );
                }

            } else {

                highCount = 0;
                lowCount = 0;
            }
        }

        return alerts;
    }

    private List<String> checkMeasurementGaps(
            Patient patient,
            Duration maximumGap) {

        List<String> alerts = new ArrayList<>();

        List<GlucoseMeasurement> measurements =
                getSortedMeasurements(patient);

        for (int i = 1; i < measurements.size(); i++) {

            LocalDateTime previous =
                    measurements.get(i - 1).getDateTime();

            LocalDateTime current =
                    measurements.get(i).getDateTime();

            Duration gap =
                    Duration.between(previous, current);

            if (gap.compareTo(maximumGap) > 0) {

                alerts.add(
                        "Informacja: pomiędzy pomiarami wystąpiła przerwa "
                                + gap.toHours()
                                + " godzin."
                );
            }
        }

        return alerts;
    }

    private List<String> checkNoRecentMeasurement(
            Patient patient,
            Duration maximumGap) {

        List<String> alerts = new ArrayList<>();

        List<GlucoseMeasurement> measurements =
                getSortedMeasurements(patient);

        if (measurements.isEmpty()) {

            alerts.add(
                    "Informacja: brak zapisanych pomiarów."
            );

            return alerts;
        }

        GlucoseMeasurement latestMeasurement =
                measurements.get(measurements.size() - 1);

        Duration sinceLastMeasurement =
                Duration.between(
                        latestMeasurement.getDateTime(),
                        LocalDateTime.now()
                );

        if (sinceLastMeasurement.compareTo(maximumGap) > 0) {

            alerts.add(
                    "Informacja: od ostatniego pomiaru minęło "
                            + sinceLastMeasurement.toHours()
                            + " godzin."
            );
        }

        return alerts;
    }

    private List<GlucoseMeasurement> getSortedMeasurements(
            Patient patient) {

        return patient.getGlucoseMeasurements()
                .stream()
                .sorted(
                        Comparator.comparing(
                                GlucoseMeasurement::getDateTime
                        )
                )
                .toList();
    }
}