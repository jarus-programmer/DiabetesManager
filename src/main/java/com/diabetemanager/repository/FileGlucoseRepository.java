package com.diabetemanager.repository;

import com.diabetemanager.model.GlucoseMeasurement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileGlucoseRepository
        implements GlucoseRepository {

    private static final Path FILE_PATH =
            Path.of(
                    "data",
                    "glucose_measurements.csv"
            );

    @Override
    public List<GlucoseMeasurement> load() {

        List<GlucoseMeasurement> measurements =
                new ArrayList<>();

        if (!Files.exists(FILE_PATH)) {
            return measurements;
        }

        try {

            List<String> lines =
                    Files.readAllLines(FILE_PATH);

            for (String line : lines) {

                if (line.isBlank()
                        || line.startsWith("id;")) {

                    continue;
                }

                String[] parts =
                        line.split(";");

                if (parts.length != 3) {
                    continue;
                }

                Long id =
                        Long.parseLong(parts[0]);

                double glucose =
                        Double.parseDouble(parts[1]);

                LocalDateTime dateTime =
                        LocalDateTime.parse(parts[2]);

                measurements.add(
                        new GlucoseMeasurement(
                                id,
                                glucose,
                                dateTime
                        )
                );
            }

        } catch (IOException |
                 NumberFormatException e) {

            throw new RuntimeException(
                    "Nie udało się wczytać pomiarów glukozy.",
                    e
            );
        }

        return measurements;
    }

    @Override
    public void save(
            List<GlucoseMeasurement> measurements) {

        try {

            Files.createDirectories(
                    FILE_PATH.getParent()
            );

            List<String> lines =
                    new ArrayList<>();

            lines.add(
                    "id;glucoseLevel;dateTime"
            );

            for (GlucoseMeasurement measurement :
                    measurements) {

                lines.add(
                        measurement.getId()
                                + ";"
                                + measurement.getGlucoseLevel()
                                + ";"
                                + measurement.getDateTime()
                );
            }

            Files.write(
                    FILE_PATH,
                    lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Nie udało się zapisać pomiarów glukozy.",
                    e
            );
        }
    }
}